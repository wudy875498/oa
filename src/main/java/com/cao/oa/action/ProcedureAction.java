package com.cao.oa.action;

import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.ProcedureService;
import com.cao.oa.service.UserService;
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
import com.cao.oa.bean.ModelOption;
import com.cao.oa.bean.ModelProcedure;
import com.cao.oa.bean.ModelShen;
import com.cao.oa.bean.ProcedureOption;
import com.cao.oa.bean.ProcedureShen;
import com.cao.oa.bean.ProcedureSubmit;
import com.cao.oa.bean.UserInfo;
import com.cao.oa.service.GroupService;
import com.cao.oa.service.PartService;
import com.cao.oa.service.ProcedureService;
import com.cao.oa.service.UserService;
import com.cao.oa.util.DownWord;
import com.cao.oa.util.JumpPrompt;

@RequestMapping("procedure/")
@Controller
public class ProcedureAction {
	@Autowired
	private UserService userServer;
	@Autowired
	private PartService partService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ProcedureService procedureService;
	
	private SimpleDateFormat sdf;
	private String filePath = "upload/file/procedure";
	
	public ProcedureAction(){
		super();
		sdf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
	}
	
	/**
	 * ���鿴ģ�����
	 * @param modelId
	 * @param req
	 * @return
	 */
	@RequestMapping("modelLook.do")
	public ModelAndView viewModelLook(String modelId,HttpServletRequest req){
		if(modelId==null || modelId.length()==0){
			String url = req.getHeader("REFERER");
			url = url.substring(url.indexOf("/procedure/"));
			return JumpPrompt.jumpOfModelAndView(url, "�ύ��������");
		}
		ModelProcedure modelP = procedureService.getModelInfoAllById(Integer.parseInt(modelId));
		
		String title = modelP.getTitle();//�����ͷ
		String produceName = modelP.getProjectName();//��Ŀ����
		String illustrate = modelP.getIllustrate();//˵��
		int enclosure = modelP.getEnclosure();//�Ƿ��и���
		Map<Integer,Map<String,Object>> blankMap = null;//С��  Map<ѡ���˳��,Map<key,value>>

		//����
		ModelOption[] mp = modelP.getOption();
		blankMap = new HashMap<Integer,Map<String,Object>>();
		if(mp!=null){
			for(int i=0;i<mp.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", mp[i].getName());
				if(mp[i].getMust()==1){
					map.put("must", true);
				}else{
					map.put("must", false);
				}
				
				map.put("id", mp[i].getId());
				blankMap.put((i+1), map);
			}
		}
		//�����б�
		ModelShen[] ss = modelP.getShen();
		Map<Integer,Map<String,Object>> processMap = new HashMap<>();
//		System.out.println(ss.length);
		for(int i=0;i<ss.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			UserInfo ui = userServer.getUserInfoByJobId(ss[i].getPerson());
			String showStr = ss[i].getName()+"��"+ui.getName()+"��";
			if(ui.getPost()==null){
				showStr += "��ְ��";
			}else{
				showStr += ui.getPost()+"��";
			}
			showStr += partService.getNameById(ui.getPart())+"��";
			showStr += groupService.getNameById(ui.getPart(), ui.getGroup())+"��";
			map.put("key", showStr);
			map.put("color", "orange");
			processMap.put(ss[i].getOrder(), map);
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureModelLook.jsp");
		model.put("myPageTitle","�鿴���̱�");
		model.put("myPageNav","3");
		
		model.put("pseModelProcessMap", processMap);
		model.put("pseModelId", modelId);//�����ͷ
		model.put("pseTitle", title);//�����ͷ
		model.put("pseProduceName", produceName);//��Ŀ����
		model.put("pseIllustrate", illustrate);//˵��
		model.put("pseEnclosure", enclosure);//�Ƿ��и���
		model.put("pseBlankMap", blankMap);//С��   Map<ѡ���˳��,Map<key,value>>
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ��Ҫ�����б�ҳ��
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("needToDealList.do")
	public ModelAndView viewNeedToDealList(String page,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		
		List<Map<String,String>> procedureList = null;//һҳ5��
		int allPage = 0;
		int currentPage = 0;
		
		//����ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//��ҳ��
		allPage = procedureService.getAllNeedToDealListPage(jobId);
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		
		List<Map<String,Object>> tempList = procedureService.getAllNeedToDealListByPage(jobId,currentPage);
		if(tempList!=null){
			procedureList = new ArrayList<Map<String,String>>();
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", ""+(int)tempList.get(i).get("id"));//���
				map.put("name", (String)tempList.get(i).get("name"));//��������
				map.put("produceName", (String)tempList.get(i).get("projectName"));//��Ŀ����
				map.put("createDate", sdf.format((Date)tempList.get(i).get("createDate")));//����ʱ��
				map.put("createPerson", userServer.getUserNameById((String)tempList.get(i).get("createPerson")));//������
				map.put("updateDate", sdf.format((Date)tempList.get(i).get("updateDate")));//����ʱ��
				procedureList.add(map);
			}
		}
		
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureDealList.jsp");
		model.put("myPageTitle","�账������̡����鿴�б�");
		model.put("myPageNav","4");
		
		model.put("pdlProcedureList", procedureList);//�б�
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * �����ύ�б�ҳ�� 
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("submitList.do")
	public ModelAndView viewSubmitList(String page,HttpServletRequest req){
		List<Map<String,String>> procedureModelList = null;//һҳ5��
		int allPage = 0;
		int currentPage = 0;
		
		
		//����ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//��ҳ��
		allPage = procedureService.getAllModelPage();
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}

		//��ȡ�б�
		List<Map<String,Object>> temp = procedureService.getAllModelByPage(currentPage);
		//��������
		procedureModelList = new ArrayList<Map<String,String>>();
		for(int i=0;i<temp.size();i++){
			Map<String,String> map = new HashMap<String,String>();
			String tempUser = (String)temp.get(i).get("createPerson");
//			System.out.println(tempUser);
			map.put("id", ""+(int)temp.get(i).get("id"));
			map.put("name", (String)temp.get(i).get("name"));
			map.put("introduction", (String)temp.get(i).get("introduction"));
			map.put("createDate", sdf.format((Date)temp.get(i).get("createDate")));
			map.put("createPerson", userServer.getUserNameById(tempUser));
			procedureModelList.add(map);
		}

		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureSubmitList.jsp");
		model.put("myPageTitle","�����ύ����ѡ���б�");
		model.put("myPageNav","1");
		
		model.put("procedureModelList", procedureModelList);
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * �ҵ������б����
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("myList.do")
	public ModelAndView viewMyList(String page,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		
		List<Map<String,String>> procedureList = null;//һҳ5��
		int allPage = 0;
		int currentPage = 0;
		
		//����ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//��ҳ��
		allPage = procedureService.getAllMyProcedurePage(jobId);
		if(currentPage>allPage && allPage!=0){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}
		
		List<Map<String, Object>> tempList = procedureService.getAllMyProcedureSimpleByPage(currentPage,jobId);
		procedureList = new ArrayList<Map<String,String>>();
		if(tempList!=null){
			for(int i=0;i<tempList.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", ""+(int)tempList.get(i).get("id"));//���
				map.put("name", (String)tempList.get(i).get("name"));//����
				map.put("produceName", (String)tempList.get(i).get("projectNameTitle"));//��Ŀ����
				map.put("createDate", sdf.format((Date)tempList.get(i).get("createDate")));//����ʱ��
				map.put("createPerson", userServer.getUserNameById((String)tempList.get(i).get("createPerson")));//������
				map.put("status", ""+(int)tempList.get(i).get("status"));//״̬
				procedureList.add(map);
			}
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/myProcedureList.jsp");
		model.put("myPageTitle","�ҵ����̡����б�");
		model.put("myPageNav","2");
		
		model.put("mplProcedureList", procedureList);
		model.put("allPage", allPage);
		model.put("currentPage", currentPage);
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ģ���б����
	 * @param page
	 * @param req
	 * @return
	 */
	@RequestMapping("modelList.do")
	public ModelAndView viewModelList(String page,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		int userKind = userServer.getUserKindByJobId(jobId);
		int allPage = 0;
		int currentPage = 0;
		boolean canAdd = true;
		List<Map<String,String>> tableList = null;//�������
		
		//��ͨ�û��޷����
		if(userKind==UserInfo.KIND_MEMBER){
			canAdd = false;
		}
		
		//����ҳ��
		if(page==null || page.length()==0){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(page);
		}
		//��ҳ��
		allPage = procedureService.getAllModelPage();
		if(currentPage>allPage){
			currentPage = allPage;
		}else if(currentPage<1){
			currentPage = 1;
		}

		//��ȡ�б�
		List<Map<String,Object>> temp = procedureService.getAllModelByPage(currentPage);
		//��������
		tableList = new ArrayList<Map<String,String>>();
		for(int i=0;i<temp.size();i++){
			Map<String,String> map = new HashMap<String,String>();
			String tempUser = (String)temp.get(i).get("createPerson");
//			System.out.println(tempUser);
			map.put("id", ""+(int)temp.get(i).get("id"));
			map.put("name", (String)temp.get(i).get("name"));
			map.put("introduction", (String)temp.get(i).get("introduction"));
			map.put("createDate", sdf.format((Date)temp.get(i).get("createDate")));
			map.put("createPerson", userServer.getUserNameById(tempUser));
			if(userServer.getUserKindByJobId(tempUser)>userKind){
				map.put("canChange", "1");
				map.put("canDel", "1");
			}else if(tempUser.equals(jobId)){
				map.put("canChange", "1");
				map.put("canDel", "1");
			}else{
				map.put("canChange", "0");
				map.put("canDel", "0");
			}
			
			tableList.add(map);
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureModel.jsp");
		model.put("myPageTitle","����ģ��");
		model.put("myPageNav","3");
		
		model.put("pmCanAdd", canAdd);//�Ƿ�������
		model.put("pmTableList", tableList);//�������
		model.put("allPage", allPage);//��ҳ��
		model.put("currentPage", currentPage);//��ǰҳ
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ģ��༭
	 * @param modelId
	 * @param req
	 * @return
	 */
	@RequestMapping("modelEdit.do")
	public ModelAndView viewModelEdit(String modelId,HttpServletRequest req){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureModelEdit.jsp");
		model.put("myPageTitle","����ģ��༭");
		model.put("myPageNav","3");
		
		boolean isNew = true;
		if(modelId==null || modelId.length()==0){
			//����µ�
			isNew = true;
		}else{
			//�༭���޸ľɵ�
			isNew = false;
			ModelProcedure mp = procedureService.getModelInfoAllById(Integer.parseInt(modelId));
			
			if(mp==null){
				String url = req.getHeader("REFERER");
				url = url.substring(url.indexOf("/procedure/"));
				return JumpPrompt.jumpOfModelAndView(url, "û��IDΪ��"+modelId+"����ģ�壡");
			}
			String name = mp.getName();
			String introduction = mp.getIntroduction();
			String illustrate = mp.getIllustrate();
			String remark = mp.getRemark();
			String title = mp.getTitle();
			String produceName = mp.getProjectName();
			int enclosure = mp.getEnclosure();//�ϴ�����
			Map<Integer,Map<String,Object>> blankMap = null;
			Map<Integer,Map<String,Object>> processMap = null;
			
			blankMap = new HashMap<Integer,Map<String,Object>>();
			processMap = new HashMap<Integer,Map<String,Object>>();
			//��ѡ���б�
			ModelOption[] opts = mp.getOption();
			for(int i=0;i<opts.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", opts[i].getName());
				if(opts[i].getMust()==0){
					map.put("must", false);
				}else{
					map.put("must", true);
				}
				blankMap.put(opts[i].getOrder(), map);
			}
			//�����б�
			ModelShen[] ss = mp.getShen();
//			System.out.println(ss.length);
			for(int i=0;i<ss.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", ss[i].getName());
				map.put("part", ss[i].getPart());
				map.put("partName", partService.getNameById(ss[i].getPart()) );
				map.put("group", ss[i].getGroup());
				map.put("groupName", groupService.getNameById(ss[i].getPart(), ss[i].getGroup()));
				map.put("personId", ss[i].getPerson());
				map.put("personName", userServer.getUserNameById(ss[i].getPerson()));
				processMap.put(ss[i].getOrder(), map);
			}

			model.put("pmeModelId", modelId);
			model.put("pmeName", name);
			model.put("pmeIntroduction", introduction);
			model.put("pmeIllustrate", illustrate);
			model.put("pmeRemark", remark);
			model.put("pmeTitle", title);
			model.put("pmeProduceName", produceName);
			model.put("pmeEnclosure", enclosure);
			model.put("pmeBlankMap", blankMap);//��ѡ���б�
			model.put("pmeProcessMap", processMap);//�����б�
		}
		model.put("pmeIsNew", isNew);//�Ƿ����µ�
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ���ύ������
	 * @param submitId
	 * @param req
	 * @return
	 */
	@RequestMapping("mySubmitDetail.do")
	public ModelAndView viewMySumbitDetail(String submitId,HttpServletRequest req){
		String backUrl = req.getHeader("REFERER");
		backUrl = backUrl.substring(backUrl.indexOf("/procedure/"));
		if(submitId==null || submitId.length()==0){
			return JumpPrompt.jumpOfModelAndView("/home.do", "�鿴ʧ�ܡ�����������");
		}
		
		ProcedureSubmit pSubmit = null;
		pSubmit = procedureService.getMySubmitAllInfoById(Integer.parseInt(submitId));
		if(pSubmit==null){
			return JumpPrompt.jumpOfModelAndView("/procedure/myList.do", "�鿴ʧ�ܡ���û�ҵ���Ӧ�����̣�");
		}
		
		Map<Integer,Map<String,String>> tblankMap = null;
		Map<Integer,Map<String,Object>> tprocedureMap = null;
		
		String tname = pSubmit.getName() + "����";//XXXXX���̱�
		String tproduceNameTitle = pSubmit.getProjectName();//��Ŀ����
		String tproduceName = pSubmit.getProjectNameTitle();//��Ŀ��������
		String tidStr = "A"+pSubmit.getId();//���
		String tid = ""+pSubmit.getId();//���
		String tcreatePerson = userServer.getUserNameById(pSubmit.getCreatePerson());//�ύ��
		String tcreateDate = sdf.format(pSubmit.getCreateDate());//����ʱ��
		String tremark = pSubmit.getRemark();//��ע
		String tshowFileName = pSubmit.getShowFileName();//�ϴ����ļ���
		String tfileName = pSubmit.getFileName();//�������ϵ��ļ���
		String tpart = pSubmit.getPartName();//��������
		String tgroup = pSubmit.getGroupName();//����С��
		int status = pSubmit.getStatus();

		//��ѡ
		ProcedureOption[] opts = pSubmit.getOpts();
		if(opts!=null){
			tblankMap = new HashMap<Integer,Map<String,String>>();
			for(int i=0;i<opts.length;i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("title", opts[i].getTitle());
				map.put("content", opts[i].getContent());
				tblankMap.put(opts[i].getOrder(), map);
			}
		}
		
		
		//����
		ProcedureShen[] shens = pSubmit.getShens();
//		System.out.println(shens);
		tprocedureMap = new HashMap<Integer,Map<String,Object>>();
		for(int i=0;i<shens.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", ""+shens[i].getId());
			map.put("title", shens[i].getName());
			map.put("content", shens[i].getContent());
			map.put("name", shens[i].getUserName());
			if(shens[i].getTime()!=null){
				map.put("time", sdf.format(shens[i].getTime()));
			}else{
				map.put("time", null);
			}
			if(shens[i].getWork()==ProcedureShen.WORK_OK){
				map.put("isWorked", "ok");
			}else if(shens[i].getWork()==ProcedureShen.WORK_PASS){
				map.put("isWorked", "pass");
			}else{
				map.put("isWorked", "no");
			}
			tprocedureMap.put(shens[i].getOrder(), map);
		}
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/myProcedureDetail.jsp");
		model.put("myPageTitle","�ҵ����̡����鿴");
		model.put("myPageNav","2");
		
		
		model.put("mpdBackUrl", backUrl);//��һ��·��
		model.put("mpdStatus", status);//����״̬
		model.put("mpdTname", tname);//XXXXX���̱�
		model.put("mpdProduceName", tproduceName);//��Ŀ����
		model.put("mpdProduceNameTitle", tproduceNameTitle);//��Ŀ��������
		model.put("mpdId", tid);//���
		model.put("mpdIdStr", tidStr);//��Ŵ�
		model.put("mpdCreatePerson", tcreatePerson);//�ύ��
		model.put("mpdCreateDate", tcreateDate);//����ʱ��
		model.put("mpdRemark", tremark);//��ע
		model.put("mpdShowFileName", tshowFileName);//�ϴ����ļ���
		model.put("mpdFileName", tfileName);//�������ϵ��ļ���
		model.put("mpdPart", tpart);//��������
		model.put("mpdGroup", tgroup);//����С��
		model.put("mpdBlankMap", tblankMap);//С���б�
		model.put("mpdProcedureMap", tprocedureMap);//�����б�
		return new ModelAndView("baseJsp",model);
	}
	
	/**
	 * ��Ҫ��������
	 * @param submitId
	 * @param req
	 * @return
	 */
	@RequestMapping("needToDealDetail.do")
	public ModelAndView viewNeedToDealDetail(String submitId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String backUrl = req.getHeader("REFERER");
		backUrl = backUrl.substring(backUrl.indexOf("/procedure/"));
		if(submitId==null || submitId.length()==0){
			return JumpPrompt.jumpOfModelAndView("/home.do", "�鿴ʧ�ܡ�����������");
		}
		
		ProcedureSubmit pSubmit = null;
		
		pSubmit = procedureService.getMySubmitAllInfoById(Integer.parseInt(submitId));
		if(pSubmit==null){
			return JumpPrompt.jumpOfModelAndView("/procedure/needToDealList.do", "�鿴ʧ�ܡ���û�ҵ���Ӧ�����̣�");
		}
		
		Map<Integer,Map<String,String>> tblankMap = null;
		Map<Integer,Map<String,Object>> tprocedureMap = null;
		
		String tname = pSubmit.getName() + "����";//XXXXX���̱�
		String tproduceName = pSubmit.getProjectName();//��Ŀ����
		String tproduceNameTitle = pSubmit.getProjectNameTitle();//��Ŀ��������
		String tid = "A"+pSubmit.getId();//���
		String tcreatePerson = userServer.getUserNameById(pSubmit.getCreatePerson());//�ύ��
		String tcreateDate = sdf.format(pSubmit.getCreateDate());//����ʱ��
		String tremark = pSubmit.getRemark();//��ע
		String tshowFileName = pSubmit.getShowFileName();//�ϴ����ļ���
		String tfileName = pSubmit.getFileName();//�������ϵ��ļ���
		String tpart = pSubmit.getPartName();//��������
		String tgroup = pSubmit.getGroupName();//����С��
		int status = pSubmit.getStatus();

		//��ѡ
		ProcedureOption[] opts = pSubmit.getOpts();
		if(opts!=null){
			tblankMap = new HashMap<Integer,Map<String,String>>();
			for(int i=0;i<opts.length;i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("title", opts[i].getTitle());
				map.put("content", opts[i].getContent());
				tblankMap.put(opts[i].getOrder(), map);
			}
		}
		
		//����
		ProcedureShen[] shens = pSubmit.getShens();
//		System.out.println(shens);
		tprocedureMap = new HashMap<Integer,Map<String,Object>>();
		for(int i=0;i<shens.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", ""+shens[i].getId());
			map.put("title", shens[i].getName());
			map.put("content", shens[i].getContent());
			map.put("name", shens[i].getUserName());
			if(shens[i].getTime()!=null){
				map.put("time", sdf.format(shens[i].getTime()));
			}else{
				map.put("time", null);
			}
			if(shens[i].getWork()==ProcedureShen.WORK_OK){
				map.put("isWorked", true);
			}else if(shens[i].getWork()==ProcedureShen.WORK_NEED || shens[i].getUserJobId().equals(jobId)){
				map.put("isWorked", false);
				map.put("needWork", true);
				map.put("spId", ""+shens[i].getId());
				map.put("proId", ""+shens[i].getProcedureId());
			}else{
				map.put("isWorked", false);
				map.put("needWork", false); 
			}
			tprocedureMap.put(shens[i].getOrder(), map);
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureDealEdit.jsp");
		model.put("myPageTitle","�账�����̡�������");
		model.put("myPageNav","4");
		
		model.put("pdeBackUrl", backUrl);//��һ��·��
		model.put("pdeStatus", status);//����״̬
		model.put("pdeTname", tname);//XXXXX���̱�
		model.put("pdeProduceName", tproduceName);//��Ŀ����
		model.put("pdeProduceNameTitle", tproduceNameTitle);//��Ŀ��������
		model.put("pdeId", tid);//���
		model.put("pdeCreatePerson", tcreatePerson);//�ύ��
		model.put("pdeCreateDate", tcreateDate);//����ʱ��
		model.put("pdeRemark", tremark);//��ע
		model.put("pdeShowFileName", tshowFileName);//�ϴ����ļ���
		model.put("pdeFileName", tfileName);//�������ϵ��ļ���
		model.put("pdePart", tpart);//��������
		model.put("pdeGroup", tgroup);//����С��
		model.put("pdeBlankMap", tblankMap);//С���б�
		model.put("pdeProcedureMap", tprocedureMap);//�����б�
		return new ModelAndView("baseJsp",model);
	}
	
	
	
	/**
	 * �µ������ύ��д�� 
	 * @param modelId
	 * @param req
	 * @return
	 */
	@RequestMapping("submitDetail.do")
	public ModelAndView viewSubmitDetail(String modelId,HttpServletRequest req){
		if(modelId==null || modelId.length()==0){
			String url = req.getHeader("REFERER");
			url = url.substring(url.indexOf("/procedure/"));
			return JumpPrompt.jumpOfModelAndView(url, "�ύ��������");
		}
		ModelProcedure modelP = procedureService.getModelInfoAllById(Integer.parseInt(modelId));
		
		String title = modelP.getTitle();//�����ͷ
		String produceName = modelP.getProjectName();//��Ŀ����
		String illustrate = modelP.getIllustrate();//˵��
		int enclosure = modelP.getEnclosure();//�Ƿ��и���
		Map<Integer,Map<String,Object>> blankMap = null;//С��  Map<ѡ���˳��,Map<key,value>>

		//����
		ModelOption[] mp = modelP.getOption();
		blankMap = new HashMap<Integer,Map<String,Object>>();
		if(mp!=null){
			for(int i=0;i<mp.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", mp[i].getName());
				if(mp[i].getMust()==1){
					map.put("must", true);
				}else{
					map.put("must", false);
				}
				
				map.put("id", mp[i].getId());
				blankMap.put((i+1), map);
			}
		}
		
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("myPageUrlName","procedure/procedureSubmitEdit.jsp");
		model.put("myPageTitle","��д���̱�");
		model.put("myPageNav","1");
		
		model.put("pseModelId", modelId);//�����ͷ
		model.put("pseTitle", title);//�����ͷ
		model.put("pseProduceName", produceName);//��Ŀ����
		model.put("pseIllustrate", illustrate);//˵��
		model.put("pseEnclosure", enclosure);//�Ƿ��и���
		model.put("pseBlankMap", blankMap);//С��   Map<ѡ���˳��,Map<key,value>>
		return new ModelAndView("baseJsp",model);
	}
	
	
	/**
	 * ��ӻ��޸�����ģ��
	 * @param req
	 * @return
	 */
	@RequestMapping("addNewModelForm.do")
	public ModelAndView addOrEditModel(HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		//��ȡ��һ����ַ
		String beforUrl = req.getHeader("REFERER");
		
		beforUrl = beforUrl.substring(beforUrl.indexOf("/procedure/"));
//		System.out.println("��һ��URI:"+beforUrl);
		int userKind = userServer.getUserKindByJobId(jobId);
		if(userKind==UserInfo.KIND_MEMBER){
			return JumpPrompt.jumpOfModelAndView("/home.do", "û��Ȩ�ޣ��޷�ʹ�á�");
		}
		
		
		ModelProcedure procedure = new ModelProcedure();
		ModelOption[] opts = null;
		ModelShen[] shens = null;
		
		String modeId = req.getParameter("modeId");
		boolean isNew = false;
		if(modeId.equals("-1")){
			isNew = true;
		}else{
			procedure.setId(Integer.parseInt(modeId));//����ID
		}

		String pname = req.getParameter("pname").trim();//��������
		if(pname.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ����������ơ�����Ϊ��");
		}
		procedure.setName(pname);
		String pdescribe = req.getParameter("pdescribe").trim();//���̼��
		if(pdescribe.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ������̼�顱����Ϊ��");
		}
		procedure.setIntroduction(pdescribe);
		String pillustration = req.getParameter("pillustration").trim();//��д˵��
		if(pillustration.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ�����д˵��������Ϊ��");
		}
		procedure.setIllustrate(pillustration);
		String premark = req.getParameter("premark").trim();//��д��ע
		if(premark.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ�����д��ע������Ϊ��");
		}
		procedure.setRemark(premark);
		String ptableTitle = req.getParameter("ptableTitle").trim();//�����ͷ
		if(ptableTitle.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ��������ͷ������Ϊ��");
		}
		procedure.setTitle(ptableTitle);
		String pprocedureTitle = req.getParameter("pprocedureTitle").trim();//��Ŀ����
		if(pprocedureTitle.length()==0){
			return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ�����Ŀ���ơ�����Ϊ��");
		}
		procedure.setProjectName(pprocedureTitle);
		String pattachmentNeed = req.getParameter("pattachmentNeed");//�ϴ���������תΪint��
		procedure.setEnclosure(Integer.parseInt(pattachmentNeed));
		procedure.setCreatePerson(jobId);//������ID
		
		int processNumber = Integer.parseInt(req.getParameter("processNumber").trim());//���̸���
		int blankNumber = Integer.parseInt(req.getParameter("blankNumber").trim());//��ѡ����
		int processNumberEnd = Integer.parseInt(req.getParameter("processNumberEnd").trim());//���̸�������
		int blankNumberEnd = Integer.parseInt(req.getParameter("blankNumberEnd").trim());//��ѡ����
		
		opts = new ModelOption[blankNumber];
		shens = new ModelShen[processNumber];
		
		int i = 1;
		int all = 0;
		//��ȡ��ѡ��
		while(i<=blankNumberEnd){
			String nameTemp = req.getParameter("pinputName"+i).trim();
			if(nameTemp.length()==0){
				return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ�����ѡ��"+(all+1)+"������Ϊ��");
			}
			if(nameTemp==null || nameTemp.length()==0){
				//������
				i++;
				continue;
			}
			all++;
			ModelOption o = new ModelOption();
			o.setName(nameTemp);
			o.setMust(Integer.parseInt(req.getParameter("pinputMust"+i).trim()));
			o.setOrder(all);
			opts[all-1] = o;//����
			if(all==blankNumber){
				//����
				break;
			}
			i++;
		}
		procedure.setOption(opts);
		
		i = 1;
		all = 0;
		//��ȡ����
		while(i<=processNumberEnd){
			String nameTemp = req.getParameter("pprocessName"+i).trim();
			if(nameTemp.length()==0){
				return JumpPrompt.jumpOfModelAndView(beforUrl, "����ʧ�ܡ�������"+(all+1)+"������Ϊ��");
			}
			if(nameTemp==null || nameTemp.length()==0){
				//������
				i++;
				continue;
			}
			all++;
			ModelShen s = new ModelShen();
			s.setName(nameTemp);
			s.setPart(Integer.parseInt(req.getParameter("ppart"+i).trim()));
			s.setGroup(Integer.parseInt(req.getParameter("pgroup"+i).trim()));
			s.setPerson(req.getParameter("pperson"+i).trim());
			s.setOrder(all);
			shens[all-1] = s;//����
			if(all==processNumber){
				//����
				break;
			}
			i++;
		}
		procedure.setShen(shens);
		
		try{
			if(isNew){
				//���д���
				if(procedureService.createNewProcedure(procedure)){
					return JumpPrompt.jumpOfModelAndView("/procedure/modelList.do", "�����ɹ���");
				}else{
					return JumpPrompt.jumpOfModelAndView("/procedure/modelList.do", "����ʧ�ܡ�");
				}
			}else{
				//�����޸�
				if(procedureService.updateProcedure(procedure)){
					return JumpPrompt.jumpOfModelAndView("/procedure/modelList.do", "�޸ĳɹ���");
				}else{
					return JumpPrompt.jumpOfModelAndView("/procedure/modelList.do", "�޸ĳɹ���");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/procedure/modelList.do", "����ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * ɾ��ģ��
	 * @param modelId
	 * @param req
	 * @return
	 */
	@RequestMapping("delModel.do")
	public ModelAndView delModel(String modelId,HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String url = req.getHeader("REFERER");
		url = url.substring(url.indexOf("/procedure/"));
		//��ȡ����
		String returnStr = "(��o��)Ŷ��";
		
		String create = procedureService.getUserOfProcedureWhoCreateById(Integer.parseInt(modelId));
		if(userServer.getUserKindByJobId(create)>userServer.getUserKindByJobId(jobId)){
			//��Ȩ��
		}else if(create.equals(jobId)){
			//�������Լ�
		}else{
			returnStr = "û��ɾ��Ȩ�ޡ�";
			return JumpPrompt.jumpOfModelAndView(url, returnStr);
		}
		if(modelId!=null && modelId.length()!=0){
			try {
				if(procedureService.delProcedureById(Integer.parseInt(modelId))){
					returnStr = "ɾ���ɹ���";
				}else{
					returnStr = "ɾ��ʧ�ܡ�";
				}
			} catch (Exception e) {
				e.printStackTrace();
				JumpPrompt.jumpOfModelAndView(url, "ɾ��ʧ�ܡ�");
			}
		}else{
			returnStr = "ɾ��ʱ��������Ϣ����";
		}
		return JumpPrompt.jumpOfModelAndView(url, returnStr);
	}
	
	
	/**
	 * ��ӻ��޸�����ģ��
	 * @param enclosure
	 * @param req
	 * @return
	 */
	@RequestMapping("addNewProcedureForm.do")
	public ModelAndView addNewProcedure(@RequestParam(value = "ptsmallFile") MultipartFile enclosure,
			HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String url = req.getHeader("REFERER");
		url = url.substring(url.indexOf("/procedure/"));
		
		if(enclosure!=null){
//			System.out.println(enclosure.getOriginalFilename());
//			System.out.println(enclosure.getSize());
		}else{
//			System.out.println("enclosure�ǿ�");
		}
		
		
		//������Ҫ�����
		ProcedureSubmit procedureSubmit = new ProcedureSubmit();
		//����ģ��
		ModelProcedure modelProcedure = new ModelProcedure();
		
		String modelId = req.getParameter("modelId");
		String name = req.getParameter("ptprocedureName");
		
		//��ȡ����ģ��
		modelProcedure = procedureService.getModelInfoAllById(Integer.parseInt(modelId));
		procedureSubmit.setName(modelProcedure.getName());
		procedureSubmit.setProjectNameTitle(modelProcedure.getProjectName());
		procedureSubmit.setProjectName(name);
		procedureSubmit.setCreatePerson(jobId);
		int tempPart = userServer.getUserPartByJobId(jobId);
		procedureSubmit.setPartName(partService.getNameById(tempPart));
		procedureSubmit.setGroupName(groupService.getNameById(tempPart, userServer.getUserGroupByJobId(jobId)));
		procedureSubmit.setRemark(modelProcedure.getRemark());
		
		//״̬
		procedureSubmit.setStatus(ProcedureSubmit.STATUS_WORKING);
		
		
		
		//�����Ҫ��ѡ ˳�򡢱��⡢�Ƿ����
		ModelOption[] opts = modelProcedure.getOption();
		ProcedureOption[] pOpts = new ProcedureOption[opts.length];//��ű����
		for(int i=0;i<opts.length;i++){
			ProcedureOption o = new ProcedureOption();
			//�õ�������
//			System.out.println(i+":"+req.getParameter("ptsmallName"+opts[i].getId()));
//			System.out.println(opts[i].getId());
			o.setContent(req.getParameter("ptsmallName"+opts[i].getId()));
			o.setOrder(opts[i].getOrder());
			o.setTitle(opts[i].getName());
			if(opts[i].getMust()==1){
				//������
				if(o.getContent()==null){
					//�����û����
					return JumpPrompt.jumpOfModelAndView(url, "�ύʧ�ܡ��������"+opts[i].getName()+"��û����д��");
				}
			}
			pOpts[i]=o;
		}
		procedureSubmit.setOpts(pOpts);
		
		//��������
		ModelShen[] shens = modelProcedure.getShen();
		ProcedureShen[] pShens = new ProcedureShen[shens.length];//��ű����
		for(int i=0;i<shens.length;i++){
			ProcedureShen ps = new ProcedureShen();
			ps.setUserGroup(shens[i].getGroup());
			ps.setUserPart(shens[i].getPart());
			ps.setUserJobId(shens[i].getPerson());
			ps.setName(shens[i].getName());
			ps.setUserName(userServer.getUserNameById(shens[i].getPerson()));
			ps.setOrder(shens[i].getOrder());
			if(shens[i].getOrder()==1){
				ps.setWork(ProcedureShen.WORK_NEED);
			}else{
				ps.setWork(ProcedureShen.WORK_NO);
			}
			pShens[i]=ps;
		}
		procedureSubmit.setShens(pShens);
		
		//ȱ�ٴ��ļ��������ļ���
		
		//��ȡ�ļ�
		File file = null;
		if(enclosure.getSize()!=0){
			//���ļ�
			//�ϴ����ļ���
			procedureSubmit.setShowFileName(enclosure.getOriginalFilename());
			ServletContext application = req.getServletContext();
			String realPath = application.getRealPath(filePath);
			int index = enclosure.getOriginalFilename().lastIndexOf(".");
			String suffix = enclosure.getOriginalFilename().substring(index+1);
			//������ʽ��jobId_1234567891235646.��׺
			String realFileName = jobId+"_"+new Date().getTime()+"."+suffix;
			String fileName = realPath+File.separator+realFileName;
			//��ȡ�ļ�
			file = new File(fileName);
			try {
				File fileTemp = new File(realPath);
				if(!fileTemp.exists()){
					fileTemp.mkdirs();
				}
				enclosure.transferTo(file);
				procedureSubmit.setFileName(realFileName);
			} catch (IllegalStateException | IOException e) {
				procedureSubmit.setShowFileName(null);
				e.printStackTrace();
			}
		}
		//����ʱ��
		procedureSubmit.setCreateDate(new Date());
		
		//���浽���ݿ�
		try {
			if(procedureService.procedureSubmit(procedureSubmit)){
				//����ɹ�
				return JumpPrompt.jumpOfModelAndView("/procedure/submitList.do", "�����ύ�ɹ���");
			}else{
				//����ʧ��
				//ɾ���ļ�
				if(file!=null){
					file.delete();
				}
				return JumpPrompt.jumpOfModelAndView("/procedure/submitList.do", "�����ύʧ��");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView("/procedure/submitList.do", "�����ύʧ�ܣ��������쳣��");
		}
	}
	
	/**
	 * ���������ύ
	 * @param proId
	 * @param spid
	 * @param content
	 * @param pass
	 * @param req
	 * @return
	 */
	@RequestMapping("dealProcedureForm.do")
	public ModelAndView dealProcedure(String proId,String spid,String content,String pass,
			HttpServletRequest req){
		String jobId = (String)req.getSession().getAttribute("userJobId");
		String url = "/procedure/needToDealList.do";
		if(proId!=null && proId.length()!=0){
			if(spid!=null && spid.length()!=0){
				if(content!=null && content.length()!=0){
					if(pass!=null && pass.length()!=0){
						
					}else{
						return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���ȱ�������");
					}
				}else{
					return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���ȱ��������ݣ�");
				}
			}else{
				return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���ȱ�ٹ��̺ţ�");
			}
		}else{
			return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ���ȱ�����̺ţ�");
		}
		
		ProcedureShen shen = new ProcedureShen();
		shen.setContent(content);
		shen.setProcedureId(Integer.parseInt(proId));
		shen.setId(Integer.parseInt(spid));
		if(pass.equals("yes")){
			shen.setPass(true);
		}else{
			shen.setPass(false);
		}
		shen.setTime(new Date());
		
		//���浽���ݿ�
		
		try {
			if(procedureService.dealOneProcedure(shen,jobId)){
				//�ɹ�
				return JumpPrompt.jumpOfModelAndView(url, "�����ɹ���");
			}else{
				//ʧ��
				return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JumpPrompt.jumpOfModelAndView(url, "����ʧ�ܡ����������쳣��");
		}
	}
	
	/**
	 * /����word�ĵ�
	 * @param submitId
	 * @param request
	 * @param response
	 */
	@RequestMapping("downFileOfWord.do")
	public void downFileToWord(String submitId,HttpServletRequest request,HttpServletResponse response){
		String showFile = "���ĵ�.doc";
		String tempFileName = submitId +"_"+ System.currentTimeMillis();
		if(submitId==null || submitId.length()==0){
			return;
		}
		ProcedureSubmit pSubmit = null;
		pSubmit = procedureService.getMySubmitAllInfoById(Integer.parseInt(submitId));
		if(pSubmit==null){
			return;
		}
		String servletPath = request.getServletContext().getRealPath("/");
		servletPath = servletPath + File.separator + "downFile";
		
		Map<String,String> info = new HashMap<>();
		Map<Integer,Map<String,String>> opt =null;
		Map<Integer,Map<String,String>> shen = null;

		
		String tname = pSubmit.getName() + "����";//XXXXX���̱�
		String tproduceNameTitle = pSubmit.getProjectName();//��Ŀ����
		String tproduceName = pSubmit.getProjectNameTitle();//��Ŀ��������
		String tid = "A"+pSubmit.getId();//���
		String tcreatePerson = userServer.getUserNameById(pSubmit.getCreatePerson());//�ύ��
		String tcreateDate = sdf.format(pSubmit.getCreateDate());//����ʱ��
		String tremark = pSubmit.getRemark();//��ע
		String tshowFileName = pSubmit.getShowFileName();//�ϴ����ļ���
		String tpart = pSubmit.getPartName();//��������
		String tgroup = pSubmit.getGroupName();//����С��
		int status = pSubmit.getStatus();
		
		showFile = pSubmit.getId() + showFile;
		info.put(DownWord.REPLACE_ALL_TITLE,tname);
		info.put(DownWord.REPLACE_PROJECT_TITLE_NAME,tproduceName);
		info.put(DownWord.REPLACE_PROJECT_NAME,tproduceNameTitle);
		info.put(DownWord.REPLACE_SUBMIT_PERSON,tcreatePerson);
		info.put(DownWord.REPLACE_SUBMIT_TIME,tcreateDate);
		info.put(DownWord.REPLACE_SUBMIT_PART,tpart);
		info.put(DownWord.REPLACE_SUBMIT_GROUP,tgroup);
		info.put(DownWord.REPLACE_ID,tid);
		info.put(DownWord.REPLACE_FU_JIAN,tshowFileName);
		info.put(DownWord.REPLACE_BEI_ZHU,tremark);
		if(status==ProcedureSubmit.STATUS_PASS){
			status = DownWord.PASS_OK;
		}else if(status==ProcedureSubmit.STATUS_NO_PASS){
			status = DownWord.PASS_NO;
		}else if(status==ProcedureSubmit.STATUS_WORKING){
			status = DownWord.PASS_WORKING;
		}else {
			status = DownWord.PASS_OTHER;
		}
		
		ProcedureOption[] opts = pSubmit.getOpts();
		if(opts!=null){
			opt = new HashMap<Integer,Map<String,String>>();
			for(int i=0;i<opts.length;i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("title", opts[i].getTitle());
				map.put("content", opts[i].getContent());
				opt.put(opts[i].getOrder(), map);
			}
		}
//		for(int i=0;i<7;i++) {
//			Map<String,String> map = new HashMap<>();
//			map.put("title", "ѡ�����"+i);
//			map.put("content", "�������ݰ�"+i);
//			opt.add(map);
//		}
		
//		for(int i=0;i<5;i++) {
//			Map<String,String> map = new HashMap<>();
//			map.put("name", "��д��"+i);
//			map.put("time", "2017��11��14��10:57:4"+i);
//			map.put("title", "���̺�"+i);
//			map.put("content", "ͬ����"+i*6);
//			shen.add(map);
//		}
		//����
		ProcedureShen[] shens = pSubmit.getShens();
//		System.out.println(shens);
		shen = new HashMap<Integer,Map<String,String>>();
		for(int i=0;i<shens.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("title", shens[i].getName());
			if(shens[i].getTime()!=null){
				map.put("time", sdf.format(shens[i].getTime()));
				map.put("content", shens[i].getContent());
				map.put("name", shens[i].getUserName());
			}else{
				map.put("time", null);
				map.put("content", null);
				map.put("name", null);
			}
			shen.put(shens[i].getOrder(), map);
		}
		
		DownWord deal = new DownWord();
		deal.createFile(servletPath,tempFileName,info, opt, shen,status);
		
		
		File file = new File(servletPath,tempFileName+".doc");
//		System.out.println("\nAction->"+file.getAbsolutePath());
		if(file.exists()){
			response.setContentType("application/octet-stream;charset=UTF-8");// ����ǿ�����ز���
			response.addHeader("Content-Length", "" + file.length());//�ļ�����
			try {
				String encodedFileName = new String(showFile.getBytes("utf-8"),"iso-8859-1");
				response.addHeader("Content-Disposition",  "attachment;fileName=\"" +encodedFileName+"\"" );
			} catch (UnsupportedEncodingException e1) {
				response.addHeader("Content-Disposition",  "attachment;fileName=" +showFile);
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
				if(os!=null)
					try {
						os.close();
					}catch (IOException e) {e.printStackTrace();
				}
				if(fis!=null)
					try {
						fis.close();
					}catch (IOException e) {e.printStackTrace();
				}
//				file.delete();
			}
		}
	}
	
	/**
	 * �����ļ�
	 * @param showFile
	 * @param realFile
	 * @param req
	 * @param response
	 */
	@RequestMapping("downFile.do")
	private void downFile(String showFile,String realFile,HttpServletRequest req,HttpServletResponse response) {
		if(realFile==null || realFile.length()==0){
			return;
		}
		ServletContext application = req.getServletContext();
		String realPath = application.getRealPath(filePath);
		String fileName = realPath+File.separator+realFile;
		File file = new File(fileName);
		if(file.exists()){
			response.setContentType("application/octet-stream;charset=UTF-8");// ����ǿ�����ز���
			response.addHeader("Content-Length", "" + file.length());//�ļ�����
			try {
				String encodedFileName = new String(showFile.getBytes("utf-8"),"iso-8859-1");
				response.addHeader("Content-Disposition",  "attachment;fileName=\"" +encodedFileName+"\"" );
			} catch (UnsupportedEncodingException e1) {
				response.addHeader("Content-Disposition",  "attachment;fileName=" +showFile);
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
		}
	}

}
