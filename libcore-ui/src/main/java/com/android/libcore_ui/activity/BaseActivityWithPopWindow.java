package com.android.libcore_ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;
import com.android.libcore_ui.activity.widget.BottomBarGroupLinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: 自带底部弹出框的{@link BaseActivity}
 *
 * 自定义底部弹出框规则:
 * <ul>
 * <li>{@link #addItemToBottomPopWindow(int, int, String)}方法用来在底部弹出的框内加上选项，用组id，
 * 元素id，和元素名称来标识，显示的顺序和添加的顺序一致，如果需要在中间插入一个组元素，则初始化添加的时候调用
 * {@link #addItemToBottomPopWindow(int, int, String)}函数的时候，groupId传一个新值，itemId传递一个小于0
 * 的数值即可，先占一个空位，方便以后来对该groupId位置的元素进行操作</li>
 * <li>{@link #removeItemFromBottomPopWindow(int, int)}方法用来删除在底部添加的按钮选项</li>
 * <li>{@link #showBottomPopWindow()}方法用来显示底部popwindow，调用之前确保已经调用
 * {@link #addItemToBottomPopWindow(int, int, String)}方法</li>
 * <li>{@link #onItemClickCallback(int, int)}方法由子类继承用来处理底部弹出框的点击回调</li>
 * </ul>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-05
 */
public class BaseActivityWithPopWindow extends BaseActivity{

    /** 全屏的半透明显示 */
    protected View ll_full_screen;
    /** 底部popWindow */
    protected ScrollView sv_bottom_content;
    protected LinearLayout ll_bottom_content;
    /** 填充19版本以上SDK　navigation bar */
    protected View v_navigation_bar;
    /** 底部弹出框数据集合 */
    protected LinkedHashMap<Integer, ArrayList<ItemHolder>> bottomItems;

    protected LayoutInflater inflater;
    protected ObjectAnimator popAnimation;
    protected ObjectAnimator reverseAnimation;

    /** 底部弹出框的默认高度 */
    protected int scrollViewMeasureHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLayout() {
        setOriginalContentView(R.layout.activity_base_layout_with_popwindow);
        base_content = (FrameLayout) findViewById(R.id.base_content);

        bottomItems = new LinkedHashMap<>();
        inflater = LayoutInflater.from(this);

        sv_bottom_content = (ScrollView) findViewById(R.id.sv_bottom_content);
        ll_bottom_content = (LinearLayout) findViewById(R.id.ll_bottom_content);
        ll_full_screen = findViewById(R.id.ll_full_screen);
        ll_full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doReverseAnimation();
            }
        });

        defineStyle();

        //如果系统没有指定action bar，则选用自定义action bar
        if (getSupportActionBar()==null)
            chooseTopBar();
        else{
            useToolbar = false;
            actionBar = getSupportActionBar();
        }
    }

    @Override
    protected void defineStyle() {
        super.defineStyle();
        //SDK19版本以上并且导航栏透明
        if (Build.VERSION.SDK_INT >= 19 &&
                (getApplicationInfo().theme==R.style.Activity_translucent_status_bar ||
                getApplicationInfo().theme==R.style.Activity_translucent_navigation_bar)
                && isNavigationTransparent){
            v_navigation_bar = findViewById(R.id.v_navigation_bar);
            v_navigation_bar.setVisibility(View.VISIBLE);
            int id = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            v_navigation_bar.getLayoutParams().height = getResources().getDimensionPixelOffset(id);
        }
    }

    /**
     * 通过添加item到底部bar来创建一系列的选项
     * @param groupId 该item的组id，不同的组id在不同的区域内,请使用大于0的数字来表示
     * @param itemId 该item的item id，用来标示该item，组内的两个item不能有相同的item id,要不然回调无法识别
     * @param name 用来显示该item的名字
     */
    protected void addItemToBottomPopWindow(int groupId, int itemId, String name){
        ArrayList<ItemHolder> temp;
        if (bottomItems.containsKey(groupId)) {
            if (itemId < 0){
                throw new IllegalArgumentException("groupId can be found,so itemId must bigger than 0 or equal 0");
            }
            temp = bottomItems.get(groupId);
            ItemHolder holder = new ItemHolder();
            holder.itemId = itemId;
            holder.name = name;
            temp.add(holder);
        }
        else {
            temp = new ArrayList<>();
            if (itemId >= 0) {
                ItemHolder holder = new ItemHolder();
                holder.itemId = itemId;
                holder.name = name;
                temp.add(holder);
            }
            bottomItems.put(groupId, temp);
        }
        buildBottomPopWindow();
    }

    /**
     * 将item从底部bar中删除
     */
    protected void removeItemFromBottomPopWindow(int groupId, int itemId){
        if (bottomItems.containsKey(groupId)){
            ArrayList<ItemHolder> temp = bottomItems.get(groupId);
            for (ItemHolder holder : temp){
                if (holder.itemId == itemId){
                    temp.remove(holder);
                    buildBottomPopWindow();
                    return;
                }
            }
            throw new IllegalArgumentException("can't find this itemId in this groupId");
        }else{
            throw new IllegalArgumentException("can't find this groupId");
        }
    }

    /**
     * 通过{@link #bottomItems}建立底部弹出框
     */
    private void buildBottomPopWindow(){
        if (bottomItems.size() <= 0)
            return;
        //现将底部弹出框的所有选项去除
        ll_bottom_content.removeAllViews();
        popAnimation = null;
        reverseAnimation = null;
        final Iterator iterator = bottomItems.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, ArrayList<ItemHolder>> entry = (Map.Entry<Integer, ArrayList<ItemHolder>>) iterator.next();
            Integer groupId = entry.getKey();
            ArrayList<ItemHolder> holder = entry.getValue();
            //如果该groupId的items不为0，代表需要将该group显示出来
            if (holder.size() >= 0){
                BottomBarGroupLinearLayout group = (BottomBarGroupLinearLayout) inflater.inflate(R.layout.bottom_group_layout, null);
                group.setmGroupId(groupId);
                group.setCallback(new BottomBarGroupLinearLayout.GroupItemClickCallback() {
                    @Override
                    public void callback(int groupId, int itemId) {
                        onItemClickCallback(groupId, itemId);
                    }
                });
                for (ItemHolder temp : holder){
                    group.addItemToGroup(temp.itemId, temp.name);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int margin = CommonUtils.dp2px(10);
                params.setMargins(margin, 0, margin, margin);
                ll_bottom_content.addView(group, params);
            }
        }
        //每次组建完底部弹出框之后，就开始计算他的高度
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        sv_bottom_content.measure(width, width);
        scrollViewMeasureHeight = sv_bottom_content.getMeasuredHeight();
    }

    @Override
    public void onBackPressed() {
        if (sv_bottom_content.getVisibility() == View.VISIBLE){
            doReverseAnimation();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 点击底部弹出框的回调
     */
    protected void onItemClickCallback(int groupId, int itemId){
        doReverseAnimation();
    }

    /**
     * 执行反向动画将其隐藏
     */
    private void doReverseAnimation(){
        if (Build.VERSION.SDK_INT < 11) {
            sv_bottom_content.setVisibility(View.GONE);
            ll_full_screen.setVisibility(View.GONE);
        }else{
            //如果弹出动画还在执行，则直接将弹出动画的值置为最终值，代表该动画结束，接着直接进行收进动画
            popAnimation.end();
            //避免用户连续快速点击造成短时间内执行两次收进动画，此处进行判断
            if (reverseAnimation != null && reverseAnimation.isRunning()){
                return;
            }
            if (reverseAnimation == null) {
                reverseAnimation = ObjectAnimator.ofInt(sv_bottom_content, "bottomMargin", 0, -scrollViewMeasureHeight);
                reverseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (Integer) animation.getAnimatedValue();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sv_bottom_content.getLayoutParams();
                        params.bottomMargin = value;
                        sv_bottom_content.setLayoutParams(params);
                        ((View) (sv_bottom_content.getParent())).invalidate();
                        if (value <= -scrollViewMeasureHeight){
                            sv_bottom_content.setVisibility(View.GONE);
                        }

                        ll_full_screen.setAlpha((float) (((scrollViewMeasureHeight + value) * 1.0) / (scrollViewMeasureHeight * 1.0)));
                        if (ll_full_screen.getAlpha()<=0){
                            ll_full_screen.setVisibility(View.GONE);
                        }
                    }
                });
                reverseAnimation.setDuration(500);
            }
            reverseAnimation.start();
        }
    }

    /**
     * 用来显示该popwindow，保证在调用该方法之前已经调用{@link #addItemToBottomPopWindow(int, int, String)}方法
     */
    protected void showBottomPopWindow(){
        if (Build.VERSION.SDK_INT >= 11) {
            //如果上次的动画还在执行，直接停止
            if (reverseAnimation != null){
                reverseAnimation.end();
            }
            sv_bottom_content.setVisibility(View.VISIBLE);
            ll_full_screen.setVisibility(View.VISIBLE);
            //需要滚动到顶部
            sv_bottom_content.scrollTo(0, 0);
            if (popAnimation == null) {
                popAnimation = ObjectAnimator.ofInt(sv_bottom_content, "bottomMargin", -scrollViewMeasureHeight, 0);
                popAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (Integer) animation.getAnimatedValue();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sv_bottom_content.getLayoutParams();
                        params.bottomMargin = value;
                        sv_bottom_content.setLayoutParams(params);
                        ((View) (sv_bottom_content.getParent())).invalidate();

                        ll_full_screen.setAlpha((float) (((scrollViewMeasureHeight + value) * 1.0) / (scrollViewMeasureHeight * 1.0)));
                    }
                });
                popAnimation.setDuration(500);
            }
            popAnimation.start();
        }else{
            ll_full_screen.setVisibility(View.VISIBLE);
            sv_bottom_content.setVisibility(View.VISIBLE);
            //需要滚动到顶部
            sv_bottom_content.scrollTo(0, 0);
        }
    }


    /**
     * 底部item的数据集合
     */
    private class ItemHolder{
        private int itemId;
        private String name;
    }
}
