package com.wangxin.buildingindustryproject_rm.activity_test;

import android.os.Bundle;
import android.view.View;

import com.wangxin.buildingindustryproject_rm.R;
import com.android.libcore.Toast.T;
import com.android.libcore.log.L;
import com.android.libcore.utils.FileUtils;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试FileUtils
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-31
 */
public class FileActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_file);
        $(R.id.btn_test_file).setOnClickListener(this);
    }

    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        T.getInstance().showShort("请查看framework目录");
        FileUtils.createFileInTempDirectory("temp.temp");
        FileUtils.createFileInImageDirectory("a.png");
        FileUtils.createFileInVideoDirectory("a.mp4");
        FileUtils.createFileInVoiceDirectory("a.mp3");
        FileUtils.createFileInHtmlDirectory("a.html");
        L.e("size" + FileUtils.getFileOrDirectorySize(FileUtils.getExternalStoragePath()));
    }
}
