package com.thirdeye.stockmarketviewer.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.thirdeye.stockmarketviewer.entity.MarketThresold;
import com.thirdeye.stockmarketviewer.externalcontrollers.Thirdeye_Messenger_Connection;
import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;
import com.thirdeye.stockmarketviewer.pojos.PriceTimestampPojo;
import com.thirdeye.stockmarketviewer.pojos.ProfitDetails;
import com.thirdeye.stockmarketviewer.services.impl.MarketThresoldServiceImpl;

@Service
public class StocksPriceChangesCalculator {
	
	private static final Logger logger = LoggerFactory.getLogger(StocksPriceChangesCalculator.class);
	
	@Autowired
	MarketThresoldServiceImpl marketThresoldImpl;
	
	@Autowired
	Thirdeye_Messenger_Connection thirdeye_Messenger_Connection;
	
    @Value("${limitInTimeSeconds}")
    private Integer limitInTimeSeconds;
	
    public PriceTimestampPojo findNearestTimestamp(List<PriceTimestampPojo> pastData, Timestamp searchTime) {
//		logger.info("In function findNearestTimestamp");
        int left = 0;
        int right = pastData.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Timestamp midTime = pastData.get(mid).getTime();
            if (midTime.before(searchTime)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left < pastData.size() && right >= 0) {
            long diffLeft = Math.abs(pastData.get(left).getTime().getTime() - searchTime.getTime());
            long diffRight = Math.abs(pastData.get(right).getTime().getTime() - searchTime.getTime());
            return diffLeft < diffRight ? pastData.get(left) : pastData.get(right);
        } else if (left < pastData.size()) {
            return pastData.get(left);
        } else if (right >= 0) {
            return pastData.get(right);
        } else {
            return null;
        }
    }
    
//    @Async("LiveStockAsynchThread")
//    public CompletableFuture<Void> calculateChanges(LiveStockPayload liveStockPayload, List<PriceTimestampPojo> pastData) {
////        logger.info("In function calculateChanges");
//        
//        if (pastData == null) {
//            logger.info("Returning because past data is null");
//            return CompletableFuture.completedFuture(null);
//        }
//
//        List<MarketThresold> allMarketThresold = new ArrayList<>(marketThresoldImpl.getAllMarketThresold());
//        for (MarketThresold marketThresold : allMarketThresold) {
//            Timestamp timeToCheckFor = new Timestamp(liveStockPayload.getTime().getTime() - marketThresold.getThresoldTime() * 1000);
//            PriceTimestampPojo pojoToCheckFor = findNearestTimestamp(pastData, timeToCheckFor);
//            
//            if (pojoToCheckFor == null) {
//                return CompletableFuture.completedFuture(null);
//            }
//
//            double gapIs = liveStockPayload.getPrice() - pojoToCheckFor.getPrice();
//            if (marketThresold.getThresoldType() == 0) {
//                gapIs = (gapIs / pojoToCheckFor.getPrice()) * 100;
//            }
//
//            if (gapIs >= marketThresold.getThresoldPrice()) {
//                if (liveStockPayload.getProfitDetailsList() == null) {
//                    liveStockPayload.setProfitDetailsList(new ArrayList<>());
//                }
//
//                ProfitDetails profitDetail = new ProfitDetails(
//                    marketThresold.getUserId(), 
//                    gapIs, 
//                    marketThresold.getThresoldType(),
//                    (liveStockPayload.getTime().getTime() - pojoToCheckFor.getTime().getTime())/1000
//                );
//                liveStockPayload.getProfitDetailsList().add(profitDetail);
//            }
//        }
//
//        if (liveStockPayload.getProfitDetailsList() != null) {
//            logger.info("Calling Messenger Services");
//        	thirdeye_Messenger_Connection.sendLiveStockPayload(liveStockPayload);
//        	
//        }
////        logger.info("Returning from Calculater");
//        // Return completed future to indicate completion of async task
//        return CompletableFuture.completedFuture(null);
//    }
    
    @Async("LiveStockAsynchThread")
    public CompletableFuture<Void> calculateChanges(LiveStockPayload liveStockPayload, List<PriceTimestampPojo> pastData) {
        if (pastData == null) {
            logger.info("Returning because past data is null");
            return CompletableFuture.completedFuture(null);
        }

        List<MarketThresold> allMarketThresold = new ArrayList<>(marketThresoldImpl.getAllMarketThresold());
        for (MarketThresold marketThresold : allMarketThresold) {
            Timestamp timeToCheckFor = new Timestamp(liveStockPayload.getTime().getTime() - marketThresold.getThresoldTime() * 1000);
            PriceTimestampPojo pojoToCheckFor = findNearestTimestamp(pastData, timeToCheckFor);
            
            if (pojoToCheckFor == null) {
                return CompletableFuture.completedFuture(null);
            }

            double gapIs = liveStockPayload.getPrice() - pojoToCheckFor.getPrice();
            if (marketThresold.getThresoldType() == 0) {
                gapIs = (gapIs / pojoToCheckFor.getPrice()) * 100;
            }
            
            boolean timeLimit = false;
            Long upperLimit = marketThresold.getThresoldTime() + limitInTimeSeconds;
            Long lowerLimit = marketThresold.getThresoldTime() - limitInTimeSeconds;
            Long currentTimeGap = (liveStockPayload.getTime().getTime() - pojoToCheckFor.getTime().getTime()) / 1000;
            
            if(currentTimeGap<=upperLimit && currentTimeGap>=lowerLimit)
            {
            	timeLimit = true;
            }

            if (gapIs >= marketThresold.getThresoldPrice() && timeLimit) {
                if (liveStockPayload.getProfitDetailsList() == null) {
                    liveStockPayload.setProfitDetailsList(new ArrayList<>());
                }

                ProfitDetails profitDetail = new ProfitDetails(
                    marketThresold.getUserId(), 
                    gapIs, 
                    marketThresold.getThresoldType(),
                    currentTimeGap,
                    pojoToCheckFor.getPrice(), liveStockPayload.getPrice()
                );
                liveStockPayload.getProfitDetailsList().add(profitDetail);
            }
        }

        if (liveStockPayload.getProfitDetailsList() != null) {
            logger.info("Calling Messenger Services");
            thirdeye_Messenger_Connection.sendLiveStockPayload(liveStockPayload);
        }

        // No need to return CompletableFuture since it's handled asynchronously
        return CompletableFuture.completedFuture(null);
    }


}
