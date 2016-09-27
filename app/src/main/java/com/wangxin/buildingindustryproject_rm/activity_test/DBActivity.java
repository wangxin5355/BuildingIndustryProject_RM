package com.wangxin.buildingindustryproject_rm.activity_test;

import android.os.Bundle;
import android.view.View;

import com.wangxin.buildingindustryproject_rm.R;
import com.android.libcore.Toast.T;
import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;
import com.wangxin.buildingindustryproject_rm.db.StudentHelper;


import java.util.ArrayList;

/**
 * Description: 数据库测试类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-22
 */
public class DBActivity extends BaseActivity implements View.OnClickListener{
//    public Integer key = 0;
//    public Integer value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_db);
        $(R.id.btn_test_add).setOnClickListener(this);
        $(R.id.btn_test_delete).setOnClickListener(this);
        $(R.id.btn_test_query).setOnClickListener(this);
        $(R.id.btn_test_clear).setOnClickListener(this);
    }

    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_add:
//                key++;
//                value++;
//                if(PermanentCacheDBHelper.getInstance().set(key+"key", value+"value"))
//                    T.getInstance().showShort("插入成功");

                if(StudentHelper.getInstance().insertStudentInfo("赵", "男", 68)
                        && StudentHelper.getInstance().insertGrade(5, 99)){
                    T.getInstance().showShort("插入成功");
                }
                ArrayList<StudentHelper.StudentInfo> lists = new ArrayList<>();
                StudentHelper.StudentInfo info = new StudentHelper.StudentInfo();
                info.gender = "男";
                info.name = "赵";
                info.weight = 100;
                lists.add(info);
                info = new StudentHelper.StudentInfo();
                info.gender = "男";
                info.name = "赵";
                info.weight = 200;
                lists.add(info);
                info = new StudentHelper.StudentInfo();
                info.gender = "男";
                info.name = "赵";
                info.weight = 300;
                lists.add(info);
                T.getInstance().showShort(StudentHelper.getInstance().insertStudentInfos(lists)+"");
                break;
            case R.id.btn_test_delete:
//                if(PermanentCacheDBHelper.getInstance().del(key+"key"))
//                    T.getInstance().showShort("删除成功");
//                key--;
//                value--;
                if(StudentHelper.getInstance().deleteStudentInfo("赵"))
                    T.getInstance().showShort("删除成功");
                break;
            case R.id.btn_test_query:
                try {
                    for (StudentHelper.StudentInfo info1 : StudentHelper.getInstance().getStudentInfo("赵")){
                        L.e(info1.id + " " + info1.name + " " + info1.gender + " " + info1.weight);
                    }
                }catch (NullPointerException e){
                    //ignore
                }
                break;
            case R.id.btn_test_clear:
                if(StudentHelper.getInstance().clear()){
                    T.getInstance().showShort("清空成功");
                }
                break;
        }
    }
}
