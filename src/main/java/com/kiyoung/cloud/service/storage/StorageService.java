package com.kiyoung.cloud.service.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.kiyoung.cloud.common.MongoDBMetaDaoImpl;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.common.exception.CSBException;
import com.kiyoung.cloud.common.service.CommonStaticService;
import com.kiyoung.cloud.service.index.lucene.BoldFormatter;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.dto.DetailSearch;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.FileInfo;
import com.kiyoung.cloud.storage.dto.UserAndBusinessConnectIdDto;
import com.kiyoung.cloud.storage.service.SessionService;
import com.kiyoung.cloud.storage.service.StorageObjectCommonService;
import com.kiyoung.cloud.storage.type.AccessTypeNew;
import com.kiyoung.cloud.storage.type.MimeType;

@Service("StorageSvc")
public class StorageService {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${rootMetaPath}")
	private String rootMetaPath;

	@Value("${ROOT_PATH}")
	private String storageRootPath;

	@Autowired
	public PostgreSQLDaoImpl postgreSQLDaoImpl;

	@Autowired
	public SessionService sessionService;

	@Autowired
	public StorageObjectCommonService commonService;

	@Autowired
	public MongoDBMetaDaoImpl mongoDaoImpl;
	
	
	@Transactional
	public DCloudResultSet indexSearch(HashMap<String, Object> inMap) throws Exception 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		
		
		
		
