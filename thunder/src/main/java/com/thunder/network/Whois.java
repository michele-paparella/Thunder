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

import org.apache.commons.net.whois.WhoisClient;

import java.io.IOException;

public class Whois {

    public static void getWhois(final String domain, final WhoisListener callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder result = new StringBuilder("");
                WhoisClient whois = new WhoisClient();
                try {
                    whois.connect(WhoisClient.DEFAULT_HOST);
                    String whoisData1 = whois.query("=" + domain);
                    result.append(whoisData1);
                    callback.onSuccess(result.toString());
                    whois.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e);
                }

            }
        }).start();
    }

    public interface WhoisListener {

        public void onSuccess(String whoisData);

        public void onError(Exception e);

    }

}
