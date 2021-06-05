package com.pvr.braincoffee.product.json;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

	@ApiModelProperty(notes = "ISO of country id", required = true, accessMode = AccessMode.READ_ONLY)
	@JsonProperty("country_id")
	private final String country_id;

	@ApiModelProperty(notes = "ISO of country name", required = true, accessMode = AccessMode.READ_ONLY)
	@JsonProperty("country_name")
	private final String country_name;

	@ApiModelProperty(notes = "created date", required = true, accessMode = AccessMode.READ_ONLY)
	@JsonProperty("createdDate")
	private final LocalDate createdDate;

	@JsonCreator
	public Country(@JsonProperty("country_id") String country_id, @JsonProperty("country_name") String country_name,
			@JsonProperty("createdDate") LocalDate createdDate) {
		super();
		this.country_id = country_id;
		this.country_name = country_name;
		this.createdDate = createdDate;
	}

	public String getCountry_id() {
		return country_id;
	}

	public String getCountry_name() {
		return country_name;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

}
