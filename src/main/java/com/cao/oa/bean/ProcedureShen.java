package com.cao.oa.bean;

import java.util.Date;

/**
 * �ύ�����̵���������
 * @author DELL
 *
 */
public class ProcedureShen {
	public static final int WORK_NO = 0;
	public static final int WORK_OK = 1;
	public static final int WORK_NEED = 2;
	public static final int WORK_PASS = 3;
	
	private int id;//��������ID
	private int procedureId;//���̱��
	private int userGroup;//����������С��
	private int userPart;//���������ڲ���
	private String userJobId;//������ID
	private String userName;//����������
	private int order;//����˳��
	private int work;//�Ƿ��Ѿ������ˣ���Ҫ����
	private String name;//�������̵�����
	
	private String content;//��������
	private Date time;//����ʱ��
	private boolean pass;//�Ƿ�ͨ��
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(int procedureId) {
		this.procedureId = procedureId;
	}
	public int getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(int userGroup) {
		this.userGroup = userGroup;
	}
	public int getUserPart() {
		return userPart;
	}
	public void setUserPart(int userPart) {
		this.userPart = userPart;
	}
	public String getUserJobId() {
		return userJobId;
	}
	public void setUserJobId(String userJobId) {
		this.userJobId = userJobId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getWork() {
		return work;
	}
	public void setWork(int work) {
		this.work = work;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public void setPass(int pass) {
		if(pass==1){
			this.pass = true;
		}else{
			this.pass = false;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ProcedureShen [id=" + id + ", procedureId=" + procedureId + ", userGroup=" + userGroup + ", userPart="
				+ userPart + ", userJobId=" + userJobId + ", userName=" + userName + ", order=" + order + ", work="
				+ work + ", name=" + name + ", content=" + content + ", time=" + time + ", pass=" + pass + "]";
	}
	
	
	
	
}
