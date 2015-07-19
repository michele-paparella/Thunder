package com.thunder.device;

/*
 * Copyright (C) 2015 Michele Paparella
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.thunder.R;
import com.thunder.exception.DataNotAvailableException;

import java.io.IOException;

public class DeviceManager {

    // Do not call this function from the main thread. Otherwise,
    // an IllegalStateException will be thrown.
    //TODO test
    public static String getAdvertisingId(Context context) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException, DataNotAvailableException {
        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        if (adInfo != null) {
            final String id = adInfo.getId();
            return id;
        } else {
            throw new DataNotAvailableException(context.getString(R.string.google_analytics_problem));
        }
    }

    //TODO test
    public static boolean isLimitAdTrackingEnabled(Context context) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException, DataNotAvailableException {
        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        if (adInfo != null) {
            final boolean isLAT = adInfo.isLimitAdTrackingEnabled();
            return isLAT;
        } else {
            throw new DataNotAvailableException(context.getString(R.string.google_analytics_problem));
        }
    }

    /**
     *
     * @param context
     * @return The absolute height of the display in pixels.
     */
    public static int getDisplayHeight(Context context){
        Point point = getDisplaySize(context);
        return point.y;
    }

    /**
     *
     * @param context
     * @return The absolute width of the display in pixels.
     */
    public static int getDisplayWidth(Context context){
        Point point = getDisplaySize(context);
        return point.x;
    }

    /**
     * Gets the size of the display, in pixels.
     * <p>
     * Note that this value should <em>not</em> be used for computing layouts,
     * since a device will typically have screen decoration (such as a status bar)
     * along the edges of the display that reduce the amount of application
     * space available from the size returned here.  Layouts should instead use
     * the window size.
     * </p><p>
     * The size is adjusted based on the current rotation of the display.
     * </p><p>
     * The size returned by this method does not necessarily represent the
     * actual raw size (native resolution) of the display.  The returned size may
     * be adjusted to exclude certain system decoration elements that are always visible.
     * It may also be scaled to provide compatibility with older applications that
     * were originally designed for smaller displays.
     * </p>
     */
    public static Point getDisplaySize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    private static DisplayMetrics getDisplayMetrics(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     *
     * @param context
     * @return The screen density expressed as dots-per-inch.
     * May be either DisplayMetrics.DENSITY_LOW, DisplayMetrics.DENSITY_MEDIUM,
     * or DisplayMetrics.DENSITY_HIGH.
     */
    public static int getDensityDpi(Context context){
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.densityDpi;
    }

    /**
     *
     * @param context
     * @return The logical density of the display.  This is a scaling factor for the
     * Density Independent Pixel unit, where one DIP is one pixel on an
     * approximately 160 dpi screen (for example a 240x320, 1.5"x2" screen),
     * providing the baseline of the system's display. Thus on a 160dpi screen
     * this density value will be 1; on a 120 dpi screen it would be .75; etc.
     *
     * <p>This value does not exactly follow the real screen size (as given by
     * DisplayMetrics.xdpi and DisplayMetrics.ydpi, but rather is used to scale the size of
     * the overall UI in steps based on gross changes in the display dpi.  For
     * example, a 240x320 screen will have a density of 1 even if its width is
     * 1.8", 1.3", etc. However, if the screen resolution is increased to
     * 320x480 but the screen size remained 1.5"x2" then the density would be
     * increased (probably to 1.5).
     *
     */
    public static float getDensity(Context context){
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.density;
    }

    /**
     * The user-visible SDK version of the framework; its possible
     * values are defined in {@link Build.VERSION_CODES}.
     */
    public static int getApiVersion(Context context){
        return Build.VERSION.SDK_INT;
    }

    /** The end-user-visible name for the end product. */
    public static String getDeviceName(Context context){
        return Build.MODEL;
    }

    /** The manufacturer of the product/hardware. */
    public static String getManufacturerName(Context context){
        return Build.MANUFACTURER;
    }

    /**
     * Returns the phone number string for line 1, for example, the MSISDN
     * for a GSM phone. Return null if it is unavailable.
     **/
    public static String getMsisdn(Context context){
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    /**
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     *
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getImei(Context context){
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getDeviceId();
    }

    public static String getBoardName(){
        return Build.BOARD;
    }

    public static String getBuildFingerprint(){
        return Build.FINGERPRINT;
    }

    public static String getBootloaderVersionNumber(){
        return Build.BOOTLOADER;
    }

    public static String getBrand(){
        return Build.BRAND;
    }

    public static String getCpuAbi(Context context){
        if (Build.VERSION.SDK_INT >= 21){
            String[] abis = Build.SUPPORTED_ABIS;
            if (abis != null && abis.length > 0) {
                return abis[0];
            } else {
                return context.getString(R.string.cpu_abi_not_available);
            }
        } else {
            return Build.CPU_ABI;
        }
    }

    public static String getIndustrialDeviceName(){
        return Build.DEVICE;
    }

    public static String getBuildId(){
        return Build.DISPLAY;
    }

    public static String getHardwareName(){
        return Build.HARDWARE;
    }

    public static String getHardwareSerialNumber() {
        return Build.SERIAL;
    }

    public static RamData getAvailableRam(Context context){
        if (DeviceManager.getApiVersion(context) >= Build.VERSION_CODES.JELLY_BEAN) {
            return getAvailableRamNewApi(context);
        } else {
            return getAvailableRamOldApi(context);
        }
    }

    /**
     * for Api < 16
     * @param context
     * @return @link RamData object with only availableMemory. Other fields, such as totalMemory are equal to -1
     */
    public static RamData getAvailableRamOldApi(Context context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMemory = mi.availMem;
        long totalMemory = -1;
        long usedMemory = -1;
        double percentage = -1;
        return new RamData(availableMemory, usedMemory, percentage, totalMemory);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static RamData getAvailableRamNewApi(Context context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMemory = mi.availMem;
        long totalMemory = mi.totalMem;
        long usedMemory = totalMemory-availableMemory;
        double percentage = ((double) usedMemory)/ totalMemory;
        return new RamData(availableMemory, usedMemory, percentage, totalMemory);
    }

    public static class RamData {

        private long availableMemory;
        private long usedMemory;
        private double percentage;
        private long totalMemory;

        public RamData(long availableMemory, long usedMemory, double percentage, long totalMemory){
            this.availableMemory = availableMemory;
            this.usedMemory = usedMemory;
            this.percentage = percentage;
            this.totalMemory = totalMemory;
        }

        public long getAvailableMemory() {
            return availableMemory;
        }

        public void setAvailableMemory(long availableMemory) {
            this.availableMemory = availableMemory;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public void setTotalMemory(long totalMemory) {
            this.totalMemory = totalMemory;
        }

        public long getUsedMemory() {
            return usedMemory;
        }

        public void setUsedMemory(long usedMemory) {
            this.usedMemory = usedMemory;
        }

        /**
         * @return true if the clients should use only the @link availableMemory field, false otherwise
         */
        public boolean availableMemoryOnlyLoaded(){
            return totalMemory == -1 && percentage == -1 && usedMemory == -1;
        }
    }

    public static FsData getAvailableSpaceOnExternalMemory(Context context){
        if (DeviceManager.getApiVersion(context) >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return getAvailableSpaceNewApi(Environment.getExternalStorageDirectory().getPath());
        } else {
            return getAvailableSpaceOldApi(Environment.getExternalStorageDirectory().getPath());
        }
    }

    public static FsData getAvailableSpaceOnInternalMemory(Context context){
        if (DeviceManager.getApiVersion(context) >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return getAvailableSpaceNewApi(Environment.getDataDirectory().getPath());
        } else {
            return getAvailableSpaceOldApi(Environment.getDataDirectory().getPath());
        }
    }

    /**
     * For target Api < 18
     * @return
     */
    private static FsData getAvailableSpaceOldApi(String path){
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long availableSpace = statFs.getAvailableBlocks()*blockSize;
        long totalSpace = statFs.getBlockCount()*blockSize;
        long usedSpace = totalSpace - availableSpace;
        double percentage = ((double) usedSpace)/ totalSpace;
        return new FsData(availableSpace, usedSpace, totalSpace, percentage);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static FsData getAvailableSpaceNewApi(String path){
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long availableSpace = statFs.getAvailableBlocksLong()*blockSize;
        long totalSpace = statFs.getBlockCountLong()*blockSize;
        long usedSpace = totalSpace - availableSpace;
        double percentage = ((double) usedSpace)/ totalSpace;
        return new FsData(availableSpace, usedSpace, totalSpace, percentage);
    }

    public static class FsData {

        private long availableSpace;

        private long usedSpace;

        private long totalSpace;

        private double percentage;

        public FsData(long availableSpace, long usedSpace, long totalSpace, double percentage) {
            this.availableSpace = availableSpace;
            this.usedSpace = usedSpace;
            this.totalSpace = totalSpace;
            this.percentage = percentage;
        }

        public long getAvailableSpace() {
            return availableSpace;
        }

        public void setAvailableSpace(long availableSpace) {
            this.availableSpace = availableSpace;
        }

        public long getUsedSpace() {
            return usedSpace;
        }

        public void setUsedSpace(long usedSpace) {
            this.usedSpace = usedSpace;
        }

        public long getTotalSpace() {
            return totalSpace;
        }

        public void setTotalSpace(long totalSpace) {
            this.totalSpace = totalSpace;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }

    public static boolean deviceHasExternalStorage(){
        return isExternalStorageReadable();
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        if (Environment.getExternalStorageDirectory().getPath().contains("emulated")){
            return false;
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}
