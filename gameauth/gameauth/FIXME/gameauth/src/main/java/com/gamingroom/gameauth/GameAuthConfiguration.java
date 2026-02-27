package com.gamingroom.gameauth;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class GameAuthConfiguration extends Configuration {
	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();
	@JsonProperty("jerseyClient")
	public JerseyClientConfiguration getJerseyConfiguration() {
		return jerseyClient;
	}
	@JsonProperty("JerseyClient")
	public void setJerseyClientConfiguration(JerseyClientConfiguration JerseyClient) {
		this.jerseyClient = JerseyClient;
	}
}