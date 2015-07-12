package com.thunder.network;

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
import android.util.Log;

import com.thunder.R;
import com.thunder.exception.DataNotAvailableException;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkManager {

    private final static String TAG = "NetworkManager";

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
            throw new DataNotAvailableException(context.getString(R.string.device_not_connected));
        }
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mgr.getActiveNetworkInfo().getType();
    }



    public static String printActiveConnectionType(Context context) throws DataNotAvailableException {
        int type = getActiveConnectionType(context);
        switch (type){
            case ConnectivityManager.TYPE_MOBILE:
                return "Mobile";
            case ConnectivityManager.TYPE_WIFI:
                return "WiFi";
            case ConnectivityManager.TYPE_WIMAX:
                return "WiMax";
            case ConnectivityManager.TYPE_ETHERNET:
                return "Ethernet";
            case ConnectivityManager.TYPE_BLUETOOTH:
                return "Bluetooth";
            case ConnectivityManager.TYPE_VPN:
                return "VPN";
            case ConnectivityManager.TYPE_MOBILE_DUN:
                return "Mobile DUN";
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                return "Mobile HIPRI";
            case ConnectivityManager.TYPE_MOBILE_MMS:
                return "Mobile MMS";
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                return "Mobile Supl";
        }
        return "";
    }


    public static List<String> getIPv4Address(Context context) throws DataNotAvailableException {
        return getIP(context, true);
    }

    public static List<String> getIPv6Address(Context context) throws DataNotAvailableException {
        return getIP(context, false);
    }

    private static List<String> getIP(Context context, boolean ipv4) throws DataNotAvailableException {
        try {
            ArrayList<String> result = new ArrayList<>(10);
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (ipv4 && inetAddress instanceof Inet4Address) {
                        result.add(inetAddress.getHostAddress());
                    }
                    else if (!ipv4 && inetAddress instanceof Inet6Address) {
                        result.add(inetAddress.getHostAddress());
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        throw new DataNotAvailableException(context.getString(R.string.ip_not_available));
    }

    /**
     *
     * @param host
     * @param times
     * @param listener
     */
    public static void ping(final String host, final int times, final OnPartialResultListener listener) {
        NetworkCommand command = new PingCommand(times);
        command.runCommand(host, listener);
    }

    /**
     * WARNING: traceroute needs root permission
     * @param host
     * @param listener
     */
    @Deprecated
    public static void traceroute(final String host, final OnPartialResultListener listener) {
        NetworkCommand command = new TracerouteCommand();
        command.runCommand(host, listener);
    }

    /**
     *
     * @param host
     * @param listener
     */
    public static void nsLookup(final String host, final OnPartialResultListener listener) {
        NetworkCommand command = new NsLookupCommand();
        command.runCommand(host, listener);
    }



}
