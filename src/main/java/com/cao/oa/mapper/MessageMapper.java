package com.cao.oa.mapper;

import com.cao.oa.bean.Message;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessageMapper {
	//��ȡĳһ����Ϣ����ϸ
	public Message findById(@Param("id") String id);
	//��ȡ�����б��ӵڼ������ڼ���
	public List<Message> getMessageOfPartFromNumToNum(@Param("kind") int kind,
      @Param("partId") int partId,
      @Param("begin") int begin, @Param("end") int end);
	//��ȡ�����б��ӵڼ������ڼ���
	public List<Message> getMessageOfCompanyFromNumToNum(@Param("kind") int kind,
      @Param("begin") int begin, @Param("end") int end);
	
	//��ȡС�鹫���б��ӵڼ������ڼ���
	public List<Message> getMessageOfGroupFromNumToNum(@Param("kind") int kind,
      @Param("part") int part, @Param("group") int group,
      @Param("begin") int begin, @Param("end") int end);
	//��ȡĳ�˵Ĳ��Ź���ҳ��
	public int getNoticeOfPartNumberByJobId(@Param("part") int part);
	//��ȡĳ�˵Ĺ�˾����ҳ��
	public int getNoticeOfCompanyNumberByJobId();
	//��ȡĳ�˵�С�鹫��ҳ��
	public int getNoticeOfGroupNumberByJobId(@Param("part") int part, @Param("group") int group);
	//��ȡĳ��ȫ����Ϣ��  �ӵڼ������ڼ���  ����Ϣ����ȫ��
	public List<Message> getAllMessageInfoByJobId(@Param("jobId") String jobId,
      @Param("part") int part, @Param("group") int group,
      @Param("begin") int begin, @Param("end") int end);
	//��ȡĳ�˵�ȫ����Ϣ����
	public int getAllMessageByJobId(@Param("jobId") String jobId, @Param("part") int part,
      @Param("group") int group);
	//�������Ϣ
	public int addNewMessageToAllPerson(Message message);
	//�������Ϣ
	public int addNewMessageToPart(Message message);
	//�������Ϣ
	public int addNewMessageToPerson(Message message);
	//�������Ϣ
	public int addNewMessageToGroup(Message message);
	//�������Ϣ
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
