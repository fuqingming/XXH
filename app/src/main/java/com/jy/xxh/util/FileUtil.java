package com.jy.xxh.util;

import java.io.File;

/**
 * @author 付庆明
 *
 */
public class FileUtil
{
	// 检查文件是否存在
	public static boolean isFileExist(String strFileName)
	{
		if(strFileName == null)
		{
			return false;
		}
		File file = new File(strFileName);
		return file.exists();
	}
	
	// 删除文件
	public static boolean deleteFile(String strFileName)
	{
		if(strFileName == null)
		{
			return true;
		}
		
		File file = new File(strFileName);
		if(!file.exists())
		{
			return true;
		}
		
		return file.delete();
	}
	
	// 创建本项目所使用的目录
	public static void creatDirsIfNeed(String strDirName)
	{
		File file = new File(strDirName);
		if (!file.exists())
		{
			file.mkdirs();
		}
	}	
	
	// 文件改名字
	public static boolean renameFile(String strOldFileName, String strNewFileName)
	{ 
        if(!strOldFileName.equals(strNewFileName))
        {
            File fOld = new File(strOldFileName); 
            File fNew = new File(strNewFileName); 
            
            if(!fOld .exists())
            {
                return false;//重命名文件不存在
            }
            
            if(fNew .exists())
            {
            	return false;
            }

            fOld .renameTo(fNew);
            return true;
        }
        else
        {
            return true;
        }
    }
	
	// 删除文件夹
	public static void deleteDir(String strDir)
	{
		deleteFiles(new File(strDir));
	}
	public static void deleteFiles(File file) 
	{
		// 判断文件是否存在
		if (file.exists()) 
		{
			// 判断是否是文件
			if (file.isFile()) 
			{ 
				file.delete(); 
			} 
			// 否则如果它是一个目录
			else if (file.isDirectory()) 
			{ 
				// 声明目录下所有的文件 files[];
				File files[] = file.listFiles(); 

				// 遍历目录下所有的文件
				for (int i = 0; i < files.length; i++)
				{ 
					deleteFiles(files[i]); 
				}
			}
			file.delete();
		} 
		else 
		{
			// do nothing
		}
	}
}
