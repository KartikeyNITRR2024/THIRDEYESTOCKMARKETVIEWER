package com.thirdeye.stockmarketviewer.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye.stockmarketviewer.entity.MarketThresold;
import com.thirdeye.stockmarketviewer.repositories.MarketThresoldRepo;
import com.thirdeye.stockmarketviewer.services.MarketThresoldService;

@Service
public class MarketThresoldServiceImpl implements MarketThresoldService {
	
	private List<MarketThresold> allMarketThresold = new ArrayList<>();
	
    private static final Logger logger = LoggerFactory.getLogger(MarketThresoldServiceImpl.class);
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Autowired
	MarketThresoldRepo marketThresoldRepo;
	
	@Override
	public void getUpdatedAllMarketThresold() throws Exception
	{
		List<MarketThresold> allMarketThresold1 = marketThresoldRepo.findAll();
		lock.writeLock().lock();
		try {
			allMarketThresold.clear();
			allMarketThresold = new ArrayList<>(allMarketThresold1);
            logger.info("Successfully updated all market thresold :", allMarketThresold1.size());
        } catch (Exception e) {
            logger.error("Error occurred while updating all market thresold: {}", e.getMessage(), e);
            throw new Exception("Failed to retrieve all market thresold", e);
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	@Override
	public Integer getAllMarketThresoldSize()
	{
		lock.readLock().lock();
        try {
        	return allMarketThresold.size();
        } finally {
            lock.readLock().unlock();
        }	
	}
	
	@Override
	public List<MarketThresold> getAllMarketThresold()
	{
		lock.readLock().lock();
        try {
        	return allMarketThresold;
        } finally {
            lock.readLock().unlock();
        }
	}
	
	
}
