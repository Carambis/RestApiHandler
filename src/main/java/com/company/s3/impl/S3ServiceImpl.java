package com.company.s3.impl;

import com.company.exception.S3Exception;
import com.company.s3.S3Service;
import com.company.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Singleton
public class S3ServiceImpl implements S3Service {

	private final static Logger logger = LogManager.getLogger(S3ServiceImpl.class);
	private final S3AsyncClient s3AsyncClient;

	@Inject
	public S3ServiceImpl(S3AsyncClient s3AsyncClient) {

		this.s3AsyncClient = s3AsyncClient;
	}

	public void saveFileToS3Bucket(String bucketName, ByteArrayOutputStream file, String userId) throws S3Exception{

		String folderPath = "posts/user=" + userId + StringUtils.RIGHT_SLASH + "posts" + StringUtils.RIGHT_SLASH;
		String fileName = folderPath + "posts_" + userId + StringUtils.UNDERSCORE + ZonedDateTime.now().toInstant().toEpochMilli() + ".json.gz";

		PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).build();
		try {
			s3AsyncClient.putObject(objectRequest, AsyncRequestBody.fromBytes(file.toByteArray())).get();
			logger.info("File: " + fileName + " uploads successfully");
		} catch (InterruptedException | ExecutionException e) {
			throw new S3Exception(e);
		}

	}

	public boolean checkBucketExist(String bucketName) throws S3Exception {

		List<Bucket> buckets = null;
		try {
			buckets = s3AsyncClient.listBuckets().get().buckets();
		} catch (InterruptedException | ExecutionException e) {
			throw new S3Exception(e);
		}
		return buckets.stream().allMatch(bucket -> bucket.name().equalsIgnoreCase(bucketName));
	}
}
