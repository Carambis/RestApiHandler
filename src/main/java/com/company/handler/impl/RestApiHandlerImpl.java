package com.company.handler.impl;

import com.company.handler.RestApiHandler;
import com.company.rest.HttpService;
import com.company.s3.S3Service;
import com.company.ssm.impl.SSMServiceImpl;
import com.company.util.FileUtils;
import com.company.util.ParameterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Singleton
public class RestApiHandlerImpl implements RestApiHandler {

	private final static Logger logger = LogManager.getLogger(RestApiHandlerImpl.class);

	private final S3Service s3Service;
	private final SSMServiceImpl ssmService;
	private final HttpService httpService;

	@Inject
	public RestApiHandlerImpl(S3Service s3Service, SSMServiceImpl ssmService, HttpService httpService) {

		this.s3Service = s3Service;
		this.ssmService = ssmService;
		this.httpService = httpService;
	}

	public String execute(Map<String, String> input) {

		String userId = input.get(ParameterUtils.ID);
		String status = "Ok";
		try {
			String url = getUrl();
			String json = httpService.getDataFromRestApi(url, userId);
			ByteArrayOutputStream compress = FileUtils.compressToGzip(json);
			String bucketName = getBucketName();
			s3Service.checkBucketExist(bucketName);
			s3Service.saveFileToS3Bucket(bucketName, compress, userId);
		} catch (Exception e) {
			logger.error(e);
			status = "Fail";
		}
		return status;
	}

	private String getUrl() {

		String restApiParameterName = System.getenv(ParameterUtils.REST_API_PARAMETER_NAME);
		return ssmService.getParameterFromParameterStore(restApiParameterName, true);
	}

	private String getBucketName() {

		String bucketParameterName = System.getenv(ParameterUtils.BUCKET_NAME);
		return ssmService.getParameterFromParameterStore(bucketParameterName, true);

	}

}
