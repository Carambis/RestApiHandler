package com.company;

import com.company.handler.impl.RestApiHandlerImpl;
import io.micronaut.function.aws.MicronautRequestHandler;

import javax.inject.Inject;
import java.util.Map;

public class Main extends MicronautRequestHandler<Map<String, String>, String> {

	@Inject
	private RestApiHandlerImpl restApiHandlerImpl;

	@Override
	public String execute(Map<String, String> input) {

		return restApiHandlerImpl.execute(input);
	}
}
