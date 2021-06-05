package com.pvr.braincoffee.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.pvr.braincoffee.product.json.Country;

import reactor.core.publisher.Mono;

@Async
@Service
public class HikariAsyncService {

	@Autowired
	WebclientService webclientService;

	public ListenableFuture<Void> processData() {
		System.out.println("PRasanna");
		webclientService.getHikariDemoCountry();
		System.out.println("Sanjay");

		Mono<Country> india = webclientService.getCountryID("INR");
		Mono<Country> nigeria = webclientService.getCountryID("NGA");
		Mono<Country> russia = webclientService.getCountryID("USSR");
		Mono<String> prasanna=Mono.just("SUCCESS");

		Mono.zip(india, nigeria,russia,prasanna).flatMap(data -> 
		{
			System.out.println("DATA Tupple ::" + data.getT1().getCountry_name());
			System.out.println("DATA Tupple ::" + data.getT2().getCountry_name());
			System.out.println("DATA Tupple ::" + data.getT3().getCountry_name());
			System.out.println("DATA Tupple ::" + data.getT4());
			return Mono.just("SUCCESS");
		}).subscribe(data->System.out.println("data :: "+data));
		return new AsyncResult<Void>(null);
	}

}
