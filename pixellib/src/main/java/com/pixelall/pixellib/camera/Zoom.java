package com.pixelall.pixellib.camera;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pixelall.pixellib.camera.CameraKit.Constants.ZOOM_OFF;
import static com.pixelall.pixellib.camera.CameraKit.Constants.ZOOM_PINCH;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ZOOM_OFF, ZOOM_PINCH})
public @interface Zoom {
}
