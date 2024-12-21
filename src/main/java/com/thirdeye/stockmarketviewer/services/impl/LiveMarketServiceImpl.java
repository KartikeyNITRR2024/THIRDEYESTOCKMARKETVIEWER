package com.thirdeye.stockmarketviewer.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;
import com.thirdeye.stockmarketviewer.pojos.PriceTimestampPojo;
import com.thirdeye.stockmarketviewer.services.LiveMarketService;
import com.thirdeye.stockmarketviewer.utils.PropertyLoader;
import com.thirdeye.stockmarketviewer.utils.StocksPriceChangesCalculator;
import com.thirdeye.stockmarketviewer.utils.TimeManagementUtil;

@Service
public class LiveMarketServiceImpl implements LiveMarketService {

	private static final Logger logger = LoggerFactory.getLogger(LiveMarketServiceImpl.class);
	
	private Map<Long, List<PriceTimestampPojo>> dataStoringMap = new HashMap<>();
	private Boolean updateMachine = true;
	private Map<String, Boolean> updateMachineStatus = new HashMap<>();
	
	@Autowired
    TimeManagementUtil timeManagementUtil;
	
	@Autowired
	StocksPriceChangesCalculator stocksPriceChangesCalculator;
	
	@Autowired
	PropertyLoader propertyLoader;
	 
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
//	@Override
//	public boolean processListMarketData(List<LiveStockPayload> liveStockData, String uniqueMachineCode) {
//        logger.info("Data is " + liveStockData);
//        lock.writeLock().lock();
//        try {
//	        logger.info("DataProcessing Starting time is : {}", timeManagementUtil.getCurrentTime());
//	        for(LiveStockPayload liveStockPayload : liveStockData)
//	        {
//	        	if(liveStockPayload.getStockId() != null && liveStockPayload.getStockId() > 0 && liveStockPayload.getPrice() != null && liveStockPayload.getPrice() > 0)
//	        	{
//	        		stocksPriceChangesCalculator.calculateChanges(liveStockPayload, dataStoringMap.get(liveStockPayload.getStockId()));
//	        		List<PriceTimestampPojo> liveStockArray = null;
//	        		if(dataStoringMap.containsKey(liveStockPayload.getStockId()))
//	        		{
//	        			liveStockArray = dataStoringMap.get(liveStockPayload.getStockId());
//	        			if(liveStockArray.size() >= 100)
//	        			{
//	        				liveStockArray.remove(0);
//	        			}
//	        		}
//	        		else
//	        		{
//	        			liveStockArray = new ArrayList<>();
//	        		}
//	    			liveStockArray.add(new PriceTimestampPojo(liveStockPayload.getTime(),liveStockPayload.getPrice()));
//	    			dataStoringMap.put(liveStockPayload.getStockId(), liveStockArray);
//	        	}
//	        }
//	        logger.info("DataProcessing Ending time is : {}", timeManagementUtil.getCurrentTime());
//	        if(updateMachine == true)
//	        {
//	        	if(machineSet.size() >= propertyLoader.noOfMachine)
//	        	{
//	        		machineSet.clear();
//	        		updateMachine = false;
//	        		logger.info("All machines are reseted.");
//	        	}
//	        	else
//	        	{
//	        		machineSet.add(uniqueMachineCode);
//	        		logger.info("Going to reset machine with uniqueMachineCode : {}", uniqueMachineCode);
//	        	}
//	        }
//        } finally {
//            lock.writeLock().unlock();
//        }
//        return updateMachine;
//	}
	
//	@Override
//	public boolean processListMarketData(List<LiveStockPayload> liveStockData, String uniqueMachineCode) {
////	    logger.info("Data is " + liveStockData);
//	    lock.writeLock().lock();
//        Boolean check = false;
//	    try {
//	        logger.info("DataProcessing Starting time is : {}", timeManagementUtil.getCurrentTime());
//	        
//	        List<CompletableFuture<Void>> futures = new ArrayList<>();
//	        
//	        for(LiveStockPayload liveStockPayload : liveStockData) {
//	            if(liveStockPayload.getStockId() != null && liveStockPayload.getStockId() > 0 
//	                    && liveStockPayload.getPrice() != null && liveStockPayload.getPrice() > 0) {
//	                
//	                // Call the async method and store the future
//	                CompletableFuture<Void> future = stocksPriceChangesCalculator.calculateChanges(liveStockPayload, dataStoringMap.get(liveStockPayload.getStockId()));
//	                futures.add(future);
//	                
//	                // Update liveStockArray logic
//	                List<PriceTimestampPojo> liveStockArray = dataStoringMap.getOrDefault(liveStockPayload.getStockId(), new ArrayList<>());
//	                if(liveStockArray.size() >= 100) {
//	                    liveStockArray.remove(0);
//	                }
//	                liveStockArray.add(new PriceTimestampPojo(liveStockPayload.getTime(), liveStockPayload.getPrice()));
//	                dataStoringMap.put(liveStockPayload.getStockId(), liveStockArray);
//	            }
//	        }
//
//	        // Wait for all async tasks to complete
//	        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//	        logger.info("DataProcessing Ending time is : {}", timeManagementUtil.getCurrentTime());
//	        
//	        // Machine reset logic
//
//	        if (updateMachine) {
//	            if (updateMachineStatus.size() >= 3) {
//	            	updateMachineStatus.clear();
//	                updateMachine = false;
//	                logger.info("All machines are reset.");
//	            } else {
//	                if(updateMachineStatus.get(uniqueMachineCode) == null)
//	                {
//	                	check = true;
//		            	updateMachineStatus.put(uniqueMachineCode,true);
//		                logger.info("Going to reset machine with uniqueMachineCode : {}", uniqueMachineCode);
//	                }
//	                else
//	                {
//	                	logger.info("Machine with uniqueMachineCode {} is allready updated.", uniqueMachineCode);
//	                }
//	            }
//	        }
//	    } finally {
//	        lock.writeLock().unlock();
//	    }
//	    return check;
//	}
	
