package com.company.rest.impl;

import com.company.dto.PostDto;
import com.company.exception.HttpRequestException;
import com.company.rest.HttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class HttpServiceImpl implements HttpService {

	private final HttpClient httpClient;
	private static final ObjectMapper MAPPER = new ObjectMapper();


	@Inject
	public HttpServiceImpl(HttpClient httpClient) {

		this.httpClient = httpClient;
	}

	public PostDto getDataFromRestApi(String url, String id) throws HttpRequestException {

		PostDto postDto = null;
		HttpResponse<String> httpResponse = null;
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "/posts/" + id)).build();
		try {
			httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			String body = httpResponse.body();
			postDto = MAPPER.readValue(body, PostDto.class);
		} catch (IOException | InterruptedException e) {
			throw new HttpRequestException(e);
		}

		return postDto;
	}

}
