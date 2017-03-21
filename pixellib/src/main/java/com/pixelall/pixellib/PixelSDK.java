package com.pixelall.pixellib;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.pixelall.pixellib.util.CheckBitmapCallback;
import com.pixelall.pixellib.util.PixelUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.https.HttpsUtils;

import okhttp3.Call;

/**
 * Created by lxl on 2017/3/14.
 *  广州像素数据技术股份有限公司
 */

public class PixelSDK implements CheckBitmapCallback{
    private Context context;
    private PixelCallBack callBack;
    private PixelResult pixelResult;
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
        initPixelParams();
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
            OkHttpUtils.get().url("http://www.baidu.com").build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    PixelResult pixelResult=new PixelResult();
                    pixelResult.setResultCode(PixelCode.OTHER_FAIL);
                    pixelResult.setResultMessage("请求发生错误");
                    callBack.checkResult(pixelResult);
                }

                @Override
                public void onResponse(String response, int id) {
                    PixelResult pixelResult=new PixelResult();
                    pixelResult.setResultCode(PixelCode.SUCCESS);
                    pixelResult.setResultMessage(response);
                    callBack.checkResult(pixelResult);
                }
            });
        }else {
            callBack.checkResult(pixelResult);
        }
    }
}
