package com.pixelall.pixellib;

import android.content.Context;
import android.graphics.Bitmap;

import com.pixelall.pixellib.util.PixelUtil;

/**
 * Created by lxl on 2017/3/14.
 *  广州像素数据技术股份有限公司
 */

public class PixelSDK {
    public static int MODE_CHECK_BITMAP=0x01;
    public static int MODE_UP_BITMAP=0x02;

    private Context context;
    private PixelCallBack callBack;
    private PixelResult pixelResult;
    private PixelParams pixelParams=new PixelParams();
    private PixelSDK pixelSDK;


    /**
     * 初始化SDK
     * @param context  上下文
     * @return
     */
    public PixelSDK getInstance(Context context) {
        this.context=context;
        pixelSDK=initPixelParams();
        return this;
    }


    private PixelSDK initPixelParams() {
        pixelParams= PixelUtil.initParams(context);
        pixelResult=PixelUtil.checkData(context,pixelParams);
        return this;
    }


    private PixelSDK checkBitmap(Bitmap bitmap){
        if (callBack==null) return getInstance(context);
        if (pixelResult.getResultCode()== PixelCode.SUCCESS){
            callBack.checkResult(MODE_CHECK_BITMAP,pixelResult);
        }else {

        }
        return this;
    }



    /**
     * 检查照片结果的回调
     * @param callBack 回调接口
     */
    public void setCheckCallBack(PixelCallBack callBack){
        this.callBack=callBack;
    }

    public void checkPhoto(){

    }


}
