package com.cao.oa.dao;

import com.cao.oa.bean.Part;
import com.cao.oa.mapper.GroupMapper;
import com.cao.oa.mapper.PartMapper;
import com.cao.oa.mapper.UserInfoMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PartDao {
	
	@Resource
	private PartMapper pMapper;
	@Resource
	private GroupMapper gMapper;
	@Resource
	private UserInfoMapper uMapper;

	
	/**
	 * ͨ�����ֻ�ȡ����ID
	 * @param name
	 * @return
	 */
	public int getPartByName(String name){
		int result = -1;
		try{
			result = pMapper.getPartIdByName(name);
		}catch (Exception e) {
			return -1;
		}
		return result;
	}
	
	/**
	 * ��Ӳ���
	 * @param name
	 * @param person
	 * @return
	 * @throws Exception 
	 */
	public boolean addPart(String name,String person) throws Exception{
		boolean result = false;
		//��Ӳ���
		Part newPart = new Part();
		newPart.setName(name);
		newPart.setCreateDate(new Date());
		newPart.setCreatePerson(person);
		int newId = pMapper.addNewPart(newPart);
		if(newId!=0){
			//���Ĭ��С��
			newId = newPart.getId();
			if(gMapper.addGroup(newId, "����С��", "ϵͳ", new Date())!=0){
				result = true;
			}
		}
		if(!result){
			throw new Exception();
		}
		return result;
	}
	
	/**
	 * ɾ������
	 * @param partId
	 * @return
	 * @throws Exception 
	 */
	public boolean delPart(int partId) throws Exception{
		boolean result = false;
		//��ȡ������
		int all = gMapper.allGroupsCount(partId);
		if(gMapper.delAllGroupsByPartId(partId)==all){
			if(pMapper.delPartById(partId)!=0){
				result = true;
			}
		}
		if(!result){
			throw new Exception();
		}
		return result;
	}
	
	/**
	 * ��ȡĳ�����ŵ�����
	 * @param partId
	 * @return
	 */
	public int getMemberOfPartNumbers(int partId){
		int result = 0;
		result = uMapper.getMemberNumbersOfPart(partId);
		return result;
	}
	
	/**
	 * ��ȡĳ�����ŵ�С����
	 * @param partId
	 * @return
	 */
	public int getGroupOfPartNumbers(int partId){
		int result = 0;
		result = gMapper.allGroupsCount(partId);
		return result;
	}
	
	/**
	 * ���شӼ���������Ϣ
	 * @param begin
	 * @param end
	 * @return ����null��û���ҵ�
	 */
	public List<Part> getPartsAllInfoByNumber(int begin,int end){
		List<Part> partList = null;
		if(begin>end){
			int temp = begin;
			begin = end;
			end = temp;
		}
		partList =  pMapper.getPartsAllInfoByNumber(begin, end);
		if(partList==null || partList.size()==0){
			partList = null;
		}
		return partList;
	}
	
	/**
	 * ��ȡ���ŵĸ���
	 * @return 
	 */
	
	public int allPartsCount(){
		int result = 0;
		result = pMapper.allPartsCount();
		return result;
	}
	
	/**
	 * ��ȡ���еĲ���id������
	 * @return
	 */
	public List<Map<String, Object>>  getAllPartsAndNames(){
		List<Map<String, Object>>  result = null;
		result = pMapper.getAllPartsAndNames();
		return result;
	}
	
	/**
	 * ����id��ȡ����
	 * @param id
	 * @return �գ���ʧ�ܡ�
	 */
	public String getNameById(int id){
		String result = null;
		result = pMapper.getNameById(id);
		return result;
	}
	
}
