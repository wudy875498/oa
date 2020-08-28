package com.cao.oa.service;

import com.cao.oa.bean.FileDepot;
import com.cao.oa.dao.FileDepotDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FileDepotService {
	public static final int PAGE_COMPANY = 5;
	public static final int PAGE_PART = 5;
	public static final int PAGE_GROUP = 5;
	@Autowired
	private FileDepotDao fileDepotDao;

	/**
	 * ��ȡ�ڼ�ҳ��˾�ļ��б�
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfCompany(int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfCompany(begin, end);
	}
	
	/**
	 * ��ȡ��˾�ļ���ҳ��
	 * @return
	 */
	public int getFileListOfCompanyPageNumber(){
		int number = fileDepotDao.getFileOfCompanyNumber();
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * ��ȡ�����ļ��б�
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfPart(int part,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfPart(part,begin, end);
	}
	
	/**
	 * ��ȡ�����ļ���ҳ��
	 * @return
	 */
	public int getFileListOfPartPageNumber(int part){
		int number = fileDepotDao.getFileOfPartNumber(part);
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * ��ȡȫ���Ĳ����ļ��б�
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfAllPart(int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfAllPart(begin, end);
	}
	
	/**
	 * ��ȡȫ���Ĳ����ļ���ҳ��
	 * @return
	 */
	public int getFileListOfAllPartPageNumber(){
		int number = fileDepotDao.getFileOfAllPartNumber();
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * ��ȡС���ļ��б�ĳ��С��
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfGroup(int part,int group,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfGroup(part,group,begin, end);
	}
	
	/**
	 * ��ȡС���ļ���ҳ����ĳ��С��
	 * @return
	 */
	public int getFileListGroupPageNumber(int part,int group){
		int number = fileDepotDao.getFileOfGroupNumber(part,group);
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * ��ȡС���ļ��б�ȫ��С��
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfAllGroup(int part,int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfAllGroup(part,begin, end);
	}
	
	/**
	 * ��ȡС���ļ���ҳ����ȫ��С��
	 * @return
	 */
	public int getFileListOfAllGroupPageNumber(int part){
		int number = fileDepotDao.getFileOfAllGroupNumber(part);
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * ��ȡС���ļ��б�ȫ������
	 * @param page
	 * @return
	 */
	public List<FileDepot> getFileListByPageOfAllPartAndGroup(int page){
		if(page<1){
			page = 1;
		}
		int begin = (page-1)*PAGE_COMPANY;
		int end = PAGE_COMPANY;
		return fileDepotDao.getFileListFromNumToNumOfAllPartAndGroup(begin, end);
	}
	
	/**
	 * ��ȡС���ļ���ҳ����ȫ������
	 * @return
	 */
	public int getFileListOfAllPartAndGroupPageNumber(){
		int number = fileDepotDao.getFileOfAllPartAndGroupNumber();
		return (int)Math.ceil(1.0*number/PAGE_COMPANY);
	}
	
	/**
	 * �����ϴ����ļ�
	 * @param fd
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public boolean addNewFile(FileDepot fd) throws Exception{
		return fileDepotDao.addNewFile(fd);
	}
	
	/**
	 * ��ȡһ���ļ�����ϸ��Ϣ
	 * @param fileId
	 * @return
	 */
	public FileDepot getOneFileInfoById(int fileId) {
		return fileDepotDao.getOneFileInfoById(fileId);
	}
	
	/**
	 * ɾ��һ���ļ�
	 * @param fileId
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public boolean delFileById(int fileId) throws Exception{
		return fileDepotDao.delFileById(fileId);
	}
	
}
