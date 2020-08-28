package com.cao.oa.mapper;

import org.apache.ibatis.annotations.Param;

public interface RemindMapper {
	//�������
	public int addRemind(@Param("msgId") int msgId, @Param("remindId") String remindId,
      @Param("isMsg") int isMsg);
	//ɾ��һ������
	public int delRemindById(@Param("msgId") int msgId, @Param("remindId") String remindId,
      @Param("isMsg") int isMSg);
	//������������Ƿ���� 
	public int hasThisRemind(@Param("msgId") int msgId, @Param("remindId") String remindId,
      @Param("isMsg") int isMSg);
	//��ȡĳ������Ҫ���ѵ���Ϣ�򹫸�ID��
	public int getNeedToRemindNumber(@Param("isMsg") int isMSg, @Param("remindId") String remindId);
	
	
	
	
	
	
	
	
	
	
	
}
