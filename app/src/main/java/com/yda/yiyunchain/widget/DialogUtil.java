package com.yda.yiyunchain.widget;

import android.app.Activity;
import android.view.Gravity;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/30.
 */

public class DialogUtil {

    public static void showBottomDialog(Activity activity , int dialogLayoutId , HashMap<String, String> params, BottomDialogView.PasswordCallBack passwordCallBack){

        BottomDialogView bottomDialogView = new BottomDialogView(activity, params,passwordCallBack);
        bottomDialogView.showAtLocation(activity.findViewById(dialogLayoutId), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置窗口显示在parent布局的位置并显示
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);//自动打开软键盘
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static void showUSDTBottomDialog(Activity activity , int dialogLayoutId , HashMap<String, String> params, BottomDialogOnclickListener bottomDialogOnclickListener){

        BottomUSDTDialogView bottomDialogView = new BottomUSDTDialogView(activity, params,bottomDialogOnclickListener);
        bottomDialogView.showAtLocation(activity.findViewById(dialogLayoutId), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置窗口显示在parent布局的位置并显示
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);//自动打开软键盘
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }


}


