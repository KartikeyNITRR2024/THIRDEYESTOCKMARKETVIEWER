package com.thirdeye.stockmarketviewer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdeye.stockmarketviewer.services.impl.LiveMarketServiceImpl;
import com.thirdeye.stockmarketviewer.services.impl.MarketThresoldServiceImpl;

import jakarta.annotation.PostConstruct;

@Component 
public class Initiatier {
	
	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	MarketThresoldServiceImpl marketThresoldImpl;
	
	@Autowired
	LiveMarketServiceImpl liveMarketServiceImpl;
	
	@Autowired
	PropertyLoader propertyLoader;
	
	private static final Logger logger = LoggerFactory.getLogger(Initiatier.class);
	
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
        allMicroservicesData.getAllMicroservicesData();
        propertyLoader.updatePropertyLoader();
        marketThresoldImpl.getUpdatedAllMarketThresold();
        liveMarketServiceImpl.refreshData();
        logger.info("Initiatier initialized.");
    }
}
