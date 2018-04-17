package com.jy.xxh;


import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.util.Utils;

public class AgreementActivity extends BaseAppCompatActivity {

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_agreement;
    }

    protected void init(){
    }

    @Override
    protected void setUpView() {
        Utils.initCommonTitle(this,"用户服务协议",true);
    }
}
