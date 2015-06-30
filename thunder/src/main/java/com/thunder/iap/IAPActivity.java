package com.thunder.iap;

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
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;
import com.thunder.iap.listener.BuyItemListener;
import com.thunder.iap.listener.BuySubscriptionListener;
import com.thunder.iap.listener.ConsumePurchaseListener;
import com.thunder.iap.listener.PurchasedItemsListener;
import com.thunder.iap.listener.SkuDetailsListener;
import com.thunder.iap.model.PurchasedItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class IAPActivity extends Activity {

    private IInAppBillingService mService;
    private BuyItemListener buyItemListener;
    private BuySubscriptionListener buySubscriptionListener;
    private static final int RC_INAPP_BUY = 1001;
    private static final int RC_SUBS_BUY = 9999;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    public void getSkuDetails(final ArrayList<String> skuList, final SkuDetailsListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                try {
                    Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
                    ArrayList<JSONObject> result = new ArrayList<JSONObject>();
                    int response = skuDetails.getInt("RESPONSE_CODE");
                    if (response == 0) {
                        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
                        for (String thisResponse : responseList) {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(thisResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //String sku = object.getString("productId");
                            //String price = object.getString("price");
                            //if (sku.equals("premiumUpgrade")) mPremiumUpgradePrice = price;
                            //else if (sku.equals("gas")) mGasPrice = price;
                            result.add(object);
                        }
                        listener.onResult(result);
                    } else {
                        listener.onServerError(skuDetails);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    public void buyItem(final String sku, final String developerPayload, final BuyItemListener buyItemListener){
        this.buyItemListener = buyItemListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                            sku, "inapp", developerPayload);
                    int response = buyIntentBundle.getInt("RESPONSE_CODE");
                    if (response == 0) {
                        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                        try {
                            startIntentSenderForResult(pendingIntent.getIntentSender(), RC_INAPP_BUY, new Intent(), Integer.valueOf(0),
                                    Integer.valueOf(0), Integer.valueOf(0));
                            //the onSuccess callback of the listener will be called on the method onActivityResult
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            buyItemListener.onError(e);
                        }
                    } else {
                        buyItemListener.onServerError(buyIntentBundle);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    buyItemListener.onError(e);
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_INAPP_BUY) {
            //in app item bought
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    //String sku = jo.getString("productId");
                    if (buyItemListener != null) {
                        buyItemListener.onSuccess(jo);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (buyItemListener != null) {
                    buyItemListener.onError(new RuntimeException("Error during onActivityResult"));
                }
            }
        }
        if (requestCode == RC_SUBS_BUY){
            //subscription bought
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    //String sku = jo.getString("productId");
                    if (buySubscriptionListener != null) {
                        buySubscriptionListener.onSuccess(jo);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (buySubscriptionListener != null) {
                    buySubscriptionListener.onError(new RuntimeException("Error during onActivityResult"));
                }
            }
        }
    }

    public void getInAppPurchasedItems(final PurchasedItemsListener purchasedItemsListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPurchasedItems("inapp", purchasedItemsListener);
            }
        }).start();
    }


    public void consumePurchase(final String purchaseToken, final ConsumePurchaseListener consumePurchaseListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int response = mService.consumePurchase(3, getPackageName(), purchaseToken);
                    if (response == 0) {
                        consumePurchaseListener.onSuccess();
                    } else {
                        consumePurchaseListener.onServerError(response);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    consumePurchaseListener.onError(e);
                }
            }
        }).start();
    }

    public void buySubscription(final String sku, final String developerPayload, final BuySubscriptionListener buySubscriptionListener){
        this.buySubscriptionListener = buySubscriptionListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bundle = mService.getBuyIntent(3, getPackageName(),
                            sku, "subs", developerPayload);
                    PendingIntent pendingIntent = bundle.getParcelable("BUY_INTENT");
                    if (bundle.getInt("RESPONSE_CODE") == 0) {
                        // Start purchase flow (this brings up the Google Play UI).
                        // Result will be delivered through onActivityResult().
                        try {
                            startIntentSenderForResult(pendingIntent.getIntentSender(), RC_SUBS_BUY, new Intent(),
                                    Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            buySubscriptionListener.onError(e);
                        }
                    } else {
                        buySubscriptionListener.onServerError(bundle);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    buySubscriptionListener.onError(e);
                }
            }
        }).start();
    }

    public void getPurchasedSubscriptions(final PurchasedItemsListener purchasedItemsListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPurchasedItems("subs", purchasedItemsListener);
            }
        }).start();
    }

    public void getPurchasedItems(String itemType, PurchasedItemsListener purchasedItemsListener){
        try {
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), itemType, null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String>  purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String>  signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");
                Map<String, PurchasedItem> skuToPurchasedItem = new HashMap<String, PurchasedItem>();
                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    // do something with this purchase information
                    // e.g. display the updated list of products owned by user
                    skuToPurchasedItem.put(sku, new PurchasedItem(purchaseData, signature, sku));
                }
                while (continuationToken != null){
                    try {
                        ownedItems = mService.getPurchases(3, getPackageName(), itemType, continuationToken);
                        response = ownedItems.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                            purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                            signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                            continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");
                            for (int i = 0; i < purchaseDataList.size(); ++i) {
                                String purchaseData = purchaseDataList.get(i);
                                String signature = signatureList.get(i);
                                String sku = ownedSkus.get(i);
                                // do something with this purchase information
                                // e.g. display the updated list of products owned by user
                                skuToPurchasedItem.put(sku, new PurchasedItem(purchaseData, signature, sku));
                            }
                        } else {
                            purchasedItemsListener.onServerError(ownedItems);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        purchasedItemsListener.onError(e);
                    }
                }
                // if continuationToken != null, call getPurchases again
                // and pass in the token to retrieve more items
            } else {
                purchasedItemsListener.onServerError(ownedItems);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            purchasedItemsListener.onError(e);
        }
    }


}
