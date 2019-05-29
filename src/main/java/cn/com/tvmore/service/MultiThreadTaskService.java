package cn.com.tvmore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import cn.com.tvmore.dao.LoginInfoDao;
import cn.com.tvmore.dao.LoginLogDao;
import cn.com.tvmore.entity.LoginLogEntity;
import cn.com.tvmore.utils.HttpUtils;

@Service
public class MultiThreadTaskService implements CommandLineRunner {
	private static final String LOCATION_URL = "http://api.tvmore.com.cn/basicservice/location?app=test";
	
	@Resource
	private LoginInfoDao loginInfoDao;
	
	@Resource
	private LoginLogDao loginLogDao;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>多线程任务启动了<<<<<<<<<<<<<");
		long startTime = System.currentTimeMillis();
		System.out.println(">>>> startTime: " + startTime);
		
		int num = 10;
		if(args != null && args.length == 1) {
			num = Integer.parseInt(args[0]);
		} else {
			System.out.println("XXXXXXX -> Please input process rows.");
			return;
		}
		
		final int rows = num;
		final CountDownLatch latch = new CountDownLatch(30);
		final Map<String, Integer> resultMap = new HashMap<>();
		
		for(int i=0; i<30; i++) {
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					start(index, rows * index, rows, latch, resultMap);
				}
			}).start();
		}
		latch.await();
		
		deleteProcessedLoginInfo(resultMap.get("maxProcessedId"));
		
		long endTime = System.currentTimeMillis();
		System.out.println(">>>> endTime: " + endTime);
		System.out.println("Take milliseconds: " + (endTime-startTime));
		
		//补偿处理login_log中province、city都为空的数据(/basicservice/location -> connect timed out)
		compensationProcess();
	}
	
	private void deleteProcessedLoginInfo(Integer id) {
		if(id != null && id.intValue() > 0) {
			System.out.println("++++ processing delete login_info before id: " + id);
			loginInfoDao.deleteLoginInfoBefore(id);
		}
		System.out.println("++++ delete login_info SUCCESS.");
	}

	private void compensationProcess() {
		System.out.println("@@@@ login_log compensation processing...");
		List<LoginLogEntity> entitys = loginLogDao.getEntitys();
		System.out.println("@@@@ compensation size: " + entitys.size());
		for (LoginLogEntity entity : entitys) {
			System.out.println("----> update login_log id:" + entity.getId());
			requestAddress(entity);
			loginLogDao.updateEntity(entity);
		}
		System.out.println("@@@@ login_log compensation SUCCESS.");
	}

	private void start(final int index, final int offset, final int rows, final CountDownLatch latch, final Map<String, Integer> resultMap) {
		String threadName;
		if(index < 9) {
			threadName = "Multi-Thread-0" + (index + 1);
		} else {
			threadName = "Multi-Thread-" + (index + 1);
		}
		System.out.println("****Thread: [" + threadName + "] start -> OFFSET: " + offset + " ,ROWS: " + rows);
		List<LoginLogEntity> entitys = loginInfoDao.getEntityList(offset, rows);
		if(entitys != null && entitys.size() > 0) {
			LoginLogEntity loginLogEntity = null;
			int listSize = entitys.size();
			int endId = 0;
			for(int i = 0; i < listSize; i++) {
				loginLogEntity = entitys.get(i);
				if(i % 500 == 0) {
					System.out.println(">>>> [" + threadName +"] processing login_info Id: " + loginLogEntity.getId());
				}
				requestAddress(loginLogEntity);
				loginLogDao.saveLoginLog(loginLogEntity);
				//loginInfoDao.deleteLoginInfoById(loginLogEntity.getId());
				endId = loginLogEntity.getId();
			}
			System.out.println("**** [" + threadName + "] ---> END ID: [" + endId + "]");
			Integer nowId = resultMap.get("maxProcessedId");
			if(nowId == null || endId > nowId.intValue()) {
				resultMap.put("maxProcessedId", endId);
			}
			//latch.countDown();
		} else {
			System.out.println("Query ----> Thread: [" + threadName + "] -> OFFSET: " + offset + " ,ROWS: " + rows + " ,SIZE: 0");
		}
		latch.countDown();
	}
	
	private void requestAddress(LoginLogEntity entity) {
		if(entity == null || Strings.isNullOrEmpty(entity.getLastloginIp()) || "127.0.0.1".equals(entity.getLastloginIp())) return;
		StringBuilder sb = new StringBuilder(LOCATION_URL).append("&ip=").append(entity.getLastloginIp());
		JSONObject json = null;
		try {
			String result = HttpUtils.doGet(sb.toString());
			json = JSON.parseObject(result);
		} catch (Exception e) {
			System.out.println("##################Request Address Error, moretv_id: " + entity.getMoretvId() + ", ip: " + entity.getLastloginIp());
			System.out.println("Exception -> e: " + e.getMessage());
		}
		if (json != null && json.getInteger("status").intValue() == 200) {
			JSONObject pcinfo = JSON.parseObject(json.getString("data"));
			if(pcinfo != null) {
				entity.setProvince(pcinfo.getString("province"));
				entity.setCity(pcinfo.getString("city"));
			}
		}
	}

}
