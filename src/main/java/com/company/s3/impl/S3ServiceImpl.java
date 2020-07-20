package com.company.s3.impl;

import com.company.s3.S3Service;
import com.company.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class S3ServiceImpl implements S3Service {

	private final static Logger logger = LogManager.getLogger(S3ServiceImpl.class);
	private final S3AsyncClient s3AsyncClient;

	@Inject
	public S3ServiceImpl(S3AsyncClient s3AsyncClient) {

		this.s3AsyncClient = s3AsyncClient;
	}

	public void saveFileToS3Bucket(String bucketName, ByteArrayOutputStream file, String userId) {

		String folderPath = userId + StringUtils.RIGHT_SLASH + "posts" + StringUtils.RIGHT_SLASH;
		createFolderInBucket(bucketName, folderPath);
		String fileName = folderPath + "posts_" + userId + StringUtils.UNDERSCORE + ZonedDateTime.now().toInstant().toEpochMilli() + ".json.gz";

		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).build();
		s3AsyncClient.putObject(objectRequest, AsyncRequestBody.fromBytes(file.toByteArray())).whenComplete((putObjectResponse, throwable) -> {
			if (putObjectResponse != null) {
				logger.info("Status code: " + putObjectResponse.sdkHttpResponse().statusCode());
			} else {
				logger.error(throwable);
			}
		}).join();

	}

	private void createFolderInBucket(String bucketName, String folderPath) {

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(folderPath).build();
		s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes()).exceptionally(throwable -> {
			PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(folderPath).build();
			PutObjectResponse join = s3AsyncClient.putObject(objectRequest, AsyncRequestBody.empty()).join();
			System.out.println(join);
			return null;
		}).join();

	}

	public void checkBucketExist(String bucketName) {

		List<Bucket> buckets = s3AsyncClient.listBuckets().join().buckets();
		long count = buckets.stream().filter(bucket -> bucket.name().equalsIgnoreCase(bucketName)).count();
		if (count == 0) {
			CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
			CreateBucketResponse createBucketResponse = s3AsyncClient.createBucket(createBucketRequest).join();
			logger.info("Bucket location: " + createBucketResponse.location());
		}
	}
}
