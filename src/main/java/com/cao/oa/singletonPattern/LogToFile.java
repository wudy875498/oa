package com.cao.oa.singletonPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogToFile {
	public static String PROJECT_NAME = "";
	private static final String fileName = "_webAccessLog.log";
	private static final String fileParentName = "webLog/cxx";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss.SSSZ");
	private static File file = null;
	private static LogToFile log = null;
	public synchronized static LogToFile getInstance(){
		if(log==null){
			log = new LogToFile();
		}
		return log;
	}
	private LogToFile(){
		String classPath = this.getClass().getResource("/").getPath();
		classPath = classPath.substring(1,classPath.indexOf("/target/classes/"));
		PROJECT_NAME = classPath.substring(classPath.lastIndexOf("/")+1);
		file = new File(fileParentName,PROJECT_NAME+fileName);
		File fileParent = new File(fileParentName);
		if(!fileParent.exists()){
			fileParent.mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String getAbsolutePath(){
		return file.getAbsolutePath();
	}
	public String readAllFile(){
		FileInputStream fis = null;
		String res = "";
		try {
			fis = new FileInputStream(file);
			
			int len;
			byte[] b = new byte[1024];
			while((len=fis.read(b))!=-1){
				res += new String(b,0,len,"utf-8");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "��ȡʧ��";
		} catch (IOException e) {
			e.printStackTrace();
			return "��ȡʧ��";
		}finally {
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	public void logStart(){
		System.out.println("��"+PROJECT_NAME+"����־�洢λ�ã�"+file.getAbsolutePath());
		String str = ""+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator();
		str += "####################ϵͳ����###########################"+System.lineSeparator();
		str += "����ʱ�䣺"+sdf.format(new Date())+System.lineSeparator();
		str += "[ʱ��]\t\t\t\t\t ���ʷ�ʽ\t ����IP\t\t �����û�\t ����\t ����·�� \t\t"+System.lineSeparator();
		wirteToFile(str);
	}
	public void logStop(){
		String str = "";
		str += "�ر�ʱ�䣺"+sdf.format(new Date())+System.lineSeparator();
		str += "####################ϵͳ�ر�###########################"+System.lineSeparator();
		str += ""+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator();
		wirteToFile(str);
		System.out.println("��"+PROJECT_NAME+"������ȫ�رգ�");
	}

	public void log(HttpServletRequest req, HttpServletResponse resp,String content){
		String str = "["+sdf.format(new Date())+"]"+"\t";//����ʱ��
		str += req.getMethod()+"\t\t";//����ʽ
		str += req.getRemoteAddr()+":"+req.getRemotePort()+"\t";//����IP
		//�����û�
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("userJobId");
		if(userId==null){
			str += "�ο�\t\t";
		}else{
			str += userId+"\t\t";
		}
		str += content+"\t";//����
		str += req.getServletPath()+"\t";//����·��
		str += req.getQueryString()+"\t";//����
		str += System.lineSeparator();
		wirteToFile(str);
		System.out.println("��"+PROJECT_NAME+"��"+str);
	}
	/**
	 * д���ļ�
	 * @param str
	 */
	private synchronized void wirteToFile(String str){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			byte[] b = str.getBytes("utf-8");
			fos.write(b);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
