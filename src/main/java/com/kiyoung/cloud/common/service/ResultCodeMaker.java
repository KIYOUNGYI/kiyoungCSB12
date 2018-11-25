package com.kiyoung.cloud.common.service;

import org.apache.camel.Exchange;
import org.springframework.context.MessageSource;

/**
 * ResultCode를 만드는 인터페이스
 */
public interface ResultCodeMaker {
    public Object makeResultCode(Exchange exchange,
                                 MessageSource messageSourceFile, String resultCodeElement,
                                 String defaultResultCode) throws Exception;
}
