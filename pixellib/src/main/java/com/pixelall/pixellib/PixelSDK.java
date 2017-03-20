package com.pixelall.pixellib;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.pixelall.pixellib.util.CheckBitmapCallback;
import com.pixelall.pixellib.util.PixelUtil;

/**
 * Created by lxl on 2017/3/14.
 *  广州像素数据技术股份有限公司
 */

public class PixelSDK implements CheckBitmapCallback{
    private Context context;
    private PixelCallBack callBack;
    private PixelResult pixelResult;
    private PixelSDK pixelSDK;
    private PixelUtil pixelUtil;


    public PixelSDK(Context context){
        this.context=context;
        getInstance(context);
    }

    public void start(Bitmap bitmap){
       startCheckAndSend(bitmap);
    }


    /**
     * 检查照片结果的回调
     * @param callBack 回调接口
     */
    public void setCheckCallBack(PixelCallBack callBack){
        this.callBack=callBack;
    }



    private PixelSDK getInstance(Context context) {
        this.context=context;
        pixelUtil=new PixelUtil();
        pixelSDK=initPixelParams();
        return this;
    }


    private PixelSDK initPixelParams() {
        pixelResult=pixelUtil.checkData(context);
        return this;
    }


    private void startCheckAndSend(Bitmap bitmap){
        if (callBack==null) return;
        if (pixelResult.getResultCode()== PixelCode.SUCCESS){
            pixelUtil.checkBitmap(bitmap);
            pixelUtil.setCheckBitmapCallback(this);
        }else {
            callBack.checkResult(pixelResult);
        }
    }

    @Override
    public void checkBitmapResult(PixelResult pixelResult) {
        if (pixelResult.getResultCode()==PixelCode.SUCCESS){
            Log.i("startCheckAndSend", "checkResult: "+pixelResult.getResultCode());
        }else {
            callBack.checkResult(pixelResult);
        }
    }
}
