package com.cao.oa.dao;

import java.util.List;

import javax.annotation.Resource;
import com.cao.oa.bean.MessageKind;
import com.cao.oa.mapper.MessageKindMapper;
import org.springframework.stereotype.Component;

@Component
public class MsgKindDao {
	
	@Resource
	private MessageKindMapper mapper;
	
	/**
	 * ���������û�����
	 * @return
	 */
	public List<MessageKind> getAllUserKind(){
		List<MessageKind> result = null;
		result = mapper.getAllKinds();
		return result;
	}
}
