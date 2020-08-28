package com.cao.oa.action;

import com.cao.oa.bean.UserInfo;
import com.cao.oa.service.RemindService;
import com.cao.oa.service.UserKindService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.JumpPrompt;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * ��¼���˳�����
 * @author DELL
 *
 */
@Controller
public class LogAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private UserKindService userKindService;
	@Autowired
	private RemindService remindService;

	public UserService getUserServer() {
		return userServer;
	}

	public void setUserServer(UserService userServer) {
		this.userServer = userServer;
	}
	
	/**
	 * Ĭ�Ϸ���ҳ�棬����¼ҳ��
	 * @return
	 */
	@RequestMapping("/welcome.do")
	public String viewWelcome(HttpServletRequest req){
		HttpSession session = req.getSession();
		if(session!=null && session.getAttribute("userJobId")!=null){
			//�����¼��
			return "redirect:/home.do";
		}else{
			//û�е�¼��
			return "login";
		}
		
	}
	
	/**
	 * ����������ҳ��
	 * @return
	 */
	@RequestMapping("/forgetPassword.do")
	public String viewForgetPassword(){
		return "forgetPassword";
	}
	
	/**
	 * ��¼�ɹ��󿴵���ҳ��
	 * @param
	 */
	@RequestMapping("/home.do")
	public ModelAndView viewHome(HttpServletRequest req){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","home.jsp");
		model.put("myPageTitle","��ӭ");
		model.put("myPageNav","-1");
		return new ModelAndView("baseJsp",model);
	}

	/**
	 * ��¼����
	 * @param username
	 * @param password
	 * @param code
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping("/loginForm.do")
	public ModelAndView loginCheck(String username,String password,String code,
			HttpServletRequest request,HttpServletResponse res){
		String codeStr = (String)request.getSession().getAttribute("codeStr");
		if(codeStr==null){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "���Ȼ�ȡ��֤��");
		}else{
			if(!codeStr.equals(code)){
				return JumpPrompt.jumpOfModelAndView("/welcome.do", "��֤����󣬵�¼ʧ�ܣ�");
			}
		}
		int status = userServer.getUserStatusByJobId(username);
		if(status==-1){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "�û�����������󣬵�¼ʧ�ܣ�");
		}else if(status== UserInfo.STATUS_NO_ACTIVITY){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ��˻�δ�����������һ�����������м����˻���");
		}else if(status==UserInfo.STATUS_DISABLE ){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ��˻������ã�����ϵ����Ա��");
		}else if(status==UserInfo.STATUS_ABNORMAL ){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ��˻��쳣����ֹ��½������ϵ����Ա��");
		}else if(status==UserInfo.STATUS_FROZEN_15_MINUTE ){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ����ڶ��������������˻�������15���ӣ�15���Ӻ�������µ�¼������������¼������ϵ����Ա��");
		}else if(status==UserInfo.STATUS_FROZEN_30_MINUTE ){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ����ڱ�����15���Ӻ��ٴζ��������������˻�������30���ӣ�30���Ӻ�������µ�¼������������¼������ϵ����Ա��");
		}else if(status==UserInfo.STATUS_FROZEN_24_HOUR ){
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ����ڱ�����30���Ӻ��ٴζ��������������˻�������24Сʱ��24Сʱ��������µ�¼������������¼������ϵ����Ա��");
		}
		UserInfo info = userServer.checkLogin(username, password);
		if(info!=null){
			//��½�ɹ�
			request.getSession().removeAttribute("codeStr");
			try {
				userServer.changeUserPasswordErrorTimes(info.getJobId(),0);
			} catch (Exception e) {
				e.printStackTrace();
				return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ����������쳣��");
			}
			HttpSession session = request.getSession();
			session.setAttribute("userKindName", userKindService.getNameById(info.getKind()));
			session.setAttribute("userName", info.getName());
			session.setAttribute("userKind", info.getKind());
			session.setAttribute("userpart", info.getPart());
			session.setAttribute("usergroup", info.getGroup());
			session.setAttribute("userJobId", info.getJobId());
			session.setAttribute("userCardId", info.getCardId());
			session.setAttribute("userSex", info.getSex());
			
			int msg = remindService.getRemindMsgNumberById(info.getJobId());
//			System.out.println("----"+info.getJobId());
			int procedure = remindService.getRemindProcedureNumberById(info.getJobId());
			int notice = remindService.getRemindNoticeNumberById(info.getJobId());
//			System.out.println("msg:"+msg);
//			System.out.println("procedure:"+procedure);
//			System.out.println("notice:"+notice);
			session.setAttribute("myPageNeedDeal", procedure);
			session.setAttribute("myPageNotice", notice);
			session.setAttribute("myPageMessage", msg);
			session.setAttribute("myPageMessagePrompt", msg+procedure+notice);
			return new ModelAndView("redirect:/home.do");
		}else{
			//��¼ʧ��
			//��ȡ����������
			int times = userServer.getUserPasawordErrorTimes(username);
			times++;
			//�����𶳽�
			try{
				if(times>=9){
					userServer.changeUserStatusByJobId(username, UserInfo.STATUS_FROZEN_24_HOUR);
				}else if(times>=6){
					userServer.changeUserStatusByJobId(username, UserInfo.STATUS_FROZEN_30_MINUTE);
				}else if(times>=3){
					userServer.changeUserStatusByJobId(username, UserInfo.STATUS_FROZEN_15_MINUTE);
				}
				//����������
				userServer.changeUserPasswordErrorTimes(username,times);
			} catch (Exception e) {
				e.printStackTrace();
				return JumpPrompt.jumpOfModelAndView("/welcome.do", "��¼ʧ�ܣ����������쳣02��");
			}
			return JumpPrompt.jumpOfModelAndView("/welcome.do", "�û�����������󣬵�¼ʧ�ܣ����������������"+times+"�Σ�");
		}
	}
	

	/**
	 * �����������
	 * @param jobId
	 * @param cardId
	 * @param username
	 * @param newPassword1
	 * @param newPassword2
	 * @return
	 */
	@RequestMapping("/forgetPasswordForm.do")
	public ModelAndView forgetPassword(String jobId,String cardId,String username,
			String newPassword1,String newPassword2){
		if(jobId!=null){
			//�д���
			if(newPassword1.equals(newPassword2)){
				//����������ͬ
				boolean res;
				try {
					res = userServer.forgetPassword(jobId, cardId, username, newPassword1);
				} catch (Exception e) {
					e.printStackTrace();
					return JumpPrompt.jumpOfModelAndView("/forgetPassword.do", "��������ʧ�ܣ����������쳣��");
				}
				if(res){
					//�޸ĳɹ�
					return new ModelAndView("redirect:/welcome.do");
				}
			}
		}
		return JumpPrompt.jumpOfModelAndView("/forgetPassword.do", "��������ʧ�ܣ�");
	}
	
	/**
	 * ע��
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:/welcome.do";
	}
	
	/**
	 * ˢ����ʾ������
	 * @param req
	 * @return
	 */
	@RequestMapping("/refreshRemind.do")
	@ResponseBody
	public String refreshRemindNumber(HttpServletRequest req){
		HttpSession session = req.getSession();
		int msg = remindService.getRemindMsgNumberById((String)session.getAttribute("userJobId"));
		int procedure = remindService.getRemindProcedureNumberById((String)session.getAttribute("userJobId"));
		String res = "msg:"+msg+",procedure:"+procedure+",all:"+(procedure+msg);
		return res;
	}
}
