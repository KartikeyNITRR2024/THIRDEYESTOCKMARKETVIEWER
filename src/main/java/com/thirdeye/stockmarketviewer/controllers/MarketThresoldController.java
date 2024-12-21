package com.thirdeye.stockmarketviewer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.stockmarketviewer.services.impl.MarketThresoldServiceImpl;
import com.thirdeye.stockmarketviewer.utils.AllMicroservicesData;

@RestController
@RequestMapping("/api/updatemarketthresold")
public class MarketThresoldController {

    @Autowired
    AllMicroservicesData allMicroservicesData;

    @Value("${uniqueMachineName}")
    private String uniqueMachineName;

    @Autowired
    MarketThresoldServiceImpl marketThresoldService;

    private static final Logger logger = LoggerFactory.getLogger(MarketThresoldController.class);

    @PostMapping("/{uniqueId}/{uniqueMachineCode}")
    public ResponseEntity<Boolean> updateHoldedStockData(@PathVariable("uniqueId") Integer pathUniqueId, 
                                                @PathVariable("uniqueMachineCode") Integer pathUniqueMachineCode) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId()) &&
            pathUniqueMachineCode.equals(allMicroservicesData.allMicroservices.get(uniqueMachineName).getMicroserviceUniqueId())) {
            try {
            	marketThresoldService.getUpdatedAllMarketThresold();
            	logger.info("Updated all market thresold successfull");
			} catch (Exception e) {
				logger.info("Fail update all market thresold successfull");
				return ResponseEntity.ok(false);
			}
            return ResponseEntity.ok(true);
        } else {
            logger.warn("Status check for uniqueId {} or uniqueMachineCode {}: Not Found", 
                        pathUniqueId, pathUniqueMachineCode);
            return ResponseEntity.ok(false);
        }
    }
}
