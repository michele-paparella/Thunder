package com.thunder.crypto;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides useful methods for computing common hashing algorithms
 */
public class CryptoHelper {

    private static final String MD2 = "MD2";
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String SHA256 = "SHA-256";
    private static final String SHA384 = "SHA-384";
    private static final String SHA512 = "SHA-512";
    private static final String UTF_8 = "UTF-8";

    /**
     *
     * @param input
     * @return the hashed string using the MD2 algorithm
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String md2(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(MD2, input);
    }

    /**
     *
     * @param input
     * @return the hashed string using the MD5 algorithm
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String md5(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(MD5, input);
    }

    /**
     *
     * @param input
     * @return the hashed string using the SHA1 algorithm
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String sha1(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(SHA1, input);
    }

    /**
     *
     * @param input
     * @return the hashed string using the SHA256 algorithm
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String sha256(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(SHA256, input);
    }

    public static String sha384(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(SHA384, input);
    }

    /**
     *
     * @param input
     * @return the hashed string using the SHA512 algorithm
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String sha512(String input) throws NoSuchAlgorithmException, IOException {
        return useAlgorithm(SHA512, input);
    }

    /**
     *
     * @param name the name of the hash algorithm
     * @param input
     * @return the hashed string using the algorithm defined by name
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static String useAlgorithm(String name, String input) throws NoSuchAlgorithmException, IOException {
        MessageDigest digester = MessageDigest.getInstance(name);
        InputStream stream = new ByteArrayInputStream(input.getBytes(UTF_8));
        byte[] bytes = new byte[8192];
        int byteCount;
        while ((byteCount = stream.read(bytes)) > 0) {
            digester.update(bytes, 0, byteCount);
        }
        byte[] digest = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : digest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }


}
