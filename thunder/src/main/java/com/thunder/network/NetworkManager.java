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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetworkManager {

    private final static String TAG = "NetworkManager";

    /**
     * WARNING: do not call this method from the main thread
     * @param host
     * @param times
     * @param listener
     */
    public static void ping(final String host, final int times, final OnPingResultAvailable listener) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    performPingRequest(host, times, listener);
                } catch (Exception e) {
                    listener.onError(e);
            }
        }};
        Thread thread = new Thread(runnable);
        thread.start();

    }

    private static void performPingRequest(String host, int times, OnPingResultAvailable listener) throws IOException, InterruptedException {
        StringBuffer echo = new StringBuffer();
        Process proc = Runtime.getRuntime().exec("ping -c " + times + " " + host);
        InputStreamReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new InputStreamReader(proc.getInputStream());
            buffer = new BufferedReader(reader);
            String newLine = "\n";
            String line = "";
            while ((line = buffer.readLine()) != null) {
                echo.append(line + newLine);
                listener.onPartialResult(line + newLine);
            }
            proc.waitFor();
            int exit = proc.exitValue();
            if (exit == 0) {
                listener.onFinish();
            } else {
                listener.onError(new Exception("Error during PING"));
            }
        } finally {
            if (reader != null){
                reader.close();
            }
            if (buffer != null){
                buffer.close();
            }
            if (proc != null){
                proc.destroy();
            }
        }
    }

    public interface OnPingResultAvailable {

        public void onPartialResult(final String result);

        public void onError(final Exception e);

        public void onFinish();

    }


}
