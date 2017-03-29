package com.pixelall.pixellib;

/**
 * Created by lxl on 2017/3/14.
 * 结果回调界面
 */

public interface PixelCallBack {
    /**
     *
     * @param type 检查类型  PixelSDK.TYPE_CHECK_FACE（图片人脸检测）
     *                       PixelSDK.TYPE_UPLOAD_BITMAP （上传图片）
     * @param pixelResult 检查结果
     */
    void checkResult(int type ,PixelResult pixelResult);
}
