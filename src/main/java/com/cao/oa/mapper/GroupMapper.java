package com.cao.oa.mapper;

import com.cao.oa.bean.Group;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface GroupMapper {
	//��ȡĳ���ŵ�����С������ID
	public List<Map<String, Object>> getAllGroupsOfPartNameAndId(@Param("partId") int partId);
	//���ݲ���ID��С��������ȡС��ID
	public int getGroupByName(@Param("partId") int partId, @Param("name") String name);
	//���С��
	public int addGroup(@Param("partId") int partId, @Param("name") String name,
      @Param("person") String person, @Param("date") Date date);
	//����id��ȡ����
	public String getNameById(@Param("id") int id, @Param("partId") int partId);
	//ɾ��С��
	public int delGroup(@Param("id") int id, @Param("partId") int partId);
	//��ȡĳС�������
	public int getMemberNumbersOfGroup(@Param("groupId") int groupId, @Param("partId") int partId);
	//���شӼ���������Ϣ
	public List<Group> getGroupsOfPartAllInfoByNumber(@Param("id") int id, @Param("begin") int begin,
      @Param("end") int end);
	//��ȡС��ĸ���
	public int allGroupsCount(@Param("partId") int partId);
	//ɾ��С�飬���ݲ���id������
	public int delGroupByPartIdAndName(@Param("name") String name, @Param("partId") int partId);
	//ɾ��ĳ�����ŵ�ȫ��С��
	public int delAllGroupsByPartId(@Param("partId") int partId);

}
