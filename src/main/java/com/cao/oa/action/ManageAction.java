package com.cao.oa.action;

import com.cao.oa.bean.Group;
import com.cao.oa.bean.Part;
import com.cao.oa.bean.UserInfo;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.JumpPrompt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//import java.util.Iterator;

@Controller
public class ManageAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	
	private SimpleDateFormat sdf;
	
	public ManageAction(){
		super();
		sdf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
	}

	/**
	 * ת���û��������޸�ҳ�棨��վ����Ա��
	 * @param jobId
	 * @return
	 */
	@RequestMapping("manage/userManagerEdit.do")
	public ModelAndView viewUserManagerEdit(String jobId,HttpServletRequest req){
		String userJobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		int userKind = userServer.getUserKindByJobId(userJobId);
		if(userKind!=UserInfo.KIND_MANAGER_WEB){
			return JumpPrompt.jumpOfModelAndView("/home.do", "�Բ������޴�Ȩ�ޣ�");
		}
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/userManagerEdit.jsp");
		model.put("myPageTitle","�û��������޸�");
		model.put("myPageNav","12");
		
		//��ȡ�û���Ϣ
		UserInfo temp = userServer.getUserInfoByJobId(jobId);
		if(temp!=null){
			model.put("umeName", temp.getName());//����
			model.put("umeSex", temp.getSex());//�Ա�
			model.put("umeJobId", jobId);//����
			model.put("umeCardId", temp.getCardId());//���֤
			model.put("umePart", temp.getPart());//����
			model.put("umeGroup", temp.getGroup());//С��
			model.put("umeTel", temp.getTel());//�绰
			model.put("umeEmail", temp.getEmail());//����
			model.put("umeAddr", temp.getAddr());//��ַ
			model.put("umeStatue", temp.getStatus());//��ַ
			model.put("umePost", temp.getPost());//��ַ
		}
		
		List<Map<String, Object>> partsList = null;
		List<Map<String, Object>> groupsList = null;
		
		partsList = partService.getAllPartsAndNames();
		groupsList = groupService.getAllGroupsOfPartNameAndId(temp.getPart());
		
		model.put("umePartList", partsList);//��ַ
		model.put("umeGroupList", groupsList);//��ַ
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת���û�����ҳ�棨��վ����Ա��
	 * @param page
	 * @param partId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/userManager.do")
	public ModelAndView viewUserManager(String page,String partId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		int userKind = userServer.getUserKindByJobId(jobId);
		if(userKind!=UserInfo.KIND_MANAGER_WEB){
			return JumpPrompt.jumpOfModelAndView("/home.do", "�Բ������޴�Ȩ�ޣ�");
		}
		
		List<Map<String,String>> personList = null;//һҳ10��
		List<Map<String,String>> partList = null;
		int currentPart = 0;
		int allPage = 0;
		int currentPage = 0;
		//���������Ĳ���
		if(partId==null || partId.length()==0){
			currentPart = 0;
		}else{
			currentPart = Integer.parseInt(partId);
		}
		//����������ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//��ȡ��ҳ��
		allPage = userServer.getAllPageByPart(currentPart);
		//��ֹ�Ƿ�ҳ��
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		//��ȡ�����б�
		List<Map<String,Object>> tempList = partService.getAllPartsAndNames();
		if(tempList!=null){
			partList = new ArrayList<Map<String,String>>();
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("partId", ""+(int)tempList.get(i).get("id"));
				map.put("partName", (String)tempList.get(i).get("name"));
				partList.add(map);
			}
		}
		//��ȡ�û��б�
		tempList = userServer.getUsersInfoOfPartByPage(currentPart, currentPage);
		if(tempList!=null){
			personList = new ArrayList<Map<String,String>>();
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("jobId", (String)tempList.get(i).get("jobId"));
				map.put("cardId", (String)tempList.get(i).get("cardId"));
				map.put("name", (String)tempList.get(i).get("name"));
				if(tempList.get(i).get("post")==null){
					map.put("post", "��");
				}else{
					map.put("post", (String)tempList.get(i).get("post"));
				}
				if((int)tempList.get(i).get("sex")==UserInfo.SEX_MALE){
					map.put("sex", "��");
				}else{
					map.put("sex", "Ů");
				}
				int pId = (int)tempList.get(i).get("part");
				map.put("part", partService.getNameById(pId));
				map.put("group", groupService.getNameById(pId, (int)tempList.get(i).get("ggroup")));
				personList.add(map);
			}
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/userManager.jsp");
		model.put("myPageTitle","�û�����");
		model.put("myPageNav","12");
		model.put("umPersonList", personList);//�û��б����6��
		model.put("umPartList", partList);//�����б�
		model.put("umCurrentPart", currentPart+"");//��ǰ��ʾ�Ĳ���
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת��Ⱥ�������桪������
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/groupManagerPart.do")
	public ModelAndView viewGroupManagerPart(String page,HttpServletRequest req){
		int allPage = 0;
		int currentPage = 0;
		List<Map<String,String>> managePartList = null;
		if(page==null || page.trim().length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page.trim());
		}
		allPage = partService.getAllPage();
		//ѡ���ҳ��������������Ϊ���
		if(currentPage>allPage){
			currentPage = allPage;
		}
		//��ֹ�Ƿ�ҳ��
		if(currentPage<1){
			currentPage = 1;
		}
		//��ȡ������Ϣ
		managePartList = null;
		List<Part> list = partService.getPartByPage(currentPage);
		if(list!=null && list.size()!=0){
			managePartList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("partId", list.get(i).getId()+"");
				map.put("name", list.get(i).getName());
				map.put("groupNum", ""+partService.getGroupOfPartNumbers(list.get(i).getId()));
				map.put("memberNum", ""+partService.getMemberOfPartNumbers(list.get(i).getId()));
				map.put("createDate", sdf.format(list.get(i).getCreateDate()));
				map.put("createPerson", list.get(i).getCreatePerson());
				managePartList.add(map);
			}
		}
		//ת��
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/groupManagerPart.jsp");
		model.put("myPageTitle","Ⱥ�����������");
		model.put("myPageNav","13");
		model.put("gmpManagePartList", managePartList);//�����б�
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת��Ⱥ�������桪��С��
	 * @param page
	 * @param partId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/groupManagerGroup.do")
	public ModelAndView viewGroupManagerGroup(String page,String partId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int kind = userServer.getUserKindByJobId(jobId);
		List<Map<String,String>> groupList = null;//һҳ6��
		List<Map<String,String>> partList = null;
		String currentPart = "";
		int allPage = 0;
		int currentPage = 0;
		
		if(kind==UserInfo.KIND_MANAGER_WEB){
			//��ȫ��
			List<Map<String, Object>> list = partService.getAllPartsAndNames();
			//������Ϣ����Ӳ���
			partList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<>();
				map.put("partId", (int)list.get(i).get("id")+"");
				map.put("partName", (String)list.get(i).get("name"));
				partList.add(map);
			}
			//��Ӳ���
			if(partId==null || partId.length()==0){
				partId = partList.get(0).get("partId");
			}
			
		}else{//kind==1
			//������
			//��Ӳ��ţ��̶�
			partId = userServer.getUserPartByJobId(jobId)+"";
			//��Ӳ���
			partList = new ArrayList<>();
			Map<String,String> map = new HashMap<>();
			map.put("partId", partId);
			map.put("partName", partService.getNameById(Integer.parseInt(partId)));
			partList.add(map);
		}
		currentPart = partId;
		//��ȡ��ҳ�� allPage
		allPage = groupService.getAllPage(Integer.parseInt(partId));
		//ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//ѡ���ҳ��������������Ϊ���
		if(currentPage>allPage){
			currentPage = allPage;
		}
		//��ֹ�Ƿ�ҳ��
		if(currentPage<1){
			currentPage = 1;
		}
		//�������� getPartByPage
		List<Group> groupsListOfSQL = groupService.getGroupsOfPartByPage(Integer.parseInt(partId), currentPage);
		//�������
		groupList = new ArrayList<>();
		for(int i=0;i<groupsListOfSQL.size();i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("groupId", groupsListOfSQL.get(i).getId()+"");
			map.put("name", groupsListOfSQL.get(i).getName());
			map.put("member", groupService.getMemberNumbersOfGroup(Integer.parseInt(partId), groupsListOfSQL.get(i).getId())+"");
			map.put("createDate", sdf.format(groupsListOfSQL.get(i).getCreateDate()));
			map.put("createPerson", groupsListOfSQL.get(i).getCreatePerson());
			groupList.add(map);
		}

		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/groupManagerGroup.jsp");
		model.put("myPageTitle","Ⱥ�������С��");
		model.put("myPageNav","13");
		model.put("gmgGroupList", groupList);//С���б����10��
		model.put("gmgPartList", partList);//�����б�
		model.put("gmgCurrentPart", currentPart);//��ǰ��ʾ�Ĳ���
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	
	/**
	 * ת��Ⱥ�������桪����Ա
	 * @param page
	 * @param partId
	 * @param groupId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/groupManagerMember.do")
	public ModelAndView viewGroupManagerMember(String page,String partId,String groupId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int kind = userServer.getUserKindByJobId(jobId);
		List<Map<String,String>> personList = null;//һҳ6��
		List<Map<String,String>> partList = null;
		List<Map<String,String>> groupList = null;
		String currentPart = "";
		String currentGroup = "";
		int allPage = 0;
		int currentPage = 0;
		
		//ȷ����ǰҳ
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		if(kind==UserInfo.KIND_MANAGER_WEB){
			//��վ����Ա
			//������Ϣ����Ӳ���
			List<Map<String, Object>> list = partService.getAllPartsAndNames();
			partList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<>();
				map.put("partId", (int)list.get(i).get("id")+"");
				map.put("partName", (String)list.get(i).get("name"));
				partList.add(map);
			}
			//��Ӳ���
			if(partId==null || partId.length()==0){
				partId = partList.get(0).get("partId");
			}
			//ȷ������
			currentPart = partId;
			//���С��
			list = groupService.getAllGroupsOfPartNameAndId(Integer.parseInt(currentPart));
			groupList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<>();
				map.put("groupId", (int)list.get(i).get("id")+"");
				map.put("groupName", (String)list.get(i).get("name"));
				groupList.add(map);
			}
			if(groupId==null || groupId.length()==0){
				groupId = groupList.get(0).get("groupId");
			}
			//ȷ��С��
			currentGroup = groupId;
		}else if(kind==UserInfo.KIND_MANAGER_PART){
			//���Ź���Ա
			//ȷ������
			currentPart = userServer.getUserPartByJobId(jobId)+"";
			//���С��
			List<Map<String, Object>> list = groupService.getAllGroupsOfPartNameAndId(Integer.parseInt(currentPart));
			groupList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				Map<String,String> map = new HashMap<>();
				map.put("groupId", (int)list.get(i).get("id")+"");
				map.put("groupName", (String)list.get(i).get("name"));
				groupList.add(map);
			}
			if(groupId==null || groupId.length()==0){
				groupId = groupList.get(0).get("groupId");
			}
			//ȷ��С��
			currentGroup = groupId;
		}else{
			//С�����Ա
			//ȷ������
			currentPart = userServer.getUserPartByJobId(jobId)+"";
			//ȷ��С��
			currentGroup = userServer.getUserGroupByJobId(jobId)+"";
		}
		
		//��ȡ��ҳ��
		allPage = userServer.getAllPageByGroup(Integer.parseInt(currentPart), Integer.parseInt(currentGroup));
		//��ȡ��ҳ
		List<UserInfo> temp = userServer.findUsersGroupOfGroupId(Integer.parseInt(currentPart), 
				Integer.parseInt(currentGroup),currentPage);
		//��ȡ��Ա��Ϣ�б�
		personList = new ArrayList<>();
		for(int i=0;i<temp.size();i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("jobId", temp.get(i).getJobId());
			map.put("cardId", temp.get(i).getCardId());
			map.put("name", temp.get(i).getName());
			if(temp.get(i).getPost()==null){
				map.put("post", "��");
			}else{
				map.put("post", temp.get(i).getPost());
			}
			
			map.put("joinDate", sdf.format(temp.get(i).getJoinTime()));
			map.put("part", partService.getNameById(temp.get(i).getPart()));
			map.put("ggroup", groupService.getNameById(temp.get(i).getPart(), temp.get(i).getGroup()));
			switch(temp.get(i).getKind()){
				case UserInfo.KIND_MANAGER_WEB:
					map.put("identity", "��վ����Ա");
					break;
				case UserInfo.KIND_MANAGER_PART:
					map.put("identity", "���Ź���Ա");
					break;
				case UserInfo.KIND_MANAGER_GROUP:
					map.put("identity", "С�����Ա");
					break;
				case UserInfo.KIND_MEMBER:
					map.put("identity", "��ͨ��Ա");
					break;
				default:
					map.put("identity", "��ȡʧ��");
					break;
			}
			personList.add(map);
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/groupManagerMember.jsp");
		model.put("myPageTitle","Ⱥ���������Ա");
		model.put("myPageNav","13");
		model.put("gmmPersonList", personList);//�û��б����6��
		model.put("gmmPartList", partList);//�����б�
		model.put("gmmGroupList", groupList);//С���б�
		model.put("gmmCurrentPart", currentPart);//��ǰ��ʾ�Ĳ���
		model.put("gmmCurrentGroup", currentGroup);//��ǰ��ʾ��С��
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת��Ⱥ�������桪���ֵ�
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/manageGroup.do")
	public ModelAndView viewManageGroupMain(HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int kind = userServer.getUserKindByJobId(jobId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/groupManager.jsp");
		model.put("myPageTitle","Ⱥ������ҳ");
		model.put("myPageNav","13");
		switch(kind){
			case UserInfo.KIND_MANAGER_WEB:
				model.put("gmCanPart",1);
				model.put("gmCanGroup",1);
				model.put("gmCanMember",1);
				break;
			case UserInfo.KIND_MANAGER_PART:
				model.put("gmCanPart",0);
				model.put("gmCanGroup",1);
				model.put("gmCanMember",1);
				break;
			case UserInfo.KIND_MANAGER_GROUP:
				model.put("gmCanPart",0);
				model.put("gmCanGroup",0);
				model.put("gmCanMember",1);
				break;
			case UserInfo.KIND_MEMBER:
				return JumpPrompt.jumpOfModelAndView("/home.do", "�Բ������޴�Ȩ�ޣ�");
			default:
				return JumpPrompt.jumpOfModelAndView("/home.do", "�Բ���Ȩ�޻�ȡ�쳣��");
		}
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ת����������û�����
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/addUser.do")
	public ModelAndView viewAddUser(HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ�û�����
		int userKind = userServer.getUserKindByJobId(jobId);
		if(userKind!=UserInfo.KIND_MANAGER_WEB){
			return JumpPrompt.jumpOfModelAndView("/home.do", "�Բ������޴�Ȩ�ޣ�");
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","manager/addUsers.jsp");
		model.put("myPageTitle","�޸ĸ�������");
		model.put("myPageNav","14");
		return new ModelAndView("baseJsp",model);
	}

	/**
	 * �޸�ĳ���û���ȫ����Ϣ
	 * @param userName
	 * @param userSex
	 * @param cardId
	 * @param jobId
	 * @param part
	 * @param group
	 * @param tel
	 * @param email
	 * @param addr
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/changeUserInfoAllFrom.do")
	public ModelAndView changeUserInfoAll(String userName,String userSex,String cardId,String jobId,
			String part,String group,String tel,String email,String addr,String userStatus,String post,
			HttpServletRequest req){
		String jumpToStr = "/manage/userManagerEdit.do?jobId="+jobId;
		String userJobId = (String)req.getSession().getAttribute("userJobId");
		if(userServer.getUserKindByJobId(userJobId)!=UserInfo.KIND_MANAGER_WEB){
			return JumpPrompt.jumpOfModelAndView(jumpToStr, "Ȩ���쳣���޷����в���");
		}
		if(post!=null && post.trim().length()!=0){
		}else{
			post = null;
		}
		UserInfo info = new UserInfo();
		info.setName(userName);
		info.setSex(Integer.parseInt(userSex));
		info.setPost(post);
		info.setCardId(cardId);
		info.setJobId(jobId);
		info.setStatus(Integer.parseInt(userStatus));
		info.setPart(Integer.parseInt(part));
		info.setGroup(Integer.parseInt(group));
		if(tel!=null && tel.length()!=0){
			info.setTel(tel);
		}else{
			info.setTel(null);
		}
		if(email!=null && email.length()!=0){
			info.setEmail(email);
		}else{
			info.setEmail(null);
		}
		if(addr!=null && addr.length()!=0){
			info.setAddr(addr);
		}else{
			info.setAddr(null);
		}
		//����ȫ��
		try {
			if(userServer.changeUserInfoAllByJobId(info)){
				return JumpPrompt.jumpOfModelAndView(jumpToStr, "�ɹ��޸�jobIdΪ��"+jobId+"�����û���Ϣ��");
			}else{
				return JumpPrompt.jumpOfModelAndView(jumpToStr, "�޸��û���Ϣʧ�ܡ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(jumpToStr, "�޸��û���Ϣʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ���ĳ�Ա
	 * @param modelShowJobId
	 * @param modelShowPart
	 * @param modelShowGroup
	 * @param modelShowUserKind
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/changeMemberForm.do")
	public ModelAndView changeMemberForm(String modelShowJobId,String modelShowPart,String modelShowGroup,
			String modelShowUserKind,String currentPage,String currentPartId,String currentGroupId,String modelShowPost,
			HttpServletRequest req){
		//������ת��ҳ���·��
		String jumpToStr = "/manage/groupManagerMember.do?page="+currentPage;
		if(currentPartId!=null && currentPartId.length()!=0){
			jumpToStr += "&partId="+currentPartId;
		}
		if(currentGroupId!=null && currentGroupId.length()!=0){
			jumpToStr += "&groupId="+currentGroupId;
		}
		if(modelShowPost!=null && modelShowPost.trim().length()!=0){
		}else{
			modelShowPost = null;
		}
		if(modelShowPart==null){
			modelShowPart = req.getParameter("modelShowPart2");
		}
		if(modelShowGroup==null){
			modelShowGroup = req.getParameter("modelShowGroup2");
		}
		if(modelShowUserKind==null){
			modelShowUserKind = req.getParameter("modelShowUserKind2");
		}
		//��ȡ�����ߵ�jobId
		String userJobId = (String)req.getSession().getAttribute("userJobId");
		int partId = userServer.getUserPartByJobId(modelShowJobId);
		boolean result = false;
		try{
			if(userServer.getUserKindByJobId(userJobId)==UserInfo.KIND_MANAGER_WEB){
				//��վ����Ա
//				System.out.println("���ġ�id:"+modelShowJobId+",part:"+modelShowPart+",group:"+modelShowGroup+",shen:"+modelShowUserKind);
				if(!modelShowUserKind.equals("0")){
					result = userServer.changeUserBaseInfoWithKind(modelShowJobId, Integer.parseInt(modelShowPart), Integer.parseInt(modelShowGroup), Integer.parseInt(modelShowUserKind),modelShowPost);
				}else{
					result = userServer.changeUserBaseInfoWithoutKind(modelShowJobId, Integer.parseInt(modelShowPart), Integer.parseInt(modelShowGroup),modelShowPost);
				}
			}else if(userServer.getUserKindByJobId(userJobId)==UserInfo.KIND_MANAGER_PART && userServer.getUserPartByJobId(userJobId)==partId){
				//���Ź���Ա
//				System.out.println("���ġ�id:"+modelShowJobId+",group:"+modelShowGroup+",shen:"+modelShowUserKind);
				if(userServer.getUserKindByJobId(modelShowJobId)<userServer.getUserKindByJobId(userJobId)){
					//�ĵ���ݣ����Լ�С�����Ը�
					result = userServer.changeUserBaseInfoWithKind(modelShowJobId, Integer.parseInt(modelShowPart), Integer.parseInt(modelShowGroup), Integer.parseInt(modelShowUserKind),modelShowPost);
				}else{
					result = userServer.changeUserBaseInfoWithoutKind(modelShowJobId, Integer.parseInt(modelShowPart), Integer.parseInt(modelShowGroup),modelShowPost);
				}
			}else{
				return JumpPrompt.jumpOfModelAndView(jumpToStr, "Ȩ���쳣���޷����в���");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(jumpToStr, "����ʧ�ܡ����������쳣��");
		}
		if(result){
			return JumpPrompt.jumpOfModelAndView(jumpToStr, "�����ɹ���");
		}else{
			return JumpPrompt.jumpOfModelAndView(jumpToStr, "����ʧ�ܡ�");
		}
	}
	
	
	/**
	 * ���С��
	 * @param newGroupName
	 * @param partId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/addGroupForm.do")
	public ModelAndView addGroup(String newGroupName,String partId,HttpServletRequest req){
		String url = req.getHeader("REFERER");
		if(url.indexOf("/manage/")==-1){
			url = "/home.do";
		}else{
			url = url.substring(url.indexOf("/manage/"));
		}
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String userName = userServer.getUserNameById(jobId);
		if(groupService.getGroupByName(Integer.parseInt(partId),newGroupName)!=-1){
			return JumpPrompt.jumpOfModelAndView(url, "���С�顰"+newGroupName+"��ʧ�ܡ�����ʾ����С���Ѵ��ڣ�");
		}
		try{
			if(groupService.addGroup(Integer.parseInt(partId) ,newGroupName, userName)){
				return JumpPrompt.jumpOfModelAndView(url, "���С�顰"+newGroupName+"���ɹ���");
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "���С�顰"+newGroupName+"��ʧ�ܡ�");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(url, "���С�顰"+newGroupName+"��ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ɾ��С��
	 * @param GroupId
	 * @param partId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/delGroupForm.do")
	public ModelAndView delGroup(String GroupId,String partId,HttpServletRequest req){
		String url = req.getHeader("REFERER");
		if(url.indexOf("/manage/")==-1){
			url = "/home.do";
		}else{
			url = url.substring(url.indexOf("/manage/"));
		}
		try {
			if(groupService.delGroup(Integer.parseInt(partId),Integer.parseInt(GroupId))){
				return JumpPrompt.jumpOfModelAndView(url, "ɾ���ɹ���");
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ����޷�ɾ�����г�Ա��С�������С�顱��");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ��Ӳ���
	 * @param newPartName
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/addPartForm.do")
	public ModelAndView addPart(String newPartName,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String userName = userServer.getUserNameById(jobId);
		if(partService.getPartByName(newPartName)!=-1){
			return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "��Ӳ��š�"+newPartName+"��ʧ�ܡ�����ʾ���ò����Ѵ��ڣ�");
		}
		try {
			if(partService.addPart(newPartName, userName)){
				return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "��Ӳ��š�"+newPartName+"���ɹ���");
			}else{
				return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "��Ӳ��š�"+newPartName+"��ʧ�ܡ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "��Ӳ��š�"+newPartName+"��ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ɾ������
	 * @param partId
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/delPartForm.do")
	public ModelAndView delPart(String partId,HttpServletRequest req){
		try {
			if(partService.getMemberOfPartNumbers(Integer.parseInt(partId))!=0){
				return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "ɾ��ʧ�ܡ����޷�ɾ������Ա��С�飩");
			}
			if(partService.delPart(Integer.parseInt(partId))){
				return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "ɾ���ɹ���");
			}else{
				return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "ɾ��ʧ�ܡ����޷�ɾ������С��ͳ�Ա�Ĳ��ţ�����Ĭ��С�飩");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/manage/groupManagerPart.do", "ɾ��ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ��������û�
	 * @param req
	 * @return
	 */
	@RequestMapping("manage/addUsersFrom.do")
	public ModelAndView addUser(HttpServletRequest req){
		int all = Integer.parseInt(req.getParameter("userNumberAll"));//��Ҫ��ӵĸ���
		int end = Integer.parseInt(req.getParameter("userNumberEnd"));//ѭ�����ҵĽ���
		if(all!=0){
			List<UserInfo> users = new ArrayList<UserInfo>();
			for(int i=1;i<=end;i++){
				String jobId = req.getParameter("jobId"+i);
				if(jobId!=null && jobId.trim().length()!=0){
					//ȷ�������ݣ���ȡ
					UserInfo user = new UserInfo();
					user.setJobId(jobId.trim());
					user.setCardId(req.getParameter("cardId"+i));
					user.setName(req.getParameter("name"+i));
					if(req.getParameter("sex"+i).equals("male")){
						user.setSex(UserInfo.SEX_MALE);
					}else{
						user.setSex(UserInfo.SEX_FAMALE);
					}
					int partId = Integer.parseInt(req.getParameter("part"+i));
					user.setPart(partId);
					user.setGroup(groupService.getGroupByName(partId, "����С��"));//��С��
					user.setKind(UserInfo.KIND_MEMBER); //���Ȩ��
					user.setStatus(UserInfo.STATUS_NO_ACTIVITY);
					users.add(user);
//					System.out.println("Action:("+i+")"+user.getKind());
					//�������ˣ��˳�ѭ��
					if(users.size()==all){
						break;
					}
				}
			}
			//���������û�
			boolean creatRes;
			try {
				creatRes = userServer.createNewUsers(users);
			} catch (Exception e) {
				e.printStackTrace();
				return JumpPrompt.jumpOfModelAndView("/manage/addUser.do", "��������ʧ�ܡ����������쳣��");
			}
			if(creatRes){
				//���������ɹ�
//				System.out.println("���������ɹ�");
				return JumpPrompt.jumpOfModelAndView("/manage/addUser.do", "�ɹ���������"+users.size()+"���û���");
			}else{
				//��������ʧ��
//				System.out.println("��������ʧ��");
				return JumpPrompt.jumpOfModelAndView("/manage/addUser.do", "��������ʧ�ܡ�");
			}
		}
		//û�ж���
		return JumpPrompt.jumpOfModelAndView("/manage/addUser.do", "û�д���ӵ��û���Ϣ��");
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
	
	

}
