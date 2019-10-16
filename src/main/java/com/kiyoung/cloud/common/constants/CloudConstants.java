package com.kiyoung.cloud.common.constants;


public class CloudConstants {
	
	public static final String SERVICE_PARAM    		= "paramText";
	public static final String SERVICE_PARAM_TYPE	    = "paramType";
	public static final String SERVICE_PARAM_TYPE_LIST  = "paramTypeList";
	public static final String SERVICE_PARAM_TYPE_SINGLE  = "paramTypeSingle";
	public static final String BLOCK_NAME				= "block";
	public static final String BLOCK_NAME_AUTH			= "DCloudAuthenticationBlock";
	public static final String BLOCK_NAME_LOC			= "DCloudLocationBlock";
	public static final String BLOCK_NAME_DAS			= "DCloudDASBlock";
	public static final String SERVICE_NAME				= "service";
	public static final String SERVICE_METHOD			= "method";
	public static final String XFORM_RSA				= "RSA";
	public static final int    XFORM_RSA_KEYSIZE		= 512; 
	public static final String XFORM_TRIPLE_DES			= "TripleDES";
	public static final String XFORM_AES				= "AES";  //횬쟈 192bit
//	public static final String XFORM_TRANSFORM			= "/ECB/PKCS7Padding";
	public static final String XFORM_TRANSFORM			= "";
	public static final String RANDOM_KEY				= "er48nsjhwlG593mjhgdb20ih";  //횬쟈 192bit
//	public static final String XFORM_TRANSFORM			= "/CBC/PKCS7Padding";
	public static final String DCLOUD_AFFECTED_CNT		= "AffectedCount"; 
	public static final String DCLOUD_USEYN_Y			= "Y"; 
	public static final String DCLOUD_USEYN_N			= "N"; 
	public static final String DCLOUD_PUBLIC_KEY		= "GBODADIGBEVIMSEG64GQCAIBAUAAGSYAGBEAEQIA3YOKV3HAQJYK77BP2Q577N7XBII5OSR375G4PYO3MNTWRJP3JHU65JVOXRWV4IKZLKWYXPO4VCDF3IBUXVKR4JTSLOSGZJLOTMLTOKYCAMAQAAI";
	public static final String DCLOUD_PRIVATE_KEY		= "GCBACVICAEADADIGBEVIMSEG64GQCAIBAUAAJAQBH4YIEAJ3AIAQAASBADPBZKXM4CBHBL74F7KDX75X64FBDV2KHP7U3R7B3NRWO2FF7NE6T3VGV26G2XRBLFNK3C553SUIMXNAGS6VKHRGOJN2I3FFN2NRONZLAIBQCAABAJACMCXQ665YQ67WTCJDJHWGGDRT4NKKCOV62EMMN7I5ZV6EMPIYHGQ46QD4PN32WQRXOY5BLN43QWAZEOKM2S45GDBLILUNRLIIY3MNCEBCCAH42GPGTMYT3QAEARXSZHUPPTFQ64NVAEY2QF6MJKWBR7XRDPHCCMBCCAHA5ARMO54G2SCZAMKZY346XKJG25BK7IZIMBW5GVTHTVQCYJBZREBCAZP2DOPOCF72T2ZU5KXSDSQY7FBWFBZTKMLU26Q7BCVBA45K22FXAIQQBVCFMSS7ZQZOBSG2M5UETVLXMRBJQFIEEXYWPQY242EHDHAS7Q3ZAIQQB3W3VZ6K23WDRR7FFUG7KCHIDUCW3KEYELEIFZAGIVA7WB3C3B3H";
	public static final String PER_ID_VMMGR_ADMIN		= "8c55b263-e482-4992-8ace-d557e166e040";
	public static final String PER_ID_LOGINBC_ADMIN		= "c24358e0-c79a-494d-a699-fa683f6f16ce";
	public static final String PER_ID_SU_USER_ADMIN		= "b6485d70-ced3-4b02-9951-04a4df7d0f48";
	public static final String PER_ID_USER_ADMIN		= "11d0f1b9-a82d-4054-9e6b-2b0d98aca602";
	public static final String REL_TYPE_CS_2_BS			= "9c0a8bfd-1471-46dc-929d-f3f590153e27";
	public static final String TOKEN_ENDTIME			= "99991231999999";
	public static final String VMLOC_STAT_ANONY			= "Anonymous";
	public static final String VMLOC_STAT_ASSIGN		= "Assigned";
	public static final String VMM_RESULT_MSG			= "RESULT_MSG";
	public static final String VMM_RESULT_CODE			= "RESULT_CODE";
	public static final String VMM_RESULT_MESSAGE		= "RESULT_MESSAGE";
	public static final String VMM_LOCATION_STAT		= "VM_LOCATION_STAT";
	public static final long   SESSION_TIMEOUT			= 172800000; // 2일 
//	public static final long   SESSION_TIMEOUT			= 86400000; //1800000 이면 30분  3600000 1시간  , 24시간
	public static final String SESSION_TEST_TIME		= "20130828110000";  //이시간 이전에 만든 세션은 테스트를 목적으로 삭제가 되지 않는다.
	
