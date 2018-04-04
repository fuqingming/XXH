package com.jy.xxh.util;

import android.os.Environment;

import java.io.File;

/**
 *
 */
public class DirSettings
{
	private static final String APP_BASE_DIR = "XXH/"; 		// 在SD卡上存放文件的主目录
	private static final String APP_CACHE_DIR = "Cache/"; 			// 从服务器下载的图片存放的子目录	

	private DirSettings()
	{

	}

	public static String getAppDir()
	{
		return getSDCardPath() + APP_BASE_DIR;
	}

	public static String getAppCacheDir()
	{
		return getAppDir() + APP_CACHE_DIR;
	}

	public static String getSDCardPath()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File dir = Environment.getExternalStorageDirectory();
			return dir.getPath() + "/";
		}
		return "/mnt/sdcard/";
	}
}
