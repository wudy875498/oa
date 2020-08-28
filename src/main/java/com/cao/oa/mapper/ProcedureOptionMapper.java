package com.cao.oa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cao.oa.bean.ProcedureOption;

public interface ProcedureOptionMapper {
	//����µ���ѡ��
	public int addNewOption(ProcedureOption procedureOption);
	//ͨ���ύ����ȫ���ύ����ѡ��
	public List<ProcedureOption> findBySubmitId(@Param("submitId") int submitId);
}
