package com.cao.oa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cao.oa.bean.MessageKind;
import com.cao.oa.dao.MsgKindDao;

@Transactional(readOnly = true)
@Service
public class MsgKindService {
	@Autowired
	private MsgKindDao msgKindDao;

	/**
	 * 返回所有用户类型
	 * @return
	 */
	public List<MessageKind> getAllUserKind(){
		return msgKindDao.getAllUserKind();
	}
}
