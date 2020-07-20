package com.company.s3;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

public interface S3Service {

	void saveFileToS3Bucket(String bucketName, ByteArrayOutputStream file, String userId) throws ExecutionException, InterruptedException;
	void checkBucketExist(String bucketName);
}