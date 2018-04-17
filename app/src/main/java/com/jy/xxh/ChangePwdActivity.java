package com.jy.xxh;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jy.xxh.alert.AlertUtils;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.response.ResponseBaseBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.data.Const;
import com.jy.xxh.http.ApiStores;
import com.jy.xxh.http.HttpCallback;
import com.jy.xxh.http.HttpClient;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.huanxin.db.DemoDBManager;
import com.jy.xxh.util.HUDProgressUtils;
import com.jy.xxh.util.MD5;
import com.jy.xxh.util.RegexUtil;
import com.jy.xxh.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hyphenate.util.EasyUtils.TAG;

public class ChangePwdActivity extends BaseAppCompatActivity {

    KProgressHUD kProgressHUD;

    @BindView(R.id.et_pwd_bellow)
    EditText m_etPwdBellow;
    @BindView(R.id.et_pwd_new)
    EditText m_etPwdNew;
    @BindView(R.id.et_pwd_again)
    EditText m_etPwdAgain;

    @BindView(R.id.tv_title_back)
    TextView m_tvTitleLeft;
    @BindView(R.id.tv_title_right)
    TextView m_tvTitleRight;

    private String m_strMd5Pwd;
    private String m_strPasswordBellow;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_change_pwd;
    }

    protected void init(){
        kProgressHUD = new HUDProgressUtils().showLoadingImage(this);
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"修改密码");
        m_tvTitleLeft.setText("取消");
        m_tvTitleRight.setText("确定");

    }



    // 检查输入项是否输入正确
    private boolean isInputValid() {

        String strPasswordBellow = m_etPwdBellow.getText().toString().trim();
        if(strPasswordBellow.isEmpty())
        {
            Utils.showToast(this, "请输入原密码");
            m_etPwdBellow.requestFocus();
            return false;
        }else if(strPasswordBellow.length() < Const.FieldRange.PASSWORD_MIN_LEN){
            Utils.showToast(this,"密码不能少于6位");
            m_etPwdBellow.requestFocus();
            return false;
        }

        String m_strPasswordNew = m_etPwdNew.getText().toString().trim();
        if(m_strPasswordNew.isEmpty())
        {
            Utils.showToast(this, "请输入新密码");
            m_etPwdNew.requestFocus();
            return false;
        }else if(m_strPasswordNew.length() < Const.FieldRange.PASSWORD_MIN_LEN){
            Utils.showToast(this,"新密码不能少于6位");
            m_etPwdNew.requestFocus();
            return false;
        }else if(!RegexUtil.checkPassword(m_strPasswordNew)){
            Utils.showToast(this,"输入6～18位数字字母组合");
            m_etPwdNew.requestFocus();
            return false;
        }

        String m_strPasswordAgain = m_etPwdAgain.getText().toString().trim();
        if(m_strPasswordAgain.isEmpty())
        {
            Utils.showToast(this, "请输入确认密码");
            m_etPwdAgain.requestFocus();
            return false;
        }else if(m_strPasswordAgain.length() < Const.FieldRange.PASSWORD_MIN_LEN){
            Utils.showToast(this,"确认密码不能少于6位");
            m_etPwdAgain.requestFocus();
            return false;
        }else if(!RegexUtil.checkPassword(m_strPasswordAgain)){
            Utils.showToast(this,"输入6～18位数字字母组合");
            m_etPwdAgain.requestFocus();
            return false;
        }

        if(!m_strPasswordNew.equals(m_strPasswordAgain)){
            Utils.showToast(this,"新密码与确认密码不一致");
            m_etPwdNew.requestFocus();
            return false;
        }

        if(strPasswordBellow.equals(m_strPasswordAgain)){
            Utils.showToast(this,"原密码与新密码一致");
            m_etPwdNew.requestFocus();
            return false;
        }

        m_strMd5Pwd = MD5.encode(m_strPasswordAgain);
        m_strPasswordBellow = MD5.encode(strPasswordBellow);
        return true;
    }

    @OnClick({R.id.tv_title_back,R.id.tv_title_right})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                if(isInputValid()){
                    callHttpForChangePwd();
                }
                break;

        }
    }

    private void callHttpForChangePwd(){
        String m_strPhone = SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserTelphone);
        String urlDataString = "?u_telphone="+m_strPhone+"&u_x_pwd="+m_strMd5Pwd+"&u_y_pwd="+m_strPasswordBellow;
        HttpClient.get(ApiStores.changePwd + urlDataString, new HttpCallback<ResponseBaseBean>() {
            @Override
            public void OnSuccess(ResponseBaseBean response) {
                Log.d("",response.toString());

                if(response.getResult()){
                    login(m_strMd5Pwd,response.getMessage());
                }else{
                    Utils.showToast(ChangePwdActivity.this,response.getMessage());
                    kProgressHUD.dismiss();
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
            }
        });
    }

    private void messageCenter(String title,String message){
        AlertUtils.MessageAlertShow(this, title, message);
    }

    private void login(String pwd,final String msg){

        DemoDBManager.getInstance().closeDB();
        DemoHelper.getInstance().setCurrentUserName(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserTelphone));
        EMClient.getInstance().login(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserTelphone), pwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                kProgressHUD.dismiss();
                Utils.showToast(ChangePwdActivity.this,msg);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,Toast.LENGTH_SHORT).show();
                        kProgressHUD.dismiss();
                        finish();
                    }
                });
            }
        });
    }

}
