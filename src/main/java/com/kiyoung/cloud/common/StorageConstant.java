package com.kiyoung.cloud.common;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.util.Version;

import com.kiyoung.cloud.storage.type.MimeType;


public class StorageConstant {
	
	public static final String STORAGE = "storage";
	public static final String charset = "UTF-8";
	public static final String CLIENTID = "STORAGE_SERVER";
	
	public static String dCloudUrlReal = "";
	public static String dCloudNotLogUrlReal = "";
	public static String dCloudLoginUrlReal = ""; 
	public static String dCloudFileUrlReal = "";
	public static String dCloudWebDirectUrlReal = ""; 
	public static String dCloudWebDirectUrlRealLink = "";
	
//	public static String jmxServerURL = "";
	
	static {
		Properties props = new Properties();
	    try {
			props.load(StorageConstant.class.getResourceAsStream("/META-INF/properties/storageUrl.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    dCloudUrlReal = props.getProperty("dCloudUrlReal");
	    dCloudNotLogUrlReal = props.getProperty("dCloudNotLogUrlReal");
	    dCloudLoginUrlReal = props.getProperty("dCloudLoginUrlReal");
	    dCloudFileUrlReal = props.getProperty("dCloudFileUrlReal");
	    dCloudWebDirectUrlReal = props.getProperty("dCloudWebDirectUrlReal");
	    dCloudWebDirectUrlRealLink = props.getProperty("dCloudWebDirectUrlRealLink");
	}
	
	static {
		Properties props = new Properties();
		try { 
			props.load(StorageConstant.class.getResourceAsStream("/META-INF/properties/biz.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		jmxServerURL = props.getProperty("jmxServerURL");
	}

	public static String TEST_ERROR = "ERROR";
	
	public static String SUCCESS_CODE = "0000";
	
	/** 공유 초대(XX공유폴더에 누구님이 누구님을 초대) */
	public static final String HISTORY_TYPE_SHARE_INVITE = "SHARE_INVITE";
	
	/** 공유 끊기(공유한 주체가 실행) */
	public static final String HISTORY_TYPE_SHARE_CUT = "SHARE_CUT";

	/** 공유 모두 끊기(공유한 주체가 실행) */
	public static final String HISTORY_TYPE_SHARE_ALLCUT = "SHARE_ALLCUT";

	/** 공유됨(공유 초대 할 때 상대방의 히스토리 종류) */
	public static final String HISTORY_TYPE_SHARE_ON = "SHARE_ON"; // 초대 받은 유저 상태

	/** 공유끊김(공유 끊기 할 때 상대방의 히스토리 종류) */
	public static final String HISTORY_TYPE_SHARE_OFF = "SHARE_OFF"; // 초대 받은 유저가 공유 끊은 상태

	/** 공유 나감(상대방이 공유해준 폴더에서 탈퇴할때 히스토리 종류) */
	public static final String HISTORY_TYPE_SHARE_OUT = "SHARE_OUT";  // 공유해준 유저 입장에서, 상대방 유저가 나갔을 때

	/** 공유탈퇴(내가 공유해준 폴더를 상대방이 탈퇴할 때) */
	public static final String HISTORY_TYPE_SHARE_SECESSION = "SHARE_SECESSION";  // 공유받은 유저입장에서, 해당 공유파일에서 탈퇴

	/** 파일 추가 */
	public static final String HISTORY_TYPE_FILE_CREATE = "FILE_CREATE";
	
	/** 파일 업로드 */ 
	public static final String HISTORY_TYPE_FILE_UPLOAD = "FILE_UPLOAD";
	
	/** 파일 다운로드 */
	public static final String HISTORY_TYPE_FILE_DOWNLOAD = "FILE_DOWNLOAD";

	/** 파일 삭제 */
	public static final String HISTORY_TYPE_FILE_DELETE = "FILE_DELETE";

	/** 보안설정 */
	public static final String HISTORY_TYPE_SECURITY_ON = "SECURITY_ON";

	/** 보안해제 */
	public static final String HISTORY_TYPE_SECURITY_OFF = "SECURITY_OFF";

	/** 수정 */
	public static final String HISTORY_TYPE_MODIFY = "MODIFY";

	/** 이름변경 */
	public static final String HISTORY_TYPE_RENAME = "RENAME";

	/** 메모 작성 */
	public static final String HISTORY_TYPE_MEMO_CREATE = "MEMO_CREATE";
	
	/** 메모 수정 */
	public static final String HISTORY_TYPE_MEMO_MODIFY = "MEMO_MODIFY";

	/** 메모 삭제 */
	public static final String HISTORY_TYPE_MEMO_DELETE = "MEMO_DELETE";
	
	/** 메모 댓글 작성 */
	public static final String HISTORY_TYPE_MEMO_REPLY_CREATE = "MEMO_REPLY_CREATE";
	
	/** 메모 댓글 삭제 */
	public static final String HISTORY_TYPE_MEMO_REPLY_DELETE = "MEMO_REPLY_DELETE";

	/** 휴지통 생성 */
	public static final String HISTORY_TYPE_WASTE_BASKET_FOLDER_CREATE = "WASTE_BASKET_FOLDER_CREATE";

	/** 휴지통에서 완전 삭제 */
	public static final String HISTORY_TYPE_WASTE_BASKET_FOLDER_DELETE = "WASTE_BASKET_FOLDER_DELETE";

	/** 휴지통 비우기 */
	public static final String HISTORY_TYPE_WASTE_BASKET_FOLDER_DELETE_ALL = "WASTE_BASKET_FOLDER_DELETE_ALL";

	/** 휴지통으로 이동 */
	public static final String HISTORY_TYPE_WASTE_BASKET_FOLDER_MOVE = "WASTE_BASKET_FOLDER_MOVE";

	/** 휴지통 파일 복원 */
	public static final String HISTORY_TYPE_WASTE_BASKET_FOLDER_RESTORE = "WASTE_BASKET_FOLDER_RESTORE";
	
	/** 파일 복사 */
	public static final String HISTORY_TYPE_FILE_COPY = "FILE_COPY";
	
	/** 파일 이동 */
	public static final String HISTORY_TYPE_FILE_MOVE = "FILE_MOVE";
	
	/** 링크 생성 */
	public static final String HISTORY_TYPE_LINK_CREATE = "LINK_CREATE";
	
	/** 링크 공유메일 전송 */
	public static final String HISTORY_TYPE_LINK_MAIL = "LINK_MAIL";
	
	/** 가상화 파일 실행 */
	public static final String HISTORY_TYPE_FILE_EXECUTE = "FILE_EXECUTE";
	
	/** 파일 압축 */
	public static final String HISTORY_TYPE_FILE_COMPRESS = "FILE_COMPRESS";
	
	/** 파일 압축 해제 */
	public static final String HISTORY_TYPE_FILE_EXTRACT = "FILE_EXTRACT";
	
	/** 커스텀 브라우저에서 파일 다운로드 */
	public static final String HISTORY_TYPE_CUSTOM_DOWNLOAD = "CUSTOM_DOWNLOAD";
	/** 커스텀 브라우저에서 TYPE에 따른 권한 코드 */
	public static final String HISTORY_TYPE_CUSTOM_DOWNLOAD_DENY = "CUSTOM_DOWNLOAD_DENY";
	public static final String HISTORY_TYPE_CUSTOM_DOWNLOAD_PAGE_DENY = "CUSTOM_DOWNLOAD_PAGE_DENY";
	
	/** 커스텀 브라우저에서 파일 첨부*/
	public static final String HISTORY_TYPE_CUSTOM_ATTACH = "CUSTOM_ATTACH";
	/** 커스텀 브라우저에서 TYPE에 따른 권한 코드 */
	public static final String HISTORY_TYPE_CUSTOM_ATTACH_DENY = "CUSTOM_ATTACH_DENY";
	public static final String HISTORY_TYPE_CUSTOM_ATTACH_PAGE_DENY = "CUSTOM_ATTACH_PAGE_DENY";
	
	/** VDI에서 파일 업로드 */
	public static final String HISTORY_TYPE_VDI_UPLOAD = "VDI_UPLOAD";
	
	/** VDI에서 파일 다운로드 */ 
	public static final String HISTORY_TYPE_VDI_DOWNLOAD = "VDI_DOWNLOAD";
	
	/** 미디어 파일 게재 */
	public static final String HISTORY_TYPE_STREAMMING_PUBLISH = "STREAMMING_PUBLISH";
	
	/** 미디어 파일 게재 취소 */ 
	public static final String HISTORY_TYPE_PUBLISH_CANCLE = "PUBLISH_RELEASE";
	
	public static final String WASTE_BASKET_FOLDER = "WASTE_BASKET_FOLDER";
	
	public static final String HISTORY_SYNC_TYPE = "_SYNC"; // kkh sync를 위해서 추가.
	
	public static final String SYNC_CONNECT_STATE_ON = "SYNC_ON";

	public static final String SYNC_CONNECT_STATE_OFF = "SYNC_OFF";

	public static final String SYNC_CONNECT_STATE_PAUSE = "SYNC_PAUSE";
	
	public static final String SYNC_DIRECTORY_NAME = "싱크폴더";
	
	public static final String STREAMMING_SERVER = "스트리밍서버";
	
	/** 문자열 보안 다운로드 */
	public static final String HISTORY_TYPE_MONITORING_STRING = "MONITORING_STRING";
	
	/** 문자열 보안 다운로드 차단*/
	public static final String HISTORY_TYPE_BLOCK_STRING = "BLOCK_STRING";
	
	public static String ROOT = "스토리지";
	
	public static String PRIVATE_MODULUS = "v8RyqBL/ZgKi1676w0Jpyu5+nU7DhymlqWnpLTq3r2C2ituhbEuP+Bs0Z2DylGM3BOhOBWZtP2pUjGlyvIbiPQ29Sod50hzh1I2lEygG+eRLJDfS/uUNbowjUG7U0V98AyW9EyTQrS1ift5bMf3frcrqviDvvdJziL9HbSGizDRFbb/cNXnkAWThZshanfx88LCFKyJo7jLNnlea0Xf2LwUujt5Mwf9UPCQPwZK2KYau5RSVyzH6+sgPkDnr9nmjJ5DYeIZYo2gp9Zk6A1C53TlcCBe/CRVGsoF7u2yyzJdxrhtKIz/f/Lr7B+4Lg79Bhxg/GgH1GK4CE44aoTdk3w==";
	
	public static String PRIVATE_D = "UU3u9senNQ7lD+EGPIcJ55oaYIOmbsX/kMZwsFZ2zoqImnLoC8w1rBGMtE/sSXGPQCtl+PAmXX6tfiKvHLNrdEtA+DpcMfNA5Cdx159e5dAe86ofcSaX/Ui5KD71SBfdsk/rzBQNzX6yheusapOipEKmKBo6yxZRGadPlzttlHnRFFNLZeHHRNjySa/jqKfrxwYqte50iTZfsQjsCnOEpJNqrXbu8ZIZW/Ag0wrmKvrXWmZ2U1oNpZ7OezQaOIbyvMe+wKbKvAasY4sMRSGRUut2gOnfdZDxBj2Zy3Oo0SZaLhhRoZonVvM/JHJgnS2kxp0Q+xpaSGS3p5hYd5YXoQ==";
	
	public static String PUBLIC_EXPONENT = "AQAB";
	
	/** 암호화된 확장자*/
	public static String ENCRYPT_EXT = "dze";
	
	public static String ENCRYPT_AES = "AES";
	
	public static String ENCRYPT_RSA = "RSA";
	
	/**	80분 */
//	public static int SESSION_TIMEOUT = 4800; // 2400 = 40분
	public static int SESSION_TIMEOUT = 86400; // 24시간
	public static int SESSION_DB = 6; // Cloud Storage Login TokenID session DB
	public static int SESSION_DB_OAUTH = 0; // oauth session DB
	
	public static java.util.List<String> EXT_OFFICE_LIST = Arrays.asList("ppt", "pptx", "xls", "xlsx", "doc", "docx", "hwp", "show", "cell", "pdf", "key", "pages", "numbers", "txt");
	public static java.util.List<String> EXT_AUDIO_LIST = Arrays.asList("mp3", "wav", "wma");
	public static java.util.List<String> EXT_VIDEO_LIST = Arrays.asList("asf", "avi", "flv", "mp4", "mpg", "swf", "wmv", "mov");
	public static java.util.List<String> EXT_IMAGE_LIST = Arrays.asList("jpg", "bmp", "psd", "ai", "png", "gif","tif","jpeg");
	public static java.util.List<String> EXT_WEB_LIST = Arrays.asList("htm", "html");
	public static java.util.List<String> EXT_FONT_LIST = Arrays.asList("otf", "ttf");
//	public static java.util.List<String> EXT_COMPRESSED_LIST = Arrays.asList("7z", "rar", "zip", "zipx", "alz");
	public static java.util.List<String> EXT_COMPRESSED_LIST = Arrays.asList("zip");
	//ETC를 검색할때 이 목록에서 없는것만 출력한다.
	public static java.util.List<String> EXT_ETC_FILE = Arrays.asList("ppt", "pptx", "xls", "xlsx", "doc", "docx", "hwp", "show", "cell", "pdf", "key", "pages", "numbers", "txt", "mp3", "wav", "wma", "asf", "avi", "flv", "mp4", "mpg", "swf", "wmv", "psd", "ai", "htm", "html", "otf", "ttf", "7z", "rar", "zip", "zipx", "alz", "png", "gif", "jpg","tif");
	// 금지된 확장자 리스트
//	public static java.util.List<String> EXT_BAN_LIST = Arrays.asList("dll", "inf", "exe", "bat", "com", "cmd");
//	public static java.util.List<String> EXT_BAN_LIST = Arrays.asList("");
	
	public static HashMap<String , java.util.List<String>> ExtMap ;
	static{
		ExtMap = new HashMap<String , java.util.List<String>>();
		ExtMap.put(MimeType.OFFICE.name(), EXT_OFFICE_LIST);
		ExtMap.put(MimeType.AUDIO.name(), EXT_AUDIO_LIST);
		ExtMap.put(MimeType.VIDEO.name(), EXT_VIDEO_LIST);
		ExtMap.put(MimeType.IMAGE.name(), EXT_IMAGE_LIST);
		ExtMap.put(MimeType.WEB.name(), EXT_WEB_LIST);
		ExtMap.put(MimeType.FONT.name(), EXT_FONT_LIST);
		ExtMap.put(MimeType.COMPRESSED.name(), EXT_COMPRESSED_LIST);
		ExtMap.put(MimeType.ETC.name(), EXT_ETC_FILE);
	}
	
	public static HashMap<String , java.util.List<String>> FileInfoMap ;
	static{
		FileInfoMap = new HashMap<String , java.util.List<String>>();
		FileInfoMap.put(MimeType.OFFICE.name(), EXT_OFFICE_LIST);
		FileInfoMap.put(MimeType.AUDIO.name(), EXT_AUDIO_LIST);
		FileInfoMap.put(MimeType.VIDEO.name(), EXT_VIDEO_LIST);
		FileInfoMap.put(MimeType.IMAGE.name(), EXT_IMAGE_LIST);
		FileInfoMap.put(MimeType.WEB.name(), EXT_WEB_LIST);
		FileInfoMap.put(MimeType.FONT.name(), EXT_FONT_LIST);
		FileInfoMap.put(MimeType.COMPRESSED.name(), EXT_COMPRESSED_LIST);
	}
	
	public static String METHOD_FILEUPLOAD = "fileUpload";
	public static String METHOD_LOGIN = "login";
	public static String METHOD_MOVEWASTEBASKET = "moveWasteBasket";
	
	public static String ENC_KEY = "plokijuhygtfrdes";
	
	public static String FAVORITEFOLDER = "_FAVORITEFOLDER_";

	/** 일반폴더 */
	public static String FOLDERTYPE_NORMAL = "NormalFolder";
	/** 스마트폴더 */
	public static String FOLDERTYPE_SMART = "SmartFolder";
	/** 즐져찾기폴더 */
	public static String FOLDERTYPE_FAVORITE = "FavoriteFolder";
	/** 스트리밍 폴더*/
	public static String FOLDERTYPE_STREAM = "StreamFile";
	/** 버전관리 폴더*/
	public static String FOLDERTYPE_VERSION = "VersionFolder";
	/** 링크 다운로드를 위한 복호화 파일이 저장되는 폴더 */
	public static String FOLDERTYPE_LINKDEC_FOLDER = "tmpLinkDec";
	
	public static String SHARETYPE_SHARE = "share";
	
	public static String SHARETYPE_SHARED = "shared";
	
	public static String HOST_ADDR = "mail.smartbizos.com";
	
	public static String TMPZIP_FOLDER = "tmpZip";
	
	public static String ENC_DOWN_FOLDER = "encDown";
	
	public static String POLICY_REDIS_KEY = "policyRedisStore";
	
	public static String SERVICE_NAME_STORAGE = "Storage";
//	public static String SERVICE_NAME_STORAGE = "가상화"; // postgres fixed name
	
	public static String SERVICE_NAME_FAX = "FAX";
	
	public static String BIZ_SHARE_FOLDER = "BIZ_SHARE";
	
	public static String GUEST_BIZ_ID = "GUEST";
	
	public static String SHAREFOLDER_INVITE_SUBJECT = "[WEHAGO] ";
	public static String LOCKFOLDER_GETPASSWORD_SUBJECT = "[WEHAGO] ";
	public static String LINK_INVITE_SUBJECT = "[WEHAGO] ";
	public static String gUEST_GETPASSWORD_SUBJECT = "[WEHAGO]게스트 임시비밀번호 발송";
	public static String SHAREFILE_SENDMAIL_SUBJECT = "[WEHAGO] ";
//	public static String AUTH_CODE_SUBJECT = "스토리지 문서 보안키 발급용 인증코드입니다.";
	public static String SUBJECT = "[WEHAGO] ";

//	public static final String META_FILE_NAME = "._access_";
//	public static final String WASTE_SPACE_FILE_NAME = "._wasteUsed_";
//	public static final String SPACE_FILE_NAME = "._used_";
//	public static final String META_FAX_SPACE = "._faxQuota_";
//	public static final String META_FAX_FILE_MAP = "._faxFileMap_";
//	public static final String META_DELETE_FAX_FILE = "._delFaxFile_";
//	public static final String SHARE_META_NAME = "._share_";
//	public static final String WASTE_META_NAME = "._waste_";
//	public static final String SMARTFOLDER_META_NAME = "._smartFolder_";
//	public static final String LOCK_META_NAME = "._lock_";
//	public static final String FAVORITE_META_NAME = "._favorite_";
//	public static final String SHARE_SMART_META_NAME = "._shareSmart_";
//	public static final String BIZSHARE_META_NAME = "._bizShare_";
//	public static final String GROUP_QUOTA_META_NAME = "._groupQuota_";
//	public static final String SHARE_LOCK_META_NAME="._shareLock_";
//	public static final String SYNC_META_FILE_NAME = "._sync_"; // kkh sync를 위해서 추가.
//	public static final String UUID_META_NAME = "._uuid_";
	
	public static final String META_FILE_NAME = "._dcpe__access_";
	public static final String WASTE_SPACE_FILE_NAME = "._dcpe__wasteUsed_";
	public static final String SPACE_FILE_NAME = "._dcpe__used_";
	public static final String META_FAX_SPACE = "._dcpe__faxQuota_";
	public static final String META_FAX_FILE_MAP = "._dcpe__faxFileMap_";
	public static final String META_DELETE_FAX_FILE = "._dcpe__delFaxFile_";
	public static final String SHARE_META_NAME = "._dcpe__share_";
	public static final String WASTE_META_NAME = "._dcpe__waste_";
	public static final String SMARTFOLDER_META_NAME = "._dcpe__smartFolder_";
	public static final String LOCK_META_NAME = "._dcpe__lock_";
	public static final String FAVORITE_META_NAME = "._dcpe__favorite_";
	public static final String SHARE_SMART_META_NAME = "._dcpe__shareSmart_";
	public static final String BIZSHARE_META_NAME = "._dcpe__bizShare_";
	public static final String GROUP_QUOTA_META_NAME = "._groupQuota_";
	public static final String SHARE_LOCK_META_NAME="._dcpe__shareLock_";
	public static final String SYNC_META_FILE_NAME = "._dcpe__sync_"; // kkh sync를 위해서 추가.
	public static final String UUID_META_NAME = "._uuid_";
	
	public static String FAX_STORAGE_FOLDER = "FAX_STORAGE";
	public static String SEND_FAX = "SEND";
	public static String RECEIVE_FAX = "RECEIVE";
	public static String RESERVE_FAX = "RESERVE";
	public static String DELETE_FAX = "DELETE";
	public static String ETC_FAX = "ETC";
	
	public static String ACCESS_TYPE_READ = "R";
	public static String ACCESS_TYPE_WRITE = "W";
	public static String ACCESS_TYPE_UPLOAD = "U";
	public static String ACCESS_TYPE_DOWNLOAD = "D";
	public static String ACCESS_TYPE_IGNORE = "I";
	
	public static String FILE_PATH = "filePath";
	public static String ACCOUNT = "account";
	
	public static long LIMITED_UPLOAD_SIZE = 104857600L;
	
	public static Version luceneVersion = Version.LUCENE_45;
	
	public static java.util.List<String> EXT_INDEX_LIST = Arrays.asList("txt", "html", "htm", "xml");
	public static java.util.List<String> DOC_INDEX_LIST = Arrays.asList("doc", "xls", "ppt");
	public static java.util.List<String> DOCX_INDEX_LIST = Arrays.asList("docx", "xlsx", "pptx");
	public static String HWP_INDEX = "hwp";
	public static String PDF_INDEX = "pdf";
	public static String redisUserListKey = "indexUserList";
	public static List<String> userList = null;
	// index queue가 시작될 최초 Num
	public static int startNum;
	// 스토리지 서버수.
	public static int instanceNum;
	// index instance에서 시작할 start 번호. 
	public static String[] indexStartNum = {"0","1"};
	
	public static String REDIS_THUMB_LIST = "redisThumbList";
	public static String THUMB_REMOVE_LIST = "thumbRemoveList";
	public static List<String> thumbList = null;
	public static List<String> removeThumbList = null;
	
//	public static String ADMIN_EMAIL_ACCOUNT = "dcloudweb@duzon.com";
//	public static String ADMIN_EMAIL_ACCOUNT = "dcloudadmin@duzon.com";
	public static String ADMIN_EMAIL_ACCOUNT = "dcloudadmin@douzone.com";
	
//	public static HashMap<String, Queue> indexServiceMap = null;
//	public static HashMap<Integer, Queue> indexFullServiceMap = new HashMap<Integer, Queue>(); // 최초 스토리지 전체 index시 사용.
	
	public static int THUMNAIL_WIDTH = 340;
	
	public static java.util.List<String> EXT_THUMB_LIST = Arrays.asList("jpg", "bmp","png", "gif","jpeg");
	
	public static int META_FILE_SIZE = 1024 * 1024;
	
	public static ConcurrentHashMap<String, Object> metaLockMap = new ConcurrentHashMap<String, Object>();
	
	public static String JMX_OBJECT_NAME = "connector:name=metaManager";
	
	public static java.util.List<String> EXT_STREAM_LIST = Arrays.asList("flv", "f4v", "mp4", "3gp", "mp3", "f4a", "m4a", "aac");
	
	public static int streamServerUrlIdx = 0;
	
	public static int streamThumbIdx = 0;
	
	public static String SUFFIX_VDI_AD = "_vdi";
	
	/** 정책 유형 (전체 정책) */
	public static String POLICY_TYPE_ALL = "all";
	
	/** 정책 유형 (기본 정책) */
	public static String POLICY_TYPE_DEFAULT = "default";
	
	/** 정책 유형 (문자열 정책) */
	public static String POLICY_TYPE_STRING = "string";
	
	/** 사용자 windows Link 저장위치. */
	public static String SYMBOLIC_LINK_DEFAULT_NAME = "[공유폴더]";
	
	/** Symbolic Link 를 한곳에 모아놓은 저장위치. */
//	public static String SYMLINKS_FOLDER = "SYMLINKS_FOLDER";
	
	/** Symbolic Link 저장 SHELL. */
//	public static String SYMBOLIC_LINK_MAKE_SHELL = "makeSymbolicLink.sh";
//	
//	/** Symbolic Link 삭제 SHELL. */
//	public static String SYMBOLIC_LINK_REMOVE_SHELL = "removeSymbolic.sh";
//	
//	/** Symbolic Link 수정 SHELL. */
//	public static String SYMBOLIC_LINK_RENAME_SHELL = "reNameSymbolicLink.sh";
	
	/** mount 생성 SHELL. */
//	public static String MOUNT_SHELL = "mount.sh";
//	
//	/** mount 삭제 SHELL. */
//	public static String UMOUNT_SHELL = "umount.sh";
	
	/** Master Key Path*/
	public static String MASTER_PATH = "./storage";
	
	/** 사용자키 발급 시 인증코드 저장하는 컬렉션*/
	public static String AUTH_CODE_COLLECTION = "AuthCode";
	
	public static final String RANDOM_KEY = "er48nsjhwlG593mjhgdb20ih";  //횬쟈 192bit
	
	public static final String STORAGE_KEY = "b7bfb8096f7a40499f4020e569d87a67";  //횬쟈 192bit
	
	/** 회사에 소속되지 않은 사용자의 기본 path */
	public static String NO_BIZ = "NO_BIZ";
	
	/** 만약 Bucket 을 계속 생성하지않는다면 기본 Bucket Name */
	public static String BUCKET_NAME = "Bucket-001";
}