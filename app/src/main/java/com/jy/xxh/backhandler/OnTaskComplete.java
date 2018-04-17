package com.jy.xxh.backhandler;

public interface OnTaskComplete
{
	public void onComplete(Object obj);		// 先调用
	public void onSuccess(Object obj);		// 如果成功，后调用
	public void onFail(Object obj);			// 如果失败，后调用
}
