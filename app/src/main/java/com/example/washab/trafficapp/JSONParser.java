package com.example.washab.trafficapp;

import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Shabab on 11/1/2015.
 */

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String jsonString = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String urlParameter, String method,
                                      List<Pair> params) {

        final String BASE_URL = "http://172.16.194.39:80443/trafficapp/v1";
        URL url;
//        List<Pair> paramaters = new ArrayList<Pair>();
        // Making HTTP request
        try{

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            url = new URL(BASE_URL + "/register");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);

//            for(int i=0;i<params.size();i++) {
//                paramaters.add(new Pair(params.get(i).first, params.get(i).second));
//            }
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();

            is = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (is == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonString = buffer.toString();
            Log.d("Input Stream: ", jsonString);


            // check for request method
//            if(method == "POST") {
                // request method is POST
                // defaultHttpClient


//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost(url);
//                httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//                HttpResponse httpResponse = httpClient.execute(httpPost);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                try{
//                    is = urlConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//                    if (is == null) {
//                        return null;
//                    }
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                        // But it does make debugging a *lot* easier if you print out the completed
//                        // buffer for debugging.
//                        buffer.append(line + "\n");
//                    }
//
//                    if (buffer.length() == 0) {
//                        // Stream was empty.  No point in parsing.
//                        return null;
//                    }
//                    forecastJsonStr = buffer.toString();
//                } catch (Exception e) {
//                    Log.e("", "Error ", e);
//
//                    return null;
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (final IOException e) {
//                            Log.e(LOG_TAG, "Error closing stream", e);
//                        }
//                    }
//                }
//            }
//            }else if(method == "GET"){
//                // request method is GET
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
//                url += "?" + paramString;
//                HttpGet httpGet = new HttpGet(url);
//
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            // Read the input stream into a String
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            forecastJsonStr = buffer.toString();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attemping
//            // to parse it.
//            return null;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//        }
//        }
 /*           BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
*/
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
    private String getQuery(List<Pair> params) throws Exception
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second.toString(), "UTF-8"));
        }

        return result.toString();
    }
}
