package com.jy.xxh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.response.ResponseLoginBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.data.Const;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.huanxin.db.DemoDBManager;
import com.jy.xxh.util.MD5;
import com.jy.xxh.util.RegexUtil;
import com.jy.xxh.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 * @author 付庆明
 *
 */
public class LoginActivity extends BaseAppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_phone)
    EditText m_etPhone;
    @BindView(R.id.et_password)
    EditText m_etPassword;
    private String m_strPhone;
    private String m_strMd5Pwd;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"登录",true);
        m_etPhone.requestFocus();
    }

    @OnClick({R.id.tv_login,R.id.tv_fast_register,R.id.tv_forget_password})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_login:
                if(isInputValid()){
                    callHttpForLogin(m_strPhone,m_strMd5Pwd);
                }
                break;
            case R.id.tv_fast_register:
                Intent it = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
                break;
            case R.id.tv_forget_password:
                Intent it1 = new Intent(LoginActivity.this,ForgetPwdActivity.class);
                startActivity(it1);
                break;
        }
    }

    // 检查输入项是否输入正确
    @SuppressLint("NewApi")
    private boolean isInputValid() {

        m_strPhone = m_etPhone.getText().toString().trim();
        if(m_strPhone.isEmpty())
        {
            Utils.showToast(this, "请输入手机号码");
            m_etPhone.requestFocus();
            return false;
        }
        else if(m_strPhone.length() < 11)
        {
            Utils.showToast(this, "手机号码需要11位长度");
            m_etPhone.requestFocus();
            return false;
        }
        else if(!RegexUtil.checkMobile(m_strPhone))
        {
            Utils.showToast(this, "请输入正确的手机号码");
            m_etPhone.requestFocus();
            return false;
        }

        String m_strPassword = m_etPassword.getText().toString().trim();
		if(m_strPassword.isEmpty())
		{
			Utils.showToast(this, "请输入密码");
            m_etPassword.requestFocus();
			return false;
		}else if(m_strPassword.length() < Const.FieldRange.PASSWORD_MIN_LEN){
		    Utils.showToast(this,"密码不能少于6位");
            m_etPassword.requestFocus();
            return false;
        }else if(!RegexUtil.checkPassword(m_strPassword)){
            Utils.showToast(this,"输入6～18位数字字母组合");
            m_etPassword.requestFocus();
            return false;
        }
        m_strMd5Pwd = MD5.encode(m_strPassword);
        return true;
    }

    private void callHttpForLogin(String userPhone,String strPassword){

        String urlDataString = "?u_telphone="+userPhone+"&pwd="+strPassword;
        HttpClient.get(ApiStores.user_send_login + urlDataString, new HttpCallback<ResponseLoginBean>() {
            @Override
            public void OnSuccess(ResponseLoginBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserId, response.getContent().getInfo().getU_id());
                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserIcon, response.getContent().getInfo().getU_photo());
                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserTelphone, response.getContent().getInfo().getU_telphone());
                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserNickame, response.getContent().getInfo().getU_name());
                    login(response.getContent().getInfo().getU_pwd());
                }else{
                    Utils.showToast(LoginActivity.this,response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
                kProgressHUD.dismiss();
            }

            @Override
            public void OnRequestStart() {
                kProgressHUD.show();
            }

            @Override
            public void OnRequestFinish() {

            }
        });
    }

    private void login(String pwd){
        DemoDBManager.getInstance().closeDB();
        DemoHelper.getInstance().setCurrentUserName(m_strPhone);
        EMClient.getInstance().login(m_strPhone, pwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                kProgressHUD.dismiss();
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,Toast.LENGTH_SHORT).show();
                        kProgressHUD.dismiss();
                    }
                });
            }
        });
    }

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }
}
