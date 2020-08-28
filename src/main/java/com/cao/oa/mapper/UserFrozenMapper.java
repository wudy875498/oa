package com.cao.oa.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserFrozenMapper {
	//��ӵ������б�
	public int changeUserStatusToFrozenByJobId(@Param("jobId") String jobId,
      @Param("times") long times);
	//�ⶳ���Ƴ������б�
	public int delUserFromFrozenByJobId(@Param("jobId") String jobId);
	//��ȡ��Ҫ�ⶳ������
	public int getNeedOutOfFrozenNumber(@Param("nowTime") long nowTime);
	//��ȡ��Ҫ�ⶳ���˵�jobId
	public String getNeedOutOfFrozenJobId();
}
