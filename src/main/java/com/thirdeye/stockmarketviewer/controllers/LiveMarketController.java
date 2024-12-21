package com.thirdeye.stockmarketviewer.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;
import com.thirdeye.stockmarketviewer.pojos.ResponsePayload;
import com.thirdeye.stockmarketviewer.services.impl.LiveMarketServiceImpl;
import com.thirdeye.stockmarketviewer.utils.AllMicroservicesData;
import com.thirdeye.stockmarketviewer.utils.TimeManagementUtil;

@RestController
@RequestMapping("/api/livemarket")
public class LiveMarketController {
    
    @Autowired
	AllMicroservicesData allMicroservicesData;
    
    @Value("${uniqueMachineCode}")
    private String uniqueMachineCode;
    
    @Autowired
    LiveMarketServiceImpl liveMarketServiceImpl;
    
    @Autowired
    TimeManagementUtil timeManagementUtil;
    
    private static final Logger logger = LoggerFactory.getLogger(LiveMarketController.class);

    @PostMapping("/{uniqueId}/{uniqueMachineCode}")
    public ResponseEntity<ResponsePayload> liveMarketData(@PathVariable("uniqueId") Integer pathUniqueId, @PathVariable("uniqueMachineCode") String pathUniqueMachineCode, @RequestBody List<LiveStockPayload> liveStockData) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
//        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId()) && pathUniqueMachineCode.equals(uniqueMachineCode)) {
        	logger.info("Status check for uniqueId {}: Found and uniqueMachineCode {}: Found", allMicroservicesData.current.getMicroserviceUniqueId(), pathUniqueMachineCode);
        	Timestamp currenttime = timeManagementUtil.getCurrentTime();
        	logger.info("Current Iteration time : {}", currenttime);
            boolean updateMachine = false;
            if(timeManagementUtil.giveAccess(currenttime))
            {
                updateMachine = liveMarketServiceImpl.processListMarketData(liveStockData, pathUniqueMachineCode);
            }
            else
            {
            	logger.info("Invalid Time");
            }
            logger.info("Next Iteration time : {}", timeManagementUtil.getNextIterationTime(currenttime));
            return ResponseEntity.ok(new ResponsePayload(timeManagementUtil.getNextIterationTime(currenttime),updateMachine));
        } else {
            logger.warn("Status check for uniqueId {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
}
