package cn.com.tvmore.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
/*import org.springframework.stereotype.Service;*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.tvmore.dao.LoginInfoDao;
import cn.com.tvmore.dao.LoginLogDao;
import cn.com.tvmore.entity.LoginLogEntity;
import cn.com.tvmore.utils.HttpUtils;

/*@Service*/
public class TaskService implements CommandLineRunner {
	private static final String LOCATION_URL = "http://api.tvmore.com.cn/basicservice/location?app=test";
	
	@Resource
	private LoginInfoDao loginInfoDao;
	
	@Resource
	private LoginLogDao loginLogDao;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>任务启动了...<<<<<<<<<<<<<");
		long startTime = System.currentTimeMillis();
		System.out.println(">>>>startTime: " + startTime);
		
		/*int startId = 0;*/
		int rows = 10;
		if(args != null && args.length == 1) {
			/*startId = Integer.parseInt(args[0]);*/
			rows = Integer.parseInt(args[0]);
		} else {
			System.out.println("XXXXXXX -> Please input process rows.");
			return;
		}
		
		/*start(startId, rows);*/
		start(rows);
		
		long endTime = System.currentTimeMillis();
		System.out.println(">>>>endTime: " + endTime);
		System.out.println("Take milliseconds: " + (endTime-startTime));
	}
	
	private void start(int rows) {
		/*LoginLogEntity loginLogEntity = new LoginLogEntity();*/
		/*List<LoginInfoEntity> infos = loginInfoDao.getLoginInfoList(startId, row);*/
		List<LoginLogEntity> entitys = loginInfoDao.getLoginInfoList(rows);
		
		if(entitys != null && entitys.size() > 0) {
			LoginLogEntity loginLogEntity = null;
			int delIndex = 0;
			int listSize = entitys.size();
			for(int i = 0; i < listSize; i++) {
				loginLogEntity = entitys.get(i);
				if(i % 100 == 0) {
					System.out.println(">>>processing login_info Id: " + loginLogEntity.getId());
				}
				/*BeanUtils.copyProperties(loginInfoEntity, loginLogEntity);*/
				requestAddress(loginLogEntity);
				loginLogDao.saveLoginLog(loginLogEntity);
				/*if (i == listSize - 1) {
					System.out.println("*********END ID: [" + loginInfoEntity.getId() + "]");
				}*/
				delIndex = loginLogEntity.getId();
			}
			
			System.out.println("Delete login_info before id: " + delIndex);
			if(delIndex > 0) {
				System.out.println("Processing DELETE login_info before id: " + delIndex);
				loginInfoDao.deleteLoginInfoBefore(delIndex);
				System.out.println("DELETE login_info END.");
			}
		}
		/*for (LoginInfoEntity loginInfoEntity : infos) {
			logger.info("processing login_info Id: {}", loginInfoEntity.getId());
			BeanUtils.copyProperties(loginInfoEntity, loginLogEntity);
			requestAddress(loginLogEntity);
			loginLogDao.saveLoginLog(loginLogEntity);
			//loginInfoDao.deleteLoginInfoById(loginInfoEntity.getId());
		}*/
		
	}
	
	private void requestAddress(LoginLogEntity entity) {
		StringBuilder sb = new StringBuilder(LOCATION_URL).append("&ip=").append(entity.getLastloginIp());
		String result = HttpUtils.doGet(sb.toString());
		JSONObject json = JSON.parseObject(result);
		if (json != null && json.getInteger("status").intValue() == 200) {
			JSONObject pcinfo = JSON.parseObject(json.getString("data"));
			if(pcinfo != null) {
				entity.setProvince(pcinfo.getString("province"));
				entity.setCity(pcinfo.getString("city"));
			}
		}
	}

}
