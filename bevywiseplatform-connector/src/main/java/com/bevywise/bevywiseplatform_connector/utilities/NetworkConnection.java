package com.bevywise.bevywiseplatform_connector.utilities;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.*;

/**
 * Created by prince on 31/01/18.
 */

public class NetworkConnection {
    private final URL url;
    private final String method;
    private final Listener listener;
    private final String token;
    private HashMap<String,String> bodyParams;

    public interface Listener {
        void onSuccess(JSONObject jsonObject);
        void onFailed(String errorDescription, NetworkErrorCode errorCode);
    }

    private NetworkConnection(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.listener = builder.listener;
        this.bodyParams = builder.bodyParams;
        this.token = builder.token;
    }

    public void Connect() {
        new ConnectTask(this).execute();
    }

    public static class Builder {
        //Required parameter
        private final URL url;
        private final Listener listener;

        //Optional parameter
        private String method = "GET";
        private HashMap<String,String> bodyParams;
        private String token;

        public Builder(URL url, Listener listener, Context context) {
            this.url = url;
            this.listener = listener;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public NetworkConnection build() {
            return new NetworkConnection(this);
        }

        public Builder addBodyParam(String key, String value) {
            if (bodyParams == null) {
                bodyParams = new HashMap<>();
            }
            bodyParams.put(key,value);
            return this;
        }
    }

    private class ConnectTask extends AsyncTask<Void,Void,Void> {

        private final NetworkConnection networkConnection;
        private String errorMessage;
        private NetworkErrorCode errorCode = NetworkErrorCode.NO_ERROR;
        private JSONObject jsonObject;

        public ConnectTask(NetworkConnection networkConnection) {
            this.networkConnection = networkConnection;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder requestBuilder;
                requestBuilder = new Request.Builder()
                        .url(url)
                        .addHeader("cache-control", "no-cache")
                        .addHeader("content-type", "application/x-www-form-urlencoded");
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer "+token);
                }
                if (bodyParams != null) {
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    String urlParameters  = getDataString(bodyParams);
                    RequestBody body = RequestBody.create(mediaType, urlParameters);
                    requestBuilder.post(body);
                } else {
                    //This is for get request which is not needed now
                }


                Response response = client.newCall(requestBuilder.build()).execute();
                if (response.code() == 200) {
                    String jsonString = response.body().string();
                    try {
                        jsonObject = new JSONObject(jsonString);
                    } catch (Exception e) {
                        errorMessage = "Server sent invalid json.Check the server api.";
                        errorCode = NetworkErrorCode.JSON_ERROR;
                    }
                } else if (response.code() == 401) {
                    errorMessage = "Invalid input or token expired";
                    errorCode = NetworkErrorCode.AUTHENTICATION_ERROR;
                } else {
                    errorMessage = "Received wrong status fix the server api";
                    errorCode = NetworkErrorCode.WRONG_STATUS;
                }
            } catch (SocketTimeoutException e) {
                errorMessage = e.getLocalizedMessage();
                errorCode = NetworkErrorCode.TIMED_OUT;
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = e.getLocalizedMessage();
                errorCode = NetworkErrorCode.UNKNOWN_ERROR;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (errorCode == NetworkErrorCode.NO_ERROR) {
                networkConnection.listener.onSuccess(jsonObject);
            } else {
                networkConnection.listener.onFailed(errorMessage, errorCode);
            }
        }

        private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }
}

