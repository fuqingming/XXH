package com.jy.xxh.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.jy.xxh.R;

/**
 * @author 付庆明
 *
 */

/**倒计时*/
public class SmsSendCounter extends CountDownTimer
{
	TextView m_btn = null;
	Context mContext;

	public SmsSendCounter(Context context,TextView btn, long millisInFuture, long countDownInterval)
	{
		super(millisInFuture, countDownInterval);
		m_btn = btn;
		mContext = context;
	}

	@Override
	public void onFinish() 
	{
		m_btn.setEnabled(true);
		m_btn.setTextColor(mContext.getResources().getColor(R.color.red));
		m_btn.setText("获取验证码");
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onTick(long millisUntilFinished) 
	{
		m_btn.setText(String.valueOf(millisUntilFinished / 1000));
		m_btn.setTextColor(mContext.getResources().getColor(R.color.dark));
	}
}	