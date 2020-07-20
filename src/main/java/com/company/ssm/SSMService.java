package com.company.ssm;

public interface SSMService {

	String getParameterFromParameterStore(String parameterName, boolean decrypt);

}
