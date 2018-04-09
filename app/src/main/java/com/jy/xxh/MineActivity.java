package com.jy.xxh;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;
import com.jy.xxh.base.BaseAppCompatActivity;
import com.jy.xxh.bean.response.ResponseChangeHeadBean;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.util.DirSettings;
import com.jy.xxh.util.FileUtil;
import com.jy.xxh.view.switchbutton.FSwitchButton;
import com.jy.xxh.http.Upload;
import com.jy.xxh.huanxin.DemoHelper;
import com.jy.xxh.util.CleanMessageUtil;
import com.jy.xxh.util.Utils;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import org.json.JSONException;
import org.json.JSONObject;

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
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverWifiPlay,true);
                        SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverIsReceiveMessage,true);
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
                    @SuppressLint("NewApi")
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        kProgressHUD.show();
                        String strUrl = "http://wap.ngwatch.top/index/User/changePhoto&u_id="+SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserId);
                        final String path = imageRadioResultEvent.getResult().getOriginalPath();

                        // 压缩图片
                        Bitmap bitmap = Utils.centerSquareScaleBitmap(BitmapFactory.decodeFile(path),MineActivity.this);

                        // 如果有必要，对图片进行旋转
                        int nDegree = Utils.readPictureDegree(path);
                        if(nDegree != 0)
                        {
                            bitmap = Utils.rotateBitmap(bitmap, nDegree);
                        }

                        // 保存图片
                        FileUtil.creatDirsIfNeed(DirSettings.getAppCacheDir());
                        if(!Utils.saveBitmap(bitmap, DirSettings.getAppCacheDir(), "myself_tmp_head_pic.png"))
                        {
                            Utils.showToast(MineActivity.this, "保存图片失败");
                        }

                        File image = new File(DirSettings.getAppCacheDir()+"myself_tmp_head_pic.png");
                        new Upload(image,MineActivity.this,kProgressHUD,new OnTaskSuccessComplete()
                        {
                            @Override
                            public void onSuccess(Object obj)
                            {
                                ResponseChangeHeadBean responseChangeHeadBean = transform((String) obj);
                                if(responseChangeHeadBean.getResult()){
                                    Toast.makeText(MineActivity.this,responseChangeHeadBean.getMessage(),Toast.LENGTH_SHORT).show();
                                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserIcon,responseChangeHeadBean.getU_photo());
                                    LoaderManager.getLoader().loadNet(m_ivIcon, SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),
                                            new ILoader.Options(R.mipmap.head_s, R.mipmap.head_s));
                                }
                            }
                        }).execute(strUrl);
                    }
                })
                .openGallery();
    }

    private ResponseChangeHeadBean transform(String response){
        JSONObject jsonObject = null;
        ResponseChangeHeadBean responseChangeHeadBean = new ResponseChangeHeadBean();
        try {
            jsonObject = new JSONObject(response);
            boolean result = jsonObject.getBoolean("result");
            String message = jsonObject.getString("message");
            int code = jsonObject.getInt("code");
            String content = jsonObject.getString("content");

            JSONObject jsonObjectContent = new JSONObject(content);
            String u_photo = jsonObjectContent.getString("u_photo");

            responseChangeHeadBean.setCode(code);
            responseChangeHeadBean.setResult(result);
            responseChangeHeadBean.setMessage(message);
            responseChangeHeadBean.setU_photo(u_photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseChangeHeadBean;
    }
}
