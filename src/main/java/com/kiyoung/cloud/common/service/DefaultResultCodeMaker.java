package com.kiyoung.cloud.common.service;

import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.kiyoung.cloud.common.constants.CloudConstants;
import com.kiyoung.cloud.common.dto.DCloudResponse;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.common.service.ResultCodeMaker;
import com.kiyoung.cloud.common.util.StringUtil;

/**
 * Channel 호출 후 Result 코드 와 메세지를 세팅하기 위한 Class
 * 
 * @author Kiyoung
 * @since 2012.10.16
 * @version 1.0
 */
public class DefaultResultCodeMaker implements ResultCodeMaker {

	private Logger logger = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	public DCloudResponse makeResultCode(Exchange exchange, MessageSource messageSourceFile, String resultCodeElement, String defaultResultCode) {
		Message in = exchange.getIn();
		DCloudResultSet dCloudResultSet = (DCloudResultSet) in.getBody();
		
		DCloudResponse res = new DCloudResponse();
		
		String resultCode = dCloudResultSet.getResultCode();
		
		String message = "";

		if ("".equals(resultCode) || resultCode == null) {
			message = CloudConstants.SERVICE_SUCCESS;
			
			if(StringUtil.isEmpty(dCloudResultSet.getServerMsg())){
				res.setServerMsg(message);
			} else {
				res.setServerMsg(dCloudResultSet.getServerMsg());
			}
			res.setResultCode(defaultResultCode);
			
		} else {
			
			String _appLang = (String)exchange.getIn().getHeader("Accept-Language");
			if(ObjectHelper.isEmpty(_appLang)) {
				_appLang = (String)exchange.getIn().getHeader("accept-language");
			}
			
			try {
				if (ObjectHelper.isEmpty(_appLang)) {
					_appLang = "ko";
				} else if (_appLang.length() >= 2) {
					_appLang = _appLang.substring(0, 2);
				} else if (_appLang.length() < 2) {
					_appLang = "ko";
				}
				_appLang = _appLang.toLowerCase();
			} catch (Exception eee) {
				_appLang = "ko";
			}
			
			message = messageSourceFile.getMessage(resultCode, null, CloudConstants.SERVICE_SUCCESS, new Locale(_appLang, "", ""));
			if(StringUtil.isEmpty(dCloudResultSet.getServerMsg())){
				res.setServerMsg(message);
			} else {
				res.setServerMsg(dCloudResultSet.getServerMsg());
			}
			res.setResultCode(resultCode);
		}
		res.setResultList(dCloudResultSet.getList());
		res.setTotalCount(dCloudResultSet.getTotalCount());

		return res;
	}

}
