package com.company.factory;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import javax.inject.Singleton;

@Factory
public class S3ClientFactory {

	@Singleton
	@Bean
	public S3AsyncClient s3AsyncClient(){
		return S3AsyncClient.builder().build();
	}

}
