package com.pixelall.pixellib.camera;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_1080P;
import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_2160P;
import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_480P;
import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_720P;
import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_HIGHEST;
import static com.pixelall.pixellib.camera.CameraKit.Constants.VIDEO_QUALITY_LOWEST;

@Retention(RetentionPolicy.SOURCE)
@IntDef({VIDEO_QUALITY_480P, VIDEO_QUALITY_720P, VIDEO_QUALITY_1080P, VIDEO_QUALITY_2160P, VIDEO_QUALITY_HIGHEST, VIDEO_QUALITY_LOWEST})
public @interface VideoQuality {
}
