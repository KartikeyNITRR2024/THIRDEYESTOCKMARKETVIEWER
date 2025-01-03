package com.thirdeye.stockmarketviewer.externalcontrollers;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;
import com.thirdeye.stockmarketviewer.pojos.LiveStockProcesserPayload;
import com.thirdeye.stockmarketviewer.utils.AllMicroservicesData;

@Service
public class Thirdeye_Processer {
	@Value("${processingMicroservice}")
    private String processingMicroservice;
    
    @Autowired
	AllMicroservicesData allMicroservicesData;

    @Autowired
    private RestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(Thirdeye_Processer.class);
    
    public void sendLiveStockProcesserPayload(LiveStockProcesserPayload liveStockProcesserPayload) {
    	CompletableFuture.runAsync(() -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                HttpEntity<Object> request = new HttpEntity<>(liveStockProcesserPayload, headers);

                // Send POST request without waiting for a response
                restTemplate.exchange(
                    allMicroservicesData.allMicroservices.get(processingMicroservice).getMicroserviceUrl() + "api/marketviewerprocesser/processdata/" + allMicroservicesData.allMicroservices.get(processingMicroservice).getMicroserviceUniqueId(),
                    HttpMethod.POST,
                    request,
                    Boolean.class
                );

                // Optionally log success
                logger.info("Successfully sent live stock payload to messenger");

            } catch (Exception e) {
                logger.error("Error while sending live stock payload to messenger: ", e);
            }
        });
    }
}
