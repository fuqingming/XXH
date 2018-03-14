package com.jy.xxh;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.response.ResponseBaseBean;
import com.jy.xxh.data.Const;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.MD5;
import com.jy.xxh.util.RegexUtil;
import com.jy.xxh.util.SmsSendCounter;
import com.jy.xxh.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.jy.xxh.huanxin.DemoHelper;

import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "RegisterActivity";
    private static final int RESEND_VERIFY_CODE_SECOND = 60;
    private SmsSendCounter m_myCount = null;

    KProgressHUD kProgressHUD;

    @BindView(R.id.et_phone)
    EditText m_etPhone;
    @BindView(R.id.et_verify_number)
    EditText m_etVerifyNumber;
    @BindView(R.id.et_password)
    EditText m_etPassword;
    @BindView(R.id.tv_send_verify_code)
    TextView m_tvSendVerifyCode;

    private String m_strPhone;
    private String m_strVerifyNumber;
    private String m_strPassword;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_register;
    }

    protected void init(){
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"注册",true);
        m_tvSendVerifyCode.setEnabled(false);
        RxTextView.textChanges(m_etPhone)
                .debounce(600, TimeUnit.MILLISECONDS)
                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(CharSequence charSequence) throws Exception {
                        return Observable.just(charSequence.toString());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<String>() {
                    @Override
                    public void accept(String queryData) throws Exception {
                        if(isPhoneValid()){
                            m_tvSendVerifyCode.setEnabled(true);
                            m_tvSendVerifyCode.setTextColor(getResources().getColor(R.color.red));
                        }else{
                            m_tvSendVerifyCode.setEnabled(false);
                            m_tvSendVerifyCode.setTextColor(getResources().getColor(R.color.verify_false_color));
                        }
                    }
                });
    }

    private boolean isPhoneValid(){
        m_strPhone = m_etPhone.getText().toString().trim();
        if(m_strPhone.isEmpty())
        {
            m_etPhone.requestFocus();
            return false;
        }
        else if(m_strPhone.length() < 11)
        {
            m_etPhone.requestFocus();
            return false;
        }
        else if(!RegexUtil.checkMobile(m_strPhone))
        {
            m_etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPhoneCode(){
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
        return true;
    }

    // 检查输入项是否输入正确
    private boolean isInputValid() {

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

        // 验证码
        m_strVerifyNumber = m_etVerifyNumber.getText().toString().trim();
        if(m_strVerifyNumber.isEmpty())
        {
            Utils.showToast(this, "请输入验证码");
            m_etVerifyNumber.requestFocus();
            return false;
        }
        else if(m_strVerifyNumber.length() < 4)
        {
            Utils.showToast(this, "验证码为4位");
            m_etVerifyNumber.requestFocus();
            return false;
        }

        m_strPassword = m_etPassword.getText().toString().trim();
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

        return true;
    }

    @OnClick({R.id.tv_send_verify_code,R.id.tv_btn})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_send_verify_code:
                if(isPhoneCode()){
                    callHttpForSendYzm(m_strPhone);
                }
                break;
            case R.id.tv_btn:
                if(isInputValid()){
                    callHttpForRegister(m_strPhone,MD5.encode(m_strPassword),m_strVerifyNumber);
                }
                break;
        }
    }

    private void callHttpForRegister(final String userPhone,final String pwd,String code){
        String urlDataString = "?u_telphone="+userPhone+"&u_pwd="+pwd+"&u_code="+code;
        HttpClient.get(ApiStores.user_register + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                if(response.getResult()){
                    DemoHelper.getInstance().setCurrentUserName(userPhone);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Utils.showToast(RegisterActivity.this,response.getMessage());
                }

            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
                kProgressHUD.show();
            }

            @Override
            public void OnRequestFinish() {
                kProgressHUD.dismiss();
            }
        });
    }

    private void callHttpForSendYzm(String userPhone){
        String urlDataString = "?u_telphone="+userPhone+"&type=1";
        HttpClient.get(ApiStores.user_send_yzm + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    m_tvSendVerifyCode.setEnabled(false);
                    m_tvSendVerifyCode.setText(String.valueOf(RESEND_VERIFY_CODE_SECOND));
                    m_myCount = new SmsSendCounter(RegisterActivity.this,m_tvSendVerifyCode, RESEND_VERIFY_CODE_SECOND * 1000, 1000);
                    m_myCount.start();
                }else{
                    Utils.showToast(RegisterActivity.this,response.getMessage());
                }
            }

            @Override
            public void OnFailure(String message) {
                messageCenter("错误",message);
            }

            @Override
            public void OnRequestStart() {
                kProgressHUD.show();
            }

            @Override
            public void OnRequestFinish() {
                kProgressHUD.dismiss();
            }
        });
    }

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }

    private void stopMyCount()
    {
        if (m_myCount != null)
        {
            m_myCount.cancel();
            m_myCount = null;
        }

        m_tvSendVerifyCode.setEnabled(true);
        m_tvSendVerifyCode.setText("获取手机验证码");
    }
}
