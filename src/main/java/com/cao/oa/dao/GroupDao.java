package com.cao.oa.dao;

import com.cao.oa.bean.Group;
import com.cao.oa.mapper.GroupMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class GroupDao {
	@Resource
	private GroupMapper mapper;
	
	/**
	 * ��ȡĳ���ŵ�����С������ID
	 * @param partId
	 * @return
	 */
	public List<Map<String, Object>> getAllGroupsOfPartNameAndId(int partId){
		List<Map<String, Object>> result = null;
		result = mapper.getAllGroupsOfPartNameAndId(partId);
		return result;
		
	}
	
	/**
	 * ���ݲ���ID��С��������ȡС��ID
	 * @param partId
	 * @param name
	 * @return
	 */
	public int getGroupByName(int partId,String name){
		int result = -1;
		try{
			result = mapper.getGroupByName(partId,name);
			return result;
		}catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * ���С��
	 * @param partId
	 * @param name
	 * @param person
	 * @return
	 * @throws Exception 
	 */
	public boolean addGroup(int partId,String name,String person) throws Exception{
		boolean result = false;
		int num = mapper.addGroup(partId,name,person,new Date());
		if(num==1){
			result = true;
		}else{
			throw new Exception();
		}
		return result;
		
	}
	
	/**
	 * ɾ��С��
	 * @param partId
	 * @param groupId
	 * @return
	 * @throws Exception 
	 */
	public boolean delGroup(int partId,int groupId) throws Exception{
		boolean result = false;
		String groupName = mapper.getNameById(groupId, partId);
		int all = mapper.getMemberNumbersOfGroup(groupId, partId);
		if(all!=0){
			return false;
		}
		if(!groupName.equals("����С��")){
			if(mapper.delGroup(groupId, partId)!=0){
				result = true;
			}
		}else{
			return false;
		}
		if(!result){
			throw new Exception();
		}else{
			return true;
		}
		
	}
	
	/**
	 * ��ȡĳС�������
	 * @param partId
	 * @param groupId
	 * @return -1Ϊ����ʧ��
	 */
	public int getMemberNumbersOfGroup(int partId,int groupId){
		int result = -1;
		result = mapper.getMemberNumbersOfGroup(groupId, partId);
		return result;
		
	}
	
	/**
	 * ���شӼ���������Ϣ
	 * @param partId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Group> getGroupsOfPartAllInfoByNumber(int partId,int begin,int end){
		List<Group> result = null;
		if(begin>end){
			int temp = begin;
			begin = end;
			end = temp;
		}
		result = mapper.getGroupsOfPartAllInfoByNumber(partId, begin, end);
		return result;
	}
	
	/**
	 * ��ȡС��ĸ���
	 * @param partId
	 * @return
	 */
	public int allGroupsCount(int partId){
		int result = 0;
		result = mapper.allGroupsCount(partId);
		return result;
		
	}
	
	/**
	 * ����id��ȡ����
	 * @param id
	 * @param partId
	 * @return
	 */
	public String getNameById(int id,int partId){
		String result = null;
		result = mapper.getNameById(id, partId);
		return result;
		
	}
}
