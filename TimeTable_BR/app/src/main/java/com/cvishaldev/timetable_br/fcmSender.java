package com.cvishaldev.timetable_br;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fcmSender {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    // the postUrl will be same
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";

    private final String fcmServerKey ="AAAAmomQZZk:APA91bHUltijRfxp_6XBWJflYqkCs1UOH1DuYay0Ry4UQaL1ix44_vauLPCiFaosa03B-6I2naLiV2VB8nPIbsI4uDU0NibmoIceYKZO-xOgfNm5zw21rWfxZrxBuy3-j4y9Vz81KlSo";

    public fcmSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }

    public void sendNotifications() {

        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", "R.drawable.ic_baseline_notifications_active_24");// if there is an icon designed then you are going to place it here
            // enter icon that exists in drawable only



            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, error -> {
                // code run is got error

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }//sending the request
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
