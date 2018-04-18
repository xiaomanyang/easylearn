package com.bim.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StringUtils;

public class SmsUtil {
	private static String SN="";
	/*主地址*/
	private static final String SMS_MASTER_URL="http://sdk.entinfo.cn:8060/webservice.asmx";
	/*备用地址*/
	private static final String SMS_SPARE_URL="http://sdk2.entinfo.cn:8060/webservice.asmx";
	/**
	 * 默认短信(普通)
	 */
	private static final String SEND_DEFAULT_SMS="mdsmssend";
	/**
	 * 个性短信
	 */
	private static final String SEND_GX_SMS="mdgxsend";
	 
	/*sn软件序列号;pwd序列号密码;province省;city城市;trade行业;entname企业名称;linkman联系人;phone联系电话;mobile移动电话;email邮件地址;fax传真;address地址;postcode邮政编码;sign签名*/
	private static final String SMS_SN="SDK-BBX-010-17411";
	private static final String SMS_PWD="aF3-76bF";//md5(sn+password)32位大写密文 
	private static final String SMS_ENCRYPT_PWD="C20B0A6DA7716F9725321F629E63BD47";
	private static final String SMS_PROVINCE="北京";
	private static final String SMS_CITY="北京市";
	private static final String SMS_TRADE="软件开发";
	private static final String SMS_ENTNAME="北京数字营国信息技术有限公司";
	private static final String SMS_LINKMAN="王星宇";
	private static final String SMS_PHONE="010-51528859";
	private static final String SMS_MOBILE="13581631201";
	private static final String SMS_EMAIL="wang.x.y@bimtechnologie.com";
	private static final String SMS_FAX="no";
	private static final String SMS_ADDRESS="和协科技大厦";
	private static final String SMS_POSTCODE="100089";
	private static final String SMS_SIGN="BIM管控平台";
	
	public static final String VCODE_TEMPLATE="【BIM管控平台】 您的验证码为s%,请妥善处理!";
	
	/**
	 * 发送验证短信 (验证码)
	 * @param mobiles 手机号字符  如 "18611112222"
	 * @param content 短信内容
	 * @return
	 */
	public static String sendSMS(String mobile){
		String vcode=String.valueOf(new Random().nextInt(999999));
		while(vcode.length()<6){
			vcode="0"+vcode;
		}
		String statusCode=sendSMS(mobile,vcode,"");
		return "200".equals(statusCode)?vcode:"";
	}
	
	/**
	 * 发送短信
	 * @param mobiles 手机号字符  如 "18611112222,18611113333"
	 * @param content 短信内容
	 * @return
	 */
	public static String sendSMS(String mobiles,String content){
		return sendSMS(mobiles,content,"");
	}
	
	/**
	 * 发送短信 
	 * @param mobiles 手机号数组  如["18611112222","18611113333"]
	 * @param content 短信内容
	 * @return
	 */
	public static String sendSMS(String[] mobiles,String content){
		return sendSMS(StringUtils.arrayToDelimitedString(mobiles, ","),content,"");
	}
	
	/**
	 * 定时发送短信 
	 * @param mobiles 手机号数组 如["18611112222","18611113333"]
	 * @param content 短信内容
	 * @param time 定时时间 例如：2010-12-29 16:27:03（空表示立即发送）
	 * @return
	 */
	public static String sendSMS(String[] mobiles,String content,String time){
		return sendSMS(StringUtils.arrayToDelimitedString(mobiles, ","),content,time);
	}
	
