package com.company.ssm;

import com.company.exception.SSMParameterStoreException;

public interface SSMService {

	String getParameterFromParameterStore(String parameterName, boolean decrypt) throws SSMParameterStoreException;

}
