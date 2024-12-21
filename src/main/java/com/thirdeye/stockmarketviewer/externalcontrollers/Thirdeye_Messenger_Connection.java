package com.thirdeye.stockmarketviewer.externalcontrollers;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;
import com.thirdeye.stockmarketviewer.utils.AllMicroservicesData;

@Service
public class Thirdeye_Messenger_Connection {
    
	@Value("${messengingMicroservice}")
    private String messengingMicroservice;
    
    @Autowired
	AllMicroservicesData allMicroservicesData;

    @Autowired
    private RestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(Thirdeye_Messenger_Connection.class);
    
    public void sendLiveStockPayload(LiveStockPayload liveStockPayload) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "application/json");
//            HttpEntity<Object> request = new HttpEntity<>(liveStockPayload, headers);
//
//            // Send POST request
////            logger.info("sending live Stock Payload To messenger: ");
//            ResponseEntity<Boolean> response = restTemplate.exchange(
//                allMicroservicesData.allMicroservices.get(messengingMicroservice).getMicroserviceUrl() + "api/marketviewermessenger/" + allMicroservicesData.allMicroservices.get(messengingMicroservice).getMicroserviceUniqueId(),
//                HttpMethod.POST,
//                request,
//                Boolean.class
//            );
//
//            // Return the response status
////            logger.info("Successfully live Stock Payload To messenger: ");
//            return response.getBody() != null && response.getBody();
//        
//        } catch (Exception e) {
//            logger.error("Error while sending live Stock Payload To messenger: ", e);
//            return false;
//        }
    	
    	CompletableFuture.runAsync(() -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                HttpEntity<Object> request = new HttpEntity<>(liveStockPayload, headers);

                // Send POST request without waiting for a response
                restTemplate.exchange(
                    allMicroservicesData.allMicroservices.get(messengingMicroservice).getMicroserviceUrl() + "api/marketviewermessenger/" + allMicroservicesData.allMicroservices.get(messengingMicroservice).getMicroserviceUniqueId(),
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
