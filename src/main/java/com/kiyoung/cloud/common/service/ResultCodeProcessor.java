package com.kiyoung.cloud.common.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.kiyoung.cloud.common.service.DefaultResultCodeMaker;
import com.kiyoung.cloud.common.service.ResultCodeMaker;

/**
 * Channel 호출 후 Result 코드를 세팅하기 위한 Processor
 * 
 * @author Kiyoung
 * @since 2019.10.15
 * @version 1.0
 */
public class ResultCodeProcessor implements Processor {

	/** 처리를 위한 Class 세팅 , Injection으로 세팅된 값이 없을 경우 기본 Class를 사용함 */
	@Autowired(required = false)
	ResultCodeMaker resultCodeMaker = new DefaultResultCodeMaker();

	@Autowired(required = false)
	private String resultCodeElement = "";

	private String defaultResultCode = "0000";

	@Autowired
	private MessageSource messageSourceFile;

//	private Properties prop;

	public void process(Exchange exchange) throws Exception 
	{
		exchange.getIn().setBody(resultCodeMaker.makeResultCode(exchange, messageSourceFile, resultCodeElement, defaultResultCode));
	}

	public String getResultCodeElement() {
		return resultCodeElement;
	}

	public void setResultCodeElement(String resultCodeElement) {
		this.resultCodeElement = resultCodeElement;
	}

	public String getDefaultResultCode() {
		return defaultResultCode;
	}

	public void setDefaultResultCode(String defaultResultCode) {
		this.defaultResultCode = defaultResultCode;
	}

	public ResultCodeMaker getResultCodeMaker() {
		return resultCodeMaker;
	}

	public void setResultCodeMaker(ResultCodeMaker resultCodeMaker) {
		this.resultCodeMaker = resultCodeMaker;
	}

}