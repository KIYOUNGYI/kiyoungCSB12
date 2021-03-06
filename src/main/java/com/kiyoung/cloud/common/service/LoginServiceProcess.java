package com.kiyoung.cloud.common.service;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiyoung.cloud.common.constants.CommonConst;

public class LoginServiceProcess implements Processor{

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

    @Produce
    ProducerTemplate producer;
    
    public void process(Exchange exchange) throws Exception {
    	
    	Map<String, Object> headers = exchange.getIn().getHeaders();

        String serviceBeanId = String.valueOf(headers.get(CommonConst.SERVICE_NAME));
        String method = String.valueOf(headers.get(CommonConst.METHOD_NAME));
        
        log.debug("service>"+serviceBeanId+" method>"+method);
        
        //성능 측정을 위한 time
        long startTime = System.currentTimeMillis();
        try {
            // 동기식 호출 처리 (serviceBeanId 가 full classpath가 입력되면 해당 bean이 새로 생성 됩니다...) 핵심코어!!!!!
            producer.send("bean:" + serviceBeanId + "?method=" + method, exchange);  
            
        } catch (Exception e) {
        	log.error("error : ",e);
        }
        if(log.isDebugEnabled()) 
        	log.debug("SERVICE 호출 완료 ( 소요 시간 : " + (System.currentTimeMillis() - startTime) + ")"); 
    }
	
}
