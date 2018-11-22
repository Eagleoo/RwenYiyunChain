package com.yda.yiyunchain.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yda.yiyunchain.R;
import com.yda.yiyunchain.util.Util;
import com.yda.yiyunchain.widget.UpDataVersionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {
    @BindView(R.id.relative_about)
    RelativeLayout relative_about;
    @BindView(R.id.relative_update)
    RelativeLayout relative_update;
    @BindView(R.id.relative_clear)
    RelativeLayout relative_clear;
    @BindView(R.id.share_iv)
    View share;
    @BindView(R.id.refresh)
    View refresh;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.center)
    TextView center;
    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.tv_clear)
    TextView tv_clear;
    @BindView(R.id.btn_cancle)
    Button btn_cancle;
    @BindView(R.id.update_show)
    ImageView update_show;

    String version;
    String content;
    String url;
    String createTime;
    boolean isNew=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);


    }



    @SuppressLint("SetTextI18n")
    @OnClick({R.id.relative_about, R.id.relative_update, R.id.relative_clear,R.id.back,R.id.btn_cancle})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.relative_about:
                Intent intent1=new Intent();
                intent1.setClass(context,ZhuanZang.class);
                intent1.putExtra("about","about");
                startActivity(intent1);
                break;
            case R.id.relative_update:
                if (isNew){
                    new UpDataVersionDialog(context, content, url).show();
                }
                else {
                    Util.show(context,"当前已是最新版本");
                }
                break;

            case R.id.relative_clear:
                try {
                    String size=Util.getTotalCacheSize(getApplicationContext());
                    size=size.substring(0,size.length()-2);
                    if (Double.valueOf(size)!=0&&!tv_clear.getText().toString().equals("0.0")){
                        Util.show(context,"已清理"+Util.getTotalCacheSize(getApplicationContext())+"缓存");
                        Util.clearAllCache(context);
                        tv_clear.setText("");

                    }
                    else {
                        Util.show(context,"暂无缓存文件");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.back:
                finish();
                break;
            case R.id.btn_cancle:
                final Intent intent=new Intent();
                intent.putExtra("loginout","loginout");
                intent.setClass(context,MainActivity.class);
                new AlertDialog.Builder(context).setTitle("注意")
                        .setMessage("确定要退出？")
                        .setIcon(R.mipmap.logo1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
        }

    }


}
