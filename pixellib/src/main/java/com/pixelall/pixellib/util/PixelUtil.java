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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by lxl on 2017/3/14.
 * 工具类
 */

public class PixelUtil {
    private FaceDetector.Face [] faces;
    private int bitmapWidth;
    private PixelParams params;
    private Bitmap mFaceBitmap565;
    private CheckBitmapCallback checkBitmapCallback;
    private UploadBitmapCallback uploadBitmapCallback;

    public PixelUtil(){

    }

    private PixelParams initParams(Context context){
        PixelParams params=new PixelParams();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String company=appInfo.metaData.getString(PixelCommon.COMPANY);
            String app_key=appInfo.metaData.getString(PixelCommon.APP_KEY);
            params.setCompanyName(company);
            params.setApp_key(app_key);
        }catch (Exception e){
            Log.i("initParams", "initParams: ApplicationInfo error");
        }
        return params;
    }


    public PixelResult checkData(Context context){
        params=initParams(context);
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


    /**
     * 检查图片
     * @param bitmap 要检查的图片
     */
    public void checkBitmap(Bitmap bitmap){
        params.setPhoto(bitmap);
        mFaceBitmap565=bitmap.copy(Bitmap.Config.RGB_565,true);
        new CheckBitmap().execute(mFaceBitmap565);
    }

    /**
     * 开始上传
     */
    public void startUpload(){
        new UploadBitmap().execute();
    }

    /**
     * 检查人脸区域
     * @return 检查结果
     */
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


    /**
     * 检测人脸
     * @param mFaceBitmap565 565格式的图片
     * @return
     */
    private int checkFace(Bitmap mFaceBitmap565) {
        FaceDetector fd;
        faces= new FaceDetector.Face[PixelCommon.MAX_FACE];
        int count= PixelCommon.ERROR_FACE;
        bitmapWidth=mFaceBitmap565.getWidth();
        int bitmapHeight=mFaceBitmap565.getHeight();
        try {
            fd = new FaceDetector(bitmapWidth, bitmapHeight, PixelCommon.MAX_FACE);
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


    /**
     * 检查公司名和加密字段是否相同
     * @param company 公司名
     * @param appKey 加密字段
     * @return 是否相同
     */
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

    /**
     * 异步检查照片
     */
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
                if (!bitmap.isRecycled())bitmap.recycle();
                if (mFaceBitmap565!=null&&!mFaceBitmap565.isRecycled()){
                    mFaceBitmap565.recycle();
                    mFaceBitmap565=null;
                }
                if (checkFaceResult == PixelCommon.TWO_FACE) {
                    pixelResult.setResultCode(PixelCode.MANY_FACE_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommon.ERROR_FACE){
                    pixelResult.setResultCode(PixelCode.FACE_EX_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommon.NO_FACE){
                    pixelResult.setResultCode(PixelCode.NO_FACE_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else if (checkFaceResult== PixelCommon.ONE_FACE){
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

    /**
     * 设置检测照片的回调
     * @param checkBitmapCallback 回调方法
     */
    public void setCheckBitmapCallback(CheckBitmapCallback checkBitmapCallback){
        this.checkBitmapCallback=checkBitmapCallback;
    }


    /**
     * 设置上传相片的回调
     * @param uploadBitmapCallback 回调方法
     */
    public void setUploadBitmapCallback(UploadBitmapCallback uploadBitmapCallback){
        this.uploadBitmapCallback=uploadBitmapCallback;
    }



    private class UploadBitmap extends AsyncTask<Void,Void,PixelResult>{
        @Override
        protected PixelResult doInBackground(Void... params) {
            return httpUrlConnectionPut();
        }
        @Override
        protected void onPostExecute(PixelResult pixelResult) {
            super.onPostExecute(pixelResult);
            uploadBitmapCallback.uploadBitmapResult(pixelResult);
        }
    }


    private PixelResult httpUrlConnectionPut() {
        String result = "";
        PixelResult pixelResult=new PixelResult();
        try{
            URL url = new URL(PixelMineCode.pixelUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 提交模式
            conn.setConnectTimeout(10000);//连接超时 单位毫秒
            conn.setReadTimeout(10000);//读取超时 单位毫秒
            conn.setDoOutput(true);// 是否输入参数

//            PixelParamsBean pixelParamsBean=new PixelParamsBean();
//            pixelParamsBean.setApp_key(params.getApp_key());
//            pixelParamsBean.setCompany(params.getCompanyName());
            Long time=System.currentTimeMillis();
//            pixelParamsBean.setTime(time);
//            pixelParamsBean.setTimeKey(getTimeKey(time.toString()));
//            pixelParamsBean.setPhotoData(getPhotoData(params.getPhoto()));



            //StringBuffer params = new StringBuffer().append(PixelCommon.PIXEL_CODE).append();
            StringBuffer pixelParams = new StringBuffer()
                    .append("v1=").append(params.getApp_key()).append("&")
                    .append("v2=").append(params.getCompanyName()).append("&")
                    .append("v3=").append(time.toString()).append("&")
                    .append("v4=").append(getTimeKey(time.toString()));
                    //.append("v5=").append(getPhotoData(params.getPhoto()));
            byte[] bytes = pixelParams.toString().getBytes();
            conn.getOutputStream().write(bytes);
            InputStream inStream=conn.getInputStream();


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isr = new InputStreamReader(inStream);
                BufferedReader br = new BufferedReader(isr);
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    result += inputLine;
                }
                isr.close();
                conn.disconnect();
            }else {
                pixelResult.setResultCode(PixelCode.SERVICE_CONNECT_ERROR);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                return pixelResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof MalformedURLException){
                pixelResult.setResultCode(PixelCode.SERVICE_URL_ERROR);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                return pixelResult;
            }else result="";
        }

        if (TextUtils.isEmpty(result)){
            pixelResult.setResultCode(PixelCode.COMMIT_DATA_ERROR);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
            return pixelResult;
        }else {
            try {
                JSONObject jsonObject=new JSONObject(result);
                int resultCode=jsonObject.getInt(PixelCommon.RESULT_CODE);
                String message=jsonObject.getString(PixelCommon.MESSAGE);
                pixelResult.setResultCode(resultCode);
                pixelResult.setResultMessage(message);
                return pixelResult;
            }catch (Exception e){
                e.printStackTrace();
                pixelResult.setResultCode(PixelCode.RETURN_DATA_ERROR);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                return pixelResult;
            }
        }
    }


    private String getTimeKey(String time){
        String result;
        try {
            result=Base64Utils.encode(RSAUtils.encryptData(time.getBytes(),RSAUtils.loadPublicKey(PixelMineCode.upKey)));
        }catch (Exception e){
            e.printStackTrace();
            result="";
        }
        return result;
    }


    private String getPhotoData(Bitmap bitmap){
        return Base64Utils.encode(Bitmap2Bytes(bitmap));
    }


    private  byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bo);
            bo.flush();
            bo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bo.toByteArray();
    }

}
