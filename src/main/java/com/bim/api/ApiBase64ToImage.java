package com.bim.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Decoder;

@Controller
@RequestMapping("api/base64")
public class ApiBase64ToImage {

	@RequestMapping("download")
	public static void imageDownload(String base64Code, String fileName, HttpServletResponse response) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] body = decoder.decodeBuffer(base64Code);
		// 调整异常数据
		for (int i = 0; i < body.length; ++i) {
			if (body[i] < 0) {
				body[i] += 256;
			}
		}
		fileName = URLEncoder.encode(fileName,"UTF-8");
	    OutputStream out = null;
	    response.reset(); 
	    response.setContentType("application/octet-stream; charset=utf-8"); 
	    response.setHeader("Content-Disposition", "attachment; filename=" + fileName); 
	    out = response.getOutputStream(); 
	    out.write(body); 
	    out.flush();
	    
	}
	
}