		return resultSet;
	}
	
	/**
	 * [본문검색] 사용자의 디렉토리와 공유디렉토리의 index 폴더조회.
	 */
	@Transactional
	public DCloudResultSet indexSearchContents(HashMap<String, Object> inMap) throws Exception 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
		String tokenId = tokenIDArr[0],oauthTokenId = tokenIDArr[1];
		String searchText = (inMap.get("SearchText").toString()).toLowerCase();
		List<FileInfo> fileResultList = new ArrayList<FileInfo>();
		if(searchText.contains("*")){resultSet.setList(fileResultList);return resultSet;}
		BooleanQuery booleanLockQuery = new BooleanQuery();
		Session session = sessionService.getSession(tokenId);
		
		String indexRootPath = commonService.getIndexFilePath(session.getEmail().toString());
		log.debug("indexRootPath > "+indexRootPath);
		String searchField = "contents";
		
		fileResultList = getIndexSearchContentDeleteLock(indexRootPath, searchField, "", searchText, fileResultList, session, null, booleanLockQuery);
		resultSet.setList(fileResultList);
		return resultSet;
	}
	
	//searchField: contents // searchText: 내가 찾는 문자열 
	public List<FileInfo> getIndexSearchContentDeleteLock(String indexRootPath, String searchField, String sharePath, Object searchText, List<FileInfo> fileResultList, Session session, AccessTypeNew accessTypeNew, BooleanQuery booleanLockQuery) throws Exception {
		
		File indexRoot = new File(indexRootPath); if(!indexRoot.exists()){log.error("storage getIndexSearchContent2 not found indexPath : "+indexRootPath);return fileResultList;}
		
		IndexReader reader = getReader(indexRootPath);
		log.debug("reader >"+reader.toString());
		
		if(reader==null) return fileResultList;
		
		int hitPerPage = reader.maxDoc() ;
		log.debug("hitPerPage >>"+hitPerPage);
		if(hitPerPage==0){return fileResultList;}
		
		IndexSearcher searcher = new IndexSearcher(reader);
		StandardAnalyzer analyzer = new StandardAnalyzer(StorageConstant.luceneVersion);
		QueryParser parser = new QueryParser(StorageConstant.luceneVersion, searchField, analyzer);
		
		parser.setAllowLeadingWildcard(true);
		
		BooleanQuery  booleanShareQuery = new BooleanQuery();
		BooleanQuery  booleanQuery1 = new BooleanQuery();
		
		if(searchText.getClass().equals(String.class))
		{
			booleanQuery1 = getQuery(booleanQuery1, searchField, (String)searchText);
			log.debug("storage getIndexSearchContent2 searchText : "+booleanQuery1);
			booleanShareQuery.add(booleanQuery1, BooleanClause.Occur.MUST);
		}else
		{
			booleanQuery1 =  getDetailQuery(booleanQuery1, (DetailSearch)searchText, searchField);
			log.debug("storage getIndexSearchContent2 DetailSearch : "+booleanQuery1);
			booleanShareQuery.add(booleanQuery1, BooleanClause.Occur.MUST);
		}
		

		BoldFormatter formatter = new BoldFormatter();
		BooleanQuery.setMaxClauseCount(30000); // 내용의 출력buffer?
		
		Highlighter highlighter = null;
		TopDocs results = null; // 1+reader.maxDoc() 해당 로직은 차후에 수정필요. paging 기능개발시..
		
		
		highlighter = new Highlighter(formatter, new QueryScorer(booleanQuery1));
		log.debug("storage getIndexSearchContent2 booleanQuery1 : "+booleanQuery1.toString());
		booleanShareQuery.add(booleanLockQuery, BooleanClause.Occur.MUST_NOT);
		log.debug("storage getIndexSearchContent2 booleanShareQuery : "+booleanShareQuery);		
		results = searcher.search(booleanQuery1, hitPerPage); 
		
		if(results.totalHits==0){return fileResultList;}
		else {log.debug("results.totalHits:"+results.totalHits);}
		
		String fileInfoString = null;
		FileInfo fileInfo = null;
		String content = null;
		String[] frag = null;
		Document doc = null;
		int id = 0;
		ScoreDoc[] hits = results.scoreDocs;
		String filePath = "";
		for (int i = 0; i < results.totalHits; i++) 
		{
			String searchContent = "";
			id = hits[i].doc;
			doc = searcher.doc(id);
			content = doc.get("contents");
			fileInfoString = doc.get("fileInfo");
			if(content !=null){
				frag = highlighter.getBestFragments(analyzer, "contents", content, 2); // 검색단어의 2 Line을 출력.
				for(int contentNum =0; contentNum<frag.length; contentNum++){
					searchContent += frag[contentNum];
				}
			}
			if (fileInfoString != null) 
			{
				fileInfo = new Gson().fromJson(fileInfoString, FileInfo.class);
				fileInfo.setContents(searchContent);
				filePath = doc.get("path");
				log.debug("storage getIndexSearchContent2 filePath : "+filePath);
				

				if(accessTypeNew != null){
					fileInfo.setAccessType(accessTypeNew);
				}

				fileResultList.add(fileResultList.size(), fileInfo);
			}
		}
		reader.close();
		analyzer.close();
		
		return fileResultList;
	}
	
	
	@Transactional
	public BooleanQuery  getQuery(BooleanQuery  booleanQuery, String searchField, String searchText) throws Exception {

		WildcardQuery query = null;
		String ORSearch = ".*\\|.*";
		String ANDSearch = ".*&.*";
		String NotSearch = ".*!.*";
		String searchWord = "";

		if(searchText.matches(ORSearch)){
			for(int i = 0; i<searchText.split("\\|").length; i++){
				searchWord="*"+searchText.split("\\|")[i].trim()+"*";
				query = new WildcardQuery(new Term(searchField, searchWord));
				booleanQuery.add(query, BooleanClause.Occur.SHOULD);
			}
		}else if(searchText.matches(ANDSearch)){
			for(int i = 0; i<searchText.split("&").length; i++){
				searchWord="*"+searchText.split("&")[i].trim()+"*";
				query = new WildcardQuery(new Term(searchField, searchWord));
				booleanQuery.add(query, BooleanClause.Occur.MUST);
			}
		}else if(searchText.matches(NotSearch)){
			for(int i = 0; i<searchText.split("!").length; i++){
				if(i==0){
					searchWord="*"+searchText.split("!")[i].trim()+"*";
					query = new WildcardQuery(new Term(searchField, searchWord));
					booleanQuery.add(query, BooleanClause.Occur.SHOULD);
				}else{
					searchWord="*"+searchText.split("!")[i].trim()+"*";
					query = new WildcardQuery(new Term(searchField, searchWord));
					booleanQuery.add(query, BooleanClause.Occur.MUST_NOT);						
				}
			}
		}else{
			searchText="*"+searchText+"*";
			query = new WildcardQuery(new Term(searchField, searchText));
			booleanQuery.add(query, BooleanClause.Occur.MUST);
		}
		return booleanQuery;
	}
	
	
	@Transactional
	public BooleanQuery  getDetailQuery(BooleanQuery  booleanQuery, DetailSearch detailSearch, String searchField) throws Exception {

		WildcardQuery query = null;
		BooleanQuery  booleanShareQuery = new BooleanQuery();

		//검색어가 있다면 추가한다.
		if(!detailSearch.getSearchText().isEmpty()){
			booleanQuery = getQuery(booleanQuery, searchField, detailSearch.getSearchText());
			booleanShareQuery.add(booleanQuery, BooleanClause.Occur.MUST);
		}
		// 파일 종류가 있거나 종류가 ALL(전체)이 아니면 비교한다.
		if(!detailSearch.getMimeType().isEmpty() && !detailSearch.getMimeType().equals(MimeType.ALL.name())) {
			// 추후 개발
		}
		// 날짜가 있으면 비교한다.
		if(!detailSearch.getEndDate().isEmpty() || !detailSearch.getStartDate().isEmpty()) {
			// 추후 개발
		}
		//용량이 있으면 비교한다.
		if(!detailSearch.getEndSize().isEmpty() || !detailSearch.getStartSize().isEmpty()) {
			// 추후 개발
		}else{
			// 추후 개발
		}
		return booleanShareQuery;
	}
	
	
	
	

	private IndexReader getReader(String indexRootPath) {
		
		IndexReader reader = null;
		File indexRoot = new File(indexRootPath);
		log.debug("indxRoot >>"+indexRoot.getAbsolutePath());
		try 
		{
			reader = DirectoryReader.open(FSDirectory.open(indexRoot));
		}
		catch(Exception e)
		{
			//예외처리 추후 개발 
			log.error("error:",e);
		}	
		return reader;
	}

	/**
	 * 
	 * 검색어, 파일종류, 크기, 기간등으로 전체 폴더에서 상세 검색을 한다. <br>
	 * 입력되지 않은 값은 null을 입력한다.
	 * @param TokenId CSB 로그인시 반환되는 TokenID
	 * @param SearchText 검색어
	 * @param mimeType 파일형식
	 * @param startDate 시작날짜(yyyyMMddHHmmss)
	 * @param endDate 종료날짜(yyyyMMddHHmmss)
	 * @param startSize 시작용량(kb)
	 * @param endSize 종료용량(kb)
	 * @return json type의 데이터
	 * * <pre>
	 * {"serverMsg":"success","resultCode":"0000","resultList":[]}
	 * </pre>
	 * @throws Exception
	 */
	@Transactional
	public DCloudResultSet searchDetail(HashMap<String, Object> inMap) throws Exception 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
