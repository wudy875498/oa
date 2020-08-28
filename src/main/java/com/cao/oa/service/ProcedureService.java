package com.cao.oa.service;

import com.cao.oa.bean.Message;
import com.cao.oa.bean.ModelProcedure;
import com.cao.oa.bean.ProcedureShen;
import com.cao.oa.bean.ProcedureSubmit;
import com.cao.oa.dao.MessageDao;
import com.cao.oa.dao.ProcedureDao;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProcedureService {
	public static final int PAGE_MODEL = 5;
	public static final int PAGE_MY = 5;
	public static final int PAGE_DEAL = 5;
	@Autowired
	private ProcedureDao procedureDao;
	@Autowired
	private MessageDao messageDao;

	
	/**
	 * ����һ������
	 * @param shen
	 * @param jobId
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public boolean dealOneProcedure(ProcedureShen shen, String jobId) throws Exception {
		Map<String,Object> map = procedureDao.dealOneProcedure(shen, jobId);
		if((boolean)map.get("finish")){
			String mtitle = "��ϵͳ������һ�������Ѿ������ɣ���鿴�����";
			String mcontent = "";
			if((boolean)map.get("result")){
				mcontent = "�����ͨ����\\(^o^)/~����ϸ�����鿴���ҵ����̡���";
			}else{
				mcontent = "���δͨ����/(��o��)/~~����ϸ�����鿴���ҵ����̡���";
			}
			sendOneSystemMessage(mtitle,mcontent,(String)map.get("person"));
		}else{
			// ����������
			if((boolean)map.get("hasNextPerson")){
				sendOneSystemMessage("��ϵͳ������һ����Ҫ����������","��ϸ�����鿴����Ҫ���������̡�",(String)map.get("nextPerson"));
			}
		}
		return true;
	}
	
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean sendOneSystemMessage(String title,String content,String jobId) throws Exception{
		Message msg = new Message();
		msg.setTitle(title);
		msg.setKind(1);
		msg.setSendPerson("0000");
		msg.setSendDate(new Date());
		msg.setContent(content);
		msg.setAcceptPerson(jobId);
//		System.out.println(msg);
//		System.out.println(title+"-"+content+"-"+jobId);
//		System.out.println(messageDao);
//		System.out.println(procedureDao);
		return messageDao.sendOneMsgToPerson(msg, true);
	}
	
	/**
	 * ��ȡ�ڼ�ҳ��  ��Ҫ��������̣����԰�
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getAllNeedToDealListByPage(String jobId,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_DEAL;
		int end = PAGE_DEAL;
		return procedureDao.getNeedToDealSimpleFromNumToNum(begin, end, jobId);
	}
	
	/**
	 * ��ȡ��Ҫ��������̵���ҳ��
	 * @return
	 */
	public int getAllNeedToDealListPage(String jobId) {
		int number = procedureDao.getAllNeedToDealNumber(jobId);
		return (int)Math.ceil(1.0*number/PAGE_DEAL);
	}
	
	/**
	 * ��ȡ�ҵ�һ���ύ��ȫ����Ϣ
	 * @param submitId
	 * @return
	 */
	public ProcedureSubmit getMySubmitAllInfoById(int submitId) {
		return procedureDao.getMySubmitAllInfoById(submitId);
	}
	
	/**
	 * ��ȡ�ڼ�ҳ�����ύ�����̣����԰�
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getAllMyProcedureSimpleByPage(int page,String jobId){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_MY;
		int end = PAGE_MY;
		return procedureDao.getAllMyProcedureSimpleFromNumToNum(begin, end, jobId);
	}
	
	/**
	 * ��ȡ���ύ�����̵���ҳ��
	 * @return
	 */
	public int getAllMyProcedurePage(String jobId){
		int number = procedureDao.getAllMyProcedureNumber(jobId);
		return (int)Math.ceil(1.0*number/PAGE_MY);
	}
	
	/**
	 * �����ύ
	 * @param psubmit
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean procedureSubmit(ProcedureSubmit psubmit){
		try{
			Map<String,Object> map = procedureDao.procedureSubmit(psubmit);
			if(map==null){
				return false;
			}else{
				//����������
				sendOneSystemMessage("��ϵͳ������һ����Ҫ����������","��ϸ�����鿴����Ҫ���������̡������̺ţ���"+(int)map.get("key")+"����",(String)map.get("needToRemind"));
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
//			System.out.println("��Ҫ�ع���");
			throw new RuntimeException();
		}
	}
	
	/**
	 * ��ȡ���̵Ĵ�����
	 * @param id
	 * @return
	 */
	public String getUserOfProcedureWhoCreateById(int id){
		return procedureDao.getUserOfProcedureWhoCreateById(id);
	}
	
	/**
	 * ����ID��ɾ��һ������
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public boolean delProcedureById(int id) throws Exception{
		return procedureDao.delProcedureById(id);
	}
	
	/**
	 * ����ģ��
	 * @param procedure
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public boolean updateProcedure(ModelProcedure procedure) throws Exception{
		return procedureDao.updateProcedure(procedure);
	}
	
	/**
	 * ��ȡĳһ��������ϸ
	 * @param modelId
	 * @return
	 */
	public ModelProcedure getModelInfoAllById(int modelId){
		return procedureDao.getModelInfoAllById(modelId);
	}
	
	/**
	 * ��ȡ�ڼ�ҳ��ģ��
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getAllModelByPage(int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_MODEL;
		int end = PAGE_MODEL;
		return procedureDao.getAllModelFromNumToNum(begin, end);
	}
	
	/**
	 * ��ȡ��ҳ��
	 * @return
	 */
	public int getAllModelPage(){
		int number = procedureDao.getAllModelNumber();
		return (int)Math.ceil(1.0*number/PAGE_MODEL);
	}

	/**
	 * �����µ�����
	 * @param procedure
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean createNewProcedure(ModelProcedure procedure) throws Exception {
		return procedureDao.createNewProcedure(procedure);
	}


}
