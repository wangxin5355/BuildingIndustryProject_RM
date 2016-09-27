package com.wangxin.buildingindustryproject_rm.activity_test;

import android.webkit.WebView;

import com.android.libcore_ui.web.WebFragment;

/**
 * Description:
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-08
 */
public class TestWebFragment extends WebFragment {
    public WebView mWebView;

    @Override
    protected void initView() {
        super.initView();
        mWebView = webView;
    }
}
