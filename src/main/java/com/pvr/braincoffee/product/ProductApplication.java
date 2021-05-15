package com.pvr.braincoffee.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pvr.braincoffee.product.handler.ProductHandler;
import com.pvr.braincoffee.product.model.Product;
import com.pvr.braincoffee.product.repository.ProductRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository productRepository) {
		return args -> {
			Flux<Product> prodFlux = Flux
					.just(new Product(null, "Kumbokonam Coffee", 40), new Product(null, "Kumbokonam fliter Coffee", 60),
							new Product(null, "Kumbokonam degree Coffee", 80))
					.flatMap(productRepository::save);

			prodFlux.thenMany(productRepository.findAll()).subscribe(System.out::println);
		};
	}

	@Bean
	RouterFunction<ServerResponse> routes(ProductHandler handler) {
		return andRouteType(handler);
	}

	private RouterFunction<ServerResponse> andRouteType(ProductHandler handler) {
		return RouterFunctions
				.route(RequestPredicates.GET("/products").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
						handler::getAllProduct)
				.andRoute(RequestPredicates.POST("/products").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
						handler::saveProduct)
				.andRoute(
						RequestPredicates.DELETE("/products").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
						handler::deleteAllProducts)
				.andRoute(RequestPredicates.GET("/products/events")
						.and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)), handler::getProductEvent)
				.andRoute(RequestPredicates.GET("/products/{id}")
						.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)).and(RequestPredicates.path("{id}")),
						handler::getProduct)
				.andRoute(RequestPredicates.PUT("/products/{id}")
						.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)).and(RequestPredicates.path("{id}")),
						handler::updateProduct)
				.andRoute(RequestPredicates.DELETE("/products/{id}")
						.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)).and(RequestPredicates.path("{id}")),
						handler::deleteProduct);
	}
	
	private RouterFunction<ServerResponse> nestRouteType(ProductHandler handler) 
	{
		return RouterFunctions.nest(RequestPredicates.path("/products"),
				RouterFunctions.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON)
						.or(RequestPredicates.contentType(MediaType.APPLICATION_JSON))
						.or(RequestPredicates.contentType(MediaType.TEXT_EVENT_STREAM))
						,RouterFunctions.route(RequestPredicates.GET("/"),handler::getAllProduct)
						.andRoute(RequestPredicates.method(HttpMethod.POST), handler::saveProduct)
						.andRoute(RequestPredicates.method(HttpMethod.DELETE), handler::deleteAllProducts)
						.andRoute(RequestPredicates.GET("/events"), handler::getProductEvent)
						.andNest(RequestPredicates.path("/{id}"),
								RouterFunctions.route(RequestPredicates.method(HttpMethod.GET), handler::getProduct)
												.andRoute(RequestPredicates.method(HttpMethod.PUT), handler::updateProduct)
												.andRoute(RequestPredicates.method(HttpMethod.DELETE), handler::deleteProduct))));
						
				
				
	}

}
