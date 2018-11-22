package com.yda.yiyunchain.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jungly.gridpasswordview.GridPasswordView;
import com.yda.yiyunchain.R;

import java.util.HashMap;

public class BottomUSDTDialogView extends PopupWindow implements PwdKeyboardView.OnKeyListener,PwdEditText.OnTextInputListener{
    private View dialogView;
    private GridPasswordView payPassEt;
    private Button confirmBtn;
    private ImageView backDialogIv;
    private HashMap<String, String> params1;
    private PwdKeyboardView keyboardView;
    private Context context;
    public BottomUSDTDialogView(final Activity context, HashMap<String, String> params, final BottomDialogOnclickListener bottomDialogOnclickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.usdt_dialog, null);
        this.context=context;
        backDialogIv = (ImageView) dialogView.findViewById(R.id.backDialogIv);
        //payPassEt = (GridPasswordView) dialogView.findViewById(R.id.payPassEt);

        keyboardView = dialogView.findViewById(R.id.key_board);
        keyboardView.setOnKeyListener(this);
        backDialogIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        params1=params;

        this.setContentView(dialogView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.DialogShowStyle); //设置SelectPicPopupWindow弹出窗体动画效果
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);


    }

    @Override
    public void onInput(String text) {

        Log.e("输入的面貌", text);
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onComplete(String result) {
        dismiss();

    }
}