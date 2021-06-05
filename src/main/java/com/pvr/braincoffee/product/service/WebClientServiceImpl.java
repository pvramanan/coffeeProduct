package com.pvr.braincoffee.product.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pvr.braincoffee.product.json.Country;

import reactor.core.publisher.Mono;

@Service
public class WebClientServiceImpl implements WebclientService {

	@Override
	public void getHikariDemoCountry() {

		System.out.println(Thread.currentThread().getId() +  "came inside ");
		Mono<List<Country>> countryListStream = WebClient.builder().baseUrl("http://localhost:8000").build().get()
				.uri("/noauth/countries").retrieve().bodyToMono(new ParameterizedTypeReference<List<Country>>() {
				}).doOnError(tet -> {
					tet.printStackTrace();
				});

		countryListStream.doOnNext(countryList -> {
			countryList.forEach(country -> {
				System.out.println(Thread.currentThread().getId() + "Country data :: " + country.getCountry_id());
				getCountryID(country.getCountry_id()).subscribe(test->System.out.println("Country data ID ::" + test.getCountry_id()));
				System.out.println(Thread.currentThread().getId() + "Country OUTSIDE data");
			});
		}).subscribe();

		System.out.println(Thread.currentThread().getId() + "came outside ");
	}

	public Mono<Country> getCountryID(String countryId) {
		System.out.println(Thread.currentThread().getId() + " :: inside the getCountryID ");
		Mono<Country> countryListStream = WebClient.builder().baseUrl("http://localhost:8000").build().get()
				.uri("/noauth/countries/{id}", countryId).retrieve().bodyToMono(Country.class).doOnError(tet -> {
					tet.printStackTrace();
				}).doOnSuccess(country -> System.out.println(Thread.currentThread().getId() + "getCountryID :: " +country.getCountry_name()));
		System.out.println(Thread.currentThread().getId() + " :: outside the getCountryID ");
		return countryListStream;
	}

}
