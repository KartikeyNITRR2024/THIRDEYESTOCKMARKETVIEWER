package com.thirdeye.stockmarketviewer.utils;

import java.sql.Time;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdeye.stockmarketviewer.repositories.ConfigUsedRepo;
import com.thirdeye.stockmarketviewer.entity.ConfigUsed;
import com.thirdeye.stockmarketviewer.entity.ConfigTable;
import com.thirdeye.stockmarketviewer.repositories.ConfigTableRepo;

@Component 
public class PropertyLoader {
    public Long timeGap;
    public Integer noOfMachine;
    private Long configId;
    public Time startTime;
    public Time endTime;

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    @Autowired
    private ConfigTableRepo configTableRepo;
    
    @Autowired
    private ConfigUsedRepo configUsedRepo;

    public void updatePropertyLoader() {
        try {
        	logger.info("Fetching currently config used.");
            ConfigUsed configUsed = configUsedRepo.findById(1L).get();
            configId = configUsed.getId();
            logger.debug("Fetching configuration for configId: {}", configId);
            Optional<ConfigTable> configTable = configTableRepo.findById(configId);
            if (configTable.isPresent()) {
            	timeGap = configTable.get().getTimeGap();
            	noOfMachine = configTable.get().getNoOfMachineForLiveMarket();
            	startTime = configTable.get().getStartTime();
            	endTime = configTable.get().getEndTime();
                logger.info("Time Gap Beetween iteration: {}", timeGap);
                logger.info("Start Time is : {}", startTime);
                logger.info("End Time is : {}", endTime);
                logger.info("Number of machines are : {}", noOfMachine);
            } else {
                logger.warn("No configuration found for configId: {}", configId);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching configuration: {}", e.getMessage(), e);
        }
    }
}
