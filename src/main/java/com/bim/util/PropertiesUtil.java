package com.bim.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件工具类
 */
public class PropertiesUtil {
	private static Properties p = new Properties();  
	  
    /** 
     * 读取properties配置文件信息 
     */  
    static{  
        try {  
            p.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("sys-config.properties"));  
        } catch (IOException e) {  
            e.printStackTrace();   
        }  
    }  
    
    /** 
     * 根据key得到value的值 
     */  
    public static String getValue(String key){  
        return p.getProperty(key);  
    }  
    
}
