package me.b0ne.app.myvolleysample;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by bone on 13/06/25.
 */
public class MyRequest extends Request<JSONObject> {
    private final Listener<JSONObject> mListener;
    private Map<String, String> mParams;

    private MultipartEntity entity = new MultipartEntity();

    public MyRequest(int method, String url, Listener<JSONObject> listener,
                     ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * リークエストのパラメーターを設定する
     *
     * @param map
     */
    public void setParams(Map<String, String> map) {
        mParams = map;
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        String resp = new String(networkResponse.data);
        JSONObject resultJson;
        try {
            resultJson = new JSONObject(resp);
        } catch (Exception e) {
            return null;
        }
        return Response.success(resultJson, getCacheEntry());
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
