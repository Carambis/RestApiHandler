package com.company.ssm.impl;

import com.company.ssm.SSMService;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;

@Singleton
public class SSMServiceImpl implements SSMService {

	private final SsmAsyncClient ssmAsyncClient;

	@Inject
	public SSMServiceImpl(SsmAsyncClient ssmAsyncClient) {

		this.ssmAsyncClient = ssmAsyncClient;
	}

	public String getParameterFromParameterStore(String parameterName, boolean decrypt) {

		GetParameterRequest request = GetParameterRequest.builder().name(parameterName).withDecryption(decrypt).build();

		GetParameterResponse getParameterResponse = ssmAsyncClient.getParameter(request).join();
		Parameter parameter = getParameterResponse.parameter();
		return parameter.value();
	}

}
