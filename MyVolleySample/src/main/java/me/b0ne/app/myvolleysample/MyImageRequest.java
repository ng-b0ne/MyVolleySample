package me.b0ne.app.myvolleysample;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by bone on 13/07/16.
 */
public class MyImageRequest extends Request<JSONObject> {
    private final Listener<JSONObject> mListener;
    private MultipartEntity mEntity;

    public MyImageRequest(String url, Listener<JSONObject> listener, ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
    }

    /**
     * リークエストのパラメーターを設定する
     * @param entity
     */
    public void setParams(MultipartEntity entity) {
        mEntity = entity;
    }

    @Override
    public String getBodyContentType() {
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        String resp = new String(networkResponse.data);
        // JSONObject型のレスポンス
        JSONObject resultJson;
        try {
            resultJson = new JSONObject(resp);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return Response.success(resultJson, getCacheEntry());
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}