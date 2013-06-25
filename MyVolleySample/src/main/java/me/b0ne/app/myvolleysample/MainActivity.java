package me.b0ne.app.myvolleysample;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "Bone");
        params.put("sex", "male");
        MyRequest myRequest = new MyRequest(Method.POST, url, myListener, myErrorListener);
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myRequest.setParams(params);
        mQueue.add(myRequest);
    }

    private Listener<JSONObject> myListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("TEST", response.toString());
        }
    };

    private ErrorListener myErrorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TEST", error.getMessage());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
