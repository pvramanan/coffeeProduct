package com.pvr.braincoffee.product.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pvr.braincoffee.product.model.Product;
import com.pvr.braincoffee.product.model.ProductEvent;
import com.pvr.braincoffee.product.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductRepository productRepository;

	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping
	public Flux<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@GetMapping("{id}")
	public Mono<ResponseEntity<Product>> getProduct(@PathVariable String id) {
		return productRepository.findById(id).map(product -> ResponseEntity.ok(product))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Mono<Product> saveProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}

	@PutMapping("{id}")
	public Mono<ResponseEntity<Product>> updateProduct(@PathVariable(value = "id") String id,
			@RequestBody Product product) {
		return productRepository.findById(id).flatMap((actualproduct) -> {
			actualproduct.setName(product.getName());
			actualproduct.setPrice(product.getPrice());
			return productRepository.save(actualproduct);
		}).map(updatedproduct -> ResponseEntity.ok(updatedproduct)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("{id}")
	public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable(value = "id") String id) {
		return productRepository.findById(id)
				.flatMap(exsistingProduct -> productRepository.delete(exsistingProduct)
						.then(Mono.just(ResponseEntity.ok().<Void>build())))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@DeleteMapping
	public Mono<Void> deleteAllProducts() {
		return productRepository.deleteAll();
	}

	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductEvent> getProductEvent() {
		return Flux.interval(Duration.ofSeconds(5)).map(val -> new ProductEvent(val, "Product Event"));
	}

}
