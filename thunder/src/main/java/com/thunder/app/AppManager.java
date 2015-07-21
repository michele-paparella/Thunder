package com.thunder.app;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import com.thunder.R;
import com.thunder.exception.DataNotAvailableException;

/**
 * This class is responsible for managing data related to the app, e.g. version code, app name.
 */
public class AppManager {

    /**
     * The version name of this package, as specified by the &lt;manifest&gt;
     * tag's attribute.
     */
    public static String getAppVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionName;
    }

    /**
     * The version number of this package, as specified by the &lt;manifest&gt;
     * tag's attribute.
     */
    public static int getAppVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionCode;
    }

    /**
     *
     * @param context
     * @return the app name string taken from the manifest
     * @throws PackageManager.NameNotFoundException
     * @throws DataNotAvailableException
     */
    public static String getAppName(Context context) throws PackageManager.NameNotFoundException, DataNotAvailableException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        if (context.getPackageManager().getApplicationLabel(pInfo.applicationInfo) != null) {
            return context.getPackageManager().getApplicationLabel(pInfo.applicationInfo).toString();
        }
        throw new DataNotAvailableException(context.getString(R.string.app_name_not_found));
    }

    /**
     *
     * @param context
     * @return the current package name
     */
    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    /**
     * it starts a new email intent
     *
     * @param context
     * @param address the recipient address
     * @param subject of the email
     * @param text the message
     */
    public static void startEmailIntent(Context context, String address, String subject, String text){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", address, null));
        if (subject != null && !subject.isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (text != null && !text.isEmpty()) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.chooser_email_title)));
    }

    /**
     * it starts a new sharing intent
     *
     * @param context
     * @param textData the shared data
     */
    public static void startSharingIntent(Context context, String textData){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textData);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    /**
     * this method hides the soft keyboard of the device
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
