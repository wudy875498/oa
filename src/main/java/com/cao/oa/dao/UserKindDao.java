package com.cao.oa.dao;

import com.cao.oa.bean.UserKind;
import com.cao.oa.mapper.UserKindMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserKindDao {
	
	@Resource
	private UserKindMapper mapper;
	
	/**
	 * ���������û�����
	 * @return
	 */
	public List<UserKind> getAllUserKind(){
		List<UserKind> result = null;
			result = mapper.getAllUserKind();
			return result;
	}
	
	/**
	 * ����ID��ȡ����
	 * @param id
	 * @return
	 */
	public String getUserKindName(int id){
		String result = null;
		result = mapper.getNameById(id);
		return result;
	}
}
