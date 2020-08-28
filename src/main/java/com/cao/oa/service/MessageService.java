package com.cao.oa.service;

import com.cao.oa.bean.Message;
import com.cao.oa.dao.MessageDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MessageService {
	@Autowired
	private MessageDao messageDao;
	public static final int PAGE_PERSON_MSG = 10;
	public static final int PAGE_NOTICE = 5;

	/**
	 * ��ȡĳһ����Ϣ����ϸ
	 * @param messageId
	 * @return
	 */
	public Message getMessageInfoByMessageId(String messageId){
		return messageDao.getMessageInfoByMessageId(messageId);
	}
	
	/**
	 * ��ȡĳ�˵Ĺ�˾�����б��ڼ�ҳ
	 * @param page
	 * @return
	 */
	public List<Message> getNoticeInfoOfCompanyToPageByJobId(int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_NOTICE;
		int end = PAGE_NOTICE;
		return messageDao.getNoticeOfCompanyFromNumToNum(begin, end);
	}
	
	/**
	 * ��ȡĳ�˵Ĳ��Ź����б��ڼ�ҳ
	 * @param jobId
	 * @param page
	 * @return
	 */
	public List<Message> getNoticeInfoOfPartToPageByJobId(String jobId,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_NOTICE;
		int end = PAGE_NOTICE;
		return messageDao.getNoticeOfPartFromNumToNum(jobId,begin, end);
	}
	
	/**
	 * ��ȡĳ�˵�С�鹫���б��ڼ�ҳ
	 * @param jobId
	 * @param page
	 * @return
	 */
	public List<Message> getNoticeInfoOfGroupToPageByJobId(String jobId,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_NOTICE;
		int end = PAGE_NOTICE;
		return messageDao.getNoticeOfGroupFromNumToNum(jobId,begin, end);
	}
	
	/**
	 * ��ȡĳ�˵Ĺ�˾����ҳ��
	 * @return
	 */
	public int getNoticeOfCompanyPageNumberByJobId(){
		int number = messageDao.getNoticeOfCompanyNumberByJobId();
		return (int)Math.ceil(1.0*number/PAGE_NOTICE);
	}
	
	/**
	 * ��ȡĳ�˵Ĳ��Ź���ҳ��
	 * @param jobId
	 * @return
	 */
	public int getNoticeOfPartPageNumberByJobId(String jobId){
		int number = messageDao.getNoticeOfPartNumberByJobId(jobId);
		return (int)Math.ceil(1.0*number/PAGE_NOTICE);
	}
	
	/**
	 * ��ȡĳ�˵�С�鹫��ҳ��
	 * @param jobId
	 * @return
	 */
	public int getNoticeOfGroupPageNumberByJobId(String jobId){
		int number = messageDao.getNoticeOfGroupNumberByJobId(jobId);
		return (int)Math.ceil(1.0*number/PAGE_NOTICE);
	}
	
	/**
	 * ��ȡĳ��ȫ����Ϣ�ĵڼ�ҳ����Ϣ����ȫ��
	 * @param jobId
	 * @param page
	 * @return
	 */
	public List<Message> getAllMessageInfoOfPageByJobId(String jobId,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_PERSON_MSG;
		int end = PAGE_PERSON_MSG;
		return messageDao.getAllMessageInfoByJobId(jobId,begin, end);
	}
	
	/**
	 * ��ȡ��ҳ����ĳ�˵���Ϣ�б�
	 * @param jobId
	 * @return
	 */
	public int getAllMessagePageNumberByJobId(String jobId){
		int number = messageDao.getAllMessageByJobId(jobId);
		return (int)Math.ceil(1.0*number/PAGE_PERSON_MSG);
	}
	
	/**
	 * �������˷���
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean sendOneMsgToAll(Message msg, boolean isMsg) throws Exception{
		return messageDao.sendOneMsgToAll(msg,isMsg);
	}
	
	/**
	 * ���ŷ�
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean sendOneMsgToPart(Message msg, boolean isMsg) throws Exception{
			return messageDao.sendOneMsgToPart(msg,isMsg);
	}
	
	/**
	 * ��ĳ�˷�
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean sendOneMsgToPerson(Message msg, boolean isMsg) throws Exception{
		return messageDao.sendOneMsgToPerson(msg,isMsg);

	}
	
	/**
	 * ��С�鷢
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public boolean sendOneMsgToGroup(Message msg, boolean isMsg) throws Exception{
		return messageDao.sendOneMsgToGroup(msg,isMsg);
	}
}
