package com.kiyoung.cloud.common.service;

import com.kiyoung.cloud.common.dto.DCloudResponse;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.context.MessageSource;

public class DefaultResultCodeMaker implements ResultCodeMaker{

    public DCloudResponse makeResultCode(Exchange exchange, MessageSource messageSourceFile, String resultCodeElement, String defaultResultCode) throws Exception {
        Message in = exchange.getIn();
        DCloudResultSet dCloudResultSet = (DCloudResultSet) in.getBody();
        DCloudResponse res = new DCloudResponse();
        String resultCode = dCloudResultSet.getResultCode();
        String message = "";

        return null;
    }
}
