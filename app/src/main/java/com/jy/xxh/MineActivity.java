package com.jy.xxh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.view.switchbutton.FSwitchButton;
import com.xiao.nicevideoplayer.constants.GlobalVariables;
import com.jy.xxh.http.Upload;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.CleanMessageUtil;
import com.jy.xxh.util.Utils;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MineActivity extends BaseAppCompatActivity {
    private static final String LOG_TAG = "MineActivity";

    @BindView(R.id.tv_nickname)
    TextView m_tvNickname;
    @BindView(R.id.iv_icon)
    SimpleDraweeView m_ivIcon;
    @BindView(R.id.tv_clean)
    TextView m_tvClean;
    @BindView(R.id.sb_switch)
    FSwitchButton m_sbSwitch;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void setUpView() {
        ButterKnife.bind(this);
        Utils.initCommonTitle(this,"我的",true);

        if(SPUtils.getInstance(GlobalVariables.serverSp).getBoolean(GlobalVariables.serverWifiPlay)){
            m_sbSwitch.setChecked(true,false,true);
        }else{
            m_sbSwitch.setChecked(false,false,false);
        }

        m_sbSwitch.setOnCheckedChangedCallback(new FSwitchButton.OnCheckedChangedCallback()
        {
            @Override
            public void onCheckedChanged(boolean checked, FSwitchButton view)
            {
                SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverWifiPlay,checked);
            }
        });
    }

    @OnClick({R.id.ll_icon,R.id.ll_nickname,R.id.ll_clean,R.id.btn_logout})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.ll_icon:
                openRadio();
                break;
            case R.id.ll_nickname:
                Intent it = new Intent(this,ChangeNicknameActivity.class);
                startActivity(it);
                break;
            case R.id.ll_clean:
                Utils.showDialogClean(MineActivity.this,m_tvClean);
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true,new EMCallBack() {

            @Override
            public void onSuccess() {
               runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserId,"");
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserIcon, "");
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserTelphone, "");
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserNickame, "");
                        finish();

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(MineActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_tvNickname.setText(SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserNickame));
        LoaderManager.getLoader().loadNet(m_ivIcon, SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),
                new ILoader.Options(R.mipmap.head_s, R.mipmap.head_s));
        String lenth = CleanMessageUtil.getTotalCacheSize(MineActivity.this);
        m_tvClean.setText(lenth);
    }

    /**
     * 自定义单选
     */
    private void openRadio() {
        RxGalleryFinal
                .with(this)
                .image()
                .radio()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        String strUrl = "http://wap.ngwatch.top/index/User/changePhoto&u_id="+SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
                        String path = imageRadioResultEvent.getResult().getOriginalPath();
                        File image = new File(path);
                        new Upload(image,MineActivity.this,m_ivIcon).execute(strUrl);
                    }
                })
                .openGallery();
    }
}
