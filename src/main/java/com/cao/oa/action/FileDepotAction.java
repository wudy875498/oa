package com.cao.oa.action;

import com.cao.oa.bean.FileDepot;
import com.cao.oa.bean.UserInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.cao.oa.service.FileDepotService;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.JumpPrompt;

@Controller
public class FileDepotAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private FileDepotService fileDepotService;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	
	private SimpleDateFormat sdf;
	
	public FileDepotAction(){
		super();
		sdf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
	}

	/**
	 * ת���ļ��ֿ�ҳ��
	 * @param page
	 * @param kind
	 * @param req
	 * @return
	 */
	@RequestMapping("file/fileHome.do")
	public ModelAndView viewExample(String page,String kind,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		int userPart = userServer.getUserPartByJobId(jobId);
		int userGroup= userServer.getUserGroupByJobId(jobId);
		String userPartName = partService.getNameById(userPart);
		String userGroupName= groupService.getNameById(userPart, userGroup);
		
		List<Map<String,String>> filesList = null;//һҳ5��
		String currentFile = "group";
		int allPage = 0;
		int currentPage = 0;
		int canUpload = 0;
		int canSelectPart = userPart;
		int canSelectGroup = userGroup;
		
		//��ǰ�ļ�����
		if(kind==null  || kind.length()==0){
			currentFile = "company";
			kind = "company";
		}else{
			currentFile = kind;
		}
		//��ǰҳ
		if(page==null  || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		
		//��ҳ��
		if(kind.equals("group")){
			//С��
			if(userKind== UserInfo.KIND_MANAGER_WEB){
				canUpload = 1;
				canSelectPart = -1;
				canSelectGroup= -1;
				allPage = fileDepotService.getFileListOfAllPartAndGroupPageNumber();
			}else if(userKind==UserInfo.KIND_MANAGER_PART){
				canUpload = 1;
				canSelectGroup= -1;
				allPage = fileDepotService.getFileListOfPartPageNumber(userPart);
			}else if(userKind==UserInfo.KIND_MANAGER_GROUP){
				canUpload = 1;
				allPage = fileDepotService.getFileListGroupPageNumber(userPart, userGroup);
			}else{
				allPage = fileDepotService.getFileListGroupPageNumber(userPart, userGroup);
			}
		}else if(kind.equals("part")){
			//����
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				canUpload = 1;
				canSelectPart = -1;
				allPage = fileDepotService.getFileListOfAllPartPageNumber();
			}else if(userKind==UserInfo.KIND_MANAGER_PART){
				canUpload = 1;
				allPage = fileDepotService.getFileListOfPartPageNumber(userPart);
			}else{
				allPage = fileDepotService.getFileListOfPartPageNumber(userPart);
			}
		}else{
			//��˾
			allPage = fileDepotService.getFileListOfCompanyPageNumber();
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				canUpload = 1;
			}
		}


		//��ֹҳ���Ƿ�
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		
		List<FileDepot> list = null;
		if(kind.equals("group")){
			//С��
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				list = fileDepotService.getFileListByPageOfAllPartAndGroup(currentPage);
			}else if(userKind==UserInfo.KIND_MANAGER_PART){
				list = fileDepotService.getFileListByPageOfAllGroup(userPart, currentPage);
			}else{
				list = fileDepotService.getFileListByPageOfGroup(userPart, userGroup, currentPage);
			}
		}else if(kind.equals("part")){
			//����
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				list = fileDepotService.getFileListByPageOfAllPart(currentPage);
			}else{
				list = fileDepotService.getFileListByPageOfPart(userPart, currentPage);
			}
		}else{
			//��˾
			list = fileDepotService.getFileListByPageOfCompany(currentPage);
		}
		//����
		if(list!=null){
			filesList = new ArrayList<Map<String,String>>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("fileId", ""+list.get(i).getId());
				map.put("showFileName", list.get(i).getShowFileName());
				//��Դ
				if(kind.equals("group")){
					//С��
					map.put("source", groupService.getNameById(list.get(i).getPart(), list.get(i).getGroup()));
				}else if(kind.equals("part")){
					//����
					map.put("source", partService.getNameById(list.get(i).getPart()));
				}else{
					//��˾
					map.put("source", "��˾");
				}
				//�ϴ�������
				String uploadName = userServer.getUserNameById(list.get(i).getCreatePerson());
				if(uploadName==null){
					//û�ҵ��˵�����
					map.put("uploadPersonName", "δ֪");
				}else{
					map.put("uploadPersonName", uploadName);
				}
				map.put("size", translateFileSize(list.get(i).getSize()));
				map.put("updateDate", sdf.format(list.get(i).getUpdateDate()));
				filesList.add(map);
			}
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","file/fileHome.jsp");
		model.put("myPageTitle","�ļ��ֿ�");
		model.put("myPageNav","5");
		
		model.put("fhPartName", userPartName);
		model.put("fhGroupName", userGroupName);
		model.put("fhCanSelectPart", canSelectPart);
		model.put("fhCanSelectGroup", canSelectGroup);
		model.put("fhFileKind", kind);//�ļ�����
		model.put("fhFilesList", filesList);//�ļ��б�
		model.put("fhCurrentFile", currentFile);//��ǰ���ļ�Ŀ¼
		model.put("fhCanUpload", canUpload);//�Ƿ�����ϴ���1Ϊ����
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת���ļ���С
	 * @param size
	 * @return
	 */
	private String translateFileSize(long size){
		String resStr = null;
		if((size/1024)<512){
			resStr = (size/1024) + "KB";
		}else if((size/1024/1024)<512){
			resStr = (size/1024/1024) + "MB";
		}else if((size/1024/1024/1024)<512){
			resStr = (size/1024/1024/1024) + "GB";
		}else{
			resStr = (size/1024/1024/1024/1024) + "TB";
		}
		return resStr;
	}
	
	/**
	 * �ϴ��ļ����ļ��ֿ�
	 * @param uploadFile
	 * @param fileKind
	 * @param selectPart
	 * @param selectGroup
	 * @param req
	 * @return
	 */
	@RequestMapping("file/uploadFileFrom.do")
	public ModelAndView uploadFile(@RequestParam(value = "file") MultipartFile uploadFile,
			String fileKind,String selectPart,String selectGroup,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		int userPart = userServer.getUserPartByJobId(jobId);
		int userGroup= userServer.getUserGroupByJobId(jobId);
		String url = req.getHeader("REFERER");
		url = url.substring(url.indexOf("/file/"));
		FileDepot fd = new FileDepot();
		//��֤Ȩ�ޡ��ж����͡������ϴ�λ��
		if(fileKind==null || fileKind.length()==0){
			return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ����ϴ����ʹ���");
		}else if(fileKind.equals("group")){
			//С��
			fd.setSource(FileDepot.SOURCE_GROUP);
			if(userKind==UserInfo.KIND_MANAGER_WEB || userKind==UserInfo.KIND_MANAGER_PART){
				fd.setPart(Integer.parseInt(selectPart));
				fd.setGroup(Integer.parseInt(selectGroup));
			}else if(userKind==UserInfo.KIND_MANAGER_GROUP){
				fd.setPart(userPart);
				fd.setGroup(userGroup);
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ�����Ȩ�ޣ�");
			}
		}else if(fileKind.equals("part")){
			//����
			fd.setSource(FileDepot.SOURCE_PART);
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				fd.setPart(Integer.parseInt(selectPart));
			}else if(userKind==UserInfo.KIND_MANAGER_PART){
				fd.setPart(userPart);
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ�����Ȩ�ޣ�");
			}
		}else{
			//��˾
			fd.setSource(FileDepot.SOURCE_COMPANY);
			if(userKind!=0){
				return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ�����Ȩ�ޣ�");
			}
		}
		File file = null;
		if(uploadFile.getSize()!=0){
			//���ļ�
			//�ϴ����ļ���
			fd.setShowFileName(uploadFile.getOriginalFilename());
			ServletContext application = req.getServletContext();
			String realPath = application.getRealPath("upload/file/fileDepot");
			int index = uploadFile.getOriginalFilename().lastIndexOf(".");
			String suffix = uploadFile.getOriginalFilename().substring(index+1);
			//������ʽ��jobId_1234567891235646.��׺
			String realFileName = jobId+"_"+System.currentTimeMillis()+"."+suffix;
			String fileName = realPath+File.separator+realFileName;
			//��ȡ�ļ�
			file = new File(fileName);
			try {
				File fileTemp = new File(realPath);
				if(!fileTemp.exists()){
					fileTemp.mkdirs();
				}
				uploadFile.transferTo(file);
				fd.setRealFileName(realFileName);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ��������������ļ�ʧ�ܣ�");
			}
			fd.setCreatePerson(jobId);
			fd.setSize(uploadFile.getSize());
			fd.setUpdateDate(new Date());
			
		}else{
			return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ����Ҳ����ļ���");
		}
		//���浽���ݿ�
		boolean tempB = false;
		try {
			tempB = fileDepotService.addNewFile(fd);
		} catch (Exception e) {
		}
		if(tempB){
			return JumpPrompt.jumpOfModelAndView(url, "�ϴ��ɹ���");
		}else{
			//����ʧ�ܣ�ɾ���ļ�
			if(file!=null){
				file.delete();
			}
			return JumpPrompt.jumpOfModelAndView(url, "�ϴ�ʧ�ܡ����������ݿ�ʧ�ܣ�");
		}
	}
	
	
	/**
	 * ���ļ��ֿ�ɾ���ļ�
	 * @param fileId
	 * @param req
	 * @return
	 */
	@RequestMapping("file/delFile.do")
	public ModelAndView delFile(String fileId, HttpServletRequest req){
		String url = req.getHeader("REFERER");
		if(url!=null){
			if(url.indexOf("/file/")==-1){
				url = "/home.do";
			}else{
				url = url.substring(url.indexOf("/file/"));
			}
		}else{
			url = "/home.do";
		}
		
		if(fileId==null || fileId.length()==0){
			return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ���ȱ�ٲ�����");
		}
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		int userPart = userServer.getUserPartByJobId(jobId);
		int userGroup= userServer.getUserGroupByJobId(jobId);
		
		FileDepot fd = fileDepotService.getOneFileInfoById(Integer.parseInt(fileId));
		if(fd==null){
			return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ���δ�ҵ���Ӧ�ļ���");
		}
		String createUserId = fd.getCreatePerson();
		
		//�ж��Ƿ����ɾ��
		boolean canDel = false;
		if(fd.getSource()==FileDepot.SOURCE_COMPANY && userKind==UserInfo.KIND_MANAGER_WEB){
			//��˾�ļ�
			canDel = true;
		}else if(fd.getSource()==FileDepot.SOURCE_PART){
			//�����ļ�
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				canDel = true;
			}else if(userKind==UserInfo.KIND_MANAGER_PART && fd.getPart()==userPart){
				canDel = true;
			}
		}else if(createUserId.equals(jobId)){
			canDel = true;
		}else{
			//С��
			if(userKind==0){
				canDel = true;
			}else if(userKind==UserInfo.KIND_MANAGER_PART && fd.getPart()==userPart){
				canDel = true;
			}else if(userKind==UserInfo.KIND_MANAGER_GROUP && fd.getPart()==userPart && fd.getGroup()==userGroup){
				canDel = true;
			}
		}
		if(!canDel){
			return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ�����Ȩ�ޣ�");
		}
		
		ServletContext application = req.getServletContext();
		String realPath = application.getRealPath("upload/file/fileDepot");
		String fileName = realPath+File.separator+fd.getRealFileName();
		File file = new File(fileName);
		if(file.exists()){
			//ɾ�����ݿ�
			boolean tempB = false;
			try {
				tempB = fileDepotService.delFileById(Integer.parseInt(fileId));
			} catch (Exception e) {}
			if(!tempB){
				return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ������ݿ�ɾ��ʧ�ܣ�");
			}
			if(file.delete()){
				return JumpPrompt.jumpOfModelAndView(url, "ɾ���ļ���"+fd.getShowFileName()+"���ɹ���");
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "ɾ���ļ���"+fd.getShowFileName()+"���ɹ�������������ɾ���ļ�ʧ�ܣ�");
			}
		}else{
			return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ����������Ҳ������ļ���");
		}
	}
	
	/**
	 * ���ļ��ֿ������ļ�
	 * @param fileId
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("file/downFile.do")
	public ModelAndView downFileMain(String fileId, HttpServletRequest req,HttpServletResponse response){
		String url = req.getHeader("REFERER");
		if(url.indexOf("/file/")==-1){
			url = "/home.do";
		}else{
			url = url.substring(url.indexOf("/file/"));
		}
		if(fileId==null || fileId.length()==0){
			return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���ȱ�ٲ�����");
		}
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		int userPart = userServer.getUserPartByJobId(jobId);
		int userGroup= userServer.getUserGroupByJobId(jobId);
		
		FileDepot fd = fileDepotService.getOneFileInfoById(Integer.parseInt(fileId));
		if(fd==null){
			return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���δ�ҵ���Ӧ�ļ���");
		}
		String createUserId = fd.getCreatePerson();
		
		//�ж��Ƿ��������
		boolean canDown = false;
		if(fd.getSource()==FileDepot.SOURCE_COMPANY){
			//��˾�ļ�
			canDown = true;
		}else if(fd.getSource()==FileDepot.SOURCE_PART){
			//�����ļ�
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				canDown = true;
			}else if(fd.getPart()==userPart){
				canDown = true;
			}
		}else if(createUserId.equals(jobId)){
			canDown = true;
		}else{
			//С��
			if(userKind==UserInfo.KIND_MANAGER_WEB){
				canDown = true;
			}else if(userKind==UserInfo.KIND_MANAGER_PART && fd.getPart()==userPart){
				canDown = true;
			}else if(fd.getPart()==userPart && fd.getGroup()==userGroup){
				canDown = true;
			}
		}
		
		if(!canDown){
			return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ�����Ȩ�ޣ�");
		}else{
			if(!downFile(fd,req,response)){
				return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ����������в����ڸ��ļ���");
			}
			return null;
		}
	}
	
	/**
	 * ��ʼ�����ļ�
	 * @param fd
	 * @param req
	 * @param response
	 */
	private boolean downFile(FileDepot fd,HttpServletRequest req,HttpServletResponse response) {
		ServletContext application = req.getServletContext();
		String realPath = application.getRealPath("upload/file/fileDepot");
		String fileName = realPath+File.separator+fd.getRealFileName();
		File file = new File(fileName);
		if(file.exists()){
			response.setContentType("application/octet-stream;charset=UTF-8");// ����ǿ�����ز���
			response.addHeader("Content-Length", "" + file.length());//�ļ�����
			try {
				String encodedFileName = new String(fd.getShowFileName().getBytes("utf-8"),"iso-8859-1");
				response.addHeader("Content-Disposition",  "attachment;fileName=\"" +encodedFileName+"\"" );
			} catch (UnsupportedEncodingException e1) {
				response.addHeader("Content-Disposition",  "attachment;fileName=" +fd.getShowFileName());
				e1.printStackTrace();
			}//�����ļ���
			OutputStream os = null;
			FileInputStream fis = null;
			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = fis.read(buffer))!=-1){
					os.write(buffer,0,len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(os!=null) {
					try {
						os.close();
					}catch (IOException e) {e.printStackTrace();
				}
				}
				if(fis!=null) {
					try {
						fis.close();
					}catch (IOException e) {e.printStackTrace();
				}
				}
			}
			return true;
		}else{
			return false;
		}
	}
}
