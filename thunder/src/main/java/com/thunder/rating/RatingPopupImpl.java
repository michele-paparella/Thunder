package com.thunder.rating;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.thunder.R;
import com.thunder.prefs.SharedPrefsManager;

/**
 *
 */
//TODO test
public class RatingPopupImpl implements RatingPopup{


    private static final String PREFS_NAME = "rating_popup_prefs";
    private static final String FIRST_RATING_POPUP = "first_rating_popup";
    private static final String MALUS_RATING_POPUP = "malus_rating_popup";
    private static final String CURRENT_RATING_POPUP = "currentRatingPopup";
    private static final String NEXT_RATING_POPUP = "nextRatingPopup";

    private RatingPopupListener listener;
    private static SharedPrefsManager sharedPrefsManager;
    private static Context context;
    private static RatingPopupImpl instance;

    public static RatingPopupImpl getInstance(Context c) {
        if (instance == null){
            context = c;
            sharedPrefsManager = new SharedPrefsManager(context, PREFS_NAME, Context.MODE_PRIVATE);
            instance = new RatingPopupImpl();
        }
        return instance;
    }

    @Override
    public void onResume() {
        if (getPrefs(CURRENT_RATING_POPUP) != -1) {
            sharedPrefsManager.incrementSync(CURRENT_RATING_POPUP, -1, 1);
            if (getPrefs(FIRST_RATING_POPUP) != -1 && getPrefs(CURRENT_RATING_POPUP) == getPrefs(FIRST_RATING_POPUP)) {
                sharedPrefsManager.putIntSync(FIRST_RATING_POPUP, -1);
                sharedPrefsManager.putIntSync(NEXT_RATING_POPUP, getPrefs(MALUS_RATING_POPUP));
                showRateDialog();
            } else if (sharedPrefsManager.getInt(CURRENT_RATING_POPUP, -1) == sharedPrefsManager.getInt(NEXT_RATING_POPUP, -1)) {
                showRateDialog();
            }
        }
    }

    private int getPrefs(String key){
        return sharedPrefsManager.getInt(key, -1);
    }

    @Override
    public void setRatingPopupListener(RatingPopupListener listener) {
        this.listener = listener;
    }

    /**
     * Show the rate dialog
     * //TODO support multiple languages
     */
    public void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rating_dialog_title);
        builder.setMessage(R.string.rating_dialog_message);
        builder.setPositiveButton(R.string.rating_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String appPackage = context.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
                context.startActivity(intent);
                //deleting all the preferences
                sharedPrefsManager.clearSync();
                listener.onRatingOk();
            }
        });
        builder.setNeutralButton(R.string.rating_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPrefsManager.putIntSync(NEXT_RATING_POPUP, getPrefs(MALUS_RATING_POPUP));
                listener.onRatingLater();
            }
        });
        builder.setNegativeButton(R.string.rating_dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //deleting all the preferences
                sharedPrefsManager.clearSync();
                listener.onRatingNo();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onRatingCancel();
            }
        });
        builder.create().show();
    }

}
