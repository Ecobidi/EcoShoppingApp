package com.example.obidi.eco_shopping_mall.Utils;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NetworkRequestUtil {
    public static final String BASE_URL = "http://10.0.3.2:3000"; //"http://127.0.0.1:3000";
    public static final String PRODUCT_IMAGE_URL = BASE_URL + "/uploads/products/photo/";
    public static final String CATEGORY_IMAGE_URL = BASE_URL + "/uploads/category/photo/";
    public static final String PROFILE_PHOTO_URL = BASE_URL + "/uploads/customers/photo/profile-photo/";

    public static RequestBody buildPostBody(HashMap<String, String> postBody) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : postBody.keySet()) {
            formBodyBuilder.add(key, postBody.get(key));
        }
        return formBodyBuilder.build();
    }

    public static String getUrlString(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                    Charset.forName("UTF-8")));
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String buildUriQuery(String urlString, HashMap<String, String[]> queryHashMap) {
        Uri.Builder uriBuilder = Uri.parse(urlString).buildUpon();
        Set<String> queryKeys = queryHashMap.keySet();

        for (String queryKey : queryKeys) {
            String[] queryValues = queryHashMap.get(queryKey);
            for (String queryValue : queryValues) {
                uriBuilder.appendQueryParameter(queryKey, queryValue);
            }
        }

        return uriBuilder.toString();
    }
}
