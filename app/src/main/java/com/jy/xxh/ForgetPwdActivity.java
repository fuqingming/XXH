package com.jy.xxh;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.response.ResponseBaseBean;
import com.jy.xxh.data.Const;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.util.Captcha;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.MD5;
import com.jy.xxh.util.RegexUtil;
import com.jy.xxh.util.SmsSendCounter;
import com.jy.xxh.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ForgetPwdActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "ForgetPwdActivity";
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
    @BindView(R.id.et_verify_number_pic)
    EditText m_etVerifyNumberPic;
    @BindView(R.id.iv_verify_number)
    ImageView m_ivVerifyNumber;
    @BindView(R.id.iv_rerush)
    ImageView m_ivRerush;

    private String m_strPhone;
    private String m_strVerifyNumber;
    private String m_strMd5Pwd;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_forget_pwd;
    }

    protected void init(){
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"忘记密码",true);
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

        refreshVerifyNumber();
    }

    public void refreshVerifyNumber()
    {
        m_etVerifyNumber.setText("");

        Log.d(LOG_TAG, "m_ivVerifyNumber.getWidth() = " + m_ivVerifyNumber.getWidth());

        if(m_ivVerifyNumber.getWidth() > 0)
        {
            m_ivVerifyNumber.setImageBitmap(Captcha.getInstance().getBitmap(m_ivVerifyNumber.getWidth(), m_ivVerifyNumber.getHeight()));
        }
        else
        {
            m_ivVerifyNumber.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_ivVerifyNumber.getWidth() <= 0)
                    {
                        return;
                    }

                    m_ivVerifyNumber.setImageBitmap(Captcha.getInstance().getBitmap(m_ivVerifyNumber.getWidth(), m_ivVerifyNumber.getHeight()));
                }
            }, 100);
        }
    }

    @SuppressLint("NewApi")
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

        // 图形验证码
        String strVerifyNumber = m_etVerifyNumberPic.getText().toString().trim();
        if(strVerifyNumber.isEmpty())
        {
            Utils.showToast(this, "请输入图形验证码");
            m_etVerifyNumberPic.requestFocus();
            return false;
        }
        else if(strVerifyNumber.compareToIgnoreCase(Captcha.getInstance().getCode()) != 0)
        {
            Utils.showToast(this, "图形验证码错误");
            m_etVerifyNumberPic.requestFocus();
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
            Utils.showToast(this, "验证码错误");
            m_etVerifyNumber.requestFocus();
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

    @OnClick({R.id.tv_send_verify_code,R.id.tv_btn,R.id.iv_rerush})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_send_verify_code:
                callHttpForSendYzm(m_strPhone);
                break;
            case R.id.tv_btn:
                if(isInputValid()){
                    callHttpForChangePwd();
                }
                break;
            case R.id.iv_rerush:
                m_ivVerifyNumber.setImageBitmap(Captcha.getInstance().getBitmap(m_ivVerifyNumber.getWidth(), m_ivVerifyNumber.getHeight()));
                break;

        }
    }

    private void callHttpForSendYzm(String userPhone){
        String urlDataString = "?u_telphone="+userPhone+"&type=2";
        HttpClient.get(ApiStores.user_send_yzm + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    m_tvSendVerifyCode.setEnabled(false);
                    m_tvSendVerifyCode.setText(String.valueOf(RESEND_VERIFY_CODE_SECOND));
                    m_myCount = new SmsSendCounter(ForgetPwdActivity.this,m_tvSendVerifyCode, RESEND_VERIFY_CODE_SECOND * 1000, 1000);
                    m_myCount.start();
                }else{
                    Utils.showToast(ForgetPwdActivity.this,response.getMessage());
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

    private void callHttpForChangePwd(){
        String urlDataString = "?u_telphone="+m_strPhone+"&u_pwd="+m_strMd5Pwd+"&u_code="+m_strVerifyNumber;
        HttpClient.get(ApiStores.chengePwd + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                Log.d("",response.toString());
                if(response.getResult()){
                    finish();
                }else{
                    Utils.showToast(ForgetPwdActivity.this,response.getMessage());
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

}
