package com.cao.oa.service;

import com.cao.oa.bean.Group;
import com.cao.oa.dao.GroupDao;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class GroupService {
	public static int PAGE_NUMBER = 5;
	@Autowired
	private GroupDao groupDao;

	/**
	 * ��ȡĳ���ŵ�����С������ID
	 * @param partId
	 * @return
	 */
	public List<Map<String, Object>> getAllGroupsOfPartNameAndId(int partId){
		return groupDao.getAllGroupsOfPartNameAndId(partId);
	}
	
	/**
	 * ���ݲ���ID��С��������ȡС��ID
	 * @param partId
	 * @param name
	 * @return
	 */
	public int getGroupByName(int partId,String name){
		return groupDao.getGroupByName(partId, name);
	}
	
	/**
	 * ɾ��С��
	 * @param partId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public boolean delGroup(int partId,int groupId) throws Exception{
		return groupDao.delGroup(partId,groupId);
	}
	
	/**
	 * ���С��
	 * @param partId
	 * @param name
	 * @param person
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public boolean addGroup(int partId,String name,String person) throws Exception{
		return groupDao.addGroup(partId,name, person);
	}
	
	/**
	 * ��ȡĳС�������
	 * @param partId
	 * @param groupId
	 * @return
	 */
	public int getMemberNumbersOfGroup(int partId,int groupId){
		return groupDao.getMemberNumbersOfGroup(partId, groupId);
	}
	
	/**
	 * ��ȡ�ڼ�ҳ������
	 * @param page
	 * @return
	 */
	public List<Group> getGroupsOfPartByPage(int partId,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_NUMBER;
		int end = PAGE_NUMBER;
		return groupDao.getGroupsOfPartAllInfoByNumber(partId,begin, end);
	}
	
	/**
	 * ��ȡ��ҳ��
	 * @return
	 */
	public int getAllPage(int partId){
		int number = groupDao.allGroupsCount(partId);
		return (int)Math.ceil(1.0*number/PAGE_NUMBER);
	}
	
	/**
	 * ����ID��ȡС������
	 * @param partId
	 * @param groupId
	 * @return
	 */
	public String getNameById(int partId,int groupId){
		return groupDao.getNameById(groupId, partId);
	}
	
	//-----------
}
