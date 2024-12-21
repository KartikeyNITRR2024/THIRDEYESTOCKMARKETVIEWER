package com.thirdeye.stockmarketviewer.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye.stockmarketviewer.entity.MicroservicesInfo;
import com.thirdeye.stockmarketviewer.repositories.MicroservicesInfoRepo;

@Component
public class AllMicroservicesData {
    
	public MicroservicesInfo current;
	public Map<String,MicroservicesInfo> allMicroservices = new HashMap<>();
	
	@Value("${spring.application.name}")
	private String microserviceName;
	
	@Autowired
	MicroservicesInfoRepo microservicesInfoRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(AllMicroservicesData.class);
	
	public void getAllMicroservicesData()
	{
		current = microservicesInfoRepo.getByMicroserviceName(microserviceName);
		logger.info("Current Microservice is : {}", current);
		List<MicroservicesInfo> allMicroservicesList = microservicesInfoRepo.findAll();
		logger.info("All Microservice are : ");
		for(MicroservicesInfo microservicesInfo : allMicroservicesList)
		{
			logger.info("Microservice : {} ", microservicesInfo);
			allMicroservices.put(microservicesInfo.getMicroserviceName(), microservicesInfo);
		}
	}
	
}