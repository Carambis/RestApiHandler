package com.company.factory;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.net.http.HttpClient;
import java.time.Duration;

@Factory
public class HttpClientFactory {

	@Singleton
	@Bean
	public HttpClient httpClient() {

		return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build();
	}
}
