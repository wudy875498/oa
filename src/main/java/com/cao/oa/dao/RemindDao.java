package com.cao.oa.dao;

import com.cao.oa.mapper.RemindMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RemindDao {
	
	@Resource
	private RemindMapper mapper;
	
	/**
	 * ɾ��һ��ָ��������
	 * @param jobId
	 * @param msgId
	 * @param isMag
	 * @return
	 * @throws Exception 
	 */
	public boolean takeIdRead(String jobId, int msgId, boolean isMag) throws Exception{
		boolean result = false;
		int isMsgNum = 0;
		if(isMag){
			isMsgNum = 1;
		}
		if(mapper.hasThisRemind(msgId, jobId, isMsgNum)==1){
			//�У�ɾ��
			int num = mapper.delRemindById(msgId, jobId, isMsgNum);
			if(num==1){
				//�ɹ�ɾ��
				result = true;
			}else{
				//ɾ��ʧ��
				throw new Exception();
			}
		}else{
			//û�з���
			result = true;
		}
		return result;
	}
	
	/**
	 * �Ƿ��Ķ�
	 * @param jobId
	 * @param msgId
	 * @param isMag
	 * @return
	 */
	public boolean isRead(String jobId, int msgId, boolean isMag) {
		boolean result = true;
		int num = 0;
		if(isMag){
			num = mapper.hasThisRemind(msgId, jobId, 1);
		}else{
			num = mapper.hasThisRemind(msgId, jobId, 0);
		}
		if(num==0){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	/**
	 * ��ȡĳ������Ҫ���ѵ���ϢID��
	 * @param jobId
	 * @return
	 */
	public int getNeedRemindMessageNumberByJobId(String jobId){
		int result = 0;
		result = mapper.getNeedToRemindNumber(1, jobId);
		return result;
	}
	
	/**
	 * ��ȡĳ������Ҫ���ѵĹ�����
	 * @param jobId
	 * @return
	 */
	public int getNeedRemindNoticeByJobId(String jobId){
		int result = 0;
		result = mapper.getNeedToRemindNumber(0, jobId);
		return result;
	}
		
}