	//message 
	public static final String SERVICE_SUCCESS			= "success";
	public static final String SERVICE_FAIL				= "error";
	public static final String SERVICE_MSG				= "serverMsg";
	public static final String SERVICE_RESULT			= "resultList";
	public static final String SERVICE_ERR_MSG			= "errorMsg";
	public static final String MSG_PERMISSION_NAME_NULL	= "DCLOUDMSG-00001-Permission Name 입력하세요.";
	public static final String MSG_BUSINSESS_NAME_NULL	= "DCLOUDMSG-00002-Business Name 입력하세요.";
	public static final String MSG_BUSINSESS_NO_NULL	= "DCLOUDMSG-00003-사업자번호 입력하세요.";
	public static final String MSG_BUSINSESS_DUPLICATE	= "DCLOUDMSG-00004-사업자번호 중복입니다.";
	public static final String MSG_ID_NULL				= "DCLOUDMSG-00005-id를 입력하세요.";
	public static final String MSG_RANDOMKEY_NULL		= "DCLOUDMSG-00006-RandomKey를 입력하세요.";
	public static final String MSG_NAME_NULL			= "DCLOUDMSG-00007-사용자이름필드(Name) 입력하세요.";
	public static final String MSG_BSNS_ID_NULL			= "DCLOUDMSG-00008-BusinessConnectID 입력하세요.";
	public static final String MSG_PER_ID_NULL			= "DCLOUDMSG-00009-PermissionID 입력하세요.";
	public static final String MSG_REL_NAME_NULL		= "DCLOUDMSG-00010-Relation Name 입력하세요.";
	public static final String MSG_IDPW_FORMAT			= "DCLOUDMSG-00011-ID@PW 포맷이 올바르지 않습니다.";
	public static final String MSG_PW_INCORRECT			= "DCLOUDMSG-00012-PW가 올바르지 않습니다.";
	public static final String MSG_ID_NOTEXIST			= "DCLOUDMSG-00013-ID가 없습니다.";
	public static final String MSG_DUP_USER				= "DCLOUDMSG-00014-ID 중복입니다.";
	public static final String MSG_INVALID_TOKEN		= "DCLOUDMSG-00015-TokenID가 유효하지 않습니다.";
	public static final String MSG_INVALID_SEQ			= "DCLOUDMSG-00016-시퀀스(Seq)가 유효하지 않습니다.";
	public static final String MSG_INVALID_USEYN		= "DCLOUDMSG-00017-삭제플래그(UseYn) 유효하지 않습니다.";
	public static final String MSG_UNKNOWN_ERROR		= "DCLOUDMSG-00017-Unknown Error";
	public static final String MSG_UNKNOWN_SERVICE		= "DCLOUDMSG-00018-ServiceName 파라메터가 없습니다.";
	public static final String MSG_UNKNOWN_METHOD		= "DCLOUDMSG-00019-MethodName 파라메터가 없습니다.";
	public static final String MSG_UNKNOWN_PARAM		= "DCLOUDMSG-00020-ParamText 파라메터가 없습니다.";
	public static final String MSG_UNKNOWN_PARAM_TYPE	= "DCLOUDMSG-00021-ParamType 파라메터가 없습니다.";
	public static final String MSG_UNKNOWN_BEAN			= "DCLOUDMSG-00022-서비스명이 유효하지 않습니다.";
	public static final String MSG_UNKNOWN_BEAN_METHOD	= "DCLOUDMSG-00023-메소드명이 유효하지 않습니다.";
	public static final String MSG_TOKEN_NULL			= "DCLOUDMSG-00024-TokenID가 비어있습니다.";
	public static final String MSG_IP_NULL				= "DCLOUDMSG-00025-IP가 비어있습니다.";
	public static final String MSG_PORT_NULL			= "DCLOUDMSG-00026-Port가 비어있습니다.";
	public static final String MSG_PERMISSIONID_DUP		= "DCLOUDMSG-00027-PermissionID가 중복입니다.";
	public static final String MSG_NOT_VMMGR			= "DCLOUDMSG-00028-VM MANAGER ADMIN 권한이 없습니다.";
	public static final String MSG_SVC_ID_NULL			= "DCLOUDMSG-00029-ServiceCatalogID 가 비어있습니다.";
	public static final String MSG_INVALID_SVC_ID		= "DCLOUDMSG-00030-ServiceCatalogID 가 유효하지 않습니다.";
	public static final String MSG_INVALID_BSNS_ID		= "DCLOUDMSG-00031-BusinessConnectID 가 유효하지 않습니다.";
	public static final String MSG_ER_ID_NULL			= "DCLOUDMSG-00032-EntityRelationID 가 비어있습니다.";
	public static final String MSG_NOT_LOINBC_ADMIN		= "DCLOUDMSG-00033-LoginBC admin 권한이 없습니다.";
	public static final String MSG_UC_ID_NULL			= "DCLOUDMSG-00034-UserConnectID가 비어있습니다.";
	public static final String MSG_NOT_LOGINBC_TOKEN	= "DCLOUDMSG-00035-loginBC Token이 아닙니다.";
	public static final String MSG_SEQ_NULL				= "DCLOUDMSG-00036-시퀀스가 비어있습니다.";
	public static final String MSG_LOCATION_ID_NULL		= "DCLOUDMSG-00037-LocationID가 비어있습니다.";
	public static final String MSG_LOC_ADD_ASSIGNED		= "DCLOUDMSG-00038-위치등록되었습니다.";
	public static final String MSG_LOC_ADD_ANONY		= "DCLOUDMSG-00039-익명위치등록되었습니다.";
	public static final String MSG_INVALID_TOK_LOC		= "DCLOUDMSG-00040-토큰정보가 유효하지 않습니다. 익명등록되었습니다.";
	public static final String MSG_VMM_CANNOT_ACCESS	= "DCLOUDMSG-00041-VMM에 접근 할 수 없습니다.";
	public static final String MSG_DIFF_BSNS_ID			= "DCLOUDMSG-00042-수정하려는 사용자의 소속정보가 다릅니다.";
	public static final String MSG_DUP_DATA_INSERT		= "DCLOUDMSG-00043-중복된 추가입니다.";
	public static final String MSG_PRMS_DETAIL_EXIST	= "DCLOUDMSG-00044-지우려는 권한에 할당된 권한 상세내역이 존재합니다.";
	public static final String MSG_NOT_JOINED			= "DCLOUDMSG-00045-가입된 사용자가 아닙니다.";
	public static final String MSG_PRMS_NOT_EXIST		= "DCLOUDMSG-00046-해당 권한이 없습니다.";
	public static final String MSG_SEC_LOGIN_FAIL		= "DCLOUDMSG-00047-2차인증 코드가 유효하지 않습니다(REDIS 서버오류).";
	
