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

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.thunder.exception.DataNotAvailableException;

import java.io.IOException;

public class DeviceManager {

    /**
     * @param context
     * @return true if the device is connected to internet, false otherwise
     */
    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Reports the type of network to which the
     * info in this {@code NetworkInfo} pertains.
     * @return one of {@link ConnectivityManager#TYPE_MOBILE}, {@link
     * ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_WIMAX}, {@link
     * ConnectivityManager#TYPE_ETHERNET},  {@link ConnectivityManager#TYPE_BLUETOOTH}, or other
     * types defined by {@link ConnectivityManager}
     */
    public static int getActiveConnectionType(Context context) throws DataNotAvailableException {
        if (!isConnected(context)){
            throw new DataNotAvailableException("device is not connected");
        }
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mgr.getActiveNetworkInfo().getType();
    }

    // Do not call this function from the main thread. Otherwise,
    // an IllegalStateException will be thrown.
    //TODO test
    public static String getAdvertisingId(Context context) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException, DataNotAvailableException {
        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        if (adInfo != null) {
            final String id = adInfo.getId();
            return id;
        } else {
            throw new DataNotAvailableException("Problem with Google Analytics - getAdvertisingId");
        }
    }

    //TODO test
    public static boolean isLimitAdTrackingEnabled(Context context) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException, DataNotAvailableException {
        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        if (adInfo != null) {
            final boolean isLAT = adInfo.isLimitAdTrackingEnabled();
            return isLAT;
        } else {
            throw new DataNotAvailableException("Problem with Google Analytics - isLimitAdTrackingEnabled");
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

}
