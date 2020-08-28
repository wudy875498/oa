package com.cao.oa.mapper;

import com.cao.oa.bean.ModelProcedure;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ModelProcedureMapper {

	//�����µ�����
	public int addNewProcedure(ModelProcedure modelProcedure);
	//��ȡ����ģ�������
	public int getAllModelNumber();
	//��ȡģ�������Ϣ���ӵڼ����ڼ���ģ�壬��ʱ������
	public List<Map<String,Object>> getAllModelFromNumToNum(@Param("begin") int begin,
      @Param("end") int end);
	
	//��ȡһ��ģ���ȫ��
	public ModelProcedure findById(@Param("id") int id);
	
	//����һ��ģ��
	public int updateModel(ModelProcedure modelProcedure);
	//ɾ��һ��ģ��
	public int delById(@Param("id") int id);
	//��ȡ���̵Ĵ�����
	public String getUserOfProcedureWhoCreateById(@Param("id") int id);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
