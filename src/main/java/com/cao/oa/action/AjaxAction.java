package com.cao.oa.action;

import com.cao.oa.bean.UserInfo;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.CodeOfLogin;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	
	private SimpleDateFormat sdf;
	
	public AjaxAction(){
		super();
		sdf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
	}
	
	/**
	 * ��ȡ��֤��
	 * ��codeStr����֤���ַ���
	 * @param req
	 * @return ��֤�����ڵ�·��
	 */
	@RequestMapping(value="ajax/getLoginCodeAjax.do")
	@ResponseBody
	public String getLoginCode(HttpServletRequest req){
		HttpSession session = req.getSession();
		String codeStr = "";//��֤���
		String codeCon = "";//��֤������
		String path = "img/loginCode";//��֤��·��
		
		ServletContext application = req.getServletContext();
		String realPath = application.getRealPath("img/loginCode");
		String fileName = System.currentTimeMillis()+".jpg";
		File file = new File(realPath+File.separator+fileName);//File.separator
		int num1 = (int)(Math.random()*10);
		int num2 = (int)(Math.random()*10);
		int fu = (int)(System.currentTimeMillis()%2);
		if(fu==1){
			codeStr = (num1+num2)+"";
			codeCon = num1+"+"+num2;
		}else{
			codeStr = (num1-num2)+"";
			codeCon = num1+"-"+num2;
		}
		codeCon += "=?";
		try {
			CodeOfLogin.outputImage(600,200,file,codeCon);
		} catch (IOException e) {
			e.printStackTrace();
			return "error,jpg";
		}
		String name = fileName;//��֤���ļ���
		session.setAttribute("codeStr", codeStr);
		return path+"/"+name;
	}
	
	/**
	 * ��ȡ�û������б�
	 * @return Map<Integer, String>���͵��б�
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> getUserKinds(HttpServletRequest request){
		return (Map<Integer, String>)request.getServletContext().getAttribute("app_userKindMap");
	}
	
	/**
	 * �첽����ȡĳ���ŵ�ȫ����Ա���ֺ�JobId
	 * @param partId
	 * @param groupId
	 * @return 
	 */
	@RequestMapping(value="ajax/getShenUserAjax.do")
	@ResponseBody
	public String getShenUser(String partId,String groupId){
		String res = "";
		if(partId!=null && partId.length()!=0){
			if(groupId!=null && groupId.length()!=0){
				List<Map<String,Object>> list = userServer.getAllUserNameAndJobIdOfGroup(Integer.parseInt(partId),Integer.parseInt(groupId));
				if(list!=null && list.size()!=0){
					for(int i=0;i<list.size();i++){
						if(i!=0){
							res += ",";
						}
						res += (String)list.get(i).get("jobId");
						res += ":";
						int userKind = (int)list.get(i).get("kind");
						switch(userKind){
							case UserInfo.KIND_MANAGER_WEB:
								res += (String)list.get(i).get("name")+"����վ����Ա��";
								break;
							case UserInfo.KIND_MANAGER_PART:
								res += (String)list.get(i).get("name")+"�����Ź���Ա��";
								break;
							case UserInfo.KIND_MANAGER_GROUP:
								res += (String)list.get(i).get("name")+"��С�����Ա��";
								break;
							default:
								res += (String)list.get(i).get("name")+"��";
								break;
						}
						if(list.get(i).get("post")==null){
							res += "����ְ��";
						}else{
							res += (String)list.get(i).get("post")+"��";
						}
						
					}
				}
			}
		}
		return res;
	}
	
	/**
	 * �첽��jobId�Ƿ��ظ�
	 * @param jobId ��Ҫ���ҵ��û�jobId
	 * @return ������ݿ����оͷ��ء�yes��������Ϊ��no��
	 */
	@RequestMapping(value="ajax/hasUserAjax.do")
	@ResponseBody
	public String hasUser(String jobId){
		String res = "no";
		if(jobId!=null){
			if(userServer.hasUserByJobId(jobId)){
				res = "yes";
			}
		}
		return res;
	}
	
	/**
	 * �첽��jobId�Ƿ��ظ��� ��ȡ�����б�
	 * �ظ��ͷ��ء�yes�������ظ��򷵻����в��ŵ�id�����֣���ʽΪ����id1:name1,id2:name2��
	 * @param jobId �������jobId
	 * @param response
	 * @return
	 */
	@RequestMapping("ajax/getPartsAjax.do")
	@ResponseBody
	public String getParts(String jobId,HttpServletResponse response)  {
		String res = "";
		if(jobId!=null){
			if(userServer.hasUserByJobId(jobId)){
				return "yes";
			}
		}else{
			//û�л��
		}
		res = getAllPartsToStr();
		return res;
	}
	
	/**
	 * ��ȡ���в��ŵ�id������
	 * @return
	 */
	@RequestMapping("ajax/getAllPartsAjax.do")
	@ResponseBody
	public String getAllParts()  {
		String res = "";
		res = getAllPartsToStr();
		return res;
	}
	
	/**
	 * ��ȡ�����б��������в��ŵ�id�����֣���ʽΪ����id1:name1,id2:name2��
	 * @return
	 */
	private String getAllPartsToStr(){
		//��ȡ���ֺ�id
		String res = "";
		List<Map<String, Object>> list = partService.getAllPartsAndNames();
		if(list==null || list.size()==0){
			return res;
		}
		//�������ݸ�ʽ
		for(int i=0;i<list.size();i++){
			if(i!=0){
				res += ",";
			}
			res += list.get(i).get("id");
			res += ":"+list.get(i).get("name");
		}
		return res;
	}
	
	/**
	 * ��ȡĳ�����ŵ�ȫ��С���ID��������
	 * @param partId ĳ�����ŵ�ID
	 * @param response
	 * @return
	 */
	@RequestMapping("ajax/getGroupAjax.do")
	@ResponseBody
	public String getGroups(String partId,HttpServletResponse response)  {
		String res = "";
		//��ȡ���ֺ�id
		List<Map<String, Object>> list = groupService.getAllGroupsOfPartNameAndId(Integer.parseInt(partId));
		if(list==null || list.size()==0){
			return res;
		}
		//�������ݸ�ʽ
		for(int i=0;i<list.size();i++){
			if(i!=0){
				res += ",";
			}
			res += list.get(i).get("id");
			res += ":"+list.get(i).get("name");
		}
		return res;
	}
	
	/**
	 * �첽����ȡ�û��Ļ�����Ϣ��������jobId��cardId��name�ͻ�ȡ���Ƿ���Ը��ı���ȡ��Ȩ�ޡ�
	 * Ȩ���Ƿ�Ϊ��true���͡�false��
	 * @param jobId
	 * @param req
	 * @return
	 */
	@RequestMapping("ajax/getUserBaseInfoAjax.do")
	@ResponseBody
	public String getUserBaseInfo(String jobId,HttpServletRequest req)  {
		String returnStr = "";
		Map<String, Object> map = userServer.getPersonInfoAllByJobId(jobId);
		System.out.println(map);
		if(map!=null){
			returnStr +=  "jobId:"+(String)map.get("jobId");
			returnStr += ",cardId:"+(String)map.get("cardId");
			returnStr += ",name:"+(String)map.get("name");
			if(map.get("post")!=null){
				returnStr += ",post:"+(String)map.get("post");
			}
			if((int)map.get("kind")>userServer.getUserKindByJobId((String)req.getSession().getAttribute("userJobId"))){
				returnStr += ",canShen:true";
			}else{
				returnStr += ",canShen:false";
			}
			System.out.println(returnStr);
		}
		return returnStr;
	}
	
	/**
	 * �첽����ȡĳ���û����ڵĲ��Ų�����ȫ������
	 * ���û����ڵ�С�鲢����ȫ���������С��
	 * ���û���Ȩ�޺�ȫ��Ȩ��
	 * @param jobId
	 * @param kind
	 * @param req
	 * @return
	 */
	@RequestMapping("ajax/getUserPartOrGroupAjax.do")
	@ResponseBody
	public String getUserPartOrGroup(String jobId,String kind,HttpServletRequest req){
		String returnStr = "";
		String userJobId = (String)req.getSession().getAttribute("userJobId");
		int partId = userServer.getUserPartByJobId(jobId);
		if(kind.equals("part")){
			returnStr += partId+":"+partService.getNameById(partId);
			if(userServer.getUserKindByJobId(userJobId)==UserInfo.KIND_MANAGER_WEB){
				//��վ����Ա���Ըı䲿��
				List<Map<String, Object>> list = partService.getAllPartsAndNames();
//				System.out.println(list);
				for(Map<String, Object> m:list){
					returnStr += ","+(int)m.get("id")+":"+(String)m.get("name");
				}
			}else{
				//�����Ըı�
			}
		}else if(kind.equals("group")){
			int groupId = userServer.getUserGroupByJobId(jobId);
			returnStr += groupId+":"+groupService.getNameById(partId, groupId);
			if(userServer.getUserKindByJobId(userJobId)==UserInfo.KIND_MANAGER_WEB ||
				(userServer.getUserKindByJobId(userJobId)==UserInfo.KIND_MANAGER_PART && userServer.getUserPartByJobId(userJobId)==partId)){
				//��վ����Ա���Ըı�С��
				//�����ŵĲ��Ź���ԱҲ���Ըı�С��
				List<Map<String, Object>> list = groupService.getAllGroupsOfPartNameAndId(partId);
//				System.out.println(list);
				for(Map<String, Object> m:list){
					returnStr += ","+(int)m.get("id")+":"+(String)m.get("name");
				}
			}else{
				//�����Ըı�
			}
		}else if(kind.equals("shen")){
			int userKind = userServer.getUserKindByJobId(jobId);
			returnStr += userKind+":";
			switch(userKind){
			case UserInfo.KIND_MANAGER_WEB:
				returnStr +="��վ����Ա";
				break;
			case UserInfo.KIND_MANAGER_PART:
				returnStr +="���Ź���Ա";
				break;
			case UserInfo.KIND_MANAGER_GROUP:
				returnStr +="С�����Ա";
				break;
			case UserInfo.KIND_MEMBER:
				returnStr +="��ͨ��Ա";
				break;
			}
			Map<Integer, String> map = getUserKinds(req);
			Iterator<Integer> it = map.keySet().iterator();
			while(it.hasNext()){
				int tempNum = it.next();
				returnStr += ","+tempNum+":"+map.get(tempNum);
			}
		}
		return returnStr;
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
