package com.jy.xxh.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 许福康
 *
 */
public class RegexUtil
{
	// 验证手机号(11位以1开头的数字)
	public static boolean checkMobile(final String str)
	{
		return regexMatch(str, "^1[0-9]{10}$");
	}

	// 验证密码（）
	public static boolean checkPassword(final String str)
	{
		return regexMatch(str, "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$");
	}

	// 验证邮箱
	public static boolean checkEmail(final String str)
	{
		return regexMatch(str, "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
	}
	
	// 验证微信
	public static boolean checkWX(final String str)
	{
		return regexMatch(str, "^[a-zA-Z][-_0-9a-zA-Z]{5,19}$");
	}
	
	// 验证银行账号(13~20位数字)
	public static boolean checkBanckNo(final String str)
	{
		return regexMatch(str, "^[0-9]{13,20}$");
	}	
	
	// 验证邮编(6位数字)
	public static boolean checkPostCode(final String str)
	{
		return regexMatch(str, "^[0-9]{6}$");
	}	
	
	// 验证金额(整数或两位小数)
	public static boolean checkMoneyAmount(final String str)
	{
		return regexMatch(str, "^[0-9]+([\\.][0-9]{1,2}){0,1}$");
	}	
	
	// 验证身份证号
	public static boolean checkCertNo(final String str)
	{
		return regexMatch(str, "^[0-9]{17}[0-9xX]$");
	}
	
	// 验证真实姓名
	public static boolean checkRealName(final String str)
	{
		if(regexMatch(str, "^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z\\.\\u4e00-\\u9fa5]*[a-zA-Z\\u4e00-\\u9fa5]$"))
		{
			return !regexFind(str, "[\\.]{2,}");
		}
		else
		{
			return false;
		}
	}
	
	// 验证密码(6位数字)
//	public static boolean checkPassword(final String str)
//	{
//		return regexMatch(str, "^[0-9]{6}$");
//	}
	
	// 验证车牌号
	public static boolean checkCarNo(final String str)
	{
		return regexMatch(str, "^[\u4e00-\u9fa5]{1}[A-Za-z]{1}[A-Za-z0-9]{5}$");
	}
	
	// 验证登录名(必须以字符或数字开头)
	public static boolean checkLoginName(final String str)
	{
		return regexMatch(str, "^[a-zA-Z0-9][a-zA-Z0-9\\._-]*$");
	}	
	
	// 检查str是否符合正则表达式
	private static boolean regexMatch(final String str, final String strRegex)
	{
		boolean bRet = false;
		try
		{
			Pattern pattern = Pattern.compile(strRegex);
			Matcher matcher = pattern.matcher(str);
			bRet = matcher.matches();
		}
		catch (Exception e)
		{
			bRet = false;
		}
		return bRet;
	}
	
	// 检查str中是否出现了正则表达式strRegex
	private static boolean regexFind(final String str, final String strRegex)
	{
		boolean bRet = false;
		try
		{
			Pattern pattern = Pattern.compile(strRegex);
			Matcher matcher = pattern.matcher(str);
			bRet = matcher.find();
		}
		catch (Exception e)
		{
			bRet = false;
		}
		return bRet;
	}		
}