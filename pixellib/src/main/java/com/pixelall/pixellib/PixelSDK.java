package com.pixelall.pixellib;

import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;
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
        showDialog("正在检测照片...");
        startCheckBitmap(bitmap);
    }

    /**
     * 开始上传
     */
    public void startUpload(){
        showDialog("正在上传给人调，请耐心等待...");
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
        return this;
    }

    private PixelSDK initPixelParams() {
        pixelResult=pixelUtil.checkData(context);
        return this;
    }

    private void startCheckBitmap(Bitmap bitmap){
        initPixelParams();
        if (callBack==null) return;
        if (pixelResult.getResultCode()== PixelCode.SUCCESS
                ||pixelResult.getResultCode()>PixelCode.APP_KEY_OR_COMPANY_ERROR){
            pixelUtil.checkBitmap(bitmap);
            pixelUtil.setCheckBitmapCallback(this);
        }else{
            hideDialog();
            callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
        }
    }

    private void startUploadBitmap(){
        if (pixelResult.getResultCode()==PixelCode.SUCCESS){
            pixelUtil.startUpload();
            pixelUtil.setUploadBitmapCallback(this);
        }else {
            hideDialog();
            callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
        }
    }

    @Override
    public void checkBitmapResult(PixelResult pixelResult) {
        if (callBack==null) return;
        this.pixelResult=pixelResult;
        hideDialog();
        callBack.checkResult(TYPE_CHECK_FACE,pixelResult);
    }

    @Override
    public void uploadBitmapResult(PixelResult pixelResult) {
        hideDialog();
        callBack.checkResult(TYPE_UPLOAD_BITMAP,pixelResult);
    }


    private void showDialog(String message){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }else if (!progressDialog.isShowing()){
            progressDialog.setMessage(message);
            progressDialog.show();
        }else {
            progressDialog.setMessage(message);
        }
    }

    private void hideDialog(){
        if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
