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


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.thunder.R;
import com.thunder.prefs.SharedPrefsManager;

/**
 * A useful class that allows you to present a RatingPopup for your app
 */
public class RatingPopupImpl implements RatingPopup{


    private static final String PREFS_NAME = "rating_popup_prefs";
    private static final String CURRENT_RATING_POPUP = "currentRatingPopup";
    //this flag is true only if the algorithm should check for the first_rating_popup value in the "thunder_config.xml" file
    private static final String SHOULD_CHECK_FOR_FIRST_RATING_POPUP = "should_check_for_first_rating_popup";
    private static final String NEXT_RATING_POPUP = "next_rating_popup";

    private static RatingPopupListener ratingPopupListener;
    private static SharedPrefsManager sharedPrefsManager;
    private static Activity activity;
    private static RatingPopupImpl instance;

    /**
     * singleton implementation
     * @param activity
     * @param listener
     * @return
     */
    public static RatingPopupImpl getInstance(Activity activity, RatingPopupListener listener) {
        if (instance == null){
            activity = activity;
            ratingPopupListener = listener;
            sharedPrefsManager = new SharedPrefsManager(activity, PREFS_NAME, Context.MODE_PRIVATE);
            instance = new RatingPopupImpl();
        }
        return instance;
    }

    @Override
    public void onResume() {
        //updating the values taken from "thunder_config.xml" file
        if (!sharedPrefsManager.contains(CURRENT_RATING_POPUP)){
            //first install
            sharedPrefsManager.putIntSync(CURRENT_RATING_POPUP, 0);
        }
        if (sharedPrefsManager.getInt(CURRENT_RATING_POPUP, -1) != -1){
            sharedPrefsManager.incrementSync(CURRENT_RATING_POPUP, 0, 1);
            if (sharedPrefsManager.getBoolean(SHOULD_CHECK_FOR_FIRST_RATING_POPUP, true) &&
                    sharedPrefsManager.getInt(CURRENT_RATING_POPUP, -1) == getConfigurationValue(R.integer.first_rating_popup)) {
                if (!activity.isFinishing()) {
                    try {
                        sharedPrefsManager.putBooleanSync(SHOULD_CHECK_FOR_FIRST_RATING_POPUP, false);
                        showRateDialog();
                        if (ratingPopupListener != null){
                            ratingPopupListener.onRatingShow();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else if (sharedPrefsManager.getInt(CURRENT_RATING_POPUP, -1) == sharedPrefsManager.getInt(NEXT_RATING_POPUP, -1)) {
                if (!activity.isFinishing()) {
                    try {
                        showRateDialog();
                        if (ratingPopupListener != null){
                            ratingPopupListener.onRatingShow();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private int getConfigurationValue(int id){
        return activity.getResources().getInteger(id);
    }

    /**
     * Show the rate dialog
     *
     */
    public void showRateDialog() {
        //TODO support multiple languages
        //AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(R.string.rating_dialog_title);
        builder.setMessage(R.string.rating_dialog_message);
        builder.setPositiveButton(R.string.rating_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String appPackage = activity.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
                activity.startActivity(intent);
                //it will never show the popup
                sharedPrefsManager.putIntSync(CURRENT_RATING_POPUP, -1);
                if (ratingPopupListener != null) {
                    ratingPopupListener.onRatingOk();
                }
            }
        });
        builder.setNeutralButton(R.string.rating_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPrefsManager.incrementSync(NEXT_RATING_POPUP, sharedPrefsManager.getInt(CURRENT_RATING_POPUP, 0),
                        getConfigurationValue(R.integer.neutral_rating_popup));
                if (ratingPopupListener != null) {
                    ratingPopupListener.onRatingLater();
                }
            }
        });
        builder.setNegativeButton(R.string.rating_dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //it will never show the popup
                sharedPrefsManager.putIntSync(CURRENT_RATING_POPUP, -1);
                if (ratingPopupListener != null) {
                    ratingPopupListener.onRatingNo();
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                sharedPrefsManager.incrementSync(NEXT_RATING_POPUP, sharedPrefsManager.getInt(CURRENT_RATING_POPUP, 0),
                        getConfigurationValue(R.integer.negative_rating_popup));
                if (ratingPopupListener != null) {
                    ratingPopupListener.onRatingCancel();
                }
            }
        });
        builder.create().show();
    }

}
