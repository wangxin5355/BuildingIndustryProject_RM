package com.android.libcore_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.libcore.dialog.BaseDialog;
import com.android.libcore_ui.R;

/**
 * Description: 扩展dialog类，用来统一dialog的样式和实线dialog的功能，请在此处定义好一个应用
 * 的dialog样式，并且保证一个应用的都使用该dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class AppDialog extends BaseDialog implements View.OnClickListener{

    /** 标题 */
    private RelativeLayout rl_title;
    private TextView tv_title;

    /** 内容 */
    private RelativeLayout rl_message;
    private TextView tv_message;

    /** 底部按钮 */
    private LinearLayout ll_bottom_button;

    private LayoutInflater inflater;

    public AppDialog(Context context) {
        super(context, R.style.theme_dialog);
        inflater = LayoutInflater.from(getContext());
        //仿QQ的dialog样式
        setContentView(R.layout.dialog_base_layout);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        ll_bottom_button = (LinearLayout) findViewById(R.id.ll_bottom_button);
    }

    @Override
    public BaseDialog setTitle(String title) {
        mHasTitle = true;
        tv_title.setText(title);
        return this;
    }

    @Override
    public BaseDialog setTitle(View title) {
        mHasTitle = true;
        rl_title.removeView(tv_title);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_title.addView(title, params);
        return this;
    }

    @Override
    public BaseDialog setMessage(String message) {
        tv_message.setText(message);
        return this;
    }

    @Override
    public BaseDialog setMessage(View message) {
        rl_message.removeView(tv_message);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_message.addView(message, params);
        return this;
    }

    private LinearLayout generateLayout(String text){
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_item_button_layout, null);
        View v_line = layout.findViewById(R.id.v_line);
        TextView tv_text = (TextView) layout.findViewById(R.id.tv_text);
        tv_text.setText(text);
        if (ll_bottom_button.getChildCount() == 0){
            layout.removeView(v_line);
            tv_text.setBackgroundResource(R.drawable.dialog_button_bottom_selector);
        }
        else if (ll_bottom_button.getChildCount() == 1){
            tv_text.setBackgroundResource(R.drawable.dialog_button_bottomright_selector);
        }
        else{
            tv_text.setBackgroundResource(R.drawable.dialog_button_bottomright_selector);
        }
        reBuildCircle();
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setLayoutParams(params);
        layout.setOnClickListener(this);
        return layout;
    }

    private void reBuildCircle(){
        //因为textview是math_parent，并且该textview的背景颜色不为空，所以覆盖掉父控件的圆弧，所以只能动态设置
        //textview的圆弧
        if (ll_bottom_button.getChildCount() == 1){
            View v = ll_bottom_button.getChildAt(0).findViewById(R.id.tv_text);
            if (v != null)
                v.setBackgroundResource(R.drawable.dialog_button_bottomleft_selector);
        }
        //如果大于等于2个，需要将上一个变成没有圆弧并且自己变成右圆弧
        else if(ll_bottom_button.getChildCount() > 1){
            View v = ll_bottom_button.getChildAt(ll_bottom_button.getChildCount() - 1);
            v = v.findViewById(R.id.tv_text);
            if (v != null)
                v.setBackgroundResource(R.drawable.dialog_button_middle_selector);
        }
    }

    @Override
    public BaseDialog setPositiveButton(String positive) {
        LinearLayout layout = generateLayout(positive);
        layout.setTag(POSITIVE_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public BaseDialog setPositiveButton(View positive) {
        View layout = positive;
        //超过一个view,应该加上一条分割线
        if (ll_bottom_button.getChildCount() > 0){
            layout = inflater.inflate(R.layout.dialog_item_button_layout, null);
            ((ViewGroup)layout).removeViewAt(1);
            ((ViewGroup)layout).addView(positive);
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setTag(POSITIVE_LISTENER);
        layout.setOnClickListener(this);

        ll_bottom_button.addView(layout, params);
        return this;
    }

    @Override
    public BaseDialog setNegativeButton(String negative) {
        LinearLayout layout = generateLayout(negative);
        layout.setTag(NEGATIVE_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public BaseDialog setNegativeButton(View negative) {
        View layout = negative;
        //超过一个view,应该加上一条分割线
        if (ll_bottom_button.getChildCount() > 0){
            layout = inflater.inflate(R.layout.dialog_item_button_layout, null);
            ((ViewGroup)layout).removeViewAt(1);
            ((ViewGroup)layout).addView(negative);
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setTag(NEGATIVE_LISTENER);
        layout.setOnClickListener(this);

        ll_bottom_button.addView(layout, params);
        return this;
    }

    @Override
    public BaseDialog setNeutralButton(String neutral) {
        LinearLayout layout = generateLayout(neutral);
        layout.setTag(NEUTRAL_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public BaseDialog setNeutralButton(View neutral) {
        View layout = neutral;
        //超过一个view,应该加上一条分割线
        if (ll_bottom_button.getChildCount() > 0){
            layout = inflater.inflate(R.layout.dialog_item_button_layout, null);
            ((ViewGroup)layout).removeViewAt(1);
            ((ViewGroup)layout).addView(neutral);
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setTag(NEUTRAL_LISTENER);
        layout.setOnClickListener(this);

        ll_bottom_button.addView(layout, params);
        return this;
    }

    @Override
    public BaseDialog addOtherButton(String other, int other_listener) {
        if (!checkIllegalId(other_listener)){
            throw new IllegalArgumentException("按钮id重复");
        }
        LinearLayout layout = generateLayout(other);
        layout.setTag(other_listener);
        mIds.add(other_listener);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public BaseDialog addOtherButton(View other, int other_listener) {
        if (!checkIllegalId(other_listener)){
            throw new IllegalArgumentException("按钮id重复");
        }
        reBuildCircle();
        View layout = other;
        layout = inflater.inflate(R.layout.dialog_item_button_layout, null);
        //超过一个view,应该加上一条分割线
        if (ll_bottom_button.getChildCount() == 0){
            ((ViewGroup)layout).removeViewAt(0);
        }
        ((ViewGroup)layout).removeViewAt(1);
        ((ViewGroup)layout).addView(other, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        other.setTag(other_listener);
        other.setOnClickListener(this);
        layout.setTag(other_listener);
        layout.setOnClickListener(this);
        ll_bottom_button.addView(layout, params);
        return this;
    }

    @Override
    public BaseDialog setGravity(int gravity) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        window.setAttributes(params);
        return this;
    }

    @Override
    public BaseDialog setPosition(int x, int y) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        return this;
    }

    @Override
    public BaseDialog setWidth(int width) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        window.setAttributes(params);
        return this;
    }

    @Override
    public BaseDialog setHeight(int height) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = height;
        window.setAttributes(params);
        return this;
    }

    @Override
    public BaseDialog setAlpha(float alpha) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
        return null;
    }

    @Override
    public void show() {
        if (!mHasTitle)
            rl_title.setVisibility(View.GONE);
        super.show();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        mListener.onButtonClick((Integer) v.getTag());
    }
}
