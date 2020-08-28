package com.cao.oa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cao.oa.bean.UserKind;
import com.cao.oa.dao.UserKindDao;

@Transactional(readOnly = true)
@Service
public class UserKindService {
	@Autowired
	private UserKindDao userKindDao;
	
	public String getNameById(int id){
		return userKindDao.getUserKindName(id);
	}
	
	/**
	 * 返回所有用户类型
	 * @return
	 */
	public List<UserKind> getAllUserKind(){
		return userKindDao.getAllUserKind();
	}
}
