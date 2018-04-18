package com.bim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("course")
public class EasyCourseController {

	@RequestMapping("init")
	public String inti() {
		return "admin/course";
	}
}
