package com.jy.xxh.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.jy.xxh.constants.GlobalVariables;
import com.jy.xxh.snow.db.DbHelper;
import com.jy.xxh.snow.loader.FrescoLoader;
import com.mob.MobSDK;
import com.mob.tools.proguard.ProtectedMemberKeeper;
import com.tencent.bugly.crashreport.CrashReport;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.interceptor.HttpLogInterceptor;
import com.vise.xsnow.loader.LoaderManager;

import com.jy.xxh.huanxin.DemoHelper;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application  implements ProtectedMemberKeeper {
    public static Context applicationContext;
    private static BaseApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        Utils.init(this);

        initLog();
        initNet();
        DbHelper.getInstance().init(this);
        LoaderManager.setLoader(new FrescoLoader());//外部定制图片加载库Fresco
        LoaderManager.getLoader().init(this);

        applicationContext = this;
        instance = this;

        // bugly start
        CrashReport.initCrashReport(getApplicationContext(), "52acc4be41", true);
        // bugly end

        //init demo helper
        DemoHelper.getInstance().init(applicationContext);

        MobSDK.init(this, this.getAppkey(), this.getAppSecret());

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    private void initLog() {
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(false);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    private void initNet() {
        ViseHttp.init(this);
        ViseHttp.CONFIG()
                //配置请求主机地址
                .baseUrl("http://192.168.1.105/")
                //配置日志拦截器
                .interceptor(new HttpLogInterceptor()
                        .setLevel(HttpLogInterceptor.Level.BODY));

    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public BaseApplication() {
    }

    protected String getAppkey() {
        return null;
    }

    protected String getAppSecret() {
        return null;
    }
}