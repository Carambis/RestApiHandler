package com.company.factory;

import com.company.exception.SSMParameterStoreException;
import com.company.ssm.SSMService;
import com.company.util.ParameterUtils;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class S3ClientFactory {

	@Inject
	private SSMService ssmService;

	@Singleton
	@Bean
	public S3AsyncClient s3AsyncClient() throws SSMParameterStoreException {

		String accessKey = ssmService.getParameter(ParameterUtils.ACCESS_KEY, true);
		String secretAccessKey = ssmService.getParameter(ParameterUtils.SECRET_ACCESS_KEY, true);

		StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretAccessKey));

		return S3AsyncClient.builder().region(Region.US_EAST_1).credentialsProvider(staticCredentialsProvider).build();
	}

}
