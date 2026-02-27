package com.gamingroom.gameauth;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gamingroom.gameauth.auth.GameAuthenticator;
import com.gamingroom.gameauth.auth.GameAuthorizer;
import com.gamingroom.gameauth.auth.GameUser;

import com.gamingroom.gameauth.controller.GameUserRESTController;
import com.gamingroom.gameauth.controller.RESTClientController;

import com.gamingroom.gameauth.healthcheck.AppHealthCheck;
import com.gamingroom.gameauth.healthcheck.HealthCheckController;


//Application<Configuration> changed to GameAuthConfiguration... caused the resource to not be registered 
public class GameAuthApplication extends Application<GameAuthConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameAuthApplication.class);

	@Override
	public void initialize(Bootstrap<GameAuthConfiguration> b) {
	}
	
	public void run(GameAuthConfiguration c, Environment e) throws Exception 
	{
		
		LOGGER.info("Registering REST resources");
 
		//register GameUserRESTController 
		e.jersey().register(new GameUserRESTController(e.getValidator()));
		
		
		


		// FIXM: Create io.dropwizard.client.JerseyClientBuilder instance and give it io.dropwizard.setup.Environment reference (based on BasicAuth Security Example)
		Client client = new JerseyClientBuilder(e).using(c.getJerseyConfiguration()).build(getName());
		
		

		// Application health check
		e.healthChecks().register("APIHealthCheck", new AppHealthCheck(client));

		// Run multiple health checks
		e.jersey().register(new HealthCheckController(e.healthChecks()));
		
		//Setup Basic Security
		e.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<GameUser>()
                .setAuthenticator(new GameAuthenticator())
                .setAuthorizer(new GameAuthorizer())
                .setRealm("App Security")
                .buildAuthFilter()));
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(GameUser.class));
        e.jersey().register(RolesAllowedDynamicFeature.class);
	}

	public static void main(String[] args) throws Exception {
		new GameAuthApplication().run(args);
	}
/*
	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		// TODO Auto-generated method stub
		
	}
	*/

	
}