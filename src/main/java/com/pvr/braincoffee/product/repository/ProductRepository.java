package com.pvr.braincoffee.product.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pvr.braincoffee.product.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
