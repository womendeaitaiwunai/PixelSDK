package com.pixelall.pixellib.util;

import android.text.TextUtils;

import com.pixelall.pixellib.PixelCode;
import com.pixelall.pixellib.PixelResult;

/**
 * Created by lxl on 2017/3/14.
 * 获取错误信息
 */

public class PixelErrorMessage {
    public static String getErrorMessage(PixelResult result){
        if (!TextUtils.isEmpty(result.getResultMessage())) return result.getResultMessage();
        if (result.getResultCode()== PixelCode.CONTEXT_ERROR){
            return "初始化上下文出错";
        }else if(result.getResultCode()== PixelCode.APP_KEY_OR_COMPANY_EMPTY_ERROR){
            return "未设置公司或APP_KEY";
        }else if(result.getResultCode()== PixelCode.APP_KEY_OR_COMPANY_ERROR){
            return "设置的公司和APP_KEY不对应";
        }else if(result.getResultCode()== PixelCode.BITMAP_NULL){
            return "检测的照片是空";
        }else if(result.getResultCode()== PixelCode.MANY_FACE_ERROR){
            return "检测到多张人脸";
        }else if(result.getResultCode()== PixelCode.NO_FACE_ERROR){
            return "未检测到人脸";
        }else if(result.getResultCode()== PixelCode.FACE_EX_ERROR){
            return "人脸识别异常问题";
        }else if(result.getResultCode()== PixelCode.FACE_AREA_BIG_ERROR){
            return "人脸区域太大";
        }else if(result.getResultCode()== PixelCode.FACE_AREA_SMALL_ERROR){
            return "人脸区域太小";
        }else if(result.getResultCode()== PixelCode.SERVICE_URL_ERROR){
            return "服务器地址错误";
        }else if(result.getResultCode()== PixelCode.SERVICE_CONNECT_ERROR){
            return "连接服务器失败";
        }else if(result.getResultCode()== PixelCode.COMMIT_DATA_ERROR){
            return "提交数据出错";
        }else if(result.getResultCode()== PixelCode.RETURN_DATA_ERROR){
            return "返回数据错误";
        }else if(result.getResultCode()== PixelCode.SERVICE_TIME_OUT){
            return "服务器连接超时";
        } else return "其他错误";
    }
}
