package com.pvr.braincoffee.product.service;

import com.pvr.braincoffee.product.json.Country;

import reactor.core.publisher.Mono;

public interface WebclientService {

	
	public void getHikariDemoCountry();
	
	public Mono<Country> getCountryID(String countryId);
	
}
