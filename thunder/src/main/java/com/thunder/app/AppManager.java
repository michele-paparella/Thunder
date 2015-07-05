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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.thunder.R;
import com.thunder.exception.DataNotAvailableException;

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

    public static String getAppName(Context context) throws PackageManager.NameNotFoundException, DataNotAvailableException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        if (context.getPackageManager().getApplicationLabel(pInfo.applicationInfo) != null) {
            return context.getPackageManager().getApplicationLabel(pInfo.applicationInfo).toString();
        }
        throw new DataNotAvailableException(context.getString(R.string.app_name_not_found));
    }

    public static String getPackage(Context context){
        return context.getPackageName();
    }

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

}
