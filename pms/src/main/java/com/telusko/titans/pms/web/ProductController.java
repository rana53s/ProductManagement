package com.telusko.titans.pms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.telusko.titans.pms.dto.ProductDto;
import com.telusko.titans.pms.exceptions.ProductNotFoundException;
import com.telusko.titans.pms.service.IProductService;

import java.util.List;

@RestController
public class ProductController {

	@Autowired
	IProductService service;

	@GetMapping("/products")
	ResponseEntity<Page<ProductDto>> getAllProducts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<ProductDto> dto = service.getAllProducts(pageable);
		return new ResponseEntity<Page<ProductDto>>(dto, HttpStatus.OK);
	}

	@GetMapping("/product/{id}")
	ResponseEntity<ProductDto> getProduct(@PathVariable int id) {
		ProductDto dto = service.getProductById(id);
		if (dto == null) {
			throw new ProductNotFoundException("Product with Id " + id + " does not exist");
		}
		return new ResponseEntity<ProductDto>(dto, HttpStatus.OK);

	}

	@PostMapping("/product")
	ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto product) {
		ProductDto dto = service.addProduct(product);
		return new ResponseEntity<ProductDto>(dto, HttpStatus.OK);
	}

	/*
	Search by brand (brand pattern is also valid search)
	Page of products of a particular brand will be returned from DB
	Used path parameter to fetch particular brand products
	*/

	@GetMapping("/search-by/brand/{brandName}")
	public ResponseEntity<Page<ProductDto>> searchProductsByBrand(@PathVariable("brandName") String brandName, @PageableDefault(size = 10 , page = 0)Pageable pageable){
		Page<ProductDto> responsePageDto = service.searchProductsByBrand(brandName,pageable);
		if(responsePageDto.getTotalElements() > 0){
			return new ResponseEntity<>(responsePageDto, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(Page.empty(), HttpStatus.NO_CONTENT);
		}
	}
}
