package com.apps.hassan.thaifoodidentifier;

import android.support.annotation.Nullable;

import com.wonderkiln.camerakit.Size;

import java.io.File;

import javax.xml.transform.Result;

public class ResultHolder {

    private static byte[] image;
    private static File video;
    private static Size nativeCaptureSize;
    private static long timeToCallback;
    private static String className;
    private static String latitude;
    private static String longitude;


    public static void setImage(@Nullable byte[] image) {

        ResultHolder.image = image;
    }

    public static void setClassName(@Nullable String className){
        ResultHolder.className = className;
    }

    public static String getClassName(){
        return className;
    }

    public static void setLatitude(@Nullable String latitude){
        ResultHolder.latitude = latitude;
    }

    public static String getLatitude(){
        return latitude;
    }

    public static void setLongitude(@Nullable String longitude){
        ResultHolder.longitude = longitude;
    }

    public static String getLongitude(){
        return longitude;
    }

    @Nullable
    public static byte[] getImage() {
        return image;
    }

    public static void setVideo(@Nullable File video) {
        ResultHolder.video = video;
    }

    @Nullable
    public static File getVideo() {
        return video;
    }

    public static void setNativeCaptureSize(@Nullable Size nativeCaptureSize) {
        ResultHolder.nativeCaptureSize = nativeCaptureSize;
    }

    @Nullable
    public static Size getNativeCaptureSize() {
        return nativeCaptureSize;
    }

    public static void setTimeToCallback(long timeToCallback) {
        ResultHolder.timeToCallback = timeToCallback;
    }

    public static long getTimeToCallback() {
        return timeToCallback;
    }

    public static void dispose() {
        setImage(null);
        setNativeCaptureSize(null);
        setTimeToCallback(0);
    }

}
