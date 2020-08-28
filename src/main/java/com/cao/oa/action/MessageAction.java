package com.cao.oa.action;

import com.cao.oa.bean.Message;
import com.cao.oa.bean.UserInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.MessageService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.RemindService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.JumpPrompt;

@Controller
public class MessageAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private RemindService remindService;
	
	private SimpleDateFormat sdf;
	
	
	public MessageAction(){
		super();
//		System.out.println(new Date().toString()+"����MessageAction");
		sdf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
	}	
	
	/**
	 * �鿴��Ϣ��ϸ
	 * @param msgId
	 * @param req
	 * @return
	 */
	@RequestMapping("message/lookMessage.do")
	public ModelAndView viewLookMsg(String msgId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ��һ����ַ
		String beforUrl = req.getHeader("REFERER");
		beforUrl = beforUrl.substring(beforUrl.indexOf("message/"));
		if(msgId==null  || msgId.length()==0){
			return JumpPrompt.jumpOfModelAndView("/"+beforUrl, "�鿴��Ϣ������󡣣�ȱ�ٲ�����");
		}
		
		String title = null;
		String kind = null;
		String person = null;
		String date = null;
		String content = null;
		
		//��ȡ���
		Message msg = messageService.getMessageInfoByMessageId(msgId);
		if(msg==null){
			return JumpPrompt.jumpOfModelAndView("/"+beforUrl, "�鿴��Ϣ���󡣣��޴���Ϣ��");
		}
		title = msg.getTitle();
		date = sdf.format(msg.getSendDate());
		content = msg.getContent();
		person = userServer.getUserNameById(msg.getSendPerson());
		if(person==null){
			person = "���޴���";
		}
		
		if(msg.getKind() == Message.KIND_MESSAGE_PART){
			kind = "������Ϣ";
		}else if(msg.getKind() == Message.KIND_MESSAGE_GROUP){
			kind = "С����Ϣ";
		}else if(msg.getKind() == Message.KIND_MESSAGE_PERSON){
			kind = "������Ϣ";
		}else if(msg.getKind() == Message.KIND_MESSAGE_COMPANY){
			kind = "��˾��Ϣ";
		}else if(msg.getKind() == Message.KIND_MESSAGE_SYSTEM){
			kind = "ϵͳ��Ϣ";
		}else if(msg.getKind() == Message.KIND_NOTICE_PART){
			kind = "���Ź���";
		}else if(msg.getKind() == Message.KIND_NOTICE_GROUP){
			kind = "С�鹫��";
		}else if(msg.getKind() == Message.KIND_NOTICE_COMPANY){
			kind = "��˾����";
		}else{
			kind = "δ֪������";
		}
		
		
		
		//��װ����
		boolean isMag = false;
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/messagePlazaDetail.jsp");
		if(beforUrl.startsWith("message/personMessageList.do")){
			model.put("myPageTitle","��Ϣ����");
			model.put("myPageNav","7");
			model.put("pmdIsNotice", 0);//����Ϣ
			isMag = true;
		}else{
			model.put("myPageTitle","��������");
			model.put("myPageNav","6");
			model.put("pmdIsNotice", 1);//�ǹ���
			isMag = false;
		}
		model.put("pmdPreUrl", beforUrl);//��һ����ַ
		model.put("pmdTitle", title);//����
		model.put("pmdKind", kind);//����
		model.put("pmdPerson", person);//������
		model.put("pmdDate", date);//ʱ��
		model.put("pmdContent", content);//����
		
		//��������ɾ��
		try {
			remindService.takeIdRead(jobId, msg.getId(), isMag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת�������б�
	 * @param req
	 * @return
	 */
	@RequestMapping("message/noticeList.do")
	public ModelAndView viewNoticeList(String kind,String page,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
//		int userKind = userServer.getUserKindByJobId(jobId);
		List<Map<String,String>> msgList = null;
		String msgKind = null;
		int allPage = 0;
		int currentPage = 0;
		
		if(kind==null  || kind.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/messagePlaza.do", "�Ƿ�����");
		}
		//��ǰҳ
		if(page==null  || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//���ͺ���ҳ��
		if(kind.equals("g")){
			msgKind = "С��";
			allPage = messageService.getNoticeOfGroupPageNumberByJobId(jobId);
		}else if(kind.equals("p")){
			msgKind = "����";
			allPage = messageService.getNoticeOfPartPageNumberByJobId(jobId);
		}else{
			msgKind = "��˾";
			allPage = messageService.getNoticeOfCompanyPageNumberByJobId();
		}
		//��ֹҳ���Ƿ�
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		//��ȡ��Ϣ�б�
		List<Message> tempList = null;
		if(kind.equals("g")){
			tempList = messageService.getNoticeInfoOfGroupToPageByJobId(jobId, currentPage);
		}else if(kind.equals("p")){
			tempList = messageService.getNoticeInfoOfPartToPageByJobId(jobId, currentPage);
		}else{
			tempList = messageService.getNoticeInfoOfCompanyToPageByJobId(currentPage);
		}
		//��������
		msgList = new ArrayList<>();
		if(tempList!=null){
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", tempList.get(i).getId()+"");
				map.put("title", tempList.get(i).getTitle());
				map.put("date", sdf.format(tempList.get(i).getSendDate()));
				if(remindService.isRead(jobId,tempList.get(i).getId(),false)){
					map.put("isRead", "1");//�Ѷ�
				}else{
					map.put("isRead", "0");//δ��
				}
				msgList.add(map);
			}
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/messagePlazaList.jsp");
		model.put("myPageTitle","����㳡�����б�");
		model.put("myPageNav","6");
		model.put("mplMsgList", msgList);//��Ϣ�б�
		model.put("mplMsgKind", msgKind);//��Ϣ����
		model.put("mplMsgKindStr", kind);
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ������ҳ��
	 * @param req
	 * @return
	 */
	@RequestMapping("message/messagePlaza.do")
	public ModelAndView viewMessagePlaza(HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		//int userKind = userServer.getUserKindByJobId(jobId);
		
		List<Map<String,String>> partNoticeList = null;
		List<Map<String,String>> companyNoticeList = null;
		List<Map<String,String>> groupNoticeList = null;
		
		List<Message> tempList = null;
		
		
		//����
		tempList = messageService.getNoticeInfoOfPartToPageByJobId(jobId, 1);
		partNoticeList = new ArrayList<Map<String,String>>();
		if(tempList!=null){
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", tempList.get(i).getId()+"");
				map.put("title", tempList.get(i).getTitle());
				map.put("date", sdf.format(tempList.get(i).getSendDate()));
				if(remindService.isRead(jobId,tempList.get(i).getId(),false)){
					map.put("isRead", "1");//�Ѷ�
				}else{
					map.put("isRead", "0");//δ��
				}
				partNoticeList.add(map);
			}
		}
		
		//��˾
		tempList = messageService.getNoticeInfoOfCompanyToPageByJobId(1);
		companyNoticeList = new ArrayList<Map<String,String>>();
		if(tempList!=null){
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", tempList.get(i).getId()+"");
				map.put("title", tempList.get(i).getTitle());
				map.put("date", sdf.format(tempList.get(i).getSendDate()));
				if(remindService.isRead(jobId,tempList.get(i).getId(),false)){
					map.put("isRead", "1");//�Ѷ�
				}else{
					map.put("isRead", "0");//δ��
				}
				companyNoticeList.add(map);
			}
		}
		
		//С��
		tempList = messageService.getNoticeInfoOfGroupToPageByJobId(jobId, 1);
		groupNoticeList = new ArrayList<Map<String,String>>();
		if(tempList!=null){
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", tempList.get(i).getId()+"");
				map.put("title", tempList.get(i).getTitle());
				map.put("date", sdf.format(tempList.get(i).getSendDate()));
				if(remindService.isRead(jobId,tempList.get(i).getId(),false)){
					map.put("isRead", "1");//�Ѷ�
				}else{
					map.put("isRead", "0");//δ��
				}
				groupNoticeList.add(map);
			}
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/messagePlaza.jsp");
		model.put("myPageTitle","�㳡");
		model.put("myPageNav","6");
		
		model.put("mpCompanyMsgList", companyNoticeList);
		model.put("mpPartMsgList", partNoticeList);
		model.put("mpNoticeMsgList", groupNoticeList);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ��������Ϣ�б�
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("message/personMessageList.do")
	public ModelAndView viewPersonMsgList(String page,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		//int userKind = userServer.getUserKindByJobId(jobId);
//		System.out.println(jobId);
		List<Map<String,String>> msgList = null;//һҳ5��
		int allPage = 0;
		int currentPage = 0;
		//��ǰҳ
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		
		//��ȡ��Ϣ����
		allPage = messageService.getAllMessagePageNumberByJobId(jobId);
		//��ֹ����ҳ��
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		//��ȡ��Ϣ
		List<Message> temp = messageService.getAllMessageInfoOfPageByJobId(jobId, currentPage);
		if(temp!=null && temp.size()!=0){
			msgList = new ArrayList<>();
			for(int i=0;i<temp.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", temp.get(i).getId()+"");
				map.put("title", temp.get(i).getTitle());
				map.put("sendPerson", userServer.getUserNameById(temp.get(i).getSendPerson()));
				switch(temp.get(i).getKind()){
					case Message.KIND_MESSAGE_SYSTEM:
						map.put("source", "ϵͳ");
						break;
					case Message.KIND_MESSAGE_COMPANY:
						map.put("source", "��˾");
						break;
					case Message.KIND_MESSAGE_PART:
						map.put("source", "����");
						break;
					case Message.KIND_MESSAGE_GROUP:
						map.put("source", "С��");
						break;
					case Message.KIND_MESSAGE_PERSON:
						map.put("source", "����");
						break;
					default:
						map.put("source", "δ֪");
						break;
				}
				map.put("date", sdf.format(temp.get(i).getSendDate()));
				//�Ƿ��Ķ�
				if(remindService.isRead(jobId,temp.get(i).getId(),true)){
					map.put("status", "1");//�Ѷ�
				}else{
					map.put("status", "0");//δ��
				}
				msgList.add(map);
			}
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/personalMessageList.jsp");
		model.put("myPageTitle","������Ϣ�����б�");
		model.put("myPageNav","7");
		model.put("pmlMsgList", msgList);//��Ϣ�б�
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ������Ϣ
	 * @param req
	 * @return
	 */
	@RequestMapping("message/sendMessage.do")
	public ModelAndView viewSendMessage(HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		int userKind = userServer.getUserKindByJobId(jobId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/sendMessage.jsp");
		model.put("myPageTitle","������Ϣ");
		model.put("myPageNav","8");
		model.put("userKindNumber",userKind);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ���͹���
	 * @param req
	 * @return
	 */
	@RequestMapping("message/sendNotice.do")
	public ModelAndView viewSendNotice(HttpServletRequest req){//
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		int userKind = userServer.getUserKindByJobId(jobId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","message/sendNotice.jsp");
		model.put("myPageTitle","��������");
		model.put("myPageNav","9");
		model.put("userKindNumber",userKind);
		return new ModelAndView("baseJsp",model);
	}
	
	
	/**
	 * ������Ϣ������
	 * @param req
	 * @return
	 */
	@RequestMapping("message/sendMessageForm.do")
	public ModelAndView sendMsg(HttpServletRequest req){
		//����
		String title = req.getParameter("mtitle");
		if(title==null || title.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ�����������û��д��");
		}
		//���͵�����
		String content = req.getParameter("mcontext");
		if(content==null || content.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ������͵�����û��д��");
		}
		//��Ϣ����
		String msgKind = req.getParameter("mkind");
		if(msgKind==null || msgKind.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ�����Ϣ����û��ѡ��");
		}
		boolean result = false;
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		
		Message msg = new Message();
		msg.setTitle(title);
		msg.setContent(content);
		msg.setSendPerson(jobId);
		try{
			if(msgKind.equals("person")){
				//������Ϣ
				//������jobId
				String accept = req.getParameter("macceptJobId");
				if(!userServer.hasUserByJobId(accept)){
					return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ���û�й���Ϊ��"+accept+"���ĳ�Ա��");
				}
				//����  accept������
				msg.setAcceptPerson(accept);
				msg.setKind(Message.KIND_MESSAGE_PERSON);
				result = messageService.sendOneMsgToPerson(msg,true);
			}else if(msgKind.equals("group")){
				//С����Ϣ
				int acceptPart = -1;
				int acceptGroup = -1;
				if(userKind==UserInfo.KIND_MANAGER_WEB){//��վ����Ա
					acceptPart = Integer.parseInt(req.getParameter("macceptpPart"));
					acceptGroup = Integer.parseInt(req.getParameter("macceptgGroup"));
				}else if(userKind==UserInfo.KIND_MANAGER_PART){//���Ź���Ա
					acceptPart = userServer.getUserPartByJobId(jobId);
					acceptGroup = Integer.parseInt(req.getParameter("macceptg"));
				}else if(userKind==UserInfo.KIND_MANAGER_GROUP){//С�����Ա
					acceptPart = userServer.getUserPartByJobId(jobId);
					acceptGroup = userServer.getUserGroupByJobId(jobId);
				}else{
					return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//������Ϣ acceptPart���� ��acceptGroupС��
				msg.setKind(Message.KIND_MESSAGE_GROUP);
				msg.setAcceptPart(acceptPart);
				msg.setAcceptGroup(acceptGroup);
				result = messageService.sendOneMsgToGroup(msg,true);
			}else if(msgKind.equals("part")){
				//������Ϣ
				int acceptPart = -1;
				if(userKind==UserInfo.KIND_MANAGER_WEB){
					acceptPart = Integer.parseInt(req.getParameter("macceptp"));
				}else if(userKind==UserInfo.KIND_MANAGER_PART){
					acceptPart = userServer.getUserPartByJobId(jobId);
				}else{
					return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//����   acceptPart����
				msg.setKind(Message.KIND_MESSAGE_PART);
				msg.setAcceptPart(acceptPart);
				result = messageService.sendOneMsgToPart(msg,true);
			}else if(msgKind.equals("company")){
				//��˾��Ϣ
				if(userKind!= UserInfo.KIND_MANAGER_WEB){
					return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//����
				msg.setKind(Message.KIND_MESSAGE_COMPANY);
				result = messageService.sendOneMsgToAll(msg,true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ����������쳣��");
		}
		if(result){
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "�ɹ���Ϣ���ͣ�");
		}else{
			return JumpPrompt.jumpOfModelAndView("/message/sendMessage.do", "��Ϣ����ʧ�ܡ�");
		}
	}
	
	
	/**
	 * ���͹���
	 * @param req
	 * @return
	 */
	@RequestMapping("message/sendNoticeForm.do")
	public ModelAndView sendNotice(HttpServletRequest req){
		//����
		String title = req.getParameter("mtitle");
		if(title==null || title.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ�����������û��д��");
		}
		//���͵�����
		String content = req.getParameter("mcontext");
		if(content==null || content.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ������͵�����û��д��");
		}
		//��Ϣ����
		String msgKind = req.getParameter("mkind");
		if(msgKind==null || msgKind.length()==0){
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ�����Ϣ����û��ѡ��");
		}
		boolean result = false;
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		
		Message msg = new Message();
		msg.setTitle(title);
		msg.setContent(content);
		msg.setSendPerson(jobId);
		try{
			if(msgKind.equals("group")){
				//С�鹫��
				int acceptPart = -1;
				int acceptGroup = -1;
				if(userKind==UserInfo.KIND_MANAGER_WEB){//��վ����Ա
					acceptPart = Integer.parseInt(req.getParameter("macceptpPart"));
					acceptGroup = Integer.parseInt(req.getParameter("macceptgGroup"));
				}else if(userKind==UserInfo.KIND_MANAGER_PART){//���Ź���Ա
					acceptPart = userServer.getUserPartByJobId(jobId);
					acceptGroup = Integer.parseInt(req.getParameter("macceptg"));
				}else if(userKind==UserInfo.KIND_MANAGER_GROUP){//С�����Ա
					acceptPart = userServer.getUserPartByJobId(jobId);
					acceptGroup = userServer.getUserGroupByJobId(jobId);
				}else{
					return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//������Ϣ acceptPart���� ��acceptGroupС��
				msg.setKind(Message.KIND_NOTICE_PART);
				msg.setAcceptPart(acceptPart);
				msg.setAcceptGroup(acceptGroup);
				result = messageService.sendOneMsgToGroup(msg,false);
			}else if(msgKind.equals("part")){
				//���Ź���
				int acceptPart = -1;
				if(userKind==UserInfo.KIND_MANAGER_WEB){
					acceptPart = Integer.parseInt(req.getParameter("macceptp"));
				}else if(userKind==UserInfo.KIND_MANAGER_PART){
					acceptPart = userServer.getUserPartByJobId(jobId);
				}else{
					return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//����   acceptPart����
				msg.setKind(Message.KIND_NOTICE_PART);
				msg.setAcceptPart(acceptPart);
				result = messageService.sendOneMsgToPart(msg,false);
			}else if(msgKind.equals("company")){
				//��˾����
				if(userKind!=UserInfo.KIND_MANAGER_WEB){
					return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ����޴�Ȩ��@_@��");
				}
				//����
				msg.setKind(Message.KIND_NOTICE_COMPANY);
				result = messageService.sendOneMsgToAll(msg,false);
			}else{
				return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ������ʹ���");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ����������쳣��");
		}
		if(result){
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "�ɹ���Ϣ���ͣ�");
		}else{
			return JumpPrompt.jumpOfModelAndView("/message/sendNotice.do", "��Ϣ����ʧ�ܡ�");
		}
	}

	public UserService getUserServer() {
		return userServer;
	}

	public void setUserServer(UserService userServer) {
		this.userServer = userServer;
	}

	public PartService getPartService() {
		return partService;
	}

	public void setPartService(PartService partService) {
		this.partService = partService;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
}
