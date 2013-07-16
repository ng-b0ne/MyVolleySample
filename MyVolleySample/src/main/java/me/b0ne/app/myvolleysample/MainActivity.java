package me.b0ne.app.myvolleysample;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static RequestQueue mQueue;
    private static String REQUEST_URL = "http://www.mywebsite.com/sample.php";
    private Button strPostBtn;
    private Button imgPostBtn;

    MultipartEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        strPostBtn = (Button)findViewById(R.id.str_post_btn);
        imgPostBtn = (Button)findViewById(R.id.img_post_btn);

        strPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testStringPost();
            }
        });

        imgPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(entityRunnable);
                thread.start();
            }
        });
    }

    private Runnable entityRunnable = new Runnable(){
        @Override
        public void run() {
            Uri iconUri = Uri.parse("android.resource://me.b0ne.app.myvolleysample/"
                    + R.drawable.ic_launcher);
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(iconUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mEntity = new MultipartEntity();
            InputStreamBody streamBody = new InputStreamBody(inputStream, "icon.png");
            mEntity.addPart("file", streamBody);

            entityHandler.sendMessage(new Message());
        }
    };

    private Handler entityHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            testImagePost();
        }
    };

    private void testImagePost(){
            MyImageRequest myImgRequest = new MyImageRequest(REQUEST_URL, myListener, myErrorListener);

            myImgRequest.setParams(mEntity);
            // リクエストのタイムアウトなどの設定
            myImgRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(myImgRequest);
    }

    private void testStringPost() {
        // 送信したいパラメーター
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "Bone");
        params.put("sex", "male");
        // リクエストの初期設定
        MyRequest myRequest = new MyRequest(Method.POST, REQUEST_URL, myListener, myErrorListener);
        // リクエストのタイムアウトなどの設定
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myRequest.setParams(params);
        // リクエストキューにリクエスト追加
        mQueue.add(myRequest);
    }

    /**
     * レスポンス受信のリスナー
     */
    private Listener<JSONObject> myListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("TEST", response.toString());
        }
    };

    /**
     * リクエストエラーのリスナー
     */
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
