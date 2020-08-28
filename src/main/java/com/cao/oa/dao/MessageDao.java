package com.cao.oa.dao;

import com.cao.oa.bean.Message;
import com.cao.oa.mapper.MessageMapper;
import com.cao.oa.mapper.RemindMapper;
import com.cao.oa.mapper.UserInfoMapper;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class MessageDao {

	@Resource
	private MessageMapper mMapper;
	@Resource
	private RemindMapper rMapper;
	@Resource
	private UserInfoMapper uMapper;
	
	/**
	 * ��ȡĳһ����Ϣ����ϸ
	 * @param messageId
	 * @return
	 */
	public Message getMessageInfoByMessageId(String messageId){
		Message result = null;
		result = mMapper.findById(messageId);
		return result;
	}
	
	/**
	 * ��ȡĳ�˵Ĳ��Ź����б��ӵڼ������ڼ���
	 * @param jobId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Message> getNoticeOfPartFromNumToNum(String jobId,int begin,int end){
		List<Message> result = null;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		result = mMapper.getMessageOfPartFromNumToNum(4, part, begin, end);
		return result;
	}
	
	/**
	 * ��ȡĳ�˵Ĺ�˾�����б��ӵڼ������ڼ���
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Message> getNoticeOfCompanyFromNumToNum(int begin,int end){
		List<Message> result = null;
		result = mMapper.getMessageOfCompanyFromNumToNum(2, begin, end);
		return result;
	}
	
	/**
	 * ��ȡĳ�˵�С�鹫���б��ӵڼ������ڼ���
	 * @param jobId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Message> getNoticeOfGroupFromNumToNum(String jobId,int begin,int end){
		List<Message> result = null;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		//��ȡС��
		int group= uMapper.getUserGroupByJobId(jobId);
		result = mMapper.getMessageOfGroupFromNumToNum(7, part, group, begin, end);
		return result;
		
	}
	
	/**
	 * ��ȡĳ�˵Ĳ��Ź���ҳ��
	 * @param jobId
	 */
	public int getNoticeOfPartNumberByJobId(String jobId){
		int result = 0;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		result = mMapper.getNoticeOfPartNumberByJobId(part);
		return result;
	}
	
	/**
	 * ��ȡĳ�˵Ĺ�˾����ҳ��
	 * @param jobId
	 * @return
	 */
	public int getNoticeOfCompanyNumberByJobId(){
		int result = 0;
		result = mMapper.getNoticeOfCompanyNumberByJobId();
		return result;
	}
	
	/**
	 * ��ȡĳ�˵�С�鹫��ҳ��
	 * @param jobId
	 */
	public int getNoticeOfGroupNumberByJobId(String jobId){
		int result = 0;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		//��ȡС��
		int group= uMapper.getUserGroupByJobId(jobId);
		result = mMapper.getNoticeOfGroupNumberByJobId(part, group);
		return result;
	}
	
	/**
	 * ��ȡĳ��ȫ����Ϣ��  �ӵڼ������ڼ���  ����Ϣ����ȫ��
	 * @param jobId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Message> getAllMessageInfoByJobId(String jobId, int begin, int end) {
		List<Message> result = null;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		//��ȡС��
		int group= uMapper.getUserGroupByJobId(jobId);
		result = mMapper.getAllMessageInfoByJobId(jobId, part, group, begin, end);
		return result;
	}
	
	/**
	 * ��ȡĳ�˵�ȫ����Ϣ����
	 * @param jobId
	 * @return
	 */
	public int getAllMessageByJobId(String jobId){
		int result = -1;
		//��ȡ����
		int part = uMapper.getUserPartByJobId(jobId);
		//��ȡС��
		int group= uMapper.getUserGroupByJobId(jobId);
		result = mMapper.getAllMessageByJobId(jobId, part, group);
		return result;
	}
	
	/**
	 * �������˷���
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public boolean sendOneMsgToAll(Message msg, boolean isMsg) throws Exception{
		boolean result = false;
		int num = 0;
		msg.setSendDate(new Date());
		num = mMapper.addNewMessageToAllPerson(msg);
		if(num!=0){
			num = msg.getId();
			result = addRemindToAll(num, isMsg);
			if(!result){
				throw new Exception();
			}
		}
		return result;
		
	}
	
	/**
	 * ���ŷ�
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public boolean sendOneMsgToPart(Message msg,boolean isMsg) throws Exception{
		boolean result = false;
		int num = 0;
		msg.setSendDate(new Date());
		num = mMapper.addNewMessageToPart(msg);
		if(num!=0){
			num = msg.getId();
			result = addRemindToPart(num, isMsg, msg.getAcceptPart());
			if(!result){
				throw new Exception();
			}
		}
		return result;
		
	}
	
	/**
	 * ��ĳ�˷�
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public boolean sendOneMsgToPerson(Message msg,boolean isMsg) throws Exception{
		boolean result = false;
		int num = 0;
		msg.setSendDate(new Date());
		num = mMapper.addNewMessageToPerson(msg);
		if(num!=0){
			num=msg.getId();
			result = addRemindToPerson(num, msg.getAcceptPerson(), isMsg);
			if(!result){
				throw new Exception();
			}
		}
		return result;
	}
	
	/**
	 * ��С�鷢
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public boolean sendOneMsgToGroup(Message msg,boolean isMsg) throws Exception{
		boolean result = false;
		int num = 0;
		msg.setSendDate(new Date());
		num = mMapper.addNewMessageToGroup(msg);
		if(num!=0){
			num = msg.getId();
			result = addRemindToGroup(num, isMsg, msg.getAcceptGroup(), msg.getAcceptPart());
			if(!result){
				throw new Exception();
			}
		}
		return result;
	}

	/**
	 * ���������������
	 * @param msgId
	 * @param isMsg
	 * @return
	 */
	public boolean addRemindToAll(int msgId,boolean isMsg){
		List<String> list = uMapper.getAllUserJobId();
		if(list==null || list.size()==0){
			return false;
		}else{
			for(String s:list){
				if(!addRemindToPerson(msgId, s, isMsg)){
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * �����������
	 * @param msgId
	 * @param isMsg
	 * @param remindPart
	 * @return
	 */
	public boolean addRemindToPart(int msgId,boolean isMsg,int remindPart){
		List<String> list = uMapper.getAllUserJobIdOfPart(remindPart);
		if(list==null || list.size()==0){
			return false;
		}else{
			for(String s:list){
				if(!addRemindToPerson(msgId, s, isMsg)){
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * ��С���������
	 * @param msgId
	 * @return
	 * @throws Exception 
	 */
	public boolean addRemindToGroup(int msgId,boolean isMsg,int remindGroup,int remindPart){
		List<String> list = uMapper.getAllUserJobIdOfGroup(remindPart, remindGroup);
		if(list==null || list.size()==0){
			return false;
		}else{
			for(String s:list){
				if(!addRemindToPerson(msgId, s, isMsg)){
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * ������������
	 * @param msgId
	 * @param remindId
	 * @param isMsg
	 * @return
	 * @throws Exception 
	 */
	public boolean addRemindToPerson(int msgId,String remindId,boolean isMsg) {
		int num = 0;
		if(isMsg){
//			System.out.println("msgId:"+msgId+",remindId:"+remindId);
			num = rMapper.addRemind(msgId, remindId, 1);
		}else{
			num = rMapper.addRemind(msgId, remindId, 0);
		}
		if(num==1){
			return true;
		}else{
			return false;
		}
	}
	
}
