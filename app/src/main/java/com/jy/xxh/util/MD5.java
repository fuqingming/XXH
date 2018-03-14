package com.jy.xxh.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.SortedMap;

import org.json.JSONObject;

public class MD5 {

	public static String encode(String str) 
	{
		byte[] byteArray = null;
		try 
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byteArray = md5.digest(str.getBytes("UTF-8"));
		} 
		catch (Exception ex) 
		{
		}
		
		return byte2hexString(byteArray);
	}
	
	private static final String byte2hexString(byte[] bytes) {
		StringBuffer bf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				bf.append("0");
			}
			bf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return bf.toString();
	}
	
	
	
	public static String getSign(SortedMap<String, ? extends Object> sort, final String strWorkKey) {
		StringBuffer sign = new StringBuffer();
		for (Iterator<String> iterator = sort.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = sort.get(key);
			if (value != null && value != JSONObject.NULL) {
				sign.append(value);
			}
		}
		sign.append(strWorkKey);
		return MD5.encode(sign.toString());
	}
}
