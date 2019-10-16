package com.kiyoung.cloud.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import com.kiyoung.cloud.common.constants.StringUtilConstant;

public class StringUtil {

	/** 빈 문자열 */
	public static final String EMPTY = "";

	/**
	 * 입력된 String 문자열이 null 혹은 빈문자열인지 확인한다.
	 * 
	 * <pre>
	 * StringUtil.isEmpty(string);
	 * </pre>
	 * 
	 * @param string null 혹은 빈 문자열 여부를 체크할 문자열
	 */
	public static boolean isEmpty(String string) {
		if (string == null || "".equals(string)) {
			return true;
		}

		return false;
	}

	/**
	 * 입력된 Object가 String 객체이고 null 혹은 빈문자열인지 확인한다.
	 * 
	 * @param string String 객체이고 null 혹은 빈 문자열 여부를 체크할 객체
	 */
	public static boolean isEmpty(Object string) {
		if (string instanceof String == false) {
			return false;
		}

		return isEmpty((String) string);
	}

	/**
	 * 입력된 StringBuffer 문자열이 null 혹은 빈문자열인지 체크한다. 만일 null인 경우 BaseRuntimeException을
	 * 던진다.
	 * 
	 * <pre>
	 * StringUtil.checkIsNull(stringBuffer);
	 * </pre>
	 * 
	 * @param stringBuffer null 혹은 빈문자열 여부를 체크할 문자열
	 */
	public static boolean isEmpty(StringBuffer stringBuffer) {
		if (stringBuffer == null || stringBuffer.length() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 입력된 문자열 배열이 null인지 체크한다. 만일 null인 경우 BaseRuntimeException을 던진다.
	 * 
	 * <pre>
	 * StringUtil.checkIsNull(string[]);
	 * </pre>
	 * 
	 * @param string null여부를 체크할 문자열
	 */
	public static boolean isNull(String[] string) {
		if (string == null) {
			return true;
		}

		return false;
	}

	/**
	 * 한글 인코딩(KSC5601)을 "8859_1" 인코딩으로 전환한다. 8859_1 인코딩은 HTTP 요청 등에서 사용되며 서유럽 언어의
	 * 표기에 필요한 US-ASCII에 없는 94개의 글자를 순차적으로 나열한 것이다.
	 * 
	 * @param str KSC5601로 인코딩된 문자열
	 * @return String 8859_1로 인코딩된 문자열
	 */
	public static String kscToAsc(String str) {
		try {
			return str != null ? new String(str.getBytes("KSC5601"), "8859_1") : str;
		} catch (UnsupportedEncodingException e) {
			return str;
//            throw new BaseRmRuntimeException(e);
		}
	}

	/**
	 * 8859_1로 인코딩된 한글 문자열을 KSC5601로 인코딩한다. ksc5601에서 한글의 범위는 0xb0a1(가)부터
	 * 0xc8fe(힝)까지이다.<br/>
	 * 완성형 한글의 코드 형태는 완성된 한글 한 글자 당 2바이트 크기의 코드를 부여하여 각 글자별로 코드값을 지정하는 방식이다.
	 * 
	 * @param str 8859_1로 인코딩된 문자열
	 * @return String KSC5601로 인코딩된 문자열
	 */
	public static String ascToKsc(String str) {

		try {
			return str != null ? new String(str.getBytes("8859_1"), "KSC5601") : str;
		} catch (UnsupportedEncodingException e) {
//            throw new BaseRmRuntimeException(e);
			return str;
		}
	}

	/**
	 * 스트링내의 특정문자를 원하는 문자열로 Swap 시킨다. <br>
	 * 하지만 이 메소드를 사용하는 것은 추천하지 않고 정규식을 사용하는 것을 권장한다. 기존 코드와의 호환성 문제로 제거하지 않고 남겨둔
	 * 
	 * 
	 * @param input  치환시킬 입력 문자열
	 * @param oldStr 치환될 대상인 부분 문자열
	 * @param newStr 치환할 부분 문자열
	 * @return String input의 oldStr이 newStr로 치환된 출력 문자열
	 */
	public static String replaceAll(String input, String oldStr, String newStr) {
		try {
			int startIdx = 0;
			int idx = 0;
			int length = oldStr.length();

			verifyStringParameter(input, oldStr);

			StringBuffer sb = new StringBuffer((int) (input.length() * 1.2));
			while ((idx = input.indexOf(oldStr, startIdx)) >= 0) {
				sb.append(input.substring(startIdx, idx));
				sb.append(newStr);
				startIdx = idx + length;
			}
			sb.append(input.substring(startIdx));
			return sb.toString();
		} catch (Exception e) {
			;
		}

		return oldStr;
	}

	/**
	 * ${string}, ${space}, 공백 등의 문자를 해당 정규식 패턴으로 변환한다.
	 * <ul>
	 * <li>${space} ==> \s*</li>
	 * <li>공백 ==> \s*</li>
	 * <li>${string} ==> \S(.*</li>
	 * </ul>
	 * 
	 * @param pattern ${string}, ${space}, 공백 등을 포함한 문자열
	 * @return String 정규식으로 변환한 문자열
	 */
	public static String replaceDollerKeywordsToRegExp(String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return pattern;
		}
		pattern = "(.*)" + pattern;
		pattern = StringUtil.replaceAll(pattern, "${space}", "\\s*");
		pattern = StringUtil.replaceAll(pattern, " ", "\\s*");
		pattern = StringUtil.replaceAll(pattern, "${Space}", "\\s*");
		pattern = StringUtil.replaceAll(pattern, "${string}", "\\S(.*)");
		pattern = StringUtil.replaceAll(pattern, "${String}", "\\S(.*)");

		return pattern;
	}

	/**
	 * 입력된 String값들 중에 null 혹은 빈 문자열인 지를 검증한 후 null 혹은 빈 문자열이면 BaseRuntimeException을
	 * 발생시킨다. 검증할 문자열 파라미터의 갯수는 제한이 없다.
	 * 
	 * <pre>
	 * StringUtil.verifyStringParameter(string1, string2, string3, stringN);
	 * </pre>
	 * 
	 * @param parameters null 혹은 빈 문자열인지 검증할 String 객체들
	 */
	public static void verifyStringParameter(String... parameters) {
//        String message = Messages.getString("StringUtil.NULL_PARAMETER_DESC"); 
//        for (String parameter : parameters) {
//            if (parameter == null) {
//                throw new BaseRmRuntimeException(message);
//            } else if (parameter.equals("")) { 
//                throw new BaseRmRuntimeException(Messages.getString("StringUtil.EMPTY_PARAMETER_DESC")); 
//            }
//        }
	}

	/**
	 * 입력 객체 값이 null인지 검사한 후 해당 경우 시 BaseRuntimeException을 발생시킨다. 검증할 객체 파라미터의 갯수는
	 * 제한이 없다.
	 * 
	 * <pre>
	 * StringUtil.verifyObjectParameter(object1, object2, object3, objectN);
	 * </pre>
	 * 
	 * @param parameters null 여부를 검증한 Object 객체들
	 */
	public static void verifyObjectParameter(Object... parameters) {
//        String message = Messages.getString("StringUtil.NULL_OBJECT_PARAMETER_DESC"); 
//        for (Object parameter : parameters) {
//            if (parameter == null) {
//                throw new BaseRmRuntimeException(message);
//            }
//        }
	}

	/**
	 * 대상 문자열 input에서 특정 문자열 oldStr을 제거하고 결과 문자열을 반환한다.
	 * 
	 * <pre>
	 * StringUtil.remove(folderPath, &quot;/&quot; + file.getName());
	 * </pre>
	 * 
	 * @param input  특정 문자열을 제거할 입력 문자열
	 * @param oldStr 제거될 특정 문자열
	 * @return String 결과 문자
	 */
	public static String remove(String input, String oldStr) {
		int startIdx = 0;
		int idx = 0;
		int length = oldStr.length();

		StringBuffer sb = new StringBuffer((int) (input.length() * 1.2));
		while ((idx = input.indexOf(oldStr, startIdx)) >= 0) {
			sb.append(input.substring(startIdx, idx));
			startIdx = idx + length;
		}
		sb.append(input.substring(startIdx));
		return sb.toString();
	}

	/**
	 * 
	 * String을 원하는 크기(byte 단위)로 줄여 마지막 접미사를 붙여 반환한다. (접미사도 length에 포함)<br/>
	 * 
	 * MS949 인코딩을 기준으로 byte[]를 얻은 후 이를 byte 단위로 나누면서 2바이트를 차지하는 글자에 대한 조정을 수행한다. 입력
	 * 문자열이 null일 경우 ""을 리턴한다. 원하는 길이가 원본 문자열의 길이보다 짧을 경우에는 원본 문자열을 반환한다.
	 * 
	 * 
	 * @param content String 내용
	 * @param length  원하는 글자 길이 (Byte 수 기준)
	 * @param suffix  잘린 글자 뒤에 붙일 문자
	 * @return String length보다 길경우 suffix를 붙인 String
	 */
	public static String fixLength(String content, int length, String suffix) {
		if (content == null) {
			return "";
		}
		if (content.getBytes().length > length) {
			int slen = 0, blen = 0;
			int realLength = length - suffix.getBytes().length;
			while (blen < realLength) {
				blen++;
				slen++;
				if (content.charAt(slen) > '\u00FF') {
					blen++; // 2-byte character..
				}
			}
			return content.substring(0, slen) + suffix;
		}
		return content;
	}

	/**
	 * String을 원하는 길이(byte 단위)로 줄여 마지막 접미사를 붙여 반환한다.<br/>
	 * 접미사의 길이도 길이에 포함되며, 원하는 크기가 접미사의 길이보다 작으면 BaseRuntimeException 던진다. 주어진 인코딩
	 * 기준으로 byte[]를 얻은 후 이를 byte 단위로 나누며, 나누는 과정에 깨지는 문자는 버린다.
	 * 
	 * <pre>
	 * StringUtil.abbreviate(content, 20, &quot;8859_1&quot;, suffix);
	 * </pre>
	 * 
	 * @param content  String 내용
	 * @param maxWidth 원하는 글자 길이 (byte 수 기준)
	 * @param enc      인코딩
	 * @param suffix   잘린 글자 뒤에 붙일 문자
	 * @throws BaseRmRuntimeException 원하는 길이보다 접미사의 길이가 긴 경우 발생
	 * @throws BaseRmRuntimeException 원하는 인코딩이 지원되지 않는 경우
	 * @return String length보다 길경우 suffix를 붙인 String.
	 */
	public static String abbreviate(String content, int maxWidth, String enc, String suffix) {
//        if (content == null)
//            return ""; 
//        if (maxWidth < suffix.length())
//            throw new BaseRmRuntimeException(new IllegalArgumentException());
//        int ptr = maxWidth - suffix.length();
//        String str = null;
//        try {
//            byte[] bytes = content.getBytes(enc);
//            str = new String(bytes, 0, (bytes.length < ptr) ? bytes.length : ptr, enc);
//        } catch (UnsupportedEncodingException e) {
//            throw new BaseRmRuntimeException(e);
//        }
//        // 인코딩 차이로 깨져 원문과 달라진 글자를 잘라낸다.
//        ptr = ((ptr = str.length() - 4) < 0) ? 0 : ptr; // 끝에서 4글자 전부터 비교 시작
//        while (ptr < str.length() && str.charAt(ptr) == content.charAt(ptr))
//            ptr++;
//
//        return str.substring(0, ptr) + suffix;
		return null;
	}

	/**
	 * 입력된 문자열을 100 단위마다 콤마를 넣어 화폐 단위로 표시한다.
	 * 
	 * <pre>
	 * StringUtil.toCurrency(&quot;123456789&quot;)
	 * result: &quot;123,456,789&quot;
	 * </pre>
	 * 
	 * @param currency 입력 문자열
	 * @return String ,가 포함된 문자
	 */
	public static String toCurrency(String currency) {
//        try {
//            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
//            return format.format(new Long(currency));
//        } catch (IllegalArgumentException e) {
//            throw new BaseRmRuntimeException(e);
//        }
		return null;
	}

	/**
	 * 입력된 숫자를 화폐 단위로 표시한다. 입력 값이 long 형인 것 외에는 toCurrency(String currency)과 동일하다.
	 * 
	 * <pre>
	 * StringUtil.toCurrency(123456789)
	 * result: &quot;123,456,789&quot;
	 * </pre>
	 * 
	 * @param currency 입력 문자열
	 * @return String ,가 포함된 문자
	 */
	public static String toCurrency(long currency) {
//        try {
//            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
//            return format.format(new Long(currency));
//        } catch (IllegalArgumentException e) {
//            throw new BaseRmRuntimeException(e);
//        }
		return null;
	}

	/**
	 * 화폐 단위 포맷 문자열을 숫자로 파싱한다. 입력된 문자열이 환폐 단위 포맷의 문자열이 아닌 경우에는 BaseRuntimeException을
	 * 던진다.
	 * 
	 * <pre>
	 * StringUtil.parseCurrency(&quot;123,456,789&quot;)
	 * result: &quot;123456789&quot;
	 * </pre>
	 * 
	 * @param myString 화폐 단위 포맷의 입력 문자열
	 * @throws BaseRmRuntimeException 파싱할 수 없는 문자열의 경우
	 * @return String ,가 제거된 숫자 문자열
	 */
	public static String parseCurrency(String myString) {
//        try {
//            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
//            return format.parse(myString).toString();
//        } catch (ParseException e) {
//            throw new BaseRmRuntimeException(e);
//        }
		return null;
	}

	/**
	 * Array 또는 List 등의 객체를 Delimiter로 구분된 하나의 문자열으로 변환하여 리턴한다.
	 * 
	 * @param obj       배열 혹은 List 형태의 입력 객체
	 * @param delimiter 구분자
	 * @return String 객체의 요소들이 합쳐진 결과 문자열
	 */
	public static String listToString(Object obj, String delimiter) {
		if (obj == null) {
			return "";
		}
		if (obj.getClass().isArray()) {
			StringBuffer buffer = new StringBuffer(512);
			int length = Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					buffer.append(delimiter);
				}
				buffer.append(Array.get(obj, i));
			}
			return buffer.toString();
		} else if (obj instanceof Collection) {
			return listToString(((Collection) obj).iterator(), delimiter);
		} else if (obj instanceof Enumeration) {
			StringBuffer buffer = new StringBuffer(512);
			boolean started = false;
			Enumeration it = (Enumeration) obj;
			while (it.hasMoreElements()) {
				if (started) {
					buffer.append(delimiter);
				} else {
					started = true;
				}
				buffer.append(it.nextElement());
			}
			return buffer.toString();
		} else if (obj instanceof Iterator) {
			StringBuffer buffer = new StringBuffer(512);
			boolean started = false;
			Iterator it = (Iterator) obj;
			while (it.hasNext()) {
				if (started) {
					buffer.append(delimiter);
				} else {
					started = true;
				}
				buffer.append(it.next());
			}
			return buffer.toString();
		} else if (obj instanceof Map) {
			return listToString(((Map) obj).values(), delimiter);
		} else {
			return obj.toString();
		}
	}

	/**
	 * 입력된 객체가 null 값일 경우 대체 객체를 대신 반환한다. null이 아닌 경우에는 입력 객체를 반환한다.
	 * 
	 * @param source     입력 객체
	 * @param alernative 입력 객체가 null일 경우 반환될 객체
	 * @return Object 실제 반환된 객체
	 */
	public static Object nvl(Object source, Object alernative) {
		if (source == null) {
			return alernative;
		}
		return source;
	}

	/**
	 * 입력된 문자열 String의 값이 null일 경우 ""를 반환한다.
	 * 
	 * @param source 입력 문자열
	 * @return String 출력 문자열. 입력이 null인 경우 "" 아닌 경우는 입력 문자열을 그대로 리턴한다.
	 */
	public static String nullStringCheck(String source) {
		return nullStringCheck(source, "");
	}

	/**
	 * 데이터베이스에서 읽어본 문자열이 null, 빈 문자열, "null" 인지 확인한다. 만일 해당하는 경우 ""를 반납하고, 아닌 경우에는
	 * 입력 문자열을 그대로 리턴한다. 현재 nexbuild.foundation.model 쪽에서만 사용하며, 일반적으로 이 메소드 대신
	 * nullStringCheck(string, string)을 사용하는 것을 추천한다.
	 * 
	 * @param source 입력 문자열
	 * @return String 입력 문자열이 null, 빈 문자열, "null" 중 하나인 경우 "", 아닌 경우엔 입력 문자열
	 */
	public static String replaceDbNull(String source) {
		if (source == null || source.length() == 0 || "null".equals(source)) {
			return "";
		} else {
			return source;
		}
	}

	/**
	 * 입력 문자열이 null 혹은 빈 문자열인지 확인한다. 만일 해당하는 경우 대체 문자열을 반납하고, 아닌 경우에는 입력 문자열을 그대로
	 * 리턴한다.
	 * 
	 * @param source     입력 문자열
	 * @param alernative 입력 문자열이 null 혹은 빈 문자열일 경우 대체할 문자열
	 * @return String 입력 문자열이 null 혹은 빈 문자열일 경우 대체 문자열, 아닌 경우엔 입력 문자열.
	 */
	public static String nullStringCheck(String source, String alernative) {
		if (source == null || source.length() == 0) {
			return alernative;
		}
		return source;
	}

	/**
	 * 입력된 숫자 문자열을 지정된 타입에 따라 ,가 포함된 문자열로 변환한다. 각각의 타입은 다음과 같다.
	 * <ul>
	 * <li>INT : ,를 포함한 정수형 포맷</li>
	 * <li>FINT : ,를 포함한 정수형 포맷. 입력된 float 타입의 숫자 문자열을 int형으로 변환한다.</li>
	 * <li>DBL : ,를 포함한 실수형 포맷. 배정도(소수점 4자리에서 반올림)</li>
	 * <li>IDBL : ,를 포함한 정수형 포맷. 입력된 double 타입의 숫자 문자열을 int형으로 변환한다.</li>
	 * <li>DDBL : ,를 포함한 실수형 포맷. 배정도.</li>
	 * <li>DDBL1 : ,를 포함한 실수형 포맷. 배정도. 소수점 1자리.</li>
	 * <li>DDBL3 : ,를 포함한 실수형 포맷. 배정도. 소수점 3자리.</li>
	 * <li>DDBL6 : ,를 포함한 실수형 포맷. 배정도. 소수점 6자리.</li>
	 * <li>DDBL7 : ,를 포함한 실수형 포맷. 배정도. 소수점 7자리.</li>
	 * <li>INT4 : ,를 포함한 정수형 포맷. 4자리 기준으로 왼쪽부터 빈 칸에 0으로 패딩한다.</li>
	 * </ul>
	 * 
	 * <pre>
	 *      StringUtil.formatNumber(&quot;123456789&quot;, &quot;INT&quot;)
	 *      result: &quot;123,456,789&quot; 
	 * 
	 *      StringUtil.formatNumber(&quot;123.123&quot;, &quot;FINT&quot;)
	 *      result: &quot;123&quot; 
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;DBL&quot;)
	 *      result: &quot;123,456,789.12&quot;
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;IDBL&quot;)
	 *      result: &quot;123,456,789&quot;
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;DDBL&quot;)
	 *      result: &quot;123,456,789.1235&quot; 
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;DDBL1&quot;)
	 *      result: &quot;123,456,789.1&quot;
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;DDBL3&quot;)
	 *      result: &quot;123,456,789.123&quot; 
	 * 
	 *      StringUtil.formatNumber(&quot;123456789.123456&quot;, &quot;DDBL6&quot;)
	 *      result: &quot;123,456,789.123456&quot;
	 *      
	 *      StringUtil.formatNumber(&quot;123456789.1234567&quot;, &quot;DDBL7&quot;)
	 *      result: &quot;123,456,789.1234567&quot;
	 * 
	 *      StringUtil.formatNumber(&quot;2&quot;, &quot;INT4&quot;)
	 *      result: &quot;0002&quot;
	 * </pre>
	 * 
	 * @param targetVal 입력된 숫자 문자열
	 * @param type      미리 지정된 출력 타입
	 * @return String 출력 문자열
	 */
	public static String formatNumber(String targetVal, String type) {

		int intVal = 0;
		double dblVal = 0;
		String rtnVal = "0";

		if (targetVal == null || targetVal.trim().length() == 0) {
			return "";
		}

		if (targetVal != null) {

			if ("INT".equals(type)) { // 순수정수형

				intVal = new Integer(targetVal).intValue();

				DecimalFormat dfInt = new DecimalFormat("#,##0");

				rtnVal = dfInt.format(intVal);

			}
			if ("FINT".equals(type)) { // 더블형에서 정수형

				intVal = Math.round(Float.parseFloat(targetVal));

				DecimalFormat dfInt = new DecimalFormat("#,##0");

				rtnVal = dfInt.format(intVal);

			} else if ("DBL".equals(type)) { // 순수 더블형

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("#,##0.00");

				rtnVal = dfDbl.format(dblVal);

			} else if ("IDBL".equals(type)) { // 정수가 OVERFLOW(LONG TYPE 정수)

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("#,##0");

				rtnVal = dfDbl.format(dblVal);

			} else if ("DDBL".equals(type)) { // 더블형이 소수점 4자리인경우

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("#,##0.0000");

				rtnVal = dfDbl.format(dblVal);
			} else if ("DDBL1".equals(type)) { // 더블형이 소수점 1자리인경우

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("#,##0.0");

				rtnVal = dfDbl.format(dblVal);
			} else if ("DDBL3".equals(type)) { // 더블형이 소수점 3자리인경우

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.000");

				rtnVal = dfDbl.format(dblVal);
			} else if ("DDBL6".equals(type)) { // 더블형이 소수점 6자리인경우

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.000000");

				rtnVal = dfDbl.format(dblVal);
			} else if ("DDBL7".equals(type)) { // 더블형이 소수점 7자리인경우

				dblVal = new Double(targetVal).doubleValue();

				DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.0000000");

				rtnVal = dfDbl.format(dblVal);
			} else if ("INT4".equals(type)) { // 정수지만 앞에 영이 붙는것 예) 0025

				int diff = 4 - targetVal.trim().length();
				rtnVal = targetVal;
				for (int i = 0; i < diff; i++) {
					rtnVal = "0" + rtnVal;
				}
			}
		}

		return rtnVal;
	}

	/**
	 * 입력된 정수형 숫자에 원하는 길이만큼 좌측에 0을 패딩하여 반환한다. 만일 입력된 정수형 숫자의 자리 수가 원하는 길이와 같거나 큰
	 * 경우에는 원본 입력 숫자가 반환된다. 음수가 입력된 경우에는 ""을 반환한다.
	 * 
	 * <pre>
	 *      StringUtil.fillNumber(0, 0)
	 *      result: &quot;0&quot;
	 * 
	 *      StringUtil.fillNumber(1, 4)
	 *      result; &quot;0001&quot;
	 * </pre>
	 * 
	 * @param targetVal 입력 정수값
	 * @param length    원하는 출력 길이
	 * @return String 출력 길이에 맞게 0으로 패딩된 출력된 문자열
	 */
	public static String fillNumber(int targetVal, int length) {

		if (targetVal < 0) {
			return "";
		}

		String rtnVal = String.valueOf(targetVal);
		int diff = length - rtnVal.length();
		for (int i = 0; i < diff; i++) {
			rtnVal = "0" + rtnVal;
		}

		return rtnVal;
	}

	/**
	 * Absolute Path에서 FileName 만 잘라서 반환한다. <br>
	 * Windows 와 Unix 계열에서의 디렉토리 구분자가 달라 동일한 데이터에 대해 다른 결과를 반환하므로, 개발/운용 시 각자가 주의 해야
	 * 한다. <br>
	 * 구분자는 System.getProperty("file.separator") 로 가져온 구분자를 사용한다.
	 * 
	 * <pre>
	 * getFileName(&quot;C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\tmp.txt&quot;)
	 * result: tmp.txt
	 * </pre>
	 * 
	 * @param fullFileName 전체 절대 파일 경로
	 * @return String 추출한 파일 명
	 */
	public static String getFileName(String fullFileName) {
		try {
			return getFileName(fullFileName, System.getProperty("file.separator"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Absolute Path에서 FileName 만 잘라서 반환한다. <br>
	 * Windows 와 Unix 계열에서의 디렉토리 구분자가 달라 동일한 데이터에 대해 다른 결과를 반환하므로, 개발/운용 시 각자가 주의 해야
	 * 한다. <br>
	 * 구분자는 Windows 와 Unix 계열 시스템에서 사용하는 '/', '\'를 구분하여 사용한다.
	 * 
	 * <pre>
	 * getFileName(&quot;C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\tmp.txt&quot;)
	 * result: tmp.txt
	 * </pre>
	 * 
	 * @param fullFileName  전체 절대 파일 경로
	 * @param fileSeparator 파일 구분자
	 * @return String 추출한 파일
	 */
	public static String getFileName(String fullFileName, String fileSeparator) {
		try {
			return fullFileName.substring(fullFileName.lastIndexOf(fileSeparator) + 1);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 전달 받은 스트링 객체를 주어진 문자 세트로 디코딩 한다.
	 * 
	 * @param value   디코딩될 값이 있는 스트링 객체.
	 * @param charset 디코딩 될 문자 세트.
	 * @return String 디코딩된 스트링 객체.
	 */
	public static String decodeCharset(String value, String charset) {
		try {
			Charset set = Charset.forName(charset);
			return set.decode(ByteBuffer.wrap(value.getBytes())).toString();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 전달 받은 스트링 객체를 주어진 문자 세트로 인코딩 한다.
	 * 
	 * @param value   인코딩될 값이 있는 스트링 객체.
	 * @param charset 인코딩 될 문자 세트.
	 * @return String 인코딩된 스트링 객체.
	 */
	public static String encodeCharset(String value, String charset) {
		try {
			Charset set = Charset.forName(charset);
			ByteBuffer bb = set.encode(value);
			return new String(bb.array(), charset);

		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 입력값의 공백을 제거한 후, 빈 문자열(&quot;&quot;)인지를 검사한다. 빈 문자열인 경우 true, 아니면 false를 리턴한다.
	 * 
	 * <pre>
	 * boolean empty = StringUtil.isEmptyAfterTrim(&quot; abc &quot;);
	 * // empty =&gt; false
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return boolean 빈 문자열 여부 (true."" false.!"")
	 */
	public static boolean isEmptyAfterTrim(String string) {
		return string == null || string.trim().equals("") ? true : false;
	}

	/**
	 * 입력값이 숫자만을 포함하는지를 검사한다.
	 * 
	 * <pre>
	 * boolean isDigit = StringUtil.isDigit(&quot;123&quot;);
	 * // isDigit =&gt; true
	 * </pre>
	 * 
	 * @param digit 입력값
	 * @return boolean 숫자 여부 (true.숫자 false.숫자 아닌 캐릭터 포함)
	 */
	public static boolean isDigit(String digit) {
		// StringUtil.isEmpty(digit);

		if (isEmptyAfterTrim(digit)) {
			return false;
		}

		char[] chars = digit.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isDigit(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 입력값이 소수를 포함하는 숫자인지를 검사한다.
	 * 
	 * <pre>
	 * boolean isDigit = StringUtil.isDoubleNumber(&quot;123.01&quot;);
	 * // isDigit =&gt; true
	 * </pre>
	 * 
	 * @param digit 입력값
	 * @return boolean 숫자 여부 (true.숫자 false.숫자 아닌 캐릭터 포함)
	 */
	public static boolean isDoubleNumber(String digit) {
		if (isEmpty(digit)) {
			return false;
		}

		Pattern pattern = Pattern.compile(StringUtilConstant.DOUBLE_NUMBER_PATTERN);

		Matcher m = pattern.matcher(digit);
		return m.matches();
	}

	/**
	 * 입력값이 문자인지를 검사한다. (특수문자 미포함)
	 * 
	 * <pre>
	 * boolean isLetter = StringUtil.isLetter(&quot;abc&quot;);
	 * // isLetter =&gt; true
	 * </pre>
	 * 
	 * @param letter 입력값
	 * @return boolean 문자 여부 (true.문자 false.문자 아닌 캐릭터 포함)
	 */
	public static boolean isLetter(String letter) {
		if (isEmpty(letter)) {
			return false;
		}

		char[] chars = letter.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetter(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 입력값이 숫자 또는 문자인지를 검사한다.
	 * 
	 * <pre>
	 * boolean isLetterOrDigit = StringUtil.isLetterOrDigit(&quot;abc123&quot;);
	 * // isLetterOrDigit =&gt; true
	 * </pre>
	 * 
	 * @param letterOrDigit 입력값
	 * @return boolean 문자 또는 숫자 여부
	 */
	public static boolean isLetterOrDigit(String letterOrDigit) {
		if (isEmpty(letterOrDigit)) {
			return false;
		}

		char[] chars = letterOrDigit.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetterOrDigit(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 입력값(문자열)이 한글인지를 검사한다.
	 * 
	 * <pre>
	 * boolean isHangul = StringUtil.isHangul(&quot;한글&quot;);
	 * // isHangul =&gt; true
	 * </pre>
	 * 
	 * @param hangul 입력값
	 * @return boolean 한글 여부 (true.한글 false.한글 아닌 캐릭터 포함)
	 */
	public static boolean isHangul(String hangul) {
		if (isEmpty(hangul)) {
			return false;
		}

		char[] chars = hangul.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!isHangul(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 입력값(캐릭터)이 한글인지를 검사한다.
	 * 
	 * <pre>
	 * boolean isHangul = StringUtil.isHangul('한');
	 * // isHangul =&gt; true
	 * </pre>
	 * 
	 * @param c 입력값
	 * @return boolean 한글 여부 (true.한글 false.한글 아닌 캐릭터)
	 */
	public static boolean isHangul(char c) {
		String unicodeBlock = Character.UnicodeBlock.of(c).toString();

		return (unicodeBlock.equals("HANGUL_JAMO") || unicodeBlock.equals("HANGUL_SYLLABLES")
				|| unicodeBlock.equals("HANGUL_COMPATIBILITY_JAMO")) ? true : false;
	}

	/**
	 * 입력값이 특정 포맷에 부합하는지를 검사한다.
	 * 
	 * 검사 포맷 종류
	 * <ul>
	 * <li>StringUtilConstant.IP_ADDRESS : IP 주소</li>
	 * <li>StringUtilConstant.EMAIL_ADDRESS : 이메일 주소</li>
	 * <li>StringUtilConstant.WEB_ADDRESS : web 주소</li>
	 * <li>StringUtilConstant.ZIP_CODE : 우편 번호</li>
	 * <li>StringUtilConstant.SS_NO : 주민 등록 번호</li>
	 * <li>StringUtilConstant.ER_NO : 사업자 등록 번호</li>
	 * <li>StringUtilConstant.TEL_NO : 전화 번호</li>
	 * </ul>
	 * 
	 * <pre>
	 * boolean isZipCode = StringUtil.isFormattedString(&quot;123-456&quot;, StringUtilConstant.ZIP_CODE);
	 * // isZipCode =&gt; true
	 * </pre>
	 * 
	 * @param string    입력값
	 * @param patternId 포맷 종류
	 * @return boolean 포맷 검사 결과 (true.포맷 적합 false.포맷 부적합 )
	 */
	public static boolean isFormattedString(String string, int patternId) {
		StringUtil.isEmpty(string);

		switch (patternId) {
		case StringUtilConstant.IP_ADDRESS:
			return isFormattedString(string, StringUtilConstant.IP_PATTERN);

		case StringUtilConstant.EMAIL_ADDRESS:
			return isFormattedString(string, StringUtilConstant.EMAIL_PATTERN);

		case StringUtilConstant.WEB_ADDRESS:
			return isFormattedString(string, StringUtilConstant.WEB_ADDRESS_PATTERN);

		case StringUtilConstant.ZIP_CODE:
			return isFormattedString(string, StringUtilConstant.ZIP_CODE_PATTERN);

		case StringUtilConstant.SS_NO:
			return isFormattedString(string, StringUtilConstant.SSNO_PATTERN);

		case StringUtilConstant.ER_NO:
			return isFormattedString(string, StringUtilConstant.ERNO_PATTERN);

		case StringUtilConstant.TEL_NO:
			return isFormattedString(string, StringUtilConstant.TELNO_PATTERN);

		default:
			return false;
		}
	}

	/**
	 * 입력값이 원하는 정규식 포맷에 부합하는지를 검사한다.
	 * 
	 * <pre>
	 * boolean isFormattedString = StringUtil.isFormattedString(&quot;123/456&quot;, &quot;[0-9]{3}/[0-9]{3}&quot;);
	 * // isFormattedString =&gt; true
	 * </pre>
	 * 
	 * @param string  입력값
	 * @param pattern 포맷
	 * @return boolean 포맷 검사 결과 (true.포맷 적합 false.포맷 부적합 )
	 */
	public static boolean isFormattedString(String string, String pattern) {
		StringUtil.isEmpty(string);
		StringUtil.isEmpty(pattern);

		return string.matches(pattern);
	}

	/**
	 * 입력값의 길이가 최소 및 최대 길이 사이에 있는지 검사한다.
	 * 
	 * <pre>
	 * boolean isStringLength = StringUtil.isStringLength(&quot;abc&quot;, 1, 5);
	 * // isStringLength =&gt; true
	 * </pre>
	 * 
	 * @param string    입력값
	 * @param minLength 최소 길이
	 * @param maxLength 최대 길이
	 * @return boolean 길이 검사 결과 (true.길이 적합 false.길이 부적합 )
	 */
	public static boolean isStringLength(String string, int minLength, int maxLength) {
		StringUtil.isEmpty(string);

		int length = string.length();
		return (minLength <= length && length <= maxLength) ? true : false;
	}

	/**
	 * 문자열의 특정 위치에 다른 문자열을 삽입한다. 문자열의 시작점은 0부터 시작한다.
	 * 
	 * <pre>
	 * String result = StringUtil.insertAt(1, &quot;ade&quot;, &quot;bc&quot;);
	 * // result =&gt; &quot;abcde&quot;
	 * </pre>
	 * 
	 * @param pos    삽입 위치
	 * @param string 문자열
	 * @param insert 삽입할 문자열
	 * @return String 특정 위치에 다른 문자열이 삽입된 문자열
	 */
	public static String insertAt(int pos, String string, String insert) {
		StringUtil.isEmpty(string);
		StringUtil.isEmpty(insert);

		if (pos < 0) {
			return string;
		}

		StringBuffer buffer = new StringBuffer(string);
		buffer.insert(pos, insert);
		return buffer.toString();
	}

	/**
	 * 문자열의 인코딩 방식을 바꾼다.
	 * 
	 * <pre>
	 * String result = StringUtil.encode(&quot;한글&quot;, &quot;utf8&quot;, &quot;ksc5601&quot;);
	 * // result의 인코딩이 ksc5601로 바뀜.
	 * </pre>
	 * 
	 * @param string       문자열
	 * @param fromEncoding 변경 전 인코딩 방식
	 * @param toEncoding   변경 후 인코딩 방식
	 * @return String 인코딩 방식이 변경된 문자열
	 */
	public static String encode(String string, String fromEncoding, String toEncoding) {
		StringUtil.isEmpty(string);
		StringUtil.isEmpty(fromEncoding);
		StringUtil.isEmpty(toEncoding);

		try {
			return new String(string.getBytes(fromEncoding), toEncoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 정수 값을 boolean 값으로 변환한다. c언어에서 활용하는 방식과 동일하다.<br/>
	 * 0 -&gt; false, !0 -&gt; true
	 * 
	 * <pre>
	 * boolean result = StringUtil.toBoolean(0);
	 * // result =&gt; false
	 * </pre>
	 * 
	 * @param i 정수값
	 * @return boolean 0 == false, 0 != true
	 */
	public static boolean toBoolean(int i) {
		return (i == 0) ? false : true;
	}

	/**
	 * 입력값을 특정 포맷으로 변환한다.<br/>
	 * 검사 포맷 종류
	 * <ul>
	 * <li>StringUtilConstant.ZIP_CODE : 우편 번호</li>
	 * <li>StringUtilConstant.ER_NO : 사업자 등록 번호</li>
	 * <li>StringUtilConstant.TEL_NO : 전화 번호</li>
	 * </ul>
	 * 
	 * <pre>
	 * String result = StringUtil.toFormattedString(&quot;123456&quot;, StringUtilConstant.ZIP_CODE);
	 * // result =&gt; &quot;123-456&quot;
	 * </pre>
	 * 
	 * @param string    입력값
	 * @param patternId 포맷 종류 ( [개요] 참조 )
	 * @return String 포맷 변환 문자열
	 */
	public static String toFormattedString(String string, int patternId) {
		StringUtil.isEmpty(string);

		switch (patternId) {
		case StringUtilConstant.ZIP_CODE:
			return toFormattedZipCode(string);

		case StringUtilConstant.ER_NO:
			return toFormattedErNo(string);

		case StringUtilConstant.TEL_NO:
			return toFormattedTelNo(string);

		default:
			return string;
//                throw new BaseRmRuntimeException("Invalid format"); 
		}
	}

	/**
	 * 입력값을 우편번호 포맷으로 변환한다.
	 * 
	 * <pre>
	 *  switch(patternId)
	 *  {            
	 *      case StringUtilConstant.ZIP_CODE : 
	 *          return toFormattedZipCode(string);
	 *      ..
	 *  }
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 우편번호 포맷으로 변환된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null이거나 자리수가 6자리가 아니거나 숫자가 아닌경우
	 */
	private static String toFormattedZipCode(String string) {
		StringUtil.isEmpty(string);

		if (string.length() != 6 || !isDigit(string)) {
//            throw new BaseRmRuntimeException("It is not zip code"); 
			return string;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(string.substring(0, 3));
		buffer.append('-');
		buffer.append(string.substring(3, 6));

		return buffer.toString();
	}

	/**
	 * 입력값을 사업자 등록번호 포맷으로 변환한다.
	 * 
	 * <pre>
	 *  switch(patternId)
	 *  {            
	 *      case StringUtilConstant.ER_NO  : 
	 *          return toFormattedErNo(string); 
	 *      ..
	 *  }
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 사업자 등록번호 포맷으로 변환된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null이거나 자리수가 10자리가 아니거나 숫자가 아닌경우
	 */
	private static String toFormattedErNo(String string) {
		StringUtil.isEmpty(string);

		if (string.length() != 10 || !isDigit(string)) {
//            throw new BaseRmRuntimeException("Invalid format"); 
			return string;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(string.substring(0, 3));
		buffer.append('-');
		buffer.append(string.substring(3, 5));
		buffer.append('-');
		buffer.append(string.substring(5, 10));

		return buffer.toString();
	}

	/**
	 * 입력값을 전화번호 포맷으로 변환한다.
	 * 
	 * <pre>
	 *  ..
	 *  switch(patternId)
	 *  {            
	 *      case StringUtilConstant.TEL_NO  : 
	 *          return toFormattedTelNo(string);
	 *      ..
	 *  }
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 전화번호 포맷으로 변환된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null이거나 자리수가 9보다 작거나 11보다 크거나 숫자가 아닌경우
	 */
	private static String toFormattedTelNo(String string) {
		StringUtil.isEmpty(string);

		int length = string.length();

		if (length < 9 || length > 11 || !isDigit(string)) {
//            throw new BaseRmRuntimeException("Invalid Telephone No format"); 
			return string;
		}

		StringBuffer buffer = new StringBuffer();

		if (string.charAt(1) == '2') {
			buffer.append(string.substring(0, 2)).append('-');
			buffer.append(string.substring(2, length - 4)).append('-');
		} else {
			buffer.append(string.substring(0, 3)).append('-');
			buffer.append(string.substring(3, length - 4)).append('-');
		}
		buffer.append(string.substring(length - 4));

		return buffer.toString();
	}

	/**
	 * 입력값의 특정 포맷을 없애고 값으로 변환한다. (punctuation 관련된 문자를 제거한다) 미리 정의된 포맷이 아니었다면
	 * BaseRuntimeException를 던진다.
	 * 
	 * 포맷 종류
	 * <ul>
	 * <li>StringUtilConstant.ZIP_CODE : 우편 번호</li>
	 * <li>StringUtilConstant.ER_NO : 사업자 등록 번호</li>
	 * <li>StringUtilConstant.TEL_NO : 전화 번호</li>
	 * </ul>
	 * 
	 * <pre>
	 * String result = StringUtil.toUnformattedString(&quot;123-456&quot;, StringUtilConstant.ZIP_CODE);
	 * // result =&gt; &quot;123456&quot;
	 * </pre>
	 * 
	 * @param string    입력값
	 * @param patternId 입력값의 포맷
	 * @return String 숫자열
	 * @throws BaseRmRuntimeException 입력값이 null이거나 일치하는 포멧이 없을경우
	 */
	public static String toUnformattedString(String string, int patternId) {
		StringUtil.isEmpty(string);

		switch (patternId) {
		case StringUtilConstant.ZIP_CODE:
			return toUnformattedString(string);

		case StringUtilConstant.ER_NO:
			return toUnformattedString(string);

		case StringUtilConstant.TEL_NO:
			return toUnformattedString(string);

		default:
			return string;
//                throw new BaseRmRuntimeException("Invalid format"); 
		}
	}

	/**
	 * 입력값에서 punctuation 캐릭터를 제거하여 숫자만을 가지는 문자열로 변환한다.
	 * 
	 * <pre>
	 *  switch(patternId)
	 *  {            
	 *      case StringUtilConstant.ZIP_CODE      : 
	 *          return toUnformattedString(string);
	 *  ..
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 숫자열
	 */
	private static String toUnformattedString(String string) {
		return string == null ? null : string.replaceAll("[^0-9]", "");
	}

	/**
	 * 입력값에서 ',', 공백을 제거한 숫자열로 변환한다. (2008.12.23 기존 메소드를 삭제하고 새로 작성함)
	 * 
	 * <pre>
	 * String result = StringUtil.toUnformattedNumber(&quot;-123,456.05&quot;);
	 * // result =&gt; &quot;-123456.05&quot;
	 * </pre>
	 * 
	 * @param formattedNumber 입력값
	 * @return String 숫자열
	 * @throws BaseRmRuntimeException 입력값이 null인 경우나 Parsing 과정에서 ParseException이
	 *                                발생한 경우
	 */
	public static String toUnformattedNumber(String formattedNumber) {
		StringUtil.isEmpty(formattedNumber);

		char[] formatted = formattedNumber.toCharArray();

		int strLength = 0;
		for (int i = 0; i < formatted.length; i++) {
			if (formatted[i] == ',' || formatted[i] == ' ') {
				strLength++;

				for (int j = i; j < formatted.length - 1; j++) {

					formatted[j] = formatted[j + 1];
				}
			}
		}

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < formatted.length - strLength; i++) {
			result.append(formatted[i]);
		}

		return result.toString();
	}

	/**
	 * 문자열을 받아서 문자열 내에 숫자만 뽑아서 리턴한다.
	 * 
	 * @param string
	 * @return String
	 */
	public static String toNumber(String string) {
		char[] stringArray = string.toCharArray();
		StringBuilder number = new StringBuilder();
		for (char ch : stringArray) {
			if (ch >= '0' && ch <= '9') {
				number.append(ch);
			}
		}

		return number.toString();
	}

	/**
	 * 숫자열의 포맷을 변환한다. (&quot;####.##&quot; -&gt; &quot;#,##0.##&quot;
	 * (StringUtilConstant.NUMBER_PATTERN 에 정의))
	 * 
	 * <pre>
	 * String result = StringUtil.toFormattedNumber(&quot;-123456.05&quot;);
	 * // result =&gt; &quot;-123,456.05&quot;
	 * </pre>
	 * 
	 * @param digit   숫자열
	 * @param pattern 숫자 패턴
	 * @return String 포맷 변환된 숫자열
	 * @throws BaseRmRuntimeException 숫자열이 null인 경우나 숫자열이 아닌 경우
	 */
	public static String toFormattedNumber(String digit, String pattern) {
		StringUtil.isEmpty(digit);

		try {
			return StringUtil.toFormattedNumber(Double.parseDouble(digit), pattern);
		} catch (NumberFormatException e) {
			return digit;
		}
	}

	/**
	 * 실수를 원하는 포맷의 숫자열로 변환한다.
	 * 
	 * <pre>
	 * String result = StringUtil.toFormattedNumber(&quot;123456.0&quot;, &quot;#,##0.0#&quot;);
	 * // result =&gt; &quot;123,456.0&quot;
	 * </pre>
	 * 
	 * @param value   실수
	 * @param pattern 포맷
	 * @return String 포맷 변환된 숫자열
	 * @throws BaseRmRuntimeException 포멧이 null인 경우
	 */
	public static String toFormattedNumber(double value, String pattern) {
		StringUtil.isEmpty(pattern);

		return new DecimalFormat(pattern).format(value);
	}

	/**
	 * 입력 문자열의 길이가 기준 길이보다 작을 경우, 문자열 앞 쪽에 공백으로 채운다. 반대로 입력 문자열의 길이가 기준 길이보다 클 경우,
	 * 지정된 길이 만큼 반환한다.
	 * 
	 * <pre>
	 * String result = StringUtil.addLeftSpace(&quot;string&quot;, 10);
	 * // result =&gt; &quot;    string&quot;
	 * </pre>
	 * 
	 * @param src    문자열
	 * @param length 기준 길이
	 * @return String 기준 길이에 맞춘 문자열
	 * @throws BaseRmRuntimeException 문자열이 null인 경우
	 */
	public static String addLeftSpace(String src, int length) {
		StringUtil.isEmpty(src);

		int srcLength = src.length();

		if (length < 0) {
			return "";
		}

		if (srcLength >= length) {
			return src.substring(0, 20);
		} else {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < length - srcLength; i++) {
				sb.append(" ");
			}
			sb.append(src);

			return sb.toString();
		}
	}

	/**
	 * 입력 문자열의 길이가 기준 길이보다 작을 경우, 문자열 뒷 쪽에 공백으로 채운다. 입력 문자열의 길이가 기준 길이보다 클 경우, 지정된
	 * 길이 만큼 반환한다.
	 * 
	 * <pre>
	 * String result = StringUtil.addRightSpace(&quot;string&quot;, 10);
	 * // result =&gt; &quot;string    &quot;
	 * </pre>
	 * 
	 * @param src    문자열
	 * @param length 기준 길이
	 * @return String 기준 길이에 맞춘 문자열
	 * @throws BaseRmRuntimeException 문자열이 null인 경우
	 */
	public static String addRightSpace(String src, int length) {
		StringUtil.isEmpty(src);

		int srcLength = src.length();

		if (length < 0) {
			return "";
		}

		if (srcLength >= length) {
			return src.substring(0, length);
		} else {
			StringBuffer sb = new StringBuffer();

			sb.append(src);
			for (int i = 0; i < length - srcLength; i++) {
				sb.append(" ");
			}

			return sb.toString();
		}
	}

	/**
	 * 문자열에서 특정 문자열을 다른 문자열로 대체한다.
	 * 
	 * <pre>
	 * String result = StringUtil.replaceAllString(&quot;abcdefg...hij...klm&quot;, &quot;...&quot;, &quot;&quot;);
	 * // result =&gt; &quot;abcdefghijklm&quot;
	 * </pre>
	 * 
	 * @param src        입력값
	 * @param indexStr   대체될 문자열
	 * @param replaceStr 대체할 문자열
	 * @return String 특정 문자열이 다른 문자열로 변경된 문자열
	 */
	public static String replaceAllString(String src, String indexStr, String replaceStr) {
		StringUtil.isEmpty(src);
		StringUtil.isEmpty(indexStr);
		StringUtil.isEmpty(replaceStr);

		if (src.indexOf(indexStr) == -1) {
			return src;
		}

		StringBuffer buffer = new StringBuffer(src);
		int index = -1;

		while ((index = buffer.indexOf(indexStr)) != -1) {
			buffer.delete(index, index + indexStr.length());
			buffer.insert(index, replaceStr);
		}

		return buffer.toString();
	}

	/**
	 * 문자열에서 특정 문자열을 배열에 저장된 순서대로 다른 문자열들로 대체한다.
	 * 
	 * <pre>
	 * String[] replaceStr = { &quot;1&quot;, &quot;2&quot; };
	 * 
	 * String result = StringUtil.replaceStringArray(&quot;abcdefghdef&quot;, &quot;def&quot;, replaceStr);
	 * // result =&gt; &quot;abc1gh2&quot;
	 * </pre>
	 * 
	 * @param src        입력값
	 * @param indexStr   대체될 문자열
	 * @param replaceStr 대체할 문자열 배열
	 * @return String 특정 문자열이 다른 문자열들로 변경된 문자열
	 */
	public static String replaceStringArray(String src, String indexStr, String replaceStr[]) {
		StringUtil.isEmpty(src);
		StringUtil.isEmpty(indexStr);
		isNull(replaceStr);

		if (src.indexOf(indexStr) == -1) {
			return src;
		}

		StringBuffer buffer = new StringBuffer(src);
		int index = -1;

		int i = 0;
		int cnt = replaceStr.length;
		while ((index = buffer.indexOf(indexStr)) != -1) {
			if (cnt == 0) {
				return buffer.toString();
			}
			buffer.delete(index, index + indexStr.length());
			buffer.insert(index, replaceStr[i++]);
			cnt--;
		}

		return buffer.toString();
	}

	/**
	 * 문자열에서 특정 문자열의 첫 번째를 다른 문자열로 대체한다.
	 * 
	 * <pre>
	 * String result = StringUtil.replaceFirstString(&quot;abcdefghdef&quot;, &quot;def&quot;, &quot;fed&quot;);
	 * // result =&gt; &quot;abcfedghdef&quot;
	 * </pre>
	 * 
	 * @param src        입력값
	 * @param indexStr   대체될 문자열
	 * @param replaceStr 대체할 문자열 배열
	 * @return String 특정 문자열이 다른 문자열로 변경된 문자열
	 */
	public static String replaceFirstString(String src, String indexStr, String replaceStr) {
		StringUtil.isEmpty(src);
		StringUtil.isEmpty(indexStr);
		StringUtil.isEmpty(replaceStr);

		if (src.indexOf(indexStr) == -1) {
			return src;
		}

		StringBuffer buffer = new StringBuffer(src);
		int index = buffer.indexOf(indexStr);

		buffer.delete(index, index + indexStr.length());
		buffer.insert(index, replaceStr);

		return buffer.toString();
	}

	/**
	 * 문자열에서 공백을 제거한 후 소문자로 변환한다.
	 * 
	 * <pre>
	 *  String result = StringUtil.toLowerCaseAfterTrim(&quot; ABC DEF &quot;)
	 *  // result =&gt; &quot;abc def&quot;
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 앞, 뒤 공백 제거 후 소문자로 변경된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null인 경우
	 */
	public static String toLowerCaseAfterTrim(String string) {
		StringUtil.isEmpty(string);

		return string.trim().toLowerCase();
	}

	/**
	 * 구분자를 기준으로 문자열을 각각의 서브 문자열로 분해한 후 문자열의 배열로 리턴한다.
	 * 
	 * <pre>
	 * String[] results = StringUtil.split(&quot;123-456&quot;, &quot;-&quot;);
	 * // results =&gt; {&quot;123&quot;, &quot;456&quot;}
	 * </pre>
	 * 
	 * @param data      문자열
	 * @param delemeter 구분자
	 * @return String[] 구분자를 기준으로 분해된 서브 문자열의 배열 delemeter가 null인 경우 data를 배열에 담아 리턴
	 */
	public static String[] split(String data, String delemeter) {
		StringUtil.isEmpty(data);

		if (delemeter == null) {
			return new String[] { data };
		}

		String[] tokens = null;

		StringTokenizer tokenizer = new StringTokenizer(data, delemeter);
		tokens = new String[tokenizer.countTokens()];
		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			tokens[i] = tokenizer.nextToken();
		}
		return tokens;
	}

	/**
	 * 문자열 배열에 특정 문자열이 들어 있는지를 검사한다. 있으면 true, 없으면 false를 리턴한다.
	 * 
	 * <pre>
	 * String[] ref = { &quot;123&quot;, &quot;456&quot;, &quot;789&quot; };
	 * 
	 * String src = &quot;123&quot;;
	 * 
	 * boolean exist = StringUtil.existString(src, ref);
	 * // exist =&gt; true
	 * </pre>
	 * 
	 * @param src 특정 문자열
	 * @param ref 문자열 배열
	 * @return boolean 포함여부 src나 ref가 null이면 false 리턴,
	 */
	public static boolean existString(String src, String[] ref) {
		StringUtil.isEmpty(src);
		if (isNull(ref)) {
			return false;
		}

		for (int i = 0; i < ref.length; i++) {
			if (ref[i].equals(src)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 문자열의 바이트 수를 구한다. (한글:2, 영문:1)
	 * 
	 * <pre>
	 * int length = StringUtil.getKoreanStringLength(&quot;한글 English&quot;);
	 * // length =&gt; 12
	 * </pre>
	 * 
	 * @param value 문자열
	 * @return int 바이트 수
	 */
	public static int getKoreanStringLength(String value) {
		StringUtil.isEmpty(value);

		char[] chars = value.toCharArray();

		int length = 0;
		Character.UnicodeBlock type;
		for (int i = 0; i < chars.length; i++) {
			type = Character.UnicodeBlock.of(chars[i]);
			if (type == Character.UnicodeBlock.HANGUL_SYLLABLES
					|| type == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
				length += 2;
			} else {
				length++;
			}
		}
		return length;

	}

	/**
	 * 문자열을 화면에 전시할 크기에 맞게 변환한다. 문자열이 화면에 전시할 크기보다 클 경우, 초과 부분을 &quot;...&quot;
	 * 처리한다. &quot;...&quot; 부분도 길이에 포함된다.
	 * 
	 * <pre>
	 * String result = StringUtil.getSubStringKorean(&quot;abcdefghijk&quot;, 10);
	 * // result =&gt; &quot;abcdefg...&quot;
	 * </pre>
	 * 
	 * @param value         문자열
	 * @param displayLength 전시 크기
	 * @return String 화면에 전시할 크기에 맞게 변환된 문자열
	 */
	public static String getSubStringKorean(String value, int displayLength) {
		StringUtil.isEmpty(value);

		char[] chars = value.toCharArray();
		Character.UnicodeBlock type;
		StringBuffer valueBuffer = new StringBuffer();

		for (int i = 0, j = 0; j + 3 < displayLength; i++, j++) {
			type = Character.UnicodeBlock.of(chars[i]);
			if (type == Character.UnicodeBlock.HANGUL_SYLLABLES
					|| type == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
				j++;
			}
			valueBuffer.append(chars[i]);
		}

		if (getKoreanStringLength(valueBuffer.toString()) + 3 > displayLength) {
			valueBuffer.append("..");
		} else {
			valueBuffer.append("...");
		}
		return valueBuffer.toString();
	}

	/**
	 * 배열에 담긴 문자열들의 오른쪽 공백을 제거한다. 왼쪽 및 문자열 내 공백은 제거하지 않는다.
	 * 
	 * <pre>
	 * String[] values = { &quot;123 &quot;, &quot; 123&quot;, &quot;12 3&quot; };
	 * 
	 * String[] results = StringUtil.rightTrim(values); // results =&gt; {&quot;123&quot;, &quot; 123&quot;, &quot;12 3&quot;}
	 * </pre>
	 * 
	 * @param values 문자열 배열
	 * @return String[] 오른쪽 공백을 제거한 문자열 배열
	 */
	public static String[] rightTrim(String[] values) {
		isNull(values);
		StringBuffer b = null;

		for (int j = 0; j < values.length; j++) {
			b = new StringBuffer(values[j]);
			for (int k = b.length() - 1; k >= 0; k--) {
				if (b.charAt(k) == ' ') {
					b.deleteCharAt(k);
				} else if (b.charAt(k) != ' ') {
					break;
				}
			}
			values[j] = b.toString();
		}

		return values;
	}

	/**
	 * 입력 문자열의 null 여부를 체크한 후 공백을 제거한다.
	 * 
	 * <pre>
	 * StringUtil.trim(value);
	 * </pre>
	 * 
	 * @param value 입력 문자열
	 * @return String null이 아닌 경우 공백이 제거된 문자열, 그외 ""
	 */
	public static String trim(String value) {
		return nullStringCheck(value).trim();
	}

	/**
	 * 입력된 문자열을 알파벳만 있는지 검사한다. 맞으면 true, 다르면 false를 리턴한다.
	 * 
	 * @param text 입력 문자열
	 * @return boolean StringUtilConstant.ALPHABET_PATTERN인 경우 true, 아니면 false
	 */
	public static boolean isAlphabet(String text) {
		StringUtil.isEmpty(text);
		if (Pattern.matches(StringUtilConstant.ALPHABET_PATTERN, text)) {
			return true;

		}
		return false;
	}

	/**
	 * 입력된 문자열을 알파벳이나 숫자만 있는지 검사한다. 맞으면 true, 다르면 false를 리턴한다.
	 * 
	 * @param text 입력문자열
	 * @return boolean StringUtilConstant.ALPHABET_DIGIT_PATTERN 인 경우 true, 아니면
	 *         false
	 */
	public static boolean isAlphabetOrDigit(String text) {
		StringUtil.isEmpty(text);
		if (Pattern.matches(StringUtilConstant.ALPHABET_DIGIT_PATTERN, text)) {
			return true;

		}
		return false;
	}

	/**
	 * 입력된 문자열을 알파벳이나 숫자, _ 만 있는지 검사한다. 맞으면 true, 다르면 false를 리턴한다.
	 * 
	 * @param text 입력문자열
	 * @return boolean StringUtilConstant.ALPHABET_DIGIT_UNDERBAR_PATTERN인 경우 true,
	 *         아니면 false
	 */
	public static boolean isAlphabetOrDigitOrUnderbar(String text) {
		StringUtil.isEmpty(text);
		if (Pattern.matches(StringUtilConstant.ALPHABET_DIGIT_UNDERBAR_PATTERN, text)) {
			return true;

		}
		return false;
	}

	/**
	 * 입력된 문자열을 이메일 주소 스트링 포맷인지 검사한다. 맞으면 true, 다르면 false를 리턴한다.
	 * 
	 * @param text 입력문자열
	 * @return boolean StringUtilConstant.EMAIL_PATTERN인 경우 true, 아니면 false
	 */
	public static boolean isEmailPattern(String text) {
		StringUtil.isEmpty(text);
		if (Pattern.matches(StringUtilConstant.EMAIL_PATTERN, text)) {
			return true;
		}
		return false;
	}

	/**
	 * 첫번째 문자를 소문자로 변환한다. 빈 문자열인 경우 ""를 리턴한다.
	 * 
	 * <pre>
	 *  String string = &quot;AAAAAA&quot;;
	 *  StringUtil.toLowerCaseAtFirstChar(string);
	 *  결과 =&gt; &quot;aAAAAA&quot;
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 변환된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null인 경우
	 */
	public static String toLowerCaseAtFirstChar(String string) {
		if (isEmpty(string)) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(string.substring(0, 1).toLowerCase());
		buffer.append(string.substring(1, string.length()));

		return buffer.toString();
	}

	/**
	 * 첫번째 문자를 대문자로 변환한다. 빈 문자열인 경우 ""를 리턴한다.
	 * 
	 * <pre>
	 *  String string = &quot;aaaaaa&quot;;
	 *  StringUtil.toUpperCaseAtFirstChar(string);
	 *  결과 =&gt; &quot;Aaaaaa&quot;
	 * </pre>
	 * 
	 * @param string 입력값
	 * @return String 변환된 문자열
	 * @throws BaseRmRuntimeException 입력값이 null인 경우
	 */
	public static String toUpperCaseAtFirstChar(String string) {
		if (isEmpty(string)) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(string.substring(0, 1).toUpperCase());
		buffer.append(string.substring(1, string.length()));

		return buffer.toString();
	}

	/**
	 * Camel 형태의 문자열을 DB Column 형태의 String으로 변환한다. 일반적으로 변수명이 abcDef 인것을 SQL 컬럼 명이
	 * ABC_DEF로 변경해준다.
	 * 
	 * <pre>
	 * StringUtil.convertToDBColumnString(&quot;carmelNotationString&quot;);
	 * result: &quot;CARMEL_NOTATION_STRING&quot;
	 * </pre>
	 * 
	 * @param columnString 변환할 입력 문자열
	 * @return String DB 칼럼 타입으로 변환된 문자열
	 */
	public static String convertToDBColumnString(String columnString) {
		StringBuffer result = new StringBuffer();
		byte[] bytes = columnString.getBytes();

		char c;

		for (int i = 0; i < bytes.length; i++) {
			c = (char) bytes[i];
			// 첫자리 무시
			if (i > 0 && Character.isUpperCase(c)) {
				result.append("_");
			}
			result.append(c);
		}
		return result.toString().toUpperCase();
	}

	/**
	 * 입력된 문자열 contents에서 index 번째의 라인 넘버를 리턴한다. 공백을 포함한다.
	 * 
	 * @param contents 입력된 문자열
	 * @param index    라인넘버를 구할 인덱스
	 * @return int 라인 넘버
	 */
	public static int countLine(String contents, int index) {
		int count = 0;
		if (index < 0 || index > contents.length()) {
			return 0;
		}

		for (int i = 0; i < index; i++) {
			if ('\n' == contents.charAt(i))
				count++;
		}
		return count;
	}

	/**
	 * 입력된 문자열의 총 라인수를 리턴한다. 공백 줄은 제외한다.
	 * 
	 * @param contents 입력 문자열
	 * @return int 주석을 제외한 라인 줄 수
	 */
	public static int getLocWithComment(String contents) {
		contents = contents.trim();
		int line = 0;

		if (contents == null || "".equals(contents)) {
			return 0;
		}

		BufferedReader br = new BufferedReader(new StringReader(contents));
		String lineString = null;
		try {
			while ((lineString = br.readLine()) != null) {
				if ("".equals(lineString.trim())) {
					continue;
				}

				line++;
			}
		} catch (IOException e) {
			line = -1;
		}
		return line;
	}

	/**
	 * 입력된 문자열 source에서 offset 번째의 라인 넘버를 리턴한다.
	 * 
	 * <pre>
	 *      String source = &quot;brown fox \n mungo jerrie \n rumble teazer \n elizabeth&quot;;
	 *      StringUtil.getLine(source, 10))
	 *      
	 *      result: 1
	 * </pre>
	 * 
	 * @param source 입력 문자열
	 * @param offset 문자열 내 위치
	 * @return int 라인 넘버
	 */
	public static int getLine(String source, int offset) {
		if (source.length() <= offset) {
			return -1;
		}

		byte[] bytes = source.getBytes();
		int index = 1;
		byte ch = 0;
		for (int i = 0; i < offset; i++) {
			ch = bytes[i];
			if (ch == '\n') {
				index++;
			}
		}

		return index;
	}

	/**
	 * CVS나 SVN 파일일 경우에는 빌드를 위해 업로드 하지 않는다.
	 * 
	 * <pre>
	 * filePath = &quot;D:\\Dworkspace\\Nexbuild3.2\\nexbuild.development.admin\\.svn&quot;;
	 * StringUtil.isFileForBuild(fileName, filePath);
	 * result: false
	 * </pre>
	 * 
	 * @param fileName 파일명
	 * @param filePath 파일 경로 명
	 * @return boolean 시스템 파일이거나 svn 혹은 cvs 관련 파일인 경우 false, 아닌 경우엔 true
	 */
	public static boolean isFileForBuild(String fileName, String filePath) {

		if (fileName.startsWith(".")) {
			return false;
		}

		if (filePath.indexOf("CVS") > -1 || filePath.indexOf("cvs") > -1 || filePath.indexOf("SVN") > -1
				|| filePath.indexOf("svn") > -1) {
			return false;
		}
		return true;
	}

	/**
	 * 입력 문자열 배열에 특정 부분 문자열이 있는지를 점검하여 있다면 true를 리턴한다. 입력 및 검색 문자열이 null, 빈 문자열인 경우
	 * 및 매치가 존재하지 않는 경우엔 false를 리터턴한다.
	 * 
	 * @param stringArray 입력 문자열 배열
	 * @param findString  검색할 부분 문자열
	 * @return boolean 있는 경우 true, 없다면 false
	 */
	public static boolean hasStringInArray(String[] stringArray, String findString) {
		if (stringArray == null || stringArray.length == 0 || findString == null || "".equals(findString)) {
			return false;
		}

		for (String string : stringArray) {
			if (findString.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 파라메터로 넘어온 객체가 null인지 체크해서 결과를 리턴한다.
	 * 
	 * @param object
	 * @return boolean
	 */
	public static boolean isNull(Object object) {
		return (object == null);
	}

	/**
	 * 인자로 넘어온 Stirng의 배열에
	 * 
	 * @param second
	 * @return boolean
	 */
	public static boolean isEmptyString(String... strings) {
		String string = "";
		for (int i = 0; i < strings.length; i++) {
			string = strings[i];
			if (string == null || "".equals(string)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 현재 실행되고 있는 OS에 적합한 Line separator를 리턴한다. 주의 : Eclipse내에서만 실행되는 메소드임
	 * 
	 * @return String
	 */
	public static String getSystemLineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 두개의 문자열이 동일한지 비교하여 리턴한다.
	 * 
	 * @param content
	 * @param content2
	 * @return boolean
	 */
	public static boolean isSame(String first, String second) {
		if (first != null && second == null) {
			return false;
		}

		if (first == null && second != null) {
			return false;
		}

		if (first != null && second != null) {
			if (!first.equals(second)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Null을 포함하여 두개의 문자열이 동일한지를 비교한다.
	 * 
	 * @param first
	 * @param second
	 * @return boolean
	 */
	public static boolean isSameString(String first, String second) {
		if (first == null && second != null) {
			return false;
		}
		if (first != null && second == null) {
			return false;
		}
		if (!first.equals(second)) {
			return false;
		}

		return true;
	}

	public static String xmlEscapeConvert(String string) {
		if (isEmpty(string)) {
			return "";
		}

		string = replaceAll(string, "\r", "&return;");
		string = replaceAll(string, "\n", "&newline;");
		string = replaceAll(string, "&", "&amp;");
		string = replaceAll(string, "<", "&lt;");
		string = replaceAll(string, ">", "&gt;");
		string = replaceAll(string, "'", "&apos;");
		string = replaceAll(string, "\"", "&quot;");

		return string;
	}

	public static String xmlEscapeReConvert(String string) {
		if (isEmpty(string)) {
			return "";
		}

		string = replaceAll(string, "&return;", "\r");
		string = replaceAll(string, "&newline;", "\n");
		string = replaceAll(string, "&amp;", "&");
		string = replaceAll(string, "&lt;", "<");
		string = replaceAll(string, "&gt;", ">");
		string = replaceAll(string, "&apos;", "'");
		string = replaceAll(string, "&quot;", "\"");

		return string;
	}

	/**
	 * DB 데이타타입을 UI 테이타타입으로 변경
	 * 
	 * @param type
	 * @return String
	 */
	public static String convertToUIDataTypeName(String type) {
		String dataType = "CHAR";
		if (type != null) {
			type = type.toUpperCase();
			if (type.equals("CHAR")) {
				dataType = "CHAR";
			} else if (type.contains("VARCHAR") || type.contains("BINARY") || type.contains("TEXT")
					|| type.contains("BLOB")) {
				dataType = "VARCHAR";
			} else if (type.contains("DOUBLE") || type.contains("FLOAT") || type.contains("INT")) {
				dataType = "NUMBER";
			} else if (type.contains("DATE") || type.contains("TIME")) {
				dataType = "DATE";
			} else if (type.contains("BOOL")) {
				dataType = "BOOLEAN";
			}
		}
		return dataType;
	}

	/**
	 * length 만큼 random 하게 생성된 byte를 return 한다. <br>
	 * RSA PKCS5에 의해서 8byte이상을 추천한다. <br>
	 * 
	 * @param length 생성할 random byte 길이
	 * @return
	 * @author <a href="mailto:heokjin@duzon.com">윤혁진</a>
	 */
	public static byte[] getSecureRandomString(int length) throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[length];
		random.nextBytes(salt);

		return salt;
	}

	/**
	 * length 만큼 random 하게 생성된 String을 return 한다. <br>
	 * 
	 * @param length 생성할 random String 길이
	 * @return
	 * @author <a href="mailto:heokjin@duzon.com">윤혁진</a>
	 */
	public static String getRandomString(int length) {
		return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890");

	}
}
