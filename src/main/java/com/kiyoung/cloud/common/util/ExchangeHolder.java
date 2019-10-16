package com.kiyoung.cloud.common.util;

import org.apache.camel.Exchange;

/**
 * Multi thread용 exchage 사용
 * 
 * @author Kiyoung
 * @since 2019.10.16
 * @version 1.0
 */
public class ExchangeHolder {
	
	private static ThreadLocal<Exchange> ExchangeStore = new ThreadLocal<Exchange>();

	public static void removeExchange() {
		ExchangeStore.remove();
	}

	public static Exchange getExchangeStore() {
		return ExchangeStore.get();
	}

	public static void setExchangeStore(Exchange exchange) {
		ExchangeStore.set(exchange);
	}

}