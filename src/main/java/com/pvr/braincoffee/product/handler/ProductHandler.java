package com.pvr.braincoffee.product.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pvr.braincoffee.product.model.Product;
import com.pvr.braincoffee.product.model.ProductEvent;
import com.pvr.braincoffee.product.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

	private ProductRepository productRepository;

	@Autowired
	public ProductHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Mono<ServerResponse> getAllProduct(ServerRequest serverRequest) {
		Flux<Product> products = productRepository.findAll();

		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Product.class);
	}

	public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		Mono<Product> productMono = productRepository.findById(id);

		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		return productMono
				.flatMap(
						product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(product)))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {
		Mono<Product> productMono = serverRequest.bodyToMono(Product.class);

		return productMono.flatMap(product -> ServerResponse.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON).body(productRepository.save(product), Product.class));

	}

	public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Product> existingProductMono = productRepository.findById(id);
		Mono<Product> updateProductMono = serverRequest.bodyToMono(Product.class);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		return updateProductMono.zipWith(existingProductMono, (updateProduct, existingProduct) -> {
			return new Product(existingProduct.getId(), updateProduct.getName(), updateProduct.getPrice());
		}).flatMap(prd -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(productRepository.save(prd), Product.class).switchIfEmpty(notFound));

	}

	public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) 
	{
		return productRepository.findById(serverRequest.pathVariable("id"))
				.flatMap(product->
				ServerResponse.ok()
				.build(productRepository.delete(product))
				.switchIfEmpty(ServerResponse.notFound().build()));
	}

	public Mono<ServerResponse> deleteAllProducts(ServerRequest serverRequest) 
	{
		return ServerResponse.ok().build(productRepository.deleteAll());
	}
	
	
	public Mono<ServerResponse> getProductEvent(ServerRequest serverRequest) 
	{
		Flux<ProductEvent> productEventFlux= Flux.interval(Duration.ofSeconds(2))
				.map(val -> new ProductEvent(val, "Product Event"));
		
		return  ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(productEventFlux,ProductEvent.class);
	}
	
	

}
