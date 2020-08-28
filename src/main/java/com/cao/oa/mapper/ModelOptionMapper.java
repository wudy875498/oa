package com.cao.oa.mapper;

import com.cao.oa.bean.ModelOption;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModelOptionMapper {
	
	//���һ���µ���ѡ��
	public int addNewOption(ModelOption opt);
	//��ȡһ��ģ���ȫ����ѡ��
	public List<ModelOption> getOptionsByProcedureId(@Param("modelId") int modelId);
	//��ȡһ��ģ�����ѡ����
	public int getNumberOfOneModel(@Param("modelId") int modelId);
	//ɾ��һ��ģ���ȫ����ѡ��
	public int delAllOptionsByModelId(@Param("modelId") int modelId);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
