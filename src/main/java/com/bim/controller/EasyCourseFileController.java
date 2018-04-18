package com.bim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("coursefile")
public class EasyCourseFileController {

	@RequestMapping("init")
	public String inti() {
		return "admin/coursefile";
	}
}
