package com.jy.xxh.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xxh.R;
import com.jy.xxh.backhandler.OnTaskSuccessComplete;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/2/7.
 */

public class Utils {

    // 设置通用Title
    public static void initCommonTitle(final Activity activity, final String strTitle)
    {
        initCommonTitle(activity, strTitle, false);
    }

    // 设置通用Title
    public static void initCommonTitle(final Activity activity, final String strTitle, Boolean bShowReturnButton)
    {
        initCommonTitle(activity, strTitle, bShowReturnButton, R.mipmap.back_b);
    }

    // 设置通用Title
    public static void initCommonTitle(	final Activity activity,
                                           final String strTitle,
                                           Boolean bShowReturnButton,
                                           final int strButtonIcon)
    {
        initCommonTitle(activity, null, strTitle, bShowReturnButton, strButtonIcon);
    }

    // 设置通用Title
    public static void initCommonTitle(final View vContent, final String strTitle,final int ivIcon)
    {
        initCommonTitle(null,vContent, strTitle,true,ivIcon);
    }

    // 设置通用Title
    public static void initCommonTitle(final View vContent, final String strTitle)
    {
        initCommonTitle(null,vContent, strTitle,false,0);
    }

    // 设置通用Title
    public static void initCommonTitle(	final Activity activity,
                                           final View vContent,
                                           final String strTitle,
                                           Boolean bShowReturnButton,
                                           final int strButtonIcon)
    {
        TextView tvTitle = null;
        if(vContent == null)
        {
            tvTitle = activity.findViewById(R.id.tv_title_text);
        }
        else
        {
            tvTitle =  vContent.findViewById(R.id.tv_title_text);
        }

        // Title
        tvTitle.setText(strTitle);

        // 返回按钮
        if(bShowReturnButton)
        {
            ImageView ivBack = null;
            if(vContent == null)
            {
                ivBack = activity.findViewById(R.id.iv_title_back);
                ivBack.setImageDrawable(activity.getResources().getDrawable(strButtonIcon));
            }
            else
            {
                ivBack = vContent.findViewById(R.id.iv_title_back);
                ivBack.setImageDrawable(vContent.getResources().getDrawable(strButtonIcon));
            }


            ivBack.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    activity.finish();
                }
            });
        }
    }

    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 打开软键盘
     * @param context
     */
    public static void showKeyboard(final Context context){
        new Timer().schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },300);

    }

    /**
     * 关闭软键盘
     * @param mContext
     */
    public static void hintKeyboard(final Context mContext,final EditText mEditText){
        new Timer().schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        },300);
    }

    // 点击EditText外的地方隐藏软键盘
    public static void setOnTouchEditTextOutSideHideIM(final Activity activity)
    {
        View vContent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        vContent.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                View view = activity.getCurrentFocus();
                if(view != null)
                {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
                else
                {
                    return true;
                }
            }
        });
    }

    // 点击EditText外的地方隐藏软键盘
    public static void setOnTouchEditTextOutSideHideIM(final Fragment fragment)
    {
        final Activity activity = fragment.getActivity();
        View vContent = fragment.getView();
        vContent.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View view, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View v = activity.getCurrentFocus();
                if(v != null)
                {
                    IBinder ib = v.getWindowToken();
                    return imm.hideSoftInputFromWindow(ib, 0);
                }
                else
                {
                    return false;
                }
            }
        });
    }

    // 点击EditText外的地方隐藏软键盘
    public static void setOnTouchEditTextOutSideHideIM(final Fragment fragment, final View rootViewInsideScrollView)
    {
        final Activity activity = fragment.getActivity();
        rootViewInsideScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View view, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

                boolean bRet = false;
                try
                {
                    bRet = imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    public static Dialog showDialog(final Context context,final Intent it)
    {
        View.OnClickListener onLeftButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        };

        View.OnClickListener onRightButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                MyApplication.getInstance().exit();
                context.startActivity(it);
            }
        };

        return showCommonDialog(context,
                "页面跳转",
                "是否离开当前页面，前往积分兑换码中心，使用兑换码。",
                "取消",
                onLeftButtonClickListener,
                "立即前往",
                onRightButtonClickListener);
    }

    public static Dialog showDialogLive(final Context context,final Intent it)
    {
        View.OnClickListener onLeftButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(it);
            }
        };

        View.OnClickListener onRightButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utils.showToast(context,"直播提醒开始");
            }
        };

        return showCommonDialog(context,
                "设置提醒",
                "设置直播提醒，我们将在直播开始前给你发送提示消息",
                "取消",
                onLeftButtonClickListener,
                "确定",
                onRightButtonClickListener);
    }

    public static Dialog showDialogClean(final Context context,final TextView tvClean)
    {
        View.OnClickListener onLeftButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        };

        View.OnClickListener onRightButtonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CleanMessageUtil.clearAllCache(context);
                tvClean.setText("0.00MB");
            }
        };

        return showCommonDialog(context,
                "清除缓存",
                "确认清除缓存吗?",
                "取消",
                onLeftButtonClickListener,
                "确定",
                onRightButtonClickListener);
    }

    public static Dialog showCommonDialog(final Context context,
                                          final String strTitle,
                                          final String strMsg,
                                          final String strLeftButtonText,
                                          final View.OnClickListener onLeftButtonClickListener,
                                          final String strRightButtonText,
                                          final View.OnClickListener onRightButtonClickListener)
    {
        View vContent = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        final Dialog dlg = new Dialog(context, R.style.common_dialog);
        dlg.setContentView(vContent);
        dlg.setCanceledOnTouchOutside(false); // 点击窗口外区域不消失
        dlg.setCancelable(false); // 返回键不消失

        // 必须用代码调整dialog的大小
        android.view.WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        //lp.width = (int) (MyApplication.s_nScreenWidth * 0.95);
        //lp.height = (int) (MyApplication.m_nScreenHeight * 0.5);
        lp.width = (int) context.getResources().getDimension(R.dimen.dialog_width);
        dlg.getWindow().setAttributes(lp);

        // title
        TextView tvTitle = vContent.findViewById(R.id.tv_title);
        if (strTitle != null && !strTitle.isEmpty())
        {
            tvTitle.setText(strTitle);
        }
        else
        {
            tvTitle.setVisibility(View.GONE);
        }

        // msg
        TextView tvMsg =  vContent.findViewById(R.id.tv_msg);
        tvMsg.setText(strMsg);

        // left button
        Button btnLeft =  vContent.findViewById(R.id.btn_left);
        btnLeft.setText(strLeftButtonText);
        btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
                if (onLeftButtonClickListener != null)
                {
                    onLeftButtonClickListener.onClick(v);
                }
            }
        });

        // right button
        Button btnRight = vContent.findViewById(R.id.btn_right);
        btnRight.setText(strRightButtonText);
        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
                if (onRightButtonClickListener != null)
                {
                    onRightButtonClickListener.onClick(v);
                }
            }
        });

        dlg.show();

        return dlg;
    }

    public static Dialog showCommonDialogEdit(final Context context, final String pwd,final OnTaskSuccessComplete onTaskSuccess)
    {
        View vContent = LayoutInflater.from(context).inflate(R.layout.dialog_common_edit, null);
        final Dialog dlg = new Dialog(context, R.style.common_dialog);
        dlg.setContentView(vContent);
        dlg.setCanceledOnTouchOutside(false); // 点击窗口外区域不消失
        dlg.setCancelable(false); // 返回键不消失

        // 必须用代码调整dialog的大小
        android.view.WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        //lp.width = (int) (MyApplication.s_nScreenWidth * 0.95);
        //lp.height = (int) (MyApplication.m_nScreenHeight * 0.5);
        lp.width = (int) context.getResources().getDimension(R.dimen.dialog_width);
        dlg.getWindow().setAttributes(lp);
        final EditText etPwd =  vContent.findViewById(R.id.et_password);
        // left button
        Button btnLeft =  vContent.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();

            }
        });

        // right button
        Button btnRight = vContent.findViewById(R.id.btn_right);
        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
                String strPwd = etPwd.getText().toString().trim();
                strPwd = MD5.encode(strPwd);
                if(pwd.equals(strPwd)){
                    if (onTaskSuccess != null)
                    {
                        onTaskSuccess.onSuccess(null);
                    }
                }else{
                    Utils.showToast(context,"密码错误");
                }
            }
        });

        dlg.show();

        return dlg;
    }

    public static void showToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
