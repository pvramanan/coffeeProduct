package com.pvr.braincoffee.product;

import javax.naming.OperationNotSupportedException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
					.just(new Product(null, "Kumbokonam Coffee", 40), 
							new Product(null, "Kumbokonam fliter Coffee", 60),
							new Product(null, "Kumbokonam degree Coffee", 80))
					.flatMap(productRepository::save);
			
			prodFlux.thenMany(productRepository.findAll()).subscribe(System.out::println);
		};

	}

}
