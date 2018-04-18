package com.bim.util;

/**
 * 常量类
 */
public class Constant {
	
	public static final int user_type_0 = 0;//超级用户
	
	public static final int user_type_1 = 1;//其他用户
	
	public static final String defult_password = "123456";//默认密码
	
	public static final String TOKEN = "123456";//默认密码
	
	public static final String TOKEN_KEY="TOKEN";
	
	/**
	 * 系统管理员
	 */
	public static final String SYSTEM_ROLE_0="0";
	
	/**
	 * 项目权限管理员
	 */
	public static final String SYSTEM_ROLE_1="1";
	
	/**
	 * 项目数据管理员
	 */
	public static final String SYSTEM_ROLE_2="2";
	
	/**
	 * 项目图片目录
	 */
	public static final String PROJECT_PRICTURE="/project/image";
	/**
	 * 问题文件目录
	 */
	public static final String QUESTION_DIR="/question";
	
	/**
	 * 过期时间
	 */
	public static final int LOGIN_EXPIRE_TIME=60*60*24*30;//一个月
	
	/**
	 * 模型文件格式
	 */
	public static final String[] MODEL_FILE_FORMAT={".igs",".ifc",".hsf"};
	
	/**
	 * 点云文件格式
	 */
	public static final String[] CLOUD_FILE_FORMAT={".rtp"};
	
	/**
	 * MI文件格式
	 */
	public static final String[] MI_FILE_FORMAT={".zip"};
	
	/**
	 * ztree父图标http://localhost:8080/Bim
	 */
	public static final String ICON_PARENT="../../Bim/webhtml/resources/bim/css/img/model_tree_icon.png";
	
	/**
	 * ztree子图标 模型
	 */
	public static final String ICON_MODEL="../../Bim/webhtml/css/icon/模型视点.png";
	
	/**
	 * ztree子图标  点云
	 */
	public static final String ICON_CLOUD="../../Bim/webhtml/css/icon/点云视点.png";
	
	/**
	 * 点云稀释倍数"1,1/4,1/9,1/16,1/25"
	 */
	public static final Integer[] CLOUD_DILUTE_SIGN={1,2,3,4,5};
	
}