	/**
	 * 定时发送短信 
	 * @param mobiles 手机号字符 如"18611112222,18611113333"
	 * @param content 短信内容
	 * @param time 定时时间 例如：2010-12-29 16:27:03（空表示立即发送）
	 * @return
	 */
	public static String sendSMS(String mobiles,String content,String time){
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("sn", SMS_SN));  
        formparams.add(new BasicNameValuePair("pwd", SMS_ENCRYPT_PWD));
        formparams.add(new BasicNameValuePair("mobile",mobiles));
        formparams.add(new BasicNameValuePair("content", content));
        formparams.add(new BasicNameValuePair("ext",""));
        formparams.add(new BasicNameValuePair("stime",time));
        formparams.add(new BasicNameValuePair("rrid", ""));
        formparams.add(new BasicNameValuePair("msgfmt", ""));
        HttpResult result=HttpClientUtil.post("http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend", formparams);
        return String.valueOf(result.getStatusCode());
	}
	
	/**
	 * get方式发送短信
	 * @param mobiles 手机号
	 * @param content 内容 内部采用utf-8编码
	 * @param time 定时时间 例如：2010-12-29 16:27:03（空表示立即发送）
	 * @return
	 */
	/*public String sendSMS(String[] mobiles,String content,String time){
		try {
			content=URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String strMobiles= StringUtils.arrayToDelimitedString(mobiles, ",");
    	StringBuilder sb=new StringBuilder();
		sb.append("sn="+SMS_SN);
		sb.append("&pwd="+MD5(SMS_SN+SMS_PWD).toUpperCase());
		sb.append("&mobile="+strMobiles);
		sb.append("&content="+content);
		sb.append("&ext=");
		sb.append("&stime=");
		sb.append("&rrid=");//短信批次标识 传什么返回什么
		sb.append("&msgfmt=");
    	String url="http://sdk.entinfo.cn:8061/webservice.asmx/mdgxsend?"+sb.toString();
    	HttpResult result= HttpClientUtil.get(url);
    	return String.valueOf(result.getStatusCode());
	}*/
	
	/*private String getUrlParam()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("sn="+SMS_SN);
		sb.append("&pwd="+MD5(SMS_SN+SMS_PWD).toUpperCase());
		sb.append("&province="+SMS_PROVINCE);
		sb.append("&city="+SMS_CITY);
		sb.append("&trade="+SMS_TRADE);
		sb.append("&entname="+SMS_ENTNAME);
		sb.append("&linkman="+SMS_LINKMAN);
		sb.append("&phone="+SMS_PHONE);
		sb.append("&mobile="+SMS_MOBILE);
		sb.append("&email="+SMS_EMAIL);
		sb.append("&fax="+SMS_FAX);
		sb.append("&address="+SMS_ADDRESS);
		sb.append("&postcode="+SMS_POSTCODE);
		sb.append("&sign="+SMS_SIGN);
		return sb.toString();
	}*/
	
	
	/*public String smsRegister(){
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("sn", SMS_SN));  
        formparams.add(new BasicNameValuePair("pwd", SMS_PWD));
        formparams.add(new BasicNameValuePair("province", SMS_PROVINCE));
        formparams.add(new BasicNameValuePair("city", SMS_CITY));
        formparams.add(new BasicNameValuePair("trade", SMS_TRADE));
        formparams.add(new BasicNameValuePair("entname", SMS_ENTNAME));
        formparams.add(new BasicNameValuePair("linkman", SMS_LINKMAN));
        formparams.add(new BasicNameValuePair("phone", SMS_PHONE));
        formparams.add(new BasicNameValuePair("mobile", SMS_MOBILE));
        formparams.add(new BasicNameValuePair("email", SMS_EMAIL));
        formparams.add(new BasicNameValuePair("fax", SMS_FAX));
        formparams.add(new BasicNameValuePair("address", SMS_ADDRESS));
        formparams.add(new BasicNameValuePair("postcode", SMS_POSTCODE));
        formparams.add(new BasicNameValuePair("sign", SMS_SIGN));
        HttpClientUtil.post("http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend", formparams);
        return "";
	}*/
	
	/**
	 * md5加密
	 * @param str
	 * @return
	 */
	/*public String MD5(String str){
		try {
			return DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}*/
}
