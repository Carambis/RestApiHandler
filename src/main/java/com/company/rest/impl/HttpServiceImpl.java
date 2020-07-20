package com.company.rest.impl;

import com.company.rest.HttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Singleton
public class HttpServiceImpl implements HttpService {

	private final static Logger logger = LogManager.getLogger(HttpServiceImpl.class);
	private final HttpClient httpClient;

	@Inject
	public HttpServiceImpl(HttpClient httpClient) {

		this.httpClient = httpClient;
	}

	public String getDataFromRestApi(String url, String id) {

		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "/posts/" + id)).build();

		CompletableFuture<HttpResponse<String>> httpResponse = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

		return httpResponse.thenApply(response -> {
			logger.info("Status code: " + response.statusCode());
			return response.body();
		}).join();
	}

}