//		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
//		String tokenId = tokenIDArr[0], oauthTokenId = tokenIDArr[1];
//		
//		DetailSearch detailSearch = new DetailSearch();
//		detailSearch = setDetailSearchVO(detailSearch,inMap);
//		log.debug("detailSearchVO : "+detailSearch.toString());
//		
//		Session session = sessionService.getSession(tokenId);
//		String objectPath = storageRootPath+File.separator+session.getBusinessConnectID()+File.separator+session.getUserConnectID();
//		File file = new File(objectPath);
//		
//		List<FileInfo> resultList = new ArrayList<FileInfo>();
//		List<File> subDirectoryList = new ArrayList<File>();
//		subDirectoryList.add(file);
//		for (int i = 0; i < subDirectoryList.size(); i++) {
//			File directory = subDirectoryList.get(i);
//
//			// 검색어가 들어가있는 fileInfo를 추가한다.
//			List<File> tempList = CommonStaticService.addDetailSearchFileInfo(detailSearch, resultList, session.getEmail(), session, directory, true, commonService);
//			if (tempList != null && tempList.size() > 0) {
//				subDirectoryList.addAll(tempList);
//			}
//		}
//		log.debug("list result >>>"+resultList.toString());
//		resultSet.setList(resultList);
		return resultSet;
	}
	
	
	private DetailSearch setDetailSearchVO(DetailSearch detailSearch,HashMap<String, Object> inMap) {
		detailSearch.setSearchText(inMap.get("SearchText").toString());
		detailSearch.setMimeType(inMap.get("MimeType").toString());
		detailSearch.setStartDate(inMap.get("StartDate").toString());
		detailSearch.setEndDate(inMap.get("EndDate").toString());
		detailSearch.setStartSize(inMap.get("StartSize").toString());
		detailSearch.setEndSize(inMap.get("EndSize").toString());
		return detailSearch;
	}


	/**
	 * OCT 28 2019 / Original Version prototype  // 솔직히 별로 좋은 코드 같지가 않음. 가독성 최악
	 * @param inMap
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public DCloudResultSet search(HashMap<String, Object> inMap) throws Exception 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		
		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
		String tokenId = tokenIDArr[0],oauthTokenId = tokenIDArr[1];
		String searchText = inMap.get("SearchText").toString();
		Session session = sessionService.getSession(tokenId);
		
		String objectPath = storageRootPath+File.separator+session.getBusinessConnectID()+File.separator+session.getUserConnectID();
		
		File file = new File(objectPath);
		
		List<FileInfo> resultList = new ArrayList<FileInfo>();
		List<File> subDirectoryList = new ArrayList<File>();
		subDirectoryList.add(file);//		log.debug("subDirectoryList first time situation >>> "+subDirectoryList.toString());
		
		for (int i = 0; i < subDirectoryList.size(); i++) {//SUBDIR -> 안의 내용물 순회하면서 내용물이 dir이면 subdirList에 추가해줌
			File directory = subDirectoryList.get(i);
			log.debug("directory ("+i+"):"+directory.toString());
			List<File> tempList = CommonStaticService.addSearchFileInfo(searchText, resultList, session.getEmail().toString(), session, directory, true, commonService);
			
			if (tempList != null && tempList.size() > 0) {subDirectoryList.addAll(tempList);}//았으면 서브디렉토리리스트 증가
		}
		
		log.debug("list result >>>"+resultList.toString());
		resultSet.setList(resultList);
		
		return resultSet;
	}
	
	/**
	 * Dummy Method
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public DCloudResultSet dummy(HashMap<String, Object> inMap) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		String email = (String) inMap.get("email");
		String result = postgreSQLDaoImpl.getAccountByEmpNo(email);
		Map<String, String> map = new HashMap<String, String>();
//		map.put("systemPath", new File(mountPath + filePath).getPath().replace("\\", "/"));
//		map.put("read", String.valueOf(read));
		map.put("user_no", result);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(map);
		resultSet.setList(list);
		log.debug("list>" + list.toString());

		FileAccessMeta m = new FileAccessMeta();
		String dummyParent = UUID.randomUUID().toString();
		String dummyFileUniqueKey = UUID.randomUUID().toString();
		m.set_id(dummyFileUniqueKey);
		m.setParentFileUniqueKey(dummyParent);
		m.setFileUniqueKey(dummyFileUniqueKey);
		mongoDaoImpl.insertAccessMeta(m);
//		mongoDa
		Thread.sleep(1000);
		mongoDaoImpl.getAccessMetaList(m);

		return resultSet;
	}

	/**
	 * 폴더 생성
	 * 
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public DCloudResultSet create(Exchange exchange) throws Exception {

		DCloudResultSet resultSet = new DCloudResultSet();
		HashMap inMap = exchange.getIn().getMandatoryBody(HashMap.class);

		String directoryPath = inMap.get("DirectoryPath").toString();
		log.debug("create exchange > " + exchange.toString());

		inMap.put("FilePath", directoryPath);
		exchange.getIn().setBody(inMap);

		Map<String, Object> map = exchange.getProperties();
		log.debug("> map:" + map.toString());

		String service = (String) exchange.getIn().getHeader("service");
		String method = (String) exchange.getIn().getHeader("method");
		log.debug("service>" + service + " method>" + method);

		// 일반 케이스
		if (service.equals("StorageSvc") && method.equals("create")) {
			return createFolder(exchange);
		}
		// 예외 케이스 // 로그인 api 호출했는데 해당 경로가 없는 경우
		else {

		}

		return resultSet;
	}

	private void createUserRootFolder(String rootSystemPath) throws CSBException {
		// [1] session 뽑고 (생략)
		// 루트시스템패스로 파일 만들고
//			String rootSystemPath = commonService.getFileSystemPath(account, session);
		log.debug("createUserRootFolder >");
		String path = rootSystemPath;
		File file = new File(path);
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e2) {
			log.error("createFail", e2);
			throw new CSBException("createFail: " + rootSystemPath);
		}
		try {
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		} catch (Exception e) {
			log.error("폴더 전체 권한주기 에러", e);
		}
	}

	/**
	 * 폴더생성 하위 메소드
	 * 
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	private DCloudResultSet createFolder(Exchange exchange) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		HashMap<String, Object> inMap = exchange.getIn().getMandatoryBody(HashMap.class);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String directoryPath = inMap.get("FilePath").toString();
		Session session = new Session();
		String account = directoryPath.substring(0, directoryPath.indexOf("/"));
		String remain = directoryPath.substring(directoryPath.indexOf("/") + 1);
		UserAndBusinessConnectIdDto userDto = commonService.getBizAndUserConnId(account);
		String fullPath = storageRootPath + File.separator + userDto.getBusinessConnectId() + File.separator
				+ userDto.getUserConnectId() + File.separator + remain;

		File folder = new File(fullPath);
		String folderName = folder.getName();
		String name = folder.getName();
		File[] listFiles = folder.getParentFile().listFiles();// log.debug(">>>"+Arrays.asList(listFiles).toString());
		boolean exists = commonService.checkFileExist(folder, listFiles);

		if (exists) {
			int i = 1;
			folderName = name + "(" + i + ")";
			while (folder.exists()) {
				folderName = name + "(" + i + ")";
				folder = new File(folder.getParent() + File.separator + folderName);
				i += 1;
			}
		}
		log.debug("folder.getName():" + folder.getName() + " folder.getParent():" + folder.getParent());

		paramMap.put("userConnectID", userDto.getUserConnectId());
		paramMap.put("folderPath", folder.getAbsolutePath());
		paramMap.put("account", account);

		createMongoMetaOfDirectory(paramMap);
		createFileMetaOfDirectory(paramMap);
		createFolder(folder, session);
		return resultSet;
	}

	private void createMongoMetaOfDirectory(HashMap<String, Object> paramMap) throws Exception {
		FileAccessMeta meta = new FileAccessMeta();
		String uuid = UUID.randomUUID().toString();
		String folderPath = (String) paramMap.get("folderPath");
		String account = (String) paramMap.get("account");
		File file = new File(folderPath);

		meta.set_id(uuid);
		meta.setSystemPath(folderPath);
		meta.setFileName(file.getName());
		meta.setUploadUserId(account);
		meta.setLastModifyUser(account);
		meta.setFileUniqueKey(uuid);

		FileAccessMeta pm = commonService.loadFileMetaByFileSystemPathMongoDB(new File(file.getParent()));
		log.debug("pm>" + pm.toString());
		meta.setParentFileUniqueKey(pm.getFileUniqueKey());
		commonService.insertAccessMeta(meta);
		log.debug(">" + new File(file.getParent()).getAbsolutePath());

		paramMap.put("uuid", uuid);
	}

	private void createFileMetaOfDirectory(HashMap<String, Object> paramMap) throws IOException {

		String fullPath = (String) paramMap.get("folderPath");
		String uuid = (String) paramMap.get("uuid");
		String metaPath = fullPath.replace(storageRootPath, rootMetaPath) + File.separator
				+ StorageConstant.UUID_META_NAME;
		File file = new File(metaPath);
		if (file.getParentFile().exists() == false) {
			FileUtils.forceMkdir(file.getParentFile());
		}
		FileUtils.writeStringToFile(file, uuid);
	}

	private void createFolder(File file, Session session) throws CSBException {

		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e2) {
			log.error("createFail", e2);
		}
		try {
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		} catch (Exception e) {
			log.error("폴더 전체 권한주기 에러", e);
		}
	}

	/**
	 * 
	 * @param inMap ( storageToken@@portalToken) FilePath
	 * @return
	 * @throws Exception
	 */
	public DCloudResultSet getSystemPathByFilePath(HashMap<String, Object> inMap) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		String filePath = inMap.get("FilePath").toString();
		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
		String tokenId = tokenIDArr[0];
		String oauthTokenId = tokenIDArr[1];
		Session session = sessionService.getSession(tokenId);

		// filePath에서 파싱한 account <-> session에서 뽑은 것 비교
		String account = filePath.substring(0, filePath.indexOf("/"));
		String remain = filePath.substring(filePath.indexOf("/"));
		log.debug("account > " + account);
		UserAndBusinessConnectIdDto userDto = commonService.getBizAndUserConnId(account);
		log.debug(">> " + userDto.toString());

		String fullPath = storageRootPath + File.separator + userDto.getBusinessConnectId() + File.separator
				+ userDto.getUserConnectId() + remain;

		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		boolean read = true;
		map.put("systemPath", fullPath);
		map.put("read", String.valueOf(read));
		list.add(map);
		resultSet.setList(list);
		return resultSet;
	}

	/**
	 * 
	 * @param inMap
	 * @return
	 * @throws Exception
	 */
	public DCloudResultSet getFileList(HashMap<String, Object> inMap) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		List<FileInfo> list = new ArrayList<FileInfo>();
		String filePath = inMap.get("DirectoryPath").toString();//
		String account = filePath.substring(0, filePath.indexOf("/"));
		String remain = filePath.substring(filePath.indexOf("/"));
		UserAndBusinessConnectIdDto userDto = commonService.getBizAndUserConnId(account);
		String fullPath = storageRootPath + File.separator + userDto.getBusinessConnectId() + File.separator
				+ userDto.getUserConnectId() + remain;

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", fullPath);
		CommonStaticService.addFileInfoForSharedAnalyze(list, paramMap, commonService);
		resultSet.setList(list);
		return resultSet;
	}

	
	
	public void subDirList(String source){

		File dir = new File(source); 
		File[] fileList = dir.listFiles(); 
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){// 파일이 있다면 파일 이름 출력
					System.out.println("\t 파일 이름 = " + file.getName());
				}else if(file.isDirectory()){
					System.out.println("디렉토리 이름 = " + file.getName());
					// 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
					subDirList(file.getCanonicalPath().toString()); 
				}
			}
		}catch(IOException e){
			log.error("error:",e);
		}

	}
	
	public static void main(String[] args) {}
}
