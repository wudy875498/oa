package com.cao.oa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cao.oa.bean.UserKind;

public interface UserKindMapper {
	//���������û�����
	public List<UserKind> getAllUserKind();
	//����ID��ȡ����
	public String getNameById(@Param("id") int id);
}
