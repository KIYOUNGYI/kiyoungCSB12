package com.kiyoung.cloud.common.constants;


public class StringUtilConstant {

    /** 스트링 종류를 나타내는 상수 : IP 주소 스트링 */
    public static final int    IP_ADDRESS             = 0;

    /** 스트링 종류를 나타내는 상수 : 이메일 주소 스트링 */
    public static final int    EMAIL_ADDRESS          = 1;

    /** 스트링 종류를 나타내는 상수 : 웹 주소 스트링 */
    public static final int    WEB_ADDRESS            = 2;

    /** 스트링 종류를 나타내는 상수 : 우편번호 스트링 */
    public static final int    ZIP_CODE               = 3;

    /** 스트링 종류를 나타내는 상수 : 주민등록번호 스트링 */
    public static final int    SS_NO                  = 4;

    /** 스트링 종류를 나타내는 상수 : 사업자등록번호 스트링 */
    public static final int    ER_NO                  = 5;

    /** 스트링 종류를 나타내는 상수 : 전화번호 스트링 */
    public static final int    TEL_NO                 = 6;

    /** IP 주소 스트링 포맷 */
    public static final String IP_PATTERN             = "((([2][0-5]{2})|([1][0-9]{2})|([1-9][0-9])|([0-9]))\\.){3}(([2][0-5]{2})|([1][0-9]{2})|([1-9][0-9])|([0-9]))"; 

    /** 이메일 주소 스트링 포맷 */
    public static final String EMAIL_PATTERN          = "(^[_0-9a-zA-Z-]+([.][_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+[.]+[_0-9a-zA-Z-]+$)"; 

    /** 웹 주소 스트링 포맷 */
    public static final String WEB_ADDRESS_PATTERN    = "http://.+"; 

    /** 우편번호 스트링 포맷 */
    public static final String ZIP_CODE_PATTERN       = "[0-9]{3}-[0-9]{3}"; 

    /** 주민등록번호 스트링 포맷 */
    public static final String SSNO_PATTERN           = "[0-9]{2}(([0][1-9])|([1][0-2]))[0|1|2|3][0-9]-[1|2][0-9]{6}"; 

    /** 사업자등록번호 스트링 포맷 */
    public static final String ERNO_PATTERN           = "[0-9]{3}-[0-9]{2}-[0-9]{5}"; 

    /** 전화번호 스트링 포맷 */
    public static final String TELNO_PATTERN          = "[0](([2])|([0-9]{2}))-[1-9][0-9]{2,3}-[0-9]{4}"; 

    /** 숫자 패턴 포맷 : '.','+','-'를 포함 */
    public static final String DOUBLE_NUMBER_PATTERN  = "[+|-]?(\\d+(\\.\\d*)?)|(\\.\\d+)"; 

    /** 알파벳 패턴 포맷 */
    public static final String ALPHABET_PATTERN       = "[a-zA-Z]*"; 

    /** 알파벳과 숫자 패턴 포맷 */
    public static final String ALPHABET_DIGIT_PATTERN = "[a-zA-Z0-9]*"; 

    /** 알파벳과 숫자 - _ 패턴 포맷 */
    public static final String ALPHABET_DIGIT_UNDERBAR_PATTERN = "[a-zA-Z0-9_-]*"; 

    /** 패키지 명 포멧 **/
    public static final String PACKAGE_PATTERN        = "[a-zA-Z.]+"; 

}