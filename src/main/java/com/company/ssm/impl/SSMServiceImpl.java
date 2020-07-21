package com.company.ssm.impl;

import com.company.exception.SSMParameterStoreException;
import com.company.ssm.SSMService;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

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

	public String getParameterFromParameterStore(String parameterName, boolean decrypt) throws SSMParameterStoreException {

		String value = null;
		GetParameterRequest request = GetParameterRequest.builder().name(parameterName).withDecryption(decrypt).build();

		GetParameterResponse getParameterResponse = null;
		try {
			getParameterResponse = ssmAsyncClient.getParameter(request).get();
			value = getParameterResponse.parameter().value();
		} catch (InterruptedException | ExecutionException e) {
			throw new SSMParameterStoreException(e);
		}
		return value;
	}

}
