package com.cao.oa.dao;

import com.cao.oa.bean.ModelOption;
import com.cao.oa.bean.ModelProcedure;
import com.cao.oa.bean.ModelShen;
import com.cao.oa.bean.ProcedureOption;
import com.cao.oa.bean.ProcedureShen;
import com.cao.oa.bean.ProcedureSubmit;
import com.cao.oa.mapper.ModelOptionMapper;
import com.cao.oa.mapper.ModelProcedureMapper;
import com.cao.oa.mapper.ModelShenMapper;
import com.cao.oa.mapper.ProcedureOptionMapper;
import com.cao.oa.mapper.ProcedureShenMapper;
import com.cao.oa.mapper.ProcedureSubmitMapper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProcedureDao {
	
	@Resource
	private ModelOptionMapper moMapper;
	@Resource
	private ModelProcedureMapper mpMapper;
	@Resource
	private ModelShenMapper msMapper;
	@Resource
	private ProcedureOptionMapper poMapper;
	@Resource
	private ProcedureShenMapper psMapper;
	@Resource
	private ProcedureSubmitMapper ppMapper;
	
	/**
	 * ��ȡĳ������Ҫ���ѵ�����ID��
	 * @param jobId
	 * @return
	 */
	public int getNeedRemindProcedureNumberByJobId(String jobId){
		int result = 0;
		result = ppMapper.getNumberByUserJobIdAndWork(jobId, ProcedureShen.WORK_NEED);
		return result;
	}
	
	/**
	 * ����һ������
	 * @param shen
	 * @param jobId
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> dealOneProcedure(ProcedureShen shen, String jobId) throws Exception{
//		System.out.println(shen);
		Map<String,Object> result = null;
		String good = psMapper.getDealPersonByIds(shen.getId(), shen.getProcedureId());
		int order = psMapper.getOrderByIds(shen.getId(),shen.getProcedureId());
		if(!good.equals(jobId)){
			//����ָ����Ա����
			return null;
		}
		int num = 0;
		if(shen.isPass()){
			//ͨ��
			//���¸�����
			shen.setTime(new Date());
			shen.setPass(1);
			shen.setWork(ProcedureShen.WORK_OK);
			num = psMapper.updateAllByIds(shen);
			if(num==1){
				int nextNum = psMapper.getHasThisOrderOfSubmit(shen.getProcedureId(), order+1);
				if(nextNum==1){
					//����һ������һ��״̬�ı�
					num = psMapper.updateWork(ProcedureShen.WORK_NEED, shen.getProcedureId(), order+1);
					if(num==1){
						//��ɣ�ͨ��������һ��
						result = new HashMap<>();
						result.put("finish", false);
						result.put("hasNextPerson", true);
						result.put("nextPerson", psMapper.getJobIdByOrderAndProcedureId(shen.getProcedureId(), order+1));
					}
				}else{
					//û����һ�������̽���
					num = ppMapper.updateStatus(shen.getProcedureId(), ProcedureSubmit.STATUS_PASS);
					if(num==1){
						//��ɣ�ͨ��������һ��
						result = new HashMap<>();
						result.put("finish", true);
						result.put("result", true);
						result.put("person", ppMapper.getCreatePerson(shen.getProcedureId()));
					}
				}
			}
		}else{
			//��ͨ��
			//���¸�����
			shen.setTime(new Date());
			shen.setPass(0);
			shen.setWork(ProcedureShen.WORK_OK);
			num = psMapper.updateAllByIds(shen);
			if(num==1){
				boolean hasError = false;
				//����ʣ������Ϊ���Թ���
				while(true){
					int nextNum = psMapper.getHasThisOrderOfSubmit(shen.getProcedureId(), order+1);
					if(nextNum==1){
						//����һ������һ��״̬�ı�
						num = psMapper.updateWork(ProcedureShen.WORK_PASS, shen.getProcedureId(), order+1);
						if(num==0){
							//�����⣬����ʧ��
							hasError = true;
							break;
						}
						order++;
					}else{
						//����һ�����˳�ѭ��
						break;
					}
				}
				if(!hasError){
					//û���⣬���������̽���
					num = ppMapper.updateStatus(shen.getProcedureId(), ProcedureSubmit.STATUS_NO_PASS);
					if(num==1){
						//��ɣ���ͨ�����ս���������
						result = new HashMap<>();
						result.put("finish", true);
						result.put("result", false);
						result.put("person", ppMapper.getCreatePerson(shen.getProcedureId()));
					}
				}
			}
		}
		if(result==null){
			throw new Exception();
		}
		return result;
	}
	
	/**
	 * ��ȡ�ӵڼ������ڼ���  ��Ҫ��������̣����԰�
	 * @param begin
	 * @param end
	 * @param jobId
	 * @return
	 */
	public List<Map<String, Object>> getNeedToDealSimpleFromNumToNum(int begin, int end, String jobId) {
		List<Map<String, Object>> result = null;
			//�ҵ���Ҫ�������������id
		List<Map<String, Object>> ids = psMapper.getNeedDealIdsByJobIdFromNumberToNumber(jobId, begin, end);
		if(ids!=null && ids.size()!=0){
			result = new ArrayList<>();
			for(int i=0;i<ids.size();i++){
				Map<String, Object> mapTemp = null;
				//��ȡ��Ҫ��Ϣ
				mapTemp = ppMapper.getMainInfoById((int)ids.get(i).get("procedureId"));
				if(mapTemp!=null){
					//��ȡ����ʱ��
					if((int)ids.get(i).get("oorder")==1){
						mapTemp.put("updateDate", mapTemp.get("createDate"));
					}else{
						Timestamp ts = psMapper.getUpdateTime((int)ids.get(i).get("procedureId"),(int)ids.get(i).get("oorder")-1);
						mapTemp.put("updateDate", ts);
					}
					result.add(mapTemp);
				}
			}
		}
		return result;
	}
	
	/**
	 * ��ȡĳ������Ҫ���������
	 * @param jobId
	 * @return
	 */
	public int getAllNeedToDealNumber(String jobId) {
		int result = 0;
		result = psMapper.getAllNeedToDealNumber(jobId);
		return result;
	}
	
	/**
	 * ��ȡ�ҵ�һ���ύ��ȫ����Ϣ
	 * @param submitId
	 * @return
	 */
	public ProcedureSubmit getMySubmitAllInfoById(int submitId) {
		ProcedureSubmit result = null;
		
		
		result = ppMapper.findById(submitId);
//		System.out.println("submitId="+submitId);
		List<ProcedureOption> temp1 = poMapper.findBySubmitId(submitId);
//		System.out.println("temp1:");
//		System.out.println(temp1);
		
		ProcedureOption[] opts = new ProcedureOption[temp1.size()];
		for(int i=0;i<temp1.size();i++){
			opts[i] = temp1.get(i);
		}
		result.setOpts(opts);
		List<ProcedureShen> temp2 = psMapper.findBySubmitId(submitId);
//		System.out.println("temp2:");
//		System.out.println(temp2);
		ProcedureShen[] shens = new ProcedureShen[temp2.size()];
		for(int i=0;i<temp2.size();i++){
			shens[i] = temp2.get(i);
		}
		result.setShens(shens);
//		System.out.println(shens);
		return result;
	}
	
	/**
	 * ��ȡ�ӵڼ����ڼ������ύ�����̣����԰�
	 * @param begin
	 * @param end
	 * @param jobId
	 * @return
	 */
	public List<Map<String, Object>> getAllMyProcedureSimpleFromNumToNum(int begin, int end, String jobId) {
		List<Map<String, Object>> result = null;
		result = ppMapper.getAllMyProcedureSimpleFromNumToNum(jobId, begin, end);
		return result;
	}
	
	/**
	 * ��ȡ���ύ�����̵�����
	 * @param jobId
	 * @return
	 */
	public int getAllMyProcedureNumber(String jobId) {
		int result = 0;
		result = ppMapper.getNumberOfSubmitByCreatePerson(jobId);
		return result;
	}
	
	/**
	 * �����ύ������
	 * @param psubmit
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> procedureSubmit(ProcedureSubmit psubmit){
		try{
			Map<String,Object> needReturn = null;
			boolean result = false;
			int key = 0;
			psubmit.setCreateDate(new Date());
			ppMapper.addNewSubmit(psubmit);
			key = psubmit.getId();
			boolean needBack = false;
			if(key>0){
				needReturn = new HashMap<>();
				needReturn.put("key", key);
//				System.out.println("key:"+key);
				ProcedureOption[] opts = psubmit.getOpts();
				if(opts!=null){
					for(int i=0;i<opts.length;i++){
						opts[i].setProcedureId(key);
						if(!addOptionOfSubmit(opts[i])){
							//ʧ����
							needBack = true;
//							System.out.println("ʧ�ܱ��1");
							break;
						}
					}
				}
				//����û������
				if(!needBack){
					ProcedureShen[] shens = psubmit.getShens();
					if(shens!=null){
						for(int i=0;i<shens.length;i++){
							if(shens[i].getOrder()==1){
								needReturn.put("needToRemind", shens[i].getUserJobId());
							}
							shens[i].setProcedureId(key);
							if(!addShenOfSubmit(shens[i])){
								//ʧ����
								needBack = true;
//								System.out.println("ʧ�ܱ��2");
								break;
							}
						}
					}
				}
				if(!needBack){
					//û�д���
					result = true;
				}
			}
			if(!result){
				needReturn = null;
				throw new RuntimeException();
			}
			return needReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * �����ύ��ѡ��
	 * @param opt
	 * @return
	 */
	//---
	public boolean addOptionOfSubmit(ProcedureOption opt){
		int num = poMapper.addNewOption(opt);
//		System.out.println("num:"+num);
		if(num==1){
			return true;
		}else{
//			System.out.println("addOptionOfSubmit"+opt.getOrder()+"����");
			return false;
		}
	}
	
	/**
	 * �����ύ������
	 * @param shen
	 * @return
	 */
	public boolean addShenOfSubmit(ProcedureShen shen){
		int num = psMapper.addNewShen(shen);
		if(num==1){
			return true;
		}else{
//			System.out.println("addShenOfSubmit"+shen.getOrder()+"����");
			return false;
		}
	}
	
	
	/**
	 * ��ȡ���̵Ĵ�����
	 * @param id
	 * @return
	 */
	public String getUserOfProcedureWhoCreateById(int id){
		String result = null;
		result = mpMapper.getUserOfProcedureWhoCreateById(id);
		return result;
	}
	
	/**
	 * ����ID��ɾ��һ������
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public boolean delProcedureById(int id) throws Exception{
		boolean resTemp = false;
			
		int num = 0;
		int num2 = 0;

		//��ѯ�м���ѡ��
		num2 = moMapper.getNumberOfOneModel(id);
		//ɾ��ѡ��
		num = moMapper.delAllOptionsByModelId(id);
		//ѡ��ȫ��ɾ��
		if(num==num2){
			//��ѯ�м�������
			num2 = msMapper.getNumberOfOneModel(id);
			//ɾ������
			num = msMapper.delAllByModelId(id);
			//����ȫ��ɾ��
			if(num==num2){
				num = mpMapper.delById(id);
				if(num==1){
					resTemp = true;
				}
			}
		}
		if(!resTemp){
			throw new Exception();
		}
		return resTemp;
	}
	
	/**
	 * ����ģ��
	 * @param procedure
	 * @return
	 * @throws Exception 
	 */
	public boolean updateProcedure(ModelProcedure procedure) throws Exception {
		boolean resTemp = false;
		int num = 0;
		int num2 = 0;
		num = mpMapper.updateModel(procedure);
		//�ɹ��޸���Ҫ
		if(num==1){
			//��ѯ�м���ѡ��
			num2 = moMapper.getNumberOfOneModel(procedure.getId());
			//ɾ��ѡ��
			num = moMapper.delAllOptionsByModelId(procedure.getId());
			//ѡ��ȫ��ɾ��
			if(num==num2){
				//��ѯ�м�������
				num2 =msMapper.getNumberOfOneModel(procedure.getId());
				//ɾ������
				num = msMapper.delAllByModelId(procedure.getId());
				//����ȫ��ɾ��
				if(num==num2){
					boolean needBack = false;//�Ƿ���Ҫ�ع�
					//���ѡ��
					ModelOption[] opts = procedure.getOption();
					for(ModelOption opt:opts){
						if(!addNewOption(opt,procedure.getId())){
							needBack = true;
							break;
						}
					}
					if(!needBack){
						//��ӹ���
						ModelShen[] shens = procedure.getShen();
						for(ModelShen s:shens){
							if(!addNewShen(s,procedure.getId())){
								needBack = true;
								break;
							}
						}
					}
					if(!needBack){
						resTemp = true;
					}
				}
			}
		}
		//����һ��ʧ��
		if(!resTemp){
			throw new Exception();
		}
		return resTemp;
	}
	
	
	/**
	 * ��ȡһ������ģ���ȫ��
	 * @param modelId
	 * @return
	 */
	public ModelProcedure getModelInfoAllById(int modelId) {
		ModelProcedure result = null;
		//����������
		result = mpMapper.findById(modelId);
		//������ѡ��
		List<ModelOption> tempO = moMapper.getOptionsByProcedureId(modelId);
		if(tempO!=null && tempO.size()!=0){
			ModelOption[] opts = new ModelOption[tempO.size()];
			for(int i=0;i<tempO.size();i++){
				opts[i] = tempO.get(i);
			}
			result.setOption(opts);
			//��������
			List<ModelShen> tempS = msMapper.getShensByProcedureId(modelId);
			if(tempS!=null && tempS.size()!=0){
				ModelShen[] ss = new ModelShen[tempS.size()];
				for(int i=0;i<tempS.size();i++){
					ss[i] = tempS.get(i);
				}
				result.setShen(ss);
			}else{
				result = null;
			}
		}else{
			result = null;
		}
		return result;
	}
	
	/**
	 * �ӵڼ����ڼ���ģ�壬��ʱ������
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Map<String,Object>> getAllModelFromNumToNum(int begin,int end){
		List<Map<String,Object>> result = null;
		result = mpMapper.getAllModelFromNumToNum(begin, end);
		return result;
	}
	
	/**
	 * ��ȡ����ģ�������
	 * @return
	 */
	public int getAllModelNumber(){
		int result = 0;
		result = mpMapper.getAllModelNumber();
		return result;
	}
	
	/**
	 * �����µ�����
	 * @param procedure
	 * @return
	 * @throws Exception 
	 */
	public boolean createNewProcedure(ModelProcedure procedure) throws Exception {
		boolean result = false;
		int key = 0;
		procedure.setCreateDate(new Date());
		key = mpMapper.addNewProcedure(procedure);
		//�ɹ��������
		if(key!=0){
			boolean needBack = false;
			key = procedure.getId();
			ModelOption[] opts = procedure.getOption();
			for(ModelOption opt:opts){
				if(!addNewOption(opt,key)){
					needBack = true;
					break;
				}
			}
			if(!needBack){
				ModelShen[] shens = procedure.getShen();
				for(ModelShen s:shens){
					if(!addNewShen(s,key)){
						needBack = true;
						break;
					}
				}
				if(!needBack){
					result = true;
				}
			}
		}
		if(!result){
			throw new Exception();
		}
		return result;
	}
	
	/**
	 * ���һ���µ�����
	 * @param s
	 * @param modelId
	 * @return
	 */
	//---
	public boolean addNewShen(ModelShen s,int modelId){
		boolean result = false;
		s.setModelId(modelId);
		int num = msMapper.addNewShen(s);
		if(num==1){
			result = true;
		}
		return result;
	}
	
	/**
	 * �����ݿ������һ���µ���ѡ��
	 * @param opt
	 * @param modelId
	 * @return
	 */
	public boolean addNewOption(ModelOption opt,int modelId){
		boolean result = false;
		opt.setModelId(modelId);
		int num = moMapper.addNewOption(opt);
		if(num==1){
			result = true;
		}
		return result;
	}

	
	
}




