	//mongodb 
	public static final String MONGODB_URL				= "convergence.duzon.co.kr"; //test server 
	public static final int    MONGODB_PORT				= 27017;
	public static final String MONGODB_SESSIONDBNAME	= "dcloudsessiondb";
	public static final String MONGODB_LOGDBNAME		= "dcloudlogdb";
	public static final String MONGODB_ID				= "mongodb";
	public static final String MONGODB_PW				= "mongodb123";
	public static final String MONGODB_COLLECTION		= "sessioncollection";
	
	public static final String SESSIONSVC				= "SessionSvc";
	public static final String SESSIONSVC_GETSESSION	= "getSession";
	public static final String SESSIONSVC_MODSESSION	= "modifySession";
	public static final String SESSIONSVC_GETSESSION_D	= "getSessionDetail";
	
	public static final String INITIAL_VECTOR = "bghtyu846qplo01k";
	
	public static final String CLIENTID	= "CSB";
	public static final String VM	= "VM";
	public static final String GATEWAY	= "GW";

	//YTN Flag
	public static final String insert	= "I";
	public static final String update	= "U";
	public static final String delete	= "D";
	public static final String PSTORAGE = "./csb";
	 
	public static final String DCLOUD_PORTAL = "DCloud-Portal";
	public static final String DCLOUD_REAPP = "ReappCore";
	public static final String DCLOUD_MOBILE = "MOBILE";
	public static final String ACCOUNT_VDI_SUFFIX = "_vdi";
	
	public static final int VM_CONNECT_TIMEOUT = 5000;
	
	public static final String OPENSTACK = "OPENSTACK";
	public static final String OPENSTACK_ID = "OPENSTACK_ID";
	public static final String OPENSTACK_PASSWORD = "OPENSTACK_PASSWORD";
	public static final String OPENSTACK_URL_TOKEN = "OPENSTACK_URL_TOKEN";
	
	public static final String HYPERV = "HYPERV";
	public static final String HYPERV_DOMAIN_ACCOUNT = "HYPERV_DOMAIN_ACCOUNT";
	public static final String HYPERV_DOMAIN_PASSWORD = "HYPERV_DOMAIN_PASSWORD";
	public static final String HYPERV_LOCAL_ACCOUNT = "HYPERV_LOCAL_ACCOUNT";
	public static final String HYPERV_LOCAL_PASSWORD = "HYPERV_LOCAL_PASSWORD";
	
	public static final String OS_KEY = "os_key";
	public static final String OS_VALUE = "os_value";
	public static final String GATEWAY_SERVER_STATE_ON = "GatewayServerStateOn";
	
	public static final String SESSION_COLLECTION = "hashMap";
	public static final String NOTI_COLLECTION = "notiMsgFormat";
	public static final String ACCOUNT_BIZMAP = "accountBizMap";
	public static final String ACCOUNT_TokenMAP = "accountTokenMap";
	public static final String old = "_old";
	public static final String ServerKey_Tdev = "947c1b8e-2600-42f6-b920-96087363c41d";
	
	public static final String SMARTAOLDDATA = "SMARTAOLDDATA";
    public static final String WEHAGOPM = "WEHAGOPM";

    public static final String NIP_DB_Plus = "NIP_DB_Plus";
}