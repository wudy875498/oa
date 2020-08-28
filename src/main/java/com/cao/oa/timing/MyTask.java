package com.cao.oa.timing;

import com.cao.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyTask {
	
	@Autowired
	private UserService userServer;
	
	@Scheduled(fixedRate=1000*5)
	public void aotoThaw(){
		String jobId = null;
		jobId = userServer.hasNeedToOutOfFrozen();
		while(jobId!=null){
			try {
				userServer.changeUserStatusOutOfFrozenByJobId(jobId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jobId = userServer.hasNeedToOutOfFrozen();
		}
//		System.out.println("��ʱ�ⶳ����ִ��");
	}
}
