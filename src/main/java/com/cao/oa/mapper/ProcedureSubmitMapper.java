package com.cao.oa.mapper;

import com.cao.oa.bean.ProcedureSubmit;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ProcedureSubmitMapper {
	//����µ��ύ
	public int addNewSubmit(ProcedureSubmit procedureSubmit);
	//��ȡĳ���ύ�����̵�����
	public int getNumberOfSubmitByCreatePerson(@Param("createPerson") String jobId);
	//��ȡ�ӵڼ����ڼ������ύ�����̣����԰�
	public List<Map<String, Object>> getAllMyProcedureSimpleFromNumToNum(
      @Param("createPerson") String jobId, @Param("begin") int begin, @Param("end") int end);
	
	//��ȡĳ���ύ��ȫ��
	public ProcedureSubmit findById(@Param("id") int id);
	//��ȡ�ύ����Ҫ��Ϣ
	public Map<String, Object> getMainInfoById(@Param("id") int id);
	//��ȡ���̵��ύ��
	public String getCreatePerson(@Param("id") int id);
	//�����ύ������״̬
	public int updateStatus(@Param("id") int id, @Param("status") int status);
	//��ȡĳ������Ҫ���ѵ�����ID��
	public int getNumberByUserJobIdAndWork(@Param("userJobId") String jobId, @Param("work") int work);
	
	//ɾ��һ���ύ������
	public int delOneSubmit(@Param("id") int id);
	
}
