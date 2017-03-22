# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidStudioSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames


-keep class com.pixelall.pixellib.PixelCallBack{*;}
-keep class com.pixelall.pixellib.PixelCode{*;}
-keep class com.pixelall.pixellib.PixelResult{*;}
-keep class com.pixelall.pixellib.PixelSDK{
  public *;
}

