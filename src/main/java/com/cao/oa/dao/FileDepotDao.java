package com.cao.oa.dao;

import com.cao.oa.bean.FileDepot;
import com.cao.oa.mapper.FileDepotMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileDepotDao {
	
	@Resource
	private FileDepotMapper mapper;

	
	/**
	 * ��ȡ��˾�ļ�����ҳ��
	 * @return
	 */
	public int getFileOfCompanyNumber() {
		int result = 0;
		result = mapper.fileOfCompanyNumber(FileDepot.SOURCE_COMPANY);
		return result;
	}
	/**
	 * ��ȡ�ӵڼ����ڼ����Ĺ�˾�ļ��б�
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfCompany(int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileFromNumToNumOfCompany(FileDepot.SOURCE_COMPANY, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
	}
	
	/**
	 * ��ȡ�����ļ�����
	 * @param part
	 * @return
	 */
	public int getFileOfPartNumber(int part) {
		int result = 0;
		result = mapper.fileOfPartNumber(FileDepot.SOURCE_PART,part);
		return result;
	}
	
	/**
	 * ��ȡ�����ļ��б��ӵڼ����ڼ���
	 * @param part
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfPart(int part, int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileFromNumToNumOfPart(FileDepot.SOURCE_PART,part, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
		
	}
	
	/**
	 * ��ȡȫ���Ĳ����ļ�����
	 * @return
	 */
	public int getFileOfAllPartNumber() {
		int result = 0;
		result = mapper.fileOfAllPartNumber(FileDepot.SOURCE_PART);
		return result;
	}
	
	/**
	 * ��ȡȫ���Ĳ����ļ��б��ӵڼ����ڼ���
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfAllPart(int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileListFromNumToNumOfAllPart(FileDepot.SOURCE_PART, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
	}
	
	/**
	 * ��ȡС���ļ�������ĳ��С��
	 * @param part
	 * @param group
	 * @return
	 */
	public int getFileOfGroupNumber(int part, int group) {
		int result = 0;
		result = mapper.fileOfGroupNumber(FileDepot.SOURCE_GROUP,part,group);
		return result;
	}
	
	/**
	 * ��ȡС���ļ��б�ĳ��С�飬�ӵڼ����ڼ���
	 * @param part
	 * @param group
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfGroup(int part, int group, int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileListFromNumToNumOfGroup(FileDepot.SOURCE_GROUP,part,group, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
	}
	
	/**
	 * ��ȡС���ļ�������ȫ��С��
	 * @param part
	 * @return
	 */
	public int getFileOfAllGroupNumber(int part) {
		int result = 0;
		result = mapper.fileOfAllGroupNumber(FileDepot.SOURCE_GROUP,part);
		return result;
	}
	
	/**
	 * ��ȡС���ļ��б�ȫ��С�飬�ӵڼ����ڼ���
	 * @param part
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfAllGroup(int part, int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileListFromNumToNumOfAllGroup(FileDepot.SOURCE_GROUP,part, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
	}
	
	/**
	 * ��ȡС���ļ�������ȫ������
	 * @return
	 */
	public int getFileOfAllPartAndGroupNumber() {
		int result = 0;
		result = mapper.fileOfAllPartAndGroupNumber(FileDepot.SOURCE_GROUP);
		return result;
		
	}
	
	/**
	 * ��ȡС���ļ��б�ȫ�����ţ��ӵڼ����ڼ���
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<FileDepot> getFileListFromNumToNumOfAllPartAndGroup(int begin, int end) {
		List<FileDepot> result = null;
		result = mapper.fileListFromNumToNumOfAllPartAndGroup(FileDepot.SOURCE_GROUP, begin, end);
		if(result==null || result.size()==0){
			result = null;
		}
		return result;
		
	}
	
	/**
	 * �����ϴ����ļ�
	 * @param fd
	 * @return
	 * @throws Exception 
	 */
	public boolean addNewFile(FileDepot fd) throws Exception {
		int num = mapper.addNewFile(fd);
		if(num==1){
			return true;
		}else{
			throw new Exception();
		}
		
	}
	
	/**
	 * ��ȡһ���ļ�����ϸ��Ϣ
	 * @param fileId
	 * @return
	 */
	public FileDepot getOneFileInfoById(int fileId) {
		FileDepot result = null;
		result = mapper.findById(fileId);
//		if(result==null){
//			System.out.println("null");
//		}else{
//			System.out.println(result);
//		}
		return result;
		
	}
	
	/**
	 * ɾ��һ���ļ�
	 * @param fileId
	 * @return
	 * @throws Exception 
	 */
	public boolean delFileById(int fileId) throws Exception {
		int num = mapper.delFileById(fileId);
		if(num==1){
			return true;
		}else{
			throw new Exception();
		}
	}
	
	
}
