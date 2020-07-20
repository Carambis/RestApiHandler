package com.company.factory;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;

import javax.inject.Singleton;

@Factory
public class SsmClientFactory {

	@Singleton
	@Bean
	public SsmAsyncClient ssmAsyncClient(){
		return SsmAsyncClient.builder().build();
	}

}
