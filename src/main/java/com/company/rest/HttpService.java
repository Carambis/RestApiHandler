package com.company.rest;

import com.company.dto.PostDto;
import com.company.exception.HttpRequestException;

public interface HttpService {

	PostDto getDataFromRestApi(String url, String id) throws HttpRequestException;

}
