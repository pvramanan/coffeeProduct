package com.pvr.braincoffee.product.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.pvr.braincoffee.product.model.Product;
import com.pvr.braincoffee.product.model.ProductEvent;
import com.pvr.braincoffee.product.repository.ProductRepository;
import com.pvr.braincoffee.product.service.HikariAsyncService;
import com.pvr.braincoffee.product.service.WebclientService;

import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Tag(value = "IntegrationTest")
public class ProductControllerTest {

	private WebTestClient testClient;

	private List<Product> productList;

	@Autowired
	private ProductRepository repository;

	@MockBean
	WebclientService test;

	@MockBean
	HikariAsyncService hikariAsyncService;

	@BeforeEach
	void beforeEach() {
		this.testClient = WebTestClient.bindToController(new ProductController(repository, test, hikariAsyncService))
				.configureClient().baseUrl("/products").build();

		this.productList = repository.findAll().collectList().block();
	}

	@Test
	void getAllProductTest() {
		this.testClient.get().uri("/").exchange().expectStatus().isOk().expectBodyList(Product.class)
				.isEqualTo(productList);
	}

	@Test
	void notFoundTest() {
		this.testClient.get().uri("/Prasanna").exchange().expectStatus().isNotFound();
	}

	@Test
	void idFoundTest() {
		this.testClient.get().uri("/{Id}", productList.get(0).getId()).exchange().expectStatus().isOk()
				.expectBody(Product.class).isEqualTo(productList.get(0));
	}

	@Test
	void testProductEvents() {
		ProductEvent event = new ProductEvent(0, "Product Event");

		FluxExchangeResult<ProductEvent> result = testClient.get().uri("/events").accept(MediaType.TEXT_EVENT_STREAM)
				.exchange().expectStatus().isOk().returnResult(ProductEvent.class);

		StepVerifier.create(result.getResponseBody()).expectNext(event).expectNextCount(2)
				.consumeNextWith(ev -> assertEquals(Long.valueOf(3), ev.getEventId())).thenCancel().verify();
	}

}
