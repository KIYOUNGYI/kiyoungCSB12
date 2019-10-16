package com.kiyoung.cloud.extension.accesscontrol;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.kiyoung.cloud.common.exception.CSBException;


/**
 * Header값 필수 파라미터 체크
 * 
 * @author Kiyoung
 * @since 2019.10.15
 * @version 1.0
 */
public class HeaderValueValidateProcessor implements Processor {

	private List<String> headerKeyList = null;
	
	private boolean testMode = false;
	
	public void process(Exchange exchange) throws Exception {
//		System.out.println("headerValueValidateProcessor call");
		
		if(!testMode){excuteValidate(exchange);}
		
	}

	private void excuteValidate(Exchange exchange) throws Exception {
		Map<String, Object> headers = exchange.getIn().getHeaders();
		//headerKeyList -> service, method 체크한다. // kiyoung-csb-frontend.xml 참고
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


