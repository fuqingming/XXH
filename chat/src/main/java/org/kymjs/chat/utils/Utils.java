package org.kymjs.chat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
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

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/2/7.
 */

public class Utils {

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

    // 点击EditText外的地方隐藏软键盘
    public static void setOnTouchEditTextOutSideHideIM(final Activity activity, final View rootViewInsideScrollView)
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

    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if(cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    public static String delHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");

        Pattern a_space = Pattern.compile("&nbsp;", Pattern.CASE_INSENSITIVE);
        Matcher b_space = a_space.matcher(htmlStr);
        htmlStr = b_space.replaceAll("");

        return htmlStr.trim(); // 返回文本字符串
    }
}
