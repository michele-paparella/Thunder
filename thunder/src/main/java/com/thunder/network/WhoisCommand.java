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

public class WhoisCommand extends NetworkCommand {

    public WhoisCommand(){
        super("whois");
    }

    @Override
    public void runCommand(final String host, final OnPartialResultListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder result = new StringBuilder("");
                WhoisClient whois = new WhoisClient();
                try {
                    whois.connect(WhoisClient.DEFAULT_HOST);
                    String whoisData1 = whois.query("=" + host);
                    result.append(whoisData1);
                    listener.onPartialResult(result.toString());
                    whois.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(e);
                }

            }
        }).start();
    }

}
