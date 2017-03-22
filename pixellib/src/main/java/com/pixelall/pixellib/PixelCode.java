package com.pixelall.pixellib;

/**
 * Created by lxl on 2017/3/14.
 * 回调错误表
 */

public class PixelCode {
    public static int OTHER_FAIL=-0x01;//其他错误
    public static int SUCCESS=0x00;//成功

    public static int CONTEXT_ERROR=0x01; //初始化Context出错
    public static int APP_KEY_OR_COMPANY_EMPTY_ERROR=0x02; //未设置公司或APP_KEY
    public static int APP_KEY_OR_COMPANY_ERROR=0x03;//设置的公司和APP_KEY不对应

    public static int BITMAP_NULL=0x04;//检测的照片是空

    public static int MANY_FACE_ERROR=0x05;//检测到多张人脸
    public static int NO_FACE_ERROR=0x06;//未检测到人脸
    public static int FACE_EX_ERROR=0x07;//人脸识别异常问题

    public static int FACE_AREA_BIG_ERROR=0x08;//人脸区域太大
    public static int FACE_AREA_SMALL_ERROR=0x09;//人脸区域太小

    public static int SERVICE_URL_ERROR=0x10;//服务器地址错误
    public static int SERVICE_CONNECT_ERROR=0x11;//连接服务器失败
    public static int COMMIT_DATA_ERROR=0x12;//提交数据出错
    public static int RETURN_DATA_ERROR=0x13;//返回数据错误

}
