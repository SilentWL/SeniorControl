package com.seniorcontrol.administrator.vollley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private NetworkImageView mNetworkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.image);
        //volley_request("http://192.168.199.18:8080/Web/Server?name=1&age=2", Request.Method.GET, 0);
        //volley_request("http://192.168.199.18:8080/Web/GsonServlet", Request.Method.GET, 1);
        //volley_request("http://192.168.199.18:8080/Web/Server?", Request.Method.POST, 0);
        //volley_request("http://192.168.199.18:8080/Web/GsonServlet", Request.Method.POST, 1);
        //volley_request("http://192.168.199.18:8080/Web/Server?name=1&age=2", Request.Method.GET, 2);
        //volley_image_request("http://d.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa34f8016bb68f8c5495ee7b9b.jpg", 0);
        //volley_image_request("http://d.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa34f8016bb68f8c5495ee7b9b.jpg", 1);
        volley_image_request("http://d.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa34f8016bb68f8c5495ee7b9b.jpg", 2);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //MyApplication.getHttpQueues().cancelAll("http://192.168.199.18:8080/Web/GsonServlet");
    }
    private void volley_image_request(String url, int requestType){
        if (requestType == 0) {
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if (bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }

                }
            }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.setTag("abc");
            MyApplication.getHttpQueues().add(request);
        }else if (requestType == 1){
            ImageLoader loader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache(getApplicationContext()));
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            loader.get(url, listener);
        }else if (requestType == 2){
            ImageLoader loader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache(getApplicationContext()));
            mNetworkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);
            mNetworkImageView.setImageUrl(url, loader);

        }
    }
    private void volley_request(String url, int method, int dataType){
        if (method == Request.Method.GET) {
            if (dataType == 0) {
                StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                stringRequest.setTag(url);
                MyApplication.getHttpQueues().add(stringRequest);
            } else if (dataType == 1) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                jsonObjectRequest.setTag(url);
                MyApplication.getHttpQueues().add(jsonObjectRequest);
            }
        }else{
            if (dataType == 0) {
                StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //return super.getParams();
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", "wl");
                        map.put("age", "20");
                        return map;
                    }
                };
                stringRequest.setTag(url);
                MyApplication.getHttpQueues().add(stringRequest);
            } else if (dataType == 1) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //return super.getParams();
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", "wl");
                        map.put("age", "20");
                        return map;
                    }
                };
                jsonObjectRequest.setTag(url);
                MyApplication.getHttpQueues().add(jsonObjectRequest);
            }else if (dataType == 2){
                VolllyRequest.RequestGet(getApplicationContext(), url, "wl", new VolleyInterface(getApplicationContext(), null, null) {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(VolleyError volleyError) {

                    }
                });
            }
        }

    }
}
