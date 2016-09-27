package com.wangxin.buildingindustryproject_rm.activity_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangxin.buildingindustryproject_rm.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试导入控件的activity
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class WidgetActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_widget);
        findViewById(R.id.btn_test_gridlayout).setOnClickListener(this);
        findViewById(R.id.btn_test_flowlayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test_gridlayout){
            startActivity(new Intent(this, GridLayoutActivity.class));
        }else if (v.getId() == R.id.btn_test_flowlayout){
            startActivity(new Intent(this, FlowLayoutActivity.class));
        }
    }
}
