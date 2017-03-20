package com.pixelall.pixellib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.pixelall.pixellib.PixelCode;
import com.pixelall.pixellib.PixelErrorMessage;
import com.pixelall.pixellib.PixelResult;


/**
 * Created by lxl on 2017/3/14.
 * 工具类
 */

public class PixelUtil {
    private  FaceDetector.Face [] faces;
    private  int bitmapWidth;
    private  CheckBitmapCallback checkBitmapCallback;

    public PixelUtil(){

    }

    private PixelParams initParams(Context context){
        PixelParams params=new PixelParams();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String company=appInfo.metaData.getString(PixelCommen.COMPANY);
            String app_key=appInfo.metaData.getString(PixelCommen.APP_KEY);
            params.setCompanyName(company);
            params.setApp_key(app_key);
        }catch (Exception e){
            Log.i("initParams", "initParams: ApplicationInfo error");
        }
        return params;
    }


    public PixelResult checkData(Context context){
        PixelParams params=initParams(context);
        PixelResult pixelResult=new PixelResult();
        if (context==null){
            pixelResult.setResultCode(PixelCode.CONTEXT_ERROR);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
        }else {
            if (TextUtils.isEmpty(params.getCompanyName())||TextUtils.isEmpty(params.getApp_key())){
                pixelResult.setResultCode(PixelCode.APP_KEY_OR_COMPANY_EMPTY_ERROR);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
            }else {
                if (!checkEqual(params.getCompanyName(),params.getApp_key())){
                    pixelResult.setResultCode(PixelCode.APP_KEY_OR_COMPANY_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else {
                    pixelResult.setResultCode(PixelCode.SUCCESS);
                }
            }
        }
        return pixelResult;
    }


    public void checkBitmap(Bitmap bitmap){
        Bitmap mFaceBitmap565=bitmap.copy(Bitmap.Config.RGB_565,true);
       new CheckBitmap().execute(mFaceBitmap565);
    }

    private PixelResult checkFaceArea(){
        PixelResult pixelResult=new PixelResult();
        pixelResult.setResultCode(PixelCode.OTHER_FAIL);
        if (faces==null||faces.length==0) return pixelResult;
        PointF pointF=new PointF();
        faces[0].getMidPoint(pointF);
        if (faces[0].eyesDistance()/bitmapWidth>0.8){
            pixelResult.setResultCode(PixelCode.FACE_AREA_BIG_ERROR);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
        }else if (faces[0].eyesDistance()/bitmapWidth<0.1){
            pixelResult.setResultCode(PixelCode.FACE_AREA_SMALL_ERROR);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
        }else {
            pixelResult.setResultCode(PixelCode.SUCCESS);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
        }
        return pixelResult;
    }


    private int checkFace(Bitmap mFaceBitmap565)
    {
        FaceDetector fd;
        faces= new FaceDetector.Face[PixelCommen.MAX_FACE];
        int count= PixelCommen.ERROR_FACE;
        bitmapWidth=mFaceBitmap565.getWidth();
        int bitmapHeight=mFaceBitmap565.getHeight();
        try {
            fd = new FaceDetector(bitmapWidth, bitmapHeight, PixelCommen.MAX_FACE);
            count = fd.findFaces(mFaceBitmap565, faces);
        } catch (Exception e) {
            Log.i("checkFace", "checkFace: error");
        }
        finally {
            if( !mFaceBitmap565.isRecycled()){
                mFaceBitmap565.recycle();
            }
        }
        return count;
    }


    private boolean checkEqual(String company,String appKey){
        String checkData = "";
        try {
            byte[] bytes=RSAUtils.decryptData(Base64Utils.decode(appKey),
                    RSAUtils.loadPrivateKey(PixelMineCode.checkKey.substring(10,PixelMineCode.checkKey.length())));
            if (bytes!=null)checkData=new String(bytes);
        }catch (Exception e){
            Log.i("checkEqual", "checkEqual: "+"发生错误");
            checkData="";
        }
        return checkData.equals(company);
    }

    private  class CheckBitmap extends AsyncTask<Bitmap,Void,PixelResult>{
        @Override
        protected PixelResult doInBackground(Bitmap... params) {
            PixelResult pixelResult=new PixelResult();
            Bitmap bitmap=params[0];
            if (bitmap==null){
                pixelResult.setResultCode(PixelCode.BITMAP_NULL);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
            }else {
                int checkFaceResult=checkFace(bitmap);
                if (checkFaceResult == PixelCommen.TWO_FACE) {
                    pixelResult.setResultCode(PixelCode.MANY_FACE_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommen.ERROR_FACE){
                    pixelResult.setResultCode(PixelCode.FACE_EX_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommen.NO_FACE){
                    pixelResult.setResultCode(PixelCode.NO_FACE_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommen.ONE_FACE){
                    pixelResult=checkFaceArea();
                }
            }
            return pixelResult;
        }

        @Override
        protected void onPostExecute(PixelResult pixelResult) {
            checkBitmapCallback.checkBitmapResult(pixelResult);
        }
    }

    public void setCheckBitmapCallback(CheckBitmapCallback checkBitmapCallback){
        this.checkBitmapCallback=checkBitmapCallback;
    }
}
