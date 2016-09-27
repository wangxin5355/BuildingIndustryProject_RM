package com.wangxin.buildingindustryproject_rm.activity_test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxin.buildingindustryproject_rm.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.dialog.LoadingDialog;
import com.android.libcore_ui.volley.VolleyApi;
import com.android.libcore_ui.volley.request.XMLRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 测试封装volley请求
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-10-20
 */
public class VolleyActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_result;
    private ImageView iv_content;
    private LoadingDialog ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_net);
        addNavigationOnBottom((ViewGroup) $(R.id.ll_content));
        tv_result = $(R.id.tv_result);
        iv_content = $(R.id.iv_content);

        $(R.id.btn_string).setOnClickListener(this);
        $(R.id.btn_jsonObject).setOnClickListener(this);
        $(R.id.btn_jsonArray).setOnClickListener(this);
        $(R.id.btn_xml).setOnClickListener(this);
        $(R.id.btn_image).setOnClickListener(this);

        ld = new LoadingDialog(this);
    }

    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        ld.show();
        switch (v.getId()){
            case R.id.btn_string:
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.baidu.com", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ld.dismiss();
                        tv_result.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ld.dismiss();
                        tv_result.setText(error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return new HashMap<>();
                    }
                };
                VolleyApi.getRequestQueue().add(stringRequest);
                break;
            case R.id.btn_jsonObject:
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://www.baidu.com", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ld.dismiss();
                        tv_result.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ld.dismiss();
                        tv_result.setText(error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return new HashMap<>();
                    }
                };
                VolleyApi.getRequestQueue().add(jsonObjectRequest);
                break;
            case R.id.btn_jsonArray:
                ld.dismiss();
                break;
            case R.id.btn_xml:
                XMLRequest xmlRequest = new XMLRequest(Request.Method.POST, "https://www.baidu.com/", new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        ld.dismiss();
                        tv_result.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ld.dismiss();
                        tv_result.setText(error.getMessage());
                    }
                });
                VolleyApi.getRequestQueue().add(xmlRequest);
                break;
            case R.id.btn_image:
                ld.dismiss();
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_content,
                        R.mipmap.ic_refresh, R.mipmap.ic_refresh_close);
                VolleyApi.getImageLoader().get("http://www.baidu.com/img/bdlogo.png", listener);
//                VolleyApi.getImageLoader().get("http://www.baidu.com/img/bdlogo.png", new ImageLoader.ImageListener() {
//                    @Override
//                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                        iv_content.setImageBitmap(response.getBitmap());
//                    }
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        ld.dismiss();
//                        tv_result.setText(error.toString());
//                    }
//                });
                break;
        }
    }
}
