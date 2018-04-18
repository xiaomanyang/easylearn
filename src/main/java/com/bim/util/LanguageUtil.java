package com.bim.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;

public class LanguageUtil {
	
	private static MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource){
		LanguageUtil.messageSource=messageSource;
	}
	
	/**
	 * 获取当前选择的多语言
	 * @return
	 */
	public static Locale getLocale(){
		return LocaleContextHolder.getLocale();
	}
	
	/**
	 * 根据key获取当前语言版本的字符
	 * @param code xxx 默认约定定义在LanguageKey文件中 
	 * @return
	 */
	public static String getMessage(String code){
		return getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据key 获取指定语言版本的字符 
	 * @param code xxx 默认约定定义在LanguageKey文件中
	 * @param locale 如：new Locale("zh","CN");
	 * @return
	 */
	public static String getMessage(String code, Locale locale){
		return getMessage(code, null, locale);
	}
	/**
	 * 获取带参数形式的当前语言字符 如资源文件中定义为 xxx=hi {0} ,你好.
	 * @param code xxx 默认约定定义在LanguageKey文件中
	 * @param args 替换占位符的参数 如 小明
	 * @return
	 */
	public static String getMessage(String code, Object[] args){
		return getMessage(code, args, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据key获取带参数且指定语言的字符 如资源文件中定义为 xxx=hi {0} ,你好.
	 * @param code xxx 默认约定定义在LanguageKey文件中
	 * @param args 替换占位符的参数 如 小明
	 * @param locale 如：new Locale("zh","CN");
	 * @return
	 */
	public static String getMessage(String code, Object[] args, Locale locale){
		return messageSource.getMessage(code, args, locale);
	}
	
	public static String getMessage(MessageSourceResolvable resolvable,  Locale locale){
		return messageSource.getMessage(resolvable, locale);
	}
}
