package com.cao.oa.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cao.oa.bean.UserInfo;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.UserKindService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.JumpPrompt;

@Controller
public class UserCenter {
	@Autowired
	private UserService userServer;
	@Autowired
	private UserKindService userKindService;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	
	/**
	 * ���޸ĸ�������ҳ��
	 * @return
	 */
	@RequestMapping("/changePassword.do")
	public ModelAndView viewChangePassword(){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","user/changePassword.jsp");
		model.put("myPageTitle","�޸ĸ�������");
		model.put("myPageNav","10");
		
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ��������Ϣҳ��
	 * @param req
	 * @return
	 */
	@RequestMapping("/personInfo.do")
	public ModelAndView viewInfo(HttpServletRequest req){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","user/persionInfo.jsp");
		model.put("myPageTitle","������Ϣ");
		model.put("myPageNav","11");
		
		HttpSession session = req.getSession();
		Map<String,Object> infoMap = userServer.getPersonInfoAllByJobId((String)session.getAttribute("userJobId"));
		if(infoMap==null){
			return JumpPrompt.jumpOfModelAndView("/home.do", "��ȡ������Ϣ����/(��o��)/~~/(��o��)/~~<br/>"
					+ "��������ϵ����Ա��");
		}
		String tsex = "";
		if((int)infoMap.get("sex") == UserInfo.SEX_MALE){
			tsex = "��";
		}else{
			tsex = "Ů";
		}
		int groupId = (int)infoMap.get("ggroup");
		int partId = (int)infoMap.get("part");
		//��ȡС���� groupService
		String groupName = groupService.getNameById(partId, groupId);
		//��ȡ������ partService
		String partName = partService.getNameById(partId);
		//��ȡ������
		Map<String,Object> res = userServer.getOtherInfoByJobId((String)infoMap.get("jobId"));
		
		model.put("piName", (String)infoMap.get("name"));
		model.put("piSex", tsex);
		model.put("piJobId", (String)infoMap.get("jobId"));
		model.put("piCardId", (String)infoMap.get("cardId"));
		model.put("piPart", partName);
		model.put("piGroup", groupName);
		if(infoMap.get("post")!=null){
			model.put("piPost", (String)infoMap.get("post"));
		}
		if(res!=null){
			model.put("piTel", (String)res.get("tel"));
			model.put("piEmail", (String)res.get("email"));
			model.put("piAddr", (String)res.get("addr"));
		}
		
		return new ModelAndView("baseJsp",model);
	}
	
	
	/**
	 * �Լ��޸��Լ��ĸ�����Ϣ
	 * @param tel
	 * @param email
	 * @param addr
	 * @param req
	 * @return
	 */
	@RequestMapping("/changePersonInfoForm.do")
	public ModelAndView changeInfo(String tel,String email,String addr,
			HttpServletRequest req){
		boolean res = false;
		String baseContent = null;
		String baseUrl = "/personInfo.do";
		String jobId = (String)req.getSession().getAttribute("userJobId");
		try {
			res = userServer.changeMyPersonInfoByJobId(jobId, tel, email, addr);
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(baseUrl, "�޸ĸ�����Ϣʧ�ܡ�/(��o��)/~~");
		}
		//������
		if(!res){
			baseContent = "�޸ĸ�����Ϣʧ�ܡ�/(��o��)/~~";
		}else{
			baseContent = "�޸ĸ�����Ϣ�ɹ���";
		}
		return JumpPrompt.jumpOfModelAndView(baseUrl, baseContent);
	}
	
	/**
	 * �޸�����
	 * @param oldPassword
	 * @param newPassword
	 * @param newPassword2
	 * @param req
	 * @return
	 */
	@RequestMapping("/changeMyPasswordForm.do")
	public ModelAndView changePwd(String oldPassword,String newPassword,String newPassword2,
			HttpServletRequest req){
		boolean res = false;
		String baseContent = null;
		String baseUrl = "/changePassword.do";
		if(newPassword.equals(newPassword2)){
			try {
				res = userServer.changeMyPassword((String)req.getSession().getAttribute("userJobId"), oldPassword, newPassword);
			} catch (Exception e) {
				e.printStackTrace();
				return JumpPrompt.jumpOfModelAndView(baseUrl, "�޸�����ʧ�ܡ�/(��o��)/~~");
			}
		}
		//������
		if(!res){
			baseContent = "�޸�����ʧ�ܡ�/(��o��)/~~";
		}else{
			baseContent = "�޸�����ɹ���";
		}
		return JumpPrompt.jumpOfModelAndView(baseUrl, baseContent);
	}
	
	
	
	
	public UserService getUserServer() {
		return userServer;
	}
	public void setUserServer(UserService userServer) {
		this.userServer = userServer;
	}
	public UserKindService getUserKindService() {
		return userKindService;
	}
	public void setUserKindService(UserKindService userKindService) {
		this.userKindService = userKindService;
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
	
}
