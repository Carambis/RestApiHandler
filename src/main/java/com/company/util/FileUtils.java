package com.company.util;

import com.company.exception.CompressionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class FileUtils {

	public static ByteArrayOutputStream compressToGzip(byte[] json) throws CompressionException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (json == null || json.length == 0) {
			return null;
		}
		try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
			gzip.write(json);
		} catch (IOException e) {
			throw new CompressionException(e);
		}
		return out;
	}
}
