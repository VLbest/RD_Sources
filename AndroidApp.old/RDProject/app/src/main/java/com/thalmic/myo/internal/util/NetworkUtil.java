// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.util;

import java.io.IOException;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil
{
    public int postJsonToUrl(final String jsonString, final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("charset", "utf-8");
        urlConnection.connect();
        final DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
        dos.writeBytes(jsonString);
        dos.flush();
        dos.close();
        return urlConnection.getResponseCode();
    }
}
