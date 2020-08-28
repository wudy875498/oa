package com.cao.oa.mapper;

import com.cao.oa.bean.FileDepot;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileDepotMapper {
	public FileDepot findById(int id);
	
	//��ȡ��˾�ļ�����ҳ��
	public int fileOfCompanyNumber(@Param("source") int source);
	//��ȡ�����ļ�����
	public int fileOfPartNumber(@Param("source") int source, @Param("part") int part);
	//��ȡȫ���Ĳ����ļ�����
	public int fileOfAllPartNumber(@Param("source") int source);
	//��ȡС���ļ�������ĳ��С��
	public int fileOfGroupNumber(@Param("source") int source, @Param("part") int part,
      @Param("group") int group);
	//��ȡС���ļ�������ȫ��С��
	public int fileOfAllGroupNumber(@Param("source") int source, @Param("part") int part);
	//��ȡС���ļ�������ȫ������
	public int fileOfAllPartAndGroupNumber(@Param("source") int source);
	//��ȡ�ӵڼ����ڼ����Ĺ�˾�ļ��б�
	public List<FileDepot> fileFromNumToNumOfCompany(@Param("source") int source,
      @Param("begin") int begin, @Param("end") int end);
	//��ȡ�����ļ��б��ӵڼ����ڼ���
	public List<FileDepot> fileFromNumToNumOfPart(@Param("source") int source,
      @Param("part") int part, @Param("begin") int begin, @Param("end") int end);
	//��ȡȫ���Ĳ����ļ��б��ӵڼ����ڼ���
	public List<FileDepot> fileListFromNumToNumOfAllPart(@Param("source") int source,
      @Param("begin") int begin, @Param("end") int end);
	//��ȡС���ļ��б�ĳ��С�飬�ӵڼ����ڼ���
	public List<FileDepot> fileListFromNumToNumOfGroup(@Param("source") int source,
      @Param("part") int part, @Param("group") int group, @Param("begin") int begin,
      @Param("end") int end);
	// ��ȡС���ļ��б�ȫ��С�飬�ӵڼ����ڼ���
	public List<FileDepot> fileListFromNumToNumOfAllGroup(@Param("source") int source,
      @Param("part") int part, @Param("begin") int begin, @Param("end") int end);
	//��ȡС���ļ��б�ȫ�����ţ��ӵڼ����ڼ���
	public List<FileDepot> fileListFromNumToNumOfAllPartAndGroup(@Param("source") int source,
      @Param("begin") int begin, @Param("end") int end);
	//�����ϴ����ļ������ݿ⣨�ɷ���������
	public int addNewFile(FileDepot fileDepot);
	//ɾ��һ���ļ�
	public int delFileById(int id);
	
}
