package com.sanjay.ws.emailnotification.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.sanjay.ws.core.ProductCreatedEvent;
import com.sanjay.ws.emailnotification.Exception.NotRetryableException;
import com.sanjay.ws.emailnotification.Exception.RetryableException;

@Component
@KafkaListener(topics = "product-created-events-topic3")
public class ProductCreatedEventHandler {
	
	private final Logger logger = LoggerFactory.getLogger(ProductCreatedEventHandler.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@KafkaHandler
	public void handle(ProductCreatedEvent productCreatedEvent) throws NotRetryableException {
		logger.info("A message Received:"+productCreatedEvent.getTitle());
		String reqUrl = "http://localhost:8082/response/200";
		try {
			ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.GET,null,String.class);
			if(response.getStatusCode().value()==HttpStatus.OK.value()) {
				logger.info("Received Response from a remote Service:"+response.getBody());
			}
		} catch (ResourceAccessException e) {
			logger.error(e.getMessage());
			throw new RetryableException(e);
		} catch (HttpServerErrorException e) {
			logger.error(e.getMessage());
			throw new NotRetryableException(e);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			throw new NotRetryableException(e);
		}
		
	}
}
