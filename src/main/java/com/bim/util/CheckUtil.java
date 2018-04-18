package com.bim.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	/**
	 * 校验文件后缀
	 * @param suffix
	 * @param rules
	 * @return
	 */
	public static boolean checkSuffix(String suffix,String [] rules){
		boolean flag = true;
		String lowercase = suffix.toLowerCase();
		if(!Arrays.asList(rules).contains(lowercase)){
			flag = false;
		}
		return flag;
	}
	
	public static boolean checkContainChinese(String str){
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
	}
}
