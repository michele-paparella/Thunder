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
import java.io.InputStreamReader;

/**
 * an abstract class that models a generic network command
 */
public abstract class NetworkCommand {

    private static String command;
    protected OnPartialResultListener listener;
    protected String domain;

    public NetworkCommand(String command){
        this.command = command;
    }

    /**
     *
     * @param host input domain
     * @param listener
     */
    public void runCommand(final String host, final OnPartialResultListener listener) {
        this.listener = listener;
        this.domain = host;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer echo = new StringBuffer();
                    Process proc = Runtime.getRuntime().exec(command +" "+ host);
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
                            listener.onError(new Exception("Error during "+command));
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
                } catch (Exception e) {
                    if (hasFallback()){
                        performFallback();
                    } else {
                        listener.onError(e);
                    }
                }
            }};
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //try to use another way to obtain data
    public boolean hasFallback(){
        //standard behavior
        return false;
    }

    public void performFallback(){

    }

}
