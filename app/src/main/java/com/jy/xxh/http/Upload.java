package com.jy.xxh.http;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jy.xxh.R;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;
import com.jy.xxh.bean.response.ResponseChangeHeadBean;
import com.kaopiz.kprogresshud.KProgressHUD;
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
    OnTaskSuccessComplete onTaskSuccess;
    KProgressHUD kProgressHUD;

    public Upload(File file, Context context, KProgressHUD kProgressHUD,OnTaskSuccessComplete onTaskSuccess){
        this.file = file;
        this.mContext = context;
        this.onTaskSuccess = onTaskSuccess;
        this.kProgressHUD = kProgressHUD;
    }
    @Override
    protected String doInBackground(String... strings) {
        return UploadImg.uploadFile(file,strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null){
            if (onTaskSuccess != null)
            {
                onTaskSuccess.onSuccess(s);
            }

        }else{
            Toast.makeText(mContext,"上传失败", Toast.LENGTH_SHORT).show();
            onTaskSuccess.onSuccess("");
        }
        kProgressHUD.dismiss();
    }
}
