package com.jy.xxh.http;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.R;
import com.jy.xxh.bean.response.ResponseChangeHeadBean;
import com.xiao.nicevideoplayer.constants.GlobalVariables;
import com.vise.xsnow.loader.ILoader;
import com.vise.xsnow.loader.LoaderManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by asus on 2018/3/6.
 */

public class Upload  extends AsyncTask<String,Void,String> {
    File file;
    Context mContext;
    SimpleDraweeView m_ivIcon;

    public Upload(File file, Context context,SimpleDraweeView m_ivIcon){
        this.file = file;
        this.mContext = context;
        this.m_ivIcon = m_ivIcon;
    }
    @Override
    protected String doInBackground(String... strings) {
        return UploadImg.uploadFile(file,strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null){
            ResponseChangeHeadBean responseChangeHeadBean = transform(s);
            if(responseChangeHeadBean.getResult()){
                Toast.makeText(mContext,responseChangeHeadBean.getMessage(),Toast.LENGTH_SHORT).show();
                SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserIcon,responseChangeHeadBean.getU_photo());
                LoaderManager.getLoader().loadNet(m_ivIcon, SPUtils.getInstance(GlobalVariables.serverSp).getString(GlobalVariables.serverUserIcon),
                        new ILoader.Options(R.mipmap.head_s, R.mipmap.head_s));
            }
        }else{
            Toast.makeText(mContext,"上传失败", Toast.LENGTH_SHORT).show();
        }
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
