package com.android.libcore.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.android.libcore.application.RootApplication;

/**
 * Description: 所有基础{@linkplain Activity}的基类，所有的Activity应该
 * 继承自该基类，以便进行context的管理、页面的管理等<br/>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public abstract class RootActivity extends AppCompatActivity{

    /** 用来在页面之间进行广播的传递 */
    private BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RootApplication.setInstanceRef(this);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //也要在onresume函数里面进行设置，保证弱引用一直引用当前的可见页面
        RootApplication.setInstanceRef(this);
    }

    /**
     * 用来在注册广播之后进行广播的接收处理
     */
    protected void onReceive(Context context, Intent intent){}

    /**
     * 用来注册广播
     * @param action 需要注册广播的action
     */
    public void registerReceiver(String action){
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    RootActivity.this.onReceive(context, intent);
                }
            };
        }

        IntentFilter filter = new IntentFilter(action);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    /**
     * 发送应用内部广播
     */
    protected final void sendLocalBroadcast(String action){
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        ActivityManager.getInstance().removeActivity(this);
        //每次在activity销毁的时候调用该函数来检测应用是否被销毁
        RootApplication.checkApplicationDestroy();
    }
}
