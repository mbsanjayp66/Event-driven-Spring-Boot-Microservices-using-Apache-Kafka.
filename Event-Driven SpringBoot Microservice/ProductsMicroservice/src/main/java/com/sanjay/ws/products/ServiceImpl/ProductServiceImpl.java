package com.sanjay.ws.products.ServiceImpl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.sanjay.ws.core.ProductCreatedEvent;
import com.sanjay.ws.products.Entity.CreateProductModel;
import com.sanjay.ws.products.Service.ProductService;


@Service
public class ProductServiceImpl implements ProductService{
	
	private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

	@Override
	public String createProduct(CreateProductModel createProductModel) throws Exception{
		// TODO Auto-generated method stub
		String productId = UUID.randomUUID().toString();
		
		//persisit the data into database
		
		ProductCreatedEvent productCreatedEvent = 
				new ProductCreatedEvent(productId, createProductModel.getTitle(), createProductModel.getPrice(), createProductModel.getQuantity());
		
		
		logger.info("Before Send:");
		SendResult<String, ProductCreatedEvent> sendResult =
				kafkaTemplate.send("product-created-events-topic3", productId, productCreatedEvent).get();
		
		logger.info("Partition:"+sendResult.getRecordMetadata().partition());
		logger.info("topic:"+sendResult.getRecordMetadata().topic());
		
		logger.info("** Returning Product id");
		return productId;
	}

}
