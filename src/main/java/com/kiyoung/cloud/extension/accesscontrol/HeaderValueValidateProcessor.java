package com.kiyoung.cloud.extension.accesscontrol;

import com.kiyoung.cloud.common.exception.CSBException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;
import java.util.Map;

/**
 * Header값 필수 파라미터 체크
 *
 * @author kiyoung
 * @since 2018.11.25
 * @version 1.0
 */
public class HeaderValueValidateProcessor implements Processor {

    private List<String> headerKeyList = null;

    private boolean testMode = false;

    public void process(Exchange exchange) throws Exception {
        System.out.println("Dude! it HeaderValueValidateProcessor process!" );
        // 설정파일에 false 로 설정되어 있으므로, 아래 메소드(validation을 수행하는 메소드는) 무조건 실행
        if(!testMode){
            excuteValidate(exchange);
        }

    }

    private void excuteValidate(Exchange exchange) throws Exception {
        Map<String, Object> headers = exchange.getIn().getHeaders();
        System.out.println("HeaderValueValidateProcessor excuteValidate headers->"+headers.toString());
        for(String headerKey : headerKeyList){

            if(!headers.containsKey(headerKey)){
                throw new CSBException("N91801", new String[] {headerKey} )
                        .setDebuggingMessage("헤더값에 " + headerKey + "를 넣어 주십시오.");
            }
        }
    }

    public void setHeaderKeyList(List<String> headerKeyList) {
        this.headerKeyList = headerKeyList;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}