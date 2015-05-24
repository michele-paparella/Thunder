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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.thunder.exception.DataNotAvailableException;

import java.io.IOException;

public class DeviceManager {

    public static String getImei(Context context) throws DataNotAvailableException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && telephonyManager.getDeviceId() != null && !telephonyManager.getDeviceId().isEmpty()) {
            return telephonyManager.getDeviceId();
        }
        else {
            throw new DataNotAvailableException("IMEI not found");
        }
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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

    //TODO
    public static void getResolution(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getScreenSize(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getApiVersion(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getDeviceName(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getManufacturerName(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getAvailableMemory(Context context){
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    public static void getMSISDN(Context context){
        throw new RuntimeException("not yet implemented");
    }

}
