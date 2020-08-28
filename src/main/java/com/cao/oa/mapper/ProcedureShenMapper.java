package com.cao.oa.mapper;

import com.cao.oa.bean.ProcedureShen;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ProcedureShenMapper {
	//�����������
	public int addNewShen(ProcedureShen procedureShen);
	//ͨ���ύ����ȫ���ύ������
	public List<ProcedureShen> findBySubmitId(@Param("submitId") int submitId);
	//��ȡĳ������Ҫ���������
	public int getAllNeedToDealNumber(@Param("userJobId") String jobId);
	
	//�ҵ���Ҫ�������������id
	public List<Map<String, Object>> getNeedDealIdsByJobIdFromNumberToNumber
  (@Param("userJobId") String jobId, @Param("begin") int begin, @Param("end") int end);
	
	//��ȡ���µ�ʱ��
	public Timestamp getUpdateTime(@Param("procedureId") int submitId, @Param("order") int order);
	
	//Ѱ�Ҵ�����Ա
	public String getDealPersonByIds(@Param("id") int shenId, @Param("procedureId") int procedureId);
	//��ø����̵�����˳��
	public int getOrderByIds(@Param("id") int shenId, @Param("procedureId") int procedureId);
	//������������
	public int updateAllByIds(ProcedureShen procedureShen);
	//����ĳ�����̵�ĳ�����������
	public String getJobIdByOrderAndProcedureId(@Param("procedureId") int procedureId,
      @Param("order") int order);
	//��ѯĳ���ύ�������Ƿ��д˲���
	public int getHasThisOrderOfSubmit(@Param("procedureId") int procedureId,
      @Param("order") int order);
	//����ĳ���ύ�����̵�ĳ�����״̬
	public int updateWork(@Param("work") int work, @Param("procedureId") int procedureId,
      @Param("order") int order);
	
}
