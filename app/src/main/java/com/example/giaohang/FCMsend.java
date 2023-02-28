package com.example.giaohang;
import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class FCMsend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAzHDYBEY:APA91bEy36czuAGZXxiAN3_Q9zi-K2aByxCY8XPorZPBWloa4e4i2anw0i2Ta1iuKq5ZP4y691_vL34mTtcENWOgkH-RFrSGcYkt_yCVoMN80ZzBiXlEfYiMNVn_bks2jskYc9_3av9A";
        public static void  FirebaseMessagingService(Context context,String token,String title,String message){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestQueue queue= Volley.newRequestQueue(context);
                try {
                    JSONObject json = new JSONObject();
                    json.put("to", token);
                    JSONObject notification = new JSONObject();
                    notification.put("title", title);
                    notification.put("body", message);
                    json.put("notification", notification);
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                            BASE_URL, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("FCM"+response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                        @Override
                        public Map<String,String>getHeaders() throws AuthFailureError{
                            Map<String,String> params=new HashMap<>();
                            params.put("Content-Type","application/json");
                            params.put("Authorization",SERVER_KEY);
                            return  params;
                        }
                    };
                    queue.add(jsonObjectRequest);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
            }
}
/*
public class FCMsend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "null";

    public static void SetServerKey(String serverKey) {
        FCMsend.SERVER_KEY = "key=" + serverKey;
    }
    protected String title = null, body = null, to = null, image = null;
    protected HashMap<String, String> datas = null;
    protected boolean topic;
    protected String result;
    public String Result() {
        return result;
    }
    public static class Builder {
        private FCMsend mFcm;
        public Builder(String to) {
            mFcm = new FCMsend();
            mFcm.to = to;
        }
        public Builder(String to, boolean topic) {
            mFcm = new FCMsend();
            mFcm.to = to;
            mFcm.topic = topic;
        }
        public Builder setTitle(String title) {
            mFcm.title = title;
            return this;
        }
        public Builder setBody(String body) {
            mFcm.body = body;
            return this;
        }
        public Builder setImage(String image) {
            mFcm.image = image;
            return this;
        }
        public Builder setData(HashMap<String, String> datas) {
            mFcm.datas = datas;
            return this;
        }
        @SuppressLint("NewApi")
        public FCMsend send() {
            if (SERVER_KEY == null) mFcm.result = "No Server Key";
            else {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    JSONObject json = new JSONObject();
                    if (mFcm.topic) json.put("to", "/topics/" + mFcm.to);
                    else json.put("to", mFcm.to);
                    JSONObject notification = new JSONObject();
                    notification.put("title", mFcm.title);
                    notification.put("body", mFcm.body);
                    if (mFcm.image != null) notification.put("image", mFcm.image);
                    notification.put("click_action", "com.deeplabstudio.fcm_NOTIFICATION");
                    json.put("notification", notification);
                    if (mFcm.datas != null){
                        JSONObject data = new JSONObject();
                        mFcm.datas.forEach((key, value) -> {
                            try {
                                data.put(key, value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        json.put("data", data);
                    }
                    HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json;");
                    conn.setRequestProperty("Authorization", SERVER_KEY);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.close();
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String result = IOUtils.toString(in, "UTF-8");
                    in.close();
                    conn.disconnect();
                    mFcm.result = result;
                } catch (JSONException | IOException e) {
                    mFcm.result = e.getMessage();
                }
            }
            return mFcm;
        }
    }
}*/
