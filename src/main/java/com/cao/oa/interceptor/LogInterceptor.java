package com.cao.oa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cao.oa.singletonPattern.LogToFile;

public class LogInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LogToFile.getInstance().log(request, response, "���ʽ���");
		if(request.getServletPath().equals("/admin/where/is/the/log/file.do")){
			System.out.println("��"+LogToFile.PROJECT_NAME+"����־�ļ�Ϊλ��Ϊ��"+LogToFile.getInstance().getAbsolutePath());
			response.getWriter().println(LogToFile.getInstance().getAbsolutePath());
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		LogToFile.getInstance().log(request, response, "�����뿪");
	}

}
