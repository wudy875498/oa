package com.cao.oa.mapper;

import com.cao.oa.bean.Part;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface PartMapper {
	//ͨ�����ֻ�ȡ����ID
	public int getPartIdByName(@Param("name") String name);
	//����²��ţ�����ȡ�²���id
	public int addNewPart(Part part);
	//ɾ�����ţ����ݲ���id
	public int delPartById(@Param("id") int partId);
	//���ŵ�ȫ����Ϣ�����شӼ���������Ϣ
	public List<Part> getPartsAllInfoByNumber(@Param("begin") int begin, @Param("end") int end);
	//��ȡ���ŵĸ���
	public int allPartsCount();
	//��ȡ���еĲ���id������
	public List<Map<String, Object>>  getAllPartsAndNames();
	//����id��ȡ����
	public String getNameById(@Param("id") int id);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
