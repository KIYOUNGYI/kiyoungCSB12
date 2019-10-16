package com.kiyoung.cloud.common.constants;

/**
 * 공통 상수
 * @author kiyoung
 * @since 2019.10.16
 * @version 1.0
 */
public class CommonConst {

	public static final String ERROR_CODE_CSB_DEFAULT = "9999";
	public static final String ERROR_CODE_CUSTOM = "9998";
	public static final String SERVICE_NAME = "service";
	public static final String METHOD_NAME = "method";
	public static final String FILTER_EXCEPTION = "FILTER_EXCEPTION";
	public static final String SESSION_KEY_USERINFO = "userInfo";

	public static String LOG_DIV_STRING = ""; 

	/** 날짜 데이터 포맷(1) - yyyy년 MM월 dd일 HH:mm */
	public static String DATE_FORMAT_1 = "yyyy년 MM월 dd일 HH:mm"; 
	/** 날짜 데이터 포맷(2) - yyyy-MM-dd */
	public static String DATE_FORMAT_2 = "yyyy-MM-dd"; 
	/** 날짜 데이터 포맷(3) - yyyy/MM/dd */
	public static String DATE_FORMAT_3 = "yyyy/MM/dd"; 
	/** 날짜 데이터 포맷(4) - yyyy-MM-dd HH:mm:ss */
	public static String DATE_FORMAT_4 = "yyyy-MM-dd HH:mm:ss"; 
	/** 날짜 데이터 포맷(5) - yyyy/MM/dd HH:mm:ss */
	public static String DATE_FORMAT_5 = "yyyy/MM/dd HH:mm:ss"; 

	/** 마스킹해야 할 문자 - ? */
	public static char MASKING_SOURCE_CHARACTER = '?';
	/** 마스킹에 사용할 문자 - ? */
	public static char MASKING_TARGET_CHARACTER = '*';
	
	 /** 세션 키(사용자) - user */
    public static String SESSION_KEY_USER = "user"; 
    
    public static enum ExpressionType {
        simple, xpath,none ;
        public ExpressionType fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (Exception ex) {
                return none;
            }
        }
    }

}
