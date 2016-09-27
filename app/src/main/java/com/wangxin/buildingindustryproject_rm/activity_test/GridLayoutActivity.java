package com.wangxin.buildingindustryproject_rm.activity_test;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangxin.buildingindustryproject_rm.R;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.widget.SimpleGridLayout;

/**
 * Description:
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-14
 */
public class GridLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_grid);
        SimpleGridLayout gll_content = (SimpleGridLayout) findViewById(R.id.gll_content);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(CommonUtils.dp2px(100), CommonUtils.dp2px(100));

        for (int i=2; i<20; i++){
            TextView textView = new TextView(this);
            textView.setLayoutParams(lp);
            textView.setText(i + "");
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);
            gll_content.addView(textView);
        }
    }
}
