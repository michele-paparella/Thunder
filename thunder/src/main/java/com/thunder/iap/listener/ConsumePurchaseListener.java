package com.thunder.iap.listener;

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

public interface ConsumePurchaseListener {

    public void onSuccess();

    //generic exception
    public void onError(Exception e);

    //this callback is called after an error code is sent from the server
    //http://developer.android.com/google/play/billing/billing_reference.html#billing-codes
    public void onServerError(int responseCode);
}
