package com.kiyoung.cloud.common.service;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiyoung.cloud.common.constants.CommonConst;
import com.kiyoung.cloud.common.service.PreService;

/**
 * Header에서 service값을 읽어들여 해당 bean을 동기식으로 호출한다.
 * 
 * @author kiyoung
 * @since 2019.10.15
 * @version 1.0
 */
public class ServiceProcess implements Processor {
    
	private Logger LOG = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

    @Produce
    ProducerTemplate producer;
    
    private PreService preService = new PreService();
    
    public void process(Exchange exchange) throws Exception {
    	
    	preService.excute(exchange);
    	
    	Map<String, Object> headers = exchange.getIn().getHeaders();

        String serviceBeanId = String.valueOf(headers.get(CommonConst.SERVICE_NAME));
        String method = String.valueOf(headers.get(CommonConst.METHOD_NAME));
        
        LOG.debug("{} {}","SERVICE BEAN ID : " , serviceBeanId); 
        
        //성능 측정을 위한 time
        long startTime = System.currentTimeMillis();
        try {
            // 동기식 호출 처리 (serviceBeanId 가 full classpath가 입력되면 해당 bean이 새로 생성 됩니다...)
            producer.send("bean:" + serviceBeanId + "?method=" + method, exchange);  
            
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        if(LOG.isDebugEnabled()) 
        	LOG.debug("SERVICE 호출 완료 ( 소요 시간 : " + (System.currentTimeMillis() - startTime) + ")"); 
    }

	public void setPreService(PreService preService) {
		this.preService = preService;
	}

}
