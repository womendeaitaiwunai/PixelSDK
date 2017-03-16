package com.pixelall.pixellib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.text.TextUtils;

import com.pixelall.pixellib.PixelCode;
import com.pixelall.pixellib.PixelCommen;
import com.pixelall.pixellib.PixelErrorMessage;
import com.pixelall.pixellib.PixelParams;
import com.pixelall.pixellib.PixelResult;


/**
 * Created by lxl on 2017/3/14.
 * 工具类
 */

public class PixelUtil {
    private static FaceDetector.Face [] faces;
    public static PixelParams initParams(Context context){
        PixelParams params=new PixelParams();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String company=appInfo.metaData.getString(PixelCommen.COMPANY);
            String app_key=appInfo.metaData.getString(PixelCommen.APP_KEY);
            params.setCompanyName(company);
            params.setApp_key(app_key);
        }catch (Exception e){}
        return params;
    }


    public static PixelResult checkData(Context context, PixelParams params){
        PixelResult pixelResult=new PixelResult();
        if (context==null){
            pixelResult.setResultCode(PixelCode.CONTEXT_ERROR);
            pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
        }else {
            if (TextUtils.isEmpty(params.getCompanyName())||TextUtils.isEmpty(params.getApp_key())){
                pixelResult.setResultCode(PixelCode.APP_KEY_OR_COMPANY_EMPTY_ERROR);
                pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
            }else {
                if (checkEqual(params.getCompanyName(),params.getApp_key())){
                    pixelResult.setResultCode(PixelCode.APP_KEY_OR_COMPANY_ERROR);
                    pixelResult.setResultMessage(PixelErrorMessage.getErrorMessage(pixelResult));
                }else {
                    pixelResult.setResultCode(PixelCode.SUCCESS);
                }
            }
        }
        return pixelResult;
    }


    public static PixelResult checkBitmap(Bitmap bitmap){
        PixelResult pixelResult=new PixelResult();
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

            }
        }
        return pixelResult;
    }

    private static int checkFaceArea(){
        return 0;
    }


    private static int checkFace(Bitmap mFaceBitmap)
    {
        Bitmap mFaceBitmap565=mFaceBitmap.copy(Bitmap.Config.RGB_565,true);
        FaceDetector fd;
        faces= new FaceDetector.Face[PixelCommen.MAX_FACE];
        int count= PixelCommen.ERROR_FACE;
        try {
            fd = new FaceDetector(mFaceBitmap565.getWidth(), mFaceBitmap565.getHeight(), PixelCommen.MAX_FACE);
            count = fd.findFaces(mFaceBitmap565, faces);
        } catch (Exception e) {}
        return count;
    }


    private static boolean checkEqual(String company,String appKey){
        return TextUtils.equals(company,appKey);
    }
}
