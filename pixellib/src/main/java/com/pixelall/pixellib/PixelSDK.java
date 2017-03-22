package com.pixelall.pixellib;

import android.content.Context;
import android.graphics.Bitmap;

import com.pixelall.pixellib.util.CheckBitmapCallback;
import com.pixelall.pixellib.util.PixelUtil;
import com.pixelall.pixellib.util.UploadBitmapCallback;

/**
 * Created by lxl on 2017/3/14.
 *  广州像素数据技术股份有限公司
 */

public class PixelSDK implements CheckBitmapCallback,UploadBitmapCallback{
    private Context context;
    private PixelCallBack callBack;
    private PixelResult pixelResult;
    private PixelUtil pixelUtil;
    public static int TYPE_CHECK_FACE=0x01;
    public static int TYPE_UPLOAD_BITMAP=0x02;


    /**
     * SDK的初始化
     * @param context 上下文
     */
    public PixelSDK(Context context){
        this.context=context;
        getInstance(context);
    }

    /**
     * 开始执行图片检测和上传
     * @param bitmap 检测和上传的图片
     */
    public void checkBitmap(Bitmap bitmap){
        startCheckBitmap(bitmap);
    }

    /**
     * 开始上传
     */
    public void startUpload(){
        startUploadBitmap();
    }


    /**
     * 检查照片和上传结果的回调
     * @param callBack 回调接口
     */
    public void setCheckCallBack(PixelCallBack callBack){
        this.callBack=callBack;
    }


    private PixelSDK getInstance(Context context) {
        this.context=context;
        pixelUtil=new PixelUtil();
        initPixelParams();
        return this;
    }

    private PixelSDK initPixelParams() {
        pixelResult=pixelUtil.checkData(context);
        return this;
    }

    private void startCheckBitmap(Bitmap bitmap){
        if (callBack==null) return;
        if (pixelResult.getResultCode()== PixelCode.SUCCESS
                ||pixelResult.getResultCode()>PixelCode.APP_KEY_OR_COMPANY_ERROR){
            pixelUtil.checkBitmap(bitmap);
            pixelUtil.setCheckBitmapCallback(this);
        }else{
            callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
        }
    }

    private void startUploadBitmap(){
        if (pixelResult.getResultCode()==PixelCode.SUCCESS){
            pixelUtil.startUpload();
            pixelUtil.setUploadBitmapCallback(this);
        }else {
            callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
        }
    }

    @Override
    public void checkBitmapResult(PixelResult pixelResult) {
        if (callBack==null) return;
        this.pixelResult=pixelResult;
        callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
    }

    @Override
    public void uploadBitmapResult(PixelResult pixelResult) {
        callBack.checkResult(TYPE_UPLOAD_BITMAP,pixelResult);
    }
}
