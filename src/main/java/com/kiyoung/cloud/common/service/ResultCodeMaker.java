package com.kiyoung.cloud.common.service;

import org.apache.camel.Exchange;
import org.springframework.context.MessageSource;

/**
 * ResultCodeMaker interface
 * 
 * @author Kiyoung
 * @since 2019.10.16
 * @version 1.0
 */
public interface ResultCodeMaker {

	public Object makeResultCode(Exchange exchange,
			MessageSource messageSourceFile, String resultCodeElement,
			String defaultResultCode) throws Exception;
}
