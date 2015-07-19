package com.thunder.sample;

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

import com.thunder.rating.RatingPopupListener;

public class CustomRatingPopupListener implements RatingPopupListener {

    @Override
    public void onRatingShow() {
        //the user sees the popup
    }

    @Override
    public void onRatingOk() {
        //the user wants to rate your app!
    }

    @Override
    public void onRatingCancel() {
        //the user cancelled the popup (e.g. throught onBackPressed)
    }

    @Override
    public void onRatingLater() {
        //the user pressed the "later" button
    }

    @Override
    public void onRatingNo() {
        //the user pressed the "never" button
    }

}
