package io.wovn.wovnjava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.*;

import javax.servlet.FilterConfig;

import net.arnx.jsonic.JSON;

public class Store {
    public Settings settings;

    public Store(FilterConfig config) {
        super();
        settings = new Settings(config);
    }

    public Values getValues(String url) {
        url = url.replaceAll("/$", "");

        String json = "";
        try {
            URL uri = new URL(settings.apiUrl + "?token=" + settings.userToken + "&url=" + url);
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(isr);
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            json += line;
                        }
                    } finally {
                        reader.close();
                        isr.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
        }

        if (json.length() == 0) {
            json = "{}";
        }

        LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>>> map = JSON.decode(json);
        return new Values(map);
    }
}
