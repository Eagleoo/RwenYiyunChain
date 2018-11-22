package com.yda.yiyunchain.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yda.yiyunchain.R;
import com.yda.yiyunchain.activity.MainActivity;

import java.util.HashMap;

public class BottomDialogView extends PopupWindow implements PwdKeyboardView.OnKeyListener,PwdEditText.OnTextInputListener{
    private View dialogView;
    private TextView forget_password;
    private Button confirmBtn;
    private ImageView backDialogIv;
    private HashMap<String, String> params1;
    private PwdEditText editText;
    private PwdKeyboardView keyboardView;
    private Context context;
    private PasswordCallBack passwordCallBack;
    public interface PasswordCallBack{
        void doCallBack(String str);
    }
    public BottomDialogView(final Activity context, HashMap<String, String> params,PasswordCallBack callBack) {
        super(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.dialog, null);
        this.context=context;
        backDialogIv = (ImageView) dialogView.findViewById(R.id.backDialogIv);
        forget_password = dialogView.findViewById(R.id.forget_password);
        editText =dialogView.findViewById(R.id.et_input);
        editText.setOnTextInputListener(this);
        keyboardView = dialogView.findViewById(R.id.key_board);
        keyboardView.setOnKeyListener(this);
        backDialogIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("forgetPassword","forgetPassword");
                context.startActivity(intent);

            }
        });
        params1=params;
        this.passwordCallBack=callBack;

        this.setContentView(dialogView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.DialogShowStyle); //设置SelectPicPopupWindow弹出窗体动画效果
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        //onPwdChangedTest(context);


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(600);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                });
//
//            }
//        }).start();


    }

    @Override
    public void onInput(String text) {
        editText.append(text);
        String content = editText.getText().toString();
        Log.e("输入的面貌", content);
    }

    @Override
    public void onDelete() {
        String content = editText.getText().toString();
        if (content.length() > 0) {
            editText.setText(content.substring(0, content.length() - 1));
        }
    }

    @Override
    public void onComplete(String result) {
        dismiss();
        if (passwordCallBack != null) {
            passwordCallBack.doCallBack(result);
        }
//        HashMap<String, String> params = new HashMap<>();
//        params.put("toUser",  params1.get("toUser"));
//        params.put("imgyzcode", params1.get("imgyzcode"));
//        params.put("moneyTypeid",  params1.get("moneyTypeid"));
//        params.put("num",  params1.get("num"));
//        params.put("token", params1.get("token"));
//        params.put("spassword", editText.getText().toString());
//        params.put("yzcode", params1.get("yzcode"));
//        HttpManger.postRequest(context, "/tools/submit_api.ashx?action=moneyTransfer", params, "请稍后...", new HttpManger.DoRequetCallBack() {
//
//            @Override
//            public void onSuccess(String o) {
//
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(o);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String msg = jsonObject.optString("msg") + "";
//                Util.show(context,msg);
//                if (msg.equals("转账成功！")){
//                    Intent intent=new Intent();
//                    intent.putExtra("url","/userusdt.aspx?action=yw_list2");
//                    intent.setClass(context,MainActivity.class);
//                    context.startActivity(intent);
//                }
//                Log.e("请求结果", "**********str**********" + o);
//                dismiss();
//            }
//
//            @Override
//            public void onError(String o) {
//                Log.e("请求结果", "erro" + o);
//            }
//        });
    }
}