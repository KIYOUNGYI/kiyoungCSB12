package com.kiyoung.cloud.common.service;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 서비스 공통 검증 작업
 * 
 * @author kiyoung
 * @since 2019.10.15
 * @version 1.0
 */
@Service
public class PreService {
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	public void excute(Exchange exchange) throws Exception{
		log.debug("PRE SERVICE");
		
	}

}
