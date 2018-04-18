package com.bim.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 */
public class FileUtil {
	//删除文件夹
	public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	/**
	 * 根据当前日期获取目录
	 * @return
	 */
	public static String getDirByDate(){
		return new SimpleDateFormat("/yyyy/MM/dd").format(new Date());
	}
	
	/**
	 * 根据当前时间获取文件名
	 * @return
	 */
	public static String getFileNameByTime(){
		return new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date())+new Random().nextInt(999);
	}
	
	/**
	 * 根据文件名获取扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtensionName(String fileName){
		int i=fileName.lastIndexOf(".");
		return i < 0 ? "" : fileName.substring(i);
	}
	
	/**
	 * 根据文件类型获取扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtensionContentType(String contentType){
		int i=contentType.lastIndexOf("/");
		return "."+(i<0?contentType:contentType.substring(i+1));
	}
	
	/**
	 * 判断是否图片类型
	 * @param file
	 * @return
	 */
	public static Boolean isImage(MultipartFile file)
	{
		String contentType=file.getContentType();
		return !StringUtils.isEmpty(contentType) && contentType.startsWith("image/");
	}
	
	//获取版本号 参数格式必须是1.0
	public static String getVersion(String version){
		String[] arr = version.split("\\.");
		int new_version = Integer.valueOf(arr[0]);
		return String.valueOf(++new_version)+".0";
	}
	
	public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        String[] filePath = file.list();
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdirs();
        }
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
                copyDir(newPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
            }
            if (new File(oldPath  + file.separator + filePath[i]).isFile()) {
                copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
            
        }
    }
	
	public static void copyFile(String oldPath, String newPath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(oldPath));
        FileOutputStream fos = new FileOutputStream(new File(newPath));
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = fis.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fis.close();
        fos.close();
    }
}
