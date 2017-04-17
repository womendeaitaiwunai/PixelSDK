package com.pixelall.pixellib.camera;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pixelall.pixellib.camera.CameraKit.Constants.FOCUS_CONTINUOUS;
import static com.pixelall.pixellib.camera.CameraKit.Constants.FOCUS_OFF;
import static com.pixelall.pixellib.camera.CameraKit.Constants.FOCUS_TAP;

@Retention(RetentionPolicy.SOURCE)
@IntDef({FOCUS_CONTINUOUS, FOCUS_TAP, FOCUS_OFF})
public @interface Focus {
}