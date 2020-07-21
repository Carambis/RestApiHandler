package com.company.handler.impl;

import com.company.exception.S3Exception;
import com.company.exception.SSMParameterStoreException;
import com.company.handler.RestApiHandler;
import com.company.rest.HttpService;
import com.company.s3.S3Service;
import com.company.ssm.impl.SSMServiceImpl;
import com.company.util.FileUtils;
import com.company.util.ParameterUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Singleton
public class RestApiHandlerImpl implements RestApiHandler {

	private final static Logger logger = LogManager.getLogger(RestApiHandlerImpl.class);
	private final static ObjectMapper mapper = new ObjectMapper();

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

		String id = input.get(ParameterUtils.ID);
		if (input.isEmpty() || Objects.isNull(id) || id.isEmpty()) {
			return "Incorrect input Json";
		}
		CompletableFuture<ByteArrayOutputStream> getDataFromRestApi = getDataFromRestApi(id);

		CompletableFuture<String> getBucketName = getBucketName();

		return getDataFromRestApi.thenCombine(getBucketName, (compress, bucketName) -> {
			String status = "Problem with json uploading";
			if (Objects.nonNull(compress) || Objects.nonNull(bucketName)) {
				try {
					s3Service.saveFileToS3Bucket(bucketName, compress, id);
					status = "Json upload finished successfully";
				} catch (S3Exception e) {
					logger.error(e.getMessage());
				}
			}
			return status;
		}).join();
	}

	private CompletableFuture<ByteArrayOutputStream> getDataFromRestApi(String id) {

		return CompletableFuture.supplyAsync(() -> {
			ByteArrayOutputStream byteArrayOutputStream = null;
			try {
				String url = getUrl();
				var post = httpService.getDataFromRestApi(url, id);
				byte[] bytes = mapper.writeValueAsBytes(post);
				byteArrayOutputStream = FileUtils.compressToGzip(bytes);
			} catch (Exception e) {
				logger.error("Incorrect Json from Rest api");
				logger.error(e.getMessage());
			}
			return byteArrayOutputStream;
		});
	}

	private CompletableFuture<String> getBucketName() {

		return CompletableFuture.supplyAsync(() -> {
			String bucketName = null;
			try {
				bucketName = getBucketNameFromParameterStore();
				boolean exist = s3Service.checkBucketExist(bucketName);
				if (!exist) {
					bucketName = null;
					logger.error("Bucket not found");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return bucketName;
		});
	}

	private String getUrl() throws SSMParameterStoreException {

		String restApiParameterName = System.getenv(ParameterUtils.REST_API_PARAMETER_NAME);
		return ssmService.getParameterFromParameterStore(restApiParameterName, true);
	}

	private String getBucketNameFromParameterStore() throws SSMParameterStoreException {

		String bucketParameterName = System.getenv(ParameterUtils.BUCKET_NAME);
		return ssmService.getParameterFromParameterStore(bucketParameterName, true);
	}

}
