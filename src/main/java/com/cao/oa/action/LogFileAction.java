package com.cao.oa.action;

import com.cao.oa.singletonPattern.LogToFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogFileAction {
	
	public LogFileAction(){
		super();
	}

	@RequestMapping("admin/where/is/the/log/file.do")
	@ResponseBody
	public String where(){
		return "redirect:/home.do";
	}
	
	@RequestMapping("admin/read/the/log/file.do")
	@ResponseBody
	public String read(){
		return LogToFile.getInstance().readAllFile();
	}

}
