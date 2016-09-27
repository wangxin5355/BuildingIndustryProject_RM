package com.android.libcore_ui.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.libcore_ui.R;

/**
 * Description: 专用控件，底部弹出框的容器，该控件不是常用控件
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-14
 */
public class BottomBarGroupLinearLayout extends LinearLayout{
    private int mGroupId;
    private boolean mHasSetGroupId = false;
    private LayoutInflater mInflater;
    private GroupItemClickCallback mCallback;

    public BottomBarGroupLinearLayout(Context context) {
        super(context);
    }

    public BottomBarGroupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** 设置该项的groupId */
    public void setmGroupId(int mGroupId){
        this.mGroupId = mGroupId;
        mHasSetGroupId = true;
        mInflater = LayoutInflater.from(getContext());
    }

    public void addItemToGroup(int itemId, String name){
        if (!mHasSetGroupId){
            throw new IllegalArgumentException("set mGroupId first");
        }
        View view = mInflater.inflate(R.layout.bottom_item_layout, null);
        TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
        tv_item_name.setText(name);

        //设置textview的弧角
        if (getChildCount() == 0){
            tv_item_name.setBackgroundResource(R.drawable.bottom_button_all_selector);
        }else{
            tv_item_name.setBackgroundResource(R.drawable.bottom_button_bottom_selector);
            if (getChildCount() == 1){
                getChildAt(getChildCount() - 1).findViewById(R.id.tv_item_name).setBackgroundResource(R.drawable.bottom_button_top_selector);
            }
            else{
                getChildAt(getChildCount() - 1).findViewById(R.id.tv_item_name).setBackgroundResource(R.drawable.bottom_button_middle_selector);
            }
        }

        view.setTag(itemId);
        this.addView(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.callback(mGroupId, (Integer) v.getTag());
            }
        });

        //将最后一个view的底部分割线去除
        for (int i=0; i<getChildCount(); i++){
            getChildAt(i).findViewById(R.id.v_line).setVisibility(View.VISIBLE);
        }
        getChildAt(getChildCount()-1).findViewById(R.id.v_line).setVisibility(View.GONE);
    }

    public void setCallback(GroupItemClickCallback callback){
        this.mCallback = callback;
    }

    /**
     * 点击item之后的回调
     */
    public interface GroupItemClickCallback{
        public void callback(int groupId, int ItemId);
    }
}
