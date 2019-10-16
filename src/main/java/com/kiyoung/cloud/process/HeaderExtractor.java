package com.kiyoung.cloud.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 크로스 도메인 간에 json data 통신을 하기 위해 <br>
 * 헤더에 jsonp형식의 값을 받아 처리하기 위한 Class
 * @author heokjin
 */
public class HeaderExtractor implements Processor {

	public void process(Exchange exchange) throws Exception {
		String jsonString = (String) exchange.getIn().getHeader("jsonpBody");
		exchange.getIn().setBody(jsonString);
	}

}