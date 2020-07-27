package com.example.garbagecollection.bmw;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class VolleyClient {
    private static RequestQueue requestQueue = null;

    public RequestQueue getRequestQueue(Context mContext) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        return requestQueue;
    }

    public void sendGetRequestNew(final Context mContext, final SharedPreferences spEditor, String apiName, String mParams, Response.Listener<JSONObject> mSuccessListener, Response.ErrorListener mErrorListener) {
        String url = spEditor.getString("URL", "") + apiName;
        if (mParams != null) {
            url += "?" + mParams;
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(GET, url, null, mSuccessListener, mErrorListener);
        loginRequest = new JsonObjectRequest(GET, url, null, mSuccessListener, mErrorListener) {
            @Override
            public Map<String, String>
            getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("core-sdk-tunnel", generateToken(spEditor.getString("sdkkey", "")));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        //JsonObjectRequest loginRequest1 = new JsonObjectRequest(GET, url, null, mSuccessListener, mErrorListener);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        loginRequest.setTag(apiName);
        getRequestQueue(mContext).add(loginRequest);
    }

    public void getJSONArrayRequest(final Context mContext, final SharedPreferences spEditor, String apiName, String mParams, Response.Listener<JSONArray> mSuccessListener, Response.ErrorListener mErrorListener) {
        String url = spEditor.getString("URL", "")  + apiName;
        if (mParams != null) {
            url += "?" + mParams;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JsonArrayRequest loginRequest = new JsonArrayRequest(Request.Method.GET, url,null, mSuccessListener, mErrorListener){
            @Override
            public Map<String, String>
            getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("core-sdk-tunnel", generateToken(spEditor.getString("sdkkey", "")));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(loginRequest);
        //loginRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //loginRequest.setTag(apiName);
        //getRequestQueue(mContext).add(loginRequest);
    }

    public void sendPostWithBearerData(final Context mContext, final SharedPreferences spEditor, String apiName, JSONObject mParams, Response.Listener<JSONObject> mSuccessListener, Response.ErrorListener mErrorListener) {
            String url = apiName;

            JsonObjectRequest loginRequest = new JsonObjectRequest(POST, url, mParams, mSuccessListener, mErrorListener);
            loginRequest = new JsonObjectRequest(POST, url, mParams, mSuccessListener, mErrorListener) {
                @Override
                public Map<String, String>
                getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    //headers.put("core-sdk-tunnel", generateToken(spEditor.getString("sdkkey", "")));
                    return headers;
                }
            };
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            loginRequest.setTag(apiName);
            getRequestQueue(mContext).add(loginRequest);

    }

    public void sendPostWithBearerRegister(final Context mContext, final SharedPreferences spEditor, String apiName, JSONObject mParams, Response.Listener<JSONObject> mSuccessListener, Response.ErrorListener mErrorListener) {
        if (mParams != null) {
            String url = spEditor.getString("URL", "")  + apiName;

            JsonObjectRequest loginRequest = new JsonObjectRequest(POST, url, mParams, mSuccessListener, mErrorListener) {
                @Override
                public Map<String, String>
                getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    //headers.put("core-sdk-tunnel", "YjgyOGZhOTFjNmE5MDJkZTkzOTlkMGJiOCRBdU0kTWpBeU1DMHdNUzB3T1ZRd09Ub3hPRG95TmxvPQ");
                    return headers;
                }
            };
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            loginRequest.setTag(apiName);
            getRequestQueue(mContext).add(loginRequest);
        }
    }
}
