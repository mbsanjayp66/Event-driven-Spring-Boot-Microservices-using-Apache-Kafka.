package com.sanjay.ws.products.Controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanjay.ws.products.Entity.CreateProductModel;
import com.sanjay.ws.products.Exception.ErrorMessage;
import com.sanjay.ws.products.Service.ProductService;
import com.sanjay.ws.products.ServiceImpl.ProductServiceImpl;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	public ProductService productService;
	
	@PostMapping
	public ResponseEntity<?>createProduct(@RequestBody CreateProductModel createProductModel){
		String productId  = "";
		try {
			productId = productService.createProduct(createProductModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(),e.getMessage(),"/products"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(productId);
	}

}
