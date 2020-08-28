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
	 * 返回所有用户类型
	 * @return
	 */
	public List<MessageKind> getAllUserKind(){
		List<MessageKind> result = null;
		result = mapper.getAllKinds();
		return result;
	}
}
