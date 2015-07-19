Thunder 2.0
==============
*Thunder* is a flexible library for Android that helps you to build apps painlessly and quickly. It contains the following modules out of the box:

- App
- Crypto
- Device
- IAP
- Network
- Prefs
- Rating

Download
==============
You can grab the last version [**HERE**] in the *aar* format


Modules 
==============
The library has been designed with one goal: make the developer life easier with a bunch of useful classes and methods. The available modules in the last version of *Thunder* are following.

App
--------------
This module is designed to gives you the chance of getting related data of your app (e.g. version code) through a simple class - [*AppManager*](thunder/src/main/java/com/thunder/app/AppManager.java). It allows, also, to send an email or to start an intent for sharing text with only one line of code (check the methods *startEmailIntent* or *startShareIntent*).

Crypto
--------------
It contains the most famous hashing algorithms, such as *MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512*. You can use this module through the class [*CryptoHelper*](thunder/src/main/java/com/thunder/crypto/CryptoHelper.java).

Device
--------------
This module provides useful data about your physical device, e.g. screen resolution, OS version, free space or available RAM - check the class [*DeviceManager*](thunder/src/main/java/com/thunder/device/DeviceManager.java) for other details.

IAP (In-App Purchases)
--------------
If you need to include [*In-App Purchases*](http://developer.android.com/google/play/billing/billing_overview.html) into your app, this module is what you need! A single entry point to manage both *In-app items* & *Subscriptions* in a glance!

Network
--------------
A module that helps a lot for network related operations (for example checking internet connection or getting your current IPv4/IPv6 address) - the entry point is the class [*NetworkManager.java*](thunder/src/main/java/com/thunder/network/NetworkManager.java). It supports the following network commands:

- ping - [*PingCommand.java*](thunder/src/main/java/com/thunder/network/PingCommand.java)
- traceroute [*TracerouteCommand.java*](thunder/src/main/java/com/thunder/network/TracerouteCommand.java)
- nsLookup [*NsLookupCommand.java*](thunder/src/main/java/com/thunder/network/NsLookupCommand.java)
- whois [*WhoisCommand.java*](thunder/src/main/java/com/thunder/network/WhoisCommand.java)

Prefs
--------------
This module allows you to have a single entry point that handle all common operations on [*SharedPreferences*](http://developer.android.com/reference/android/content/SharedPreferences.html) - e.g. store a string or increment an integer value and then commit. If you want to discover more, please see the class [*SharedPrefsManager*](thunder/src/main/java/com/thunder/prefs/SharedPrefsManager.java).

Rating
--------------
It provides a really simple way (only one line of code) for implementing a rating popup. You can also build your custom rating popup through the *res/values/thunder_config.xml* file. In order to use this popup, the first step is to declare your custom RatingPopupListener:

	public class CustomRatingPopupListener implements com.thunder.rating.RatingPopupListener {

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

and then override the onResume method of an Activity:

	@Override
    protected void onResume() {
        super.onResume();
        if (listener == null){
       		listener = new CustomRatingPopupListener();
        }
        RatingPopupImpl.getInstance(this, listener).onResume();
    }

The important values are:

- *first_rating_popup*: the number of times that the app goes to the RatingPopupImpl onResume method before showing the rating popup for the first time (after an install) - default 2 
- *negative_rating_popup*: the number of times that the app goes to the RatingPopupImpl onResume method before showing the rating popup after a user cancelled the popup - default 2
- *neutral_rating_popup*: the number of times that the app goes to the RatingPopupImpl onResume method before showing the rating popup after a user taps on the neutral button - default 1

To override these values, simply create a new file **thunder_config.xml** in *res/values* and declare the new values, e.g.:

	<?xml version="1.0" encoding="utf-8"?>
	<resources>

    	<integer name="first_rating_popup">2</integer>
    	<integer name="negative_rating_popup">2</integer>
   		<integer name="neutral_rating_popup">1</integer>

	</resources>

This is an example of the popup:

![alt tag](doc/rating_popup.png)

Important details
========

Sample project
---------------
In order to figure out how to integrate *Thunder* into your app, a sample project has been published - [*Thunder Sample*](https://github.com/michele-paparella/ThunderSample).

Compatibility
---------------
*Thunder* is compatible from Android API 13 (Android 3.2) and above

Permissions
---------------
In order to use *Thunder*, please be aware that the library uses the following permissions:

- *android.permission.READ_PHONE_STATE*
- *android.permission.ACCESS_NETWORK_STATE*
- *android.permission.INTERNET*
- *android.permission.READ_EXTERNAL_STORAGE*
- *com.android.vending.BILLING*

License
==============
	Copyright 2015 Michele Paparella

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