	public boolean processListMarketData(List<LiveStockPayload> liveStockData, String uniqueMachineCode) {
        lock.writeLock().lock();
        Boolean check = false;
        try {
            logger.info("DataProcessing Starting time is : {}", timeManagementUtil.getCurrentTime());

            for (LiveStockPayload liveStockPayload : liveStockData) {
                if (liveStockPayload.getStockId() != null && liveStockPayload.getStockId() > 0 
                        && liveStockPayload.getPrice() != null && liveStockPayload.getPrice() > 0) {
                    
                    // Call the async method without waiting for the future
                    stocksPriceChangesCalculator.calculateChanges(liveStockPayload, dataStoringMap.get(liveStockPayload.getStockId()));
                    
                    // Update liveStockArray logic
                    List<PriceTimestampPojo> liveStockArray = dataStoringMap.getOrDefault(liveStockPayload.getStockId(), new ArrayList<>());
                    if (liveStockArray.size() >= 100) {
                        liveStockArray.remove(0);
                    }
                    liveStockArray.add(new PriceTimestampPojo(liveStockPayload.getTime(), liveStockPayload.getPrice()));
                    dataStoringMap.put(liveStockPayload.getStockId(), liveStockArray);
                }
            }

            logger.info("DataProcessing Ending time is : {}", timeManagementUtil.getCurrentTime());

            // Machine reset logic
            if (updateMachine) {
                if (updateMachineStatus.size() >= propertyLoader.noOfMachine) {
                    updateMachineStatus.clear();
                    updateMachine = false;
                    logger.info("All machines are reset.");
                } else {
                    if (updateMachineStatus.get(uniqueMachineCode) == null) {
                        check = true;
                        updateMachineStatus.put(uniqueMachineCode, true);
                        logger.info("Going to reset machine with uniqueMachineCode : {}", uniqueMachineCode);
                    } else {
                        logger.info("Machine with uniqueMachineCode {} is already updated.", uniqueMachineCode);
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
        return check;
    }

	
	public void refreshData()
	{
		lock.writeLock().lock();
		try {
			logger.info("Refreshing all data.");
			dataStoringMap.clear();
			updateMachine = true;
			updateMachineStatus.clear();
		} finally {
            lock.writeLock().unlock();
        }
	}

}
