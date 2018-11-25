package com.kiyoung.cloud.common.service;

import com.kiyoung.cloud.common.consts.CommonConsts;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Header에서 service값을 읽어들여 해당 bean을 동기식으로 호출한다.
 *
 * @author kiyoung
 * @since 2018.11.25
 * @version 1.0
 */
public class LoginServiceProcess implements Processor {

    private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

    @Autowired
    ProducerTemplate producerTemplate;

    public void process(Exchange exchange) throws Exception {

        Map<String, Object> headers = exchange.getIn().getHeaders();
        String serviceBeanId = String.valueOf(headers.get(CommonConsts.SERVICE_NAME));
        String method = String.valueOf(headers.get(CommonConsts.METHOD_NAME));

//        log.debug("{} {}","SERVICE BEAN ID : " , serviceBeanId);
        System.out.println("SERVICE BEAN ID : "+serviceBeanId);

        //성능 측정을 위한 time
        long startTime = System.currentTimeMillis();
        try {
            producerTemplate.send("bean:" + serviceBeanId + "?method=" + method, exchange);
        }catch (Exception e){
            log.error(">>>>fucked");
            System.out.println("fuck????");
//            System.out.println("here??????");
            e.printStackTrace();
            throw e;
        }
//        if(log.isDebugEnabled())
//            log.debug("SERVICE 호출 완료 ( 소요 시간 : " + (System.currentTimeMillis() - startTime) + ")");
        System.out.println("SERVICE 호출 완료 (소요 시간:"+(System.currentTimeMillis() - startTime) + ")");
    }
}
