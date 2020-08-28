package com.cao.oa.service;

import com.cao.oa.bean.UserKind;
import com.cao.oa.dao.UserKindDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserKindService {
	@Autowired
	private UserKindDao userKindDao;
	
	public String getNameById(int id){
		return userKindDao.getUserKindName(id);
	}
	
	/**
	 * ���������û�����
	 * @return
	 */
	public List<UserKind> getAllUserKind(){
		return userKindDao.getAllUserKind();
	}
	
}
