package com.company.s3;

import com.company.exception.S3Exception;

import java.io.ByteArrayOutputStream;

public interface S3Service {

	void saveFileToS3Bucket(String bucketName, ByteArrayOutputStream file, String userId) throws S3Exception;

	boolean checkBucketExist(String bucketName) throws S3Exception;
}