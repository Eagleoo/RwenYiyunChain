package com.yda.yiyunchain.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yda.yiyunchain.Bean.BiBean;
import com.yda.yiyunchain.Bean.UserInfoBean;
import com.yda.yiyunchain.HttpUtil.HttpManger;
import com.yda.yiyunchain.MyTextWatcher;
import com.yda.yiyunchain.R;
import com.yda.yiyunchain.util.CommonUtil;
import com.yda.yiyunchain.util.MD5;
import com.yda.yiyunchain.util.Util;
import com.yda.yiyunchain.widget.BottomDialogView;
import com.yda.yiyunchain.widget.DialogUtil;
import com.yda.yiyunchain.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class USDTZhuanZang extends BaseActivity implements BottomDialogView.PasswordCallBack{
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.sao)
    ImageView sao;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.linear_username)
    LinearLayout linear_username;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.edt_password)
    TextView edt_password;
    @BindView(R.id.edt_sellnum)
    EditText edt_sellnum;
    @BindView(R.id.receive_money)
    TextView receive_money;
    @BindView(R.id.warnning)
    TextView warnning;
    @BindView(R.id.ok)
    Button ok;

    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.linear_money)
    LinearLayout linear_money;

    String edt_name="";
    int id ;//货币id
    double tips ;//货币tips
    String money="";//账户余额
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private SharedPreferences sp;
    public static final int UPDATE = 0x1;
    private Animation shake;
    List<BiBean> list=new ArrayList<>();
    MyTextWatcher textWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
           if (editable.toString().length()==16){
               edt_name=editable.toString().trim();
               Req_User();
           }
           else {
               linear_username.setVisibility(View.GONE);
           }
        }
    };


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    tv_money.setText("可用余额："+ money);break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usdt);

        ButterKnife.bind(this);

        Intent intent=getIntent();
        sp= getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        shake = AnimationUtils.loadAnimation(this, R.anim.shaker);
        if (intent.getStringExtra("result")!=null){
            edt_name=intent.getStringExtra("result");
            edt_username.setText(edt_name);
            Req_User();
        }
        Req_Token(false);
        Req_USDT();

        edt_username.addTextChangedListener(textWatcher);
        edt_sellnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    try {
                        if (Double.valueOf(s.toString().replace("可用余额：",""))>Double.valueOf(tv_money.getText().toString().replace("可用余额：",""))){
                            warnning.setVisibility(View.VISIBLE);
                            warnning.startAnimation(shake);
                        }
                        else {
                            warnning.setVisibility(View.GONE);
                            receive_money.setText(Double.valueOf(s.toString())+Double.valueOf(s.toString())*tips+"");

                        }
                    }catch (Exception e){
                       e.printStackTrace();
                    }


                }
                else {
                    receive_money.setText("");
                }

            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                tv_money.setText("可用余额："+ money);
                if (!tv_money.getText().toString().equals("可用余额：正在获取...")){
                    refreshLayout.finishRefresh();
                }
            }
        });
    }



    @OnClick({R.id.edt_username, R.id.back, R.id.sao, R.id.ok,R.id.edt_sellnum,R.id.edt_password})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:finish();
                break;
            case R.id.sao:
                try{
                    final String[] requestPermissionstr = {

                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    };
                    Util.checkpermissions(requestPermissionstr, context, new Util.PermissionsCallBack() {
                        @Override
                        public void success() {
                            if (CommonUtil.isCameraCanUse()) {
                                Intent intent = new Intent(context, CaptureActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);
                            } else {
                                Toast.makeText(context, "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure() {

                        }
                    });
                }catch (Exception e){
                    Util.show(context,e.getMessage());
                }


                break;

            case R.id.ok:
                Commit();
                break;

            case R.id.edt_password:
                Req_Transfer();
                break;

            case R.id.edt_sellnum:

                break;

        }

    }

    public void Req_User(){
        HashMap<String, String> params = new HashMap<>();
        params.put("guidno",edt_name);
        //user_login
        HttpManger.postRequest(USDTZhuanZang.this, "/tools/submit_api.ashx?action=GetUserByGuid", params, "请稍后...", new HttpManger.DoRequetCallBack() {

            @Override
            public void onSuccess(String o) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(o);
                    String subUsername="";
                    String user_name = jsonObject.getJSONObject("model").optString("bank_user") + "";
                    String mobile = jsonObject.getJSONObject("model").optString("mobile") + "";
                    String status = jsonObject.optString("status") + "";
                    String phonenum = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
                    if (user_name.length()>1){
                        subUsername="*"+user_name.substring(1,user_name.length());
                    }
                    else {
                        subUsername=user_name;
                    }
                    username.setText(subUsername);
                    phone.setText(phonenum);
                    if (!status.equals("0")&&!user_name.equals("")){
                        linear_username.setVisibility(View.VISIBLE);
                    }
                    else {
                        Util.show(USDTZhuanZang.this, "没有找到该用户");
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                Log.e("请求结果", "str" + o);
            }

            @Override
            public void onError(String o) {
                Log.e("请求结果", o);
            }
        });
    }


    public void Commit(){

        if (edt_username.getText().toString().equals("") ||edt_sellnum.getText().toString().equals("")||receive_money.getText().toString().equals("")
                ){
            Util.show(USDTZhuanZang.this,"请填写完整数据！");
        }
        else if (edt_password.getText().toString().equals("")){Util.show(USDTZhuanZang.this,"密码不能为空！");}
        else if (linear_username.getVisibility()==View.GONE){
            Util.show(USDTZhuanZang.this,"没有找到该用户！");
        }
        else {
            HashMap<String, String> params = new HashMap<>();
            params.put("toUser",  edt_username.getText().toString());
            params.put("imgyzcode", "1");
            params.put("moneyTypeid", String.valueOf(id));
            params.put("num", edt_sellnum.getText().toString());
            params.put("token",sp.getString("token",""));
            params.put("spassword", edt_password.getText().toString());
            params.put("yzcode", "1");
            HttpManger.postRequest(context, "/tools/submit_api.ashx?action=moneyTransfer", params, "请稍后...", new HttpManger.DoRequetCallBack() {

            @Override
            public void onSuccess(String o) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(o);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = jsonObject.optString("msg") + "";
                Util.show(context,msg);
                if (msg.equals("转账成功！")){
                    Intent intent=new Intent();
                    intent.putExtra("url","/userusdt.aspx?action=yw_list2");
                    intent.setClass(context,MainActivity.class);
                    context.startActivity(intent);
                }
                Log.e("请求结果", "**********str**********" + o);
            }

            @Override
            public void onError(String o) {
                Log.e("请求结果", "erro" + o);
            }
        });
        }

    }

    public void Req_Transfer(){

        if (edt_username.getText().toString().equals("") ||edt_sellnum.getText().toString().equals("")||receive_money.getText().toString().equals("")
               ){
            Util.show(USDTZhuanZang.this,"请填写完整数据！");
        }
        else {
            HashMap<String, String> params = new HashMap<>();
            params.put("toUser",  edt_username.getText().toString());
            params.put("imgyzcode", "1");
            params.put("moneyTypeid", String.valueOf(id));
            params.put("num", receive_money.getText().toString());
            params.put("token",sp.getString("token",""));
            params.put("yzcode", "1");

            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(USDTZhuanZang.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

            DialogUtil.showBottomDialog(USDTZhuanZang.this, R.id.pay ,params,this);
        }

    }
    public void Req_Money(){

        HashMap<String, String> params = new HashMap<>();
        params.put("site_id", "1");
        HttpManger.postRequest(USDTZhuanZang.this, "/tools/submit_api.ashx?action=get_user_info", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    JSONObject array=json.getJSONObject("model");
                    money=array.optString("usdt");
                    Message msg = new Message();
                    msg.what = UPDATE;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("请求结果", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
}

    public void Req_USDT(){

        HashMap<String, String> params = new HashMap<>();
        params.put("site_id", "1");
        HttpManger.postRequest(USDTZhuanZang.this, "/tools/submit_api.ashx?action=get_moneyType_list", params, "请稍后...", new HttpManger.DoRequetCallBack() {

            @Override
            public void onSuccess(String o) {
                list=Util.getJsonArrays("list",o);
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getId()==3){
                        tips=list.get(i).getTips();
                        id=list.get(i).getId();
                        if (!edt_sellnum.getText().toString().equals("")){
                            receive_money.setText((Double.valueOf(edt_sellnum.getText().toString())+Double.valueOf(edt_sellnum.getText().toString())*tips)+"");
                        }

                        break;
                    }
                }
                Req_Money();
                linear_money.setVisibility(View.VISIBLE);

                if (!edt_sellnum.getText().toString().equals("")){
                    receive_money.setText((Double.valueOf(edt_sellnum.getText().toString())+Double.valueOf(edt_sellnum.getText().toString())*tips)+"");
                }
                Log.e("请求结果", "str" + o);
            }

            @Override
            public void onError(String o) {

            }
        });
    }

    //token过期重新获取
    public void Req_Token(boolean isYD){
        String lName = sp.getString("username","");
        String lpwd = sp.getString("password","");
        HashMap<String, String> params = new HashMap<>();
        params.put("txtUserName", lName);
        params.put("txtPassword", isYD ? new MD5().getMD5ofStr(lpwd) : lpwd);
        params.put("site_id", "1");
        String mAction = isYD ? "user_login_yd" : "user_login";
        //user_login
        HttpManger.postRequest(context, "/tools/submit_api.ashx?action=" + mAction, params, "登录中", new HttpManger.DoRequetCallBack() {
//        HttpManger.postRequest(context, "/asp/login.asp", params, "登录中", new HttpManger.DoRequetCallBack() {


            @Override
            public void onSuccess(String o) {
                Log.e("请求结果", "str" + o);

                Gson gson = new Gson();
                UserInfoBean userInfoBean = gson.fromJson(o, UserInfoBean.class);
                UserInfoBean.setUser(userInfoBean);
                if (userInfoBean != null) {
                  if (userInfoBean.getStatus() == 1) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token",userInfoBean.getToken());
                        editor.putString("bank_user",userInfoBean.getModel().getBank_user());
                        editor.commit();
                    }
                }


            }

            @Override
            public void onError(String o) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            Log.e("将扫描出的信息显示出来", "scanResult:" + scanResult);
            edt_username.setText(scanResult);
            Req_User();
        }
    }

    @Override
    public void doCallBack(String str) {
        edt_password.setText(str);
    }
}
