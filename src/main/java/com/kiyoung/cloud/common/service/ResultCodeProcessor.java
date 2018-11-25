package com.kiyoung.cloud.common.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * 호출 후 Result 코드를 세팅하기 위한 Processor
 *
 * @author kiyoung
 * @since 2018.11.22
 * @version 1.0
 */
public class ResultCodeProcessor implements Processor {


    @Autowired(required = false)
    ResultCodeMaker resultCodeMaker = new DefaultResultCodeMaker();

    @Autowired(required = false)
    private String resultCodeElement = "";

    private String defaultResultCode = "0000";

    @Autowired
    private MessageSource messageSourceFile;



    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(resultCodeMaker.makeResultCode(exchange, messageSourceFile,
                resultCodeElement, defaultResultCode));
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
