package com.kiyoung.cloud.service.index.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.storage.dto.FileInfo;
import com.kiyoung.cloud.storage.service.StorageObjectCommonService;
import com.kiyoung.cloud.storage.util.DateUtils;

@Service("IndexService")
public class IndexService {
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	//
	@Value("${ROOT_PATH}")
	private String storageRootPath;

	@Value("${thumb_img_url}")
	private String thumb_img_url;

	@Value("${THUMB_USE_MODE}")
	private boolean thumbUseMode;
	
	@Value("${INDEX_USE_MODE}")
	private boolean indexUseMode;
	
	@Autowired
	private StorageObjectCommonService commonService;
	
	@Autowired
	private IndexFilesETCDocs IndexFilesETCDocs;
	
	
	public StandardAnalyzer analyzer = new StandardAnalyzer(StorageConstant.luceneVersion);
	
	public HashMap<String, Object> getIndexFileMap(String indexRootPath, File indexFile, FileInfo fileInfo){
		HashMap<String, Object> indexMap = new HashMap<String, Object>();
		String fileInfoJson = new Gson().toJson(fileInfo);
		indexMap.put("indexRootPath", indexRootPath);
		indexMap.put("file", indexFile.getAbsolutePath());
		indexMap.put("fileInfo", fileInfoJson);
		indexMap.put("method", "indexFilesDocs");
		return indexMap;
	}
	
	public void getQueue(String receiveMessage) throws Exception 
	{
		
		String method = "";
		// rabbitMQ start
		if(log.isDebugEnabled()) log.debug("receiveMessage : "+receiveMessage);
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		JsonObject jo = (JsonObject)parser.parse(receiveMessage);
		HashMap<String, Object> indexMap = gson.fromJson(jo, HashMap.class);
		method = indexMap.get("method").toString();
		if(method.equals("indexFilesDocs"))
		{
			indexFilesDocs(indexMap);	
		}
	}
	//indexRootPath:/duzon/index/e8da03df-cf50-49e8-91a5-5cbbc825e383/dff58a8a-2bcd-4dde-8058-b88eba3f20f2/
	private void indexFilesDocs(HashMap<String, Object> body) throws IOException 
	{
		if(log.isDebugEnabled()) log.debug("indexFilesDocs : "+body);
		String indexRootPath = body.get("indexRootPath").toString();
		File indexFile = new File(indexRootPath);
		if (!indexFile.exists())indexRootDirectoryDocs(indexRootPath); // kkh index 스토리지에 본인폴더가 다 완성된후 해당 폴더의 index폴더 생성.
		
		File file = new File(body.get("file").toString());
		if(!file.exists()) return;
		String fileInfo = body.get("fileInfo").toString();
		String extension = "";
		FileInputStream fis = null;
		BufferedReader br = null;
		String fileName = null;
		Document doc = new Document();
		IndexWriter writer = null;
		
		try 
		{
			fileName = file.getName();
			writer = getIndexWriter(indexRootPath);
			extension = FilenameUtils.getExtension(fileName);
			extension = extension.toLowerCase();
			fileDocumentWriter(file, extension, indexRootPath, fileInfo, writer, doc, fis, br);
		}
		catch(Exception e) 
		{
			log.error("error:",e);
			exception1(file, doc, writer);
		}
		finally 
		{
			if(fis!=null)fis.close();
			if(br!=null)br.close();
			if(writer!=null)writer.close();
		}
	}

	private void exception1(File file, Document doc, IndexWriter writer) {
		try 
		{
			writer.updateDocument(new Term("path", file.getPath()), doc); // index 중 에러처리는 path 정도만 저장되어진다.
			writer.close();
		}
		catch(Exception e1) 
		{
			log.error("error:",e1);
		}
	}
	
	public void indexRootDirectoryDocs(String indexRootPath) throws IOException 
	{
		IndexWriter writer = null;
		try 
		{
			writer = getIndexWriterCreate(indexRootPath);
			writer.close();
		}
		catch(IOException e) 
		{
			log.error("error:",e);
		}
		finally 
		{
			if(writer!=null) {writer.close();}
		}
	}

	
	private IndexWriter getIndexWriterCreate(String indexRootPath) throws IOException {
		
		IndexWriterConfig iwc = new IndexWriterConfig(StorageConstant.luceneVersion, analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = null;
		Directory dir = null;
		dir = FSDirectory.open(new File(indexRootPath));
		writer = new IndexWriter(dir, iwc);
		return writer;
	}
	
	
	public IndexWriter getIndexWriter (String indexRootPath) throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig(StorageConstant.luceneVersion, analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = null;
		Directory dir = null;
		dir = FSDirectory.open(new File(indexRootPath));
		if(log.isDebugEnabled()) log.debug("getIndexWriter dir : "+dir);
		writer = new IndexWriter(dir, iwc);
		if(log.isDebugEnabled()) log.debug("getIndexWriter writer : "+writer);
		return writer;
	}
	
	
	
	public void fileDocumentWriter(final File file, String extension, String indexRootPath, String fileInfo, final IndexWriter writer, final Document doc, final FileInputStream fis, final BufferedReader br) throws FileNotFoundException, IOException, XmlException, OpenXML4JException{
		final OPCPackage op = null;
		try{
			if(!file.exists()) {log.error("fileDocumentWriter File Not Found  : "+file);return;}
			
			final String extensionName = extension.toLowerCase();
			doc.add(new StringField("path", file.getPath(),	Field.Store.YES)); if(log.isDebugEnabled()) log.debug("IndexSeravice fileDocumentWriter file.getName : "+(file.getName()).toLowerCase());
			setDocument(file, fileInfo, doc, extensionName);

			if((file.length()/1048576)<10) // 본문내용 parsing start, 10MB 미만 파싱
			{
				ExecutorService exec = Executors.newCachedThreadPool();// 비동기 thread 실행 start
				exec.execute(new Runnable() {
					public void run() {
						try {
							log.debug("thread working ~~~~");
							excuteIndexThread(file, extensionName, writer, doc, fis, br, op);
							log.debug("doc content >>> "+doc.toString());
						} catch (Exception e) {
							log.error("error:",e);
						}
					}
				});
				exec.shutdown();
				try 
				{
					int shutdownTime = 60; // 60초
					boolean isThreadTermination = exec.awaitTermination(shutdownTime, TimeUnit.SECONDS);
					if (!isThreadTermination) 
					{
						log.error("fileDocumentWriter task excuteIndexThread overTime ["+shutdownTime+" sec] Forcing shutdown");
						exec.shutdownNow();
					}
				} 
				catch (Exception e) 
				{
					log.error("error:",e);
				}
			}
			else
			{
				if(log.isDebugEnabled()) log.debug("Over Size 10MB contents Skip : {}", file);
			}
			// 본문내용 parsing end
			
			if(doc!=null)
			{
				if(!isMeta(extensionName))
				{
					log.debug("working 1 ?");
					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) 
					{
						log.debug("working 2 ?");
						writer.addDocument(doc);
					} 
					else 
					{
						log.debug("working 3 ?");
						writer.updateDocument(new Term("path", file.getPath()), doc);
					}							
				}
			}
		}catch(Exception e)
		{
			log.error("fileDocumentWriter error file ["+ file+"] Exception Class : ["+e.getClass()+"]     Message : ["+e.getMessage()+"]");
		}
		finally
		{
			if(op!=null) op.close();
			if(fis!=null) fis.close();
		}
	}

	private void setDocument(final File file, String fileInfo, final Document doc, final String extensionName) {
		doc.add(new StringField("name", (file.getName()).toLowerCase(),	Field.Store.YES));
		doc.add(new StringField("fileInfo", fileInfo, Field.Store.YES));
		doc.add(new IntField("lastModifiedDT", Integer.parseInt(DateUtils.getDateStr(new Date(file.lastModified()), "yyyyMMdd")),	Field.Store.YES));
		doc.add(new LongField("size", file.length(), Field.Store.YES));
		if(log.isDebugEnabled()) log.debug("fileDocumentWriter file : "+file+"    mimeType : "+extensionName+"     IMAGE : "+StorageConstant.EXT_IMAGE_LIST.contains(extensionName));
		if(StorageConstant.EXT_OFFICE_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "OFFICE", Field.Store.YES));
		}else if(StorageConstant.EXT_AUDIO_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "AUDIO", Field.Store.YES));
		}else if(StorageConstant.EXT_VIDEO_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "VIDEO", Field.Store.YES));
		}else if(StorageConstant.EXT_IMAGE_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "IMAGE", Field.Store.YES));
		}else if(StorageConstant.EXT_WEB_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "WEB",	Field.Store.YES));
		}else if(StorageConstant.EXT_FONT_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "FONT", Field.Store.YES));
		}else if(StorageConstant.EXT_COMPRESSED_LIST.contains(extensionName)){
			doc.add(new StringField("mimeType", "COMPRESSED", Field.Store.YES));
		}else {
			doc.add(new StringField("mimeType", "ETC", Field.Store.YES));
		}
		log.debug("document >> "+doc.toString());
	}
	
	public void excuteIndexThread(File file, String extension, IndexWriter writer, Document doc, FileInputStream fis, BufferedReader br, OPCPackage op)
	{
		try 
		{
			if(StorageConstant.EXT_INDEX_LIST.contains(extension)) 
			{
				fis = new FileInputStream(file);
				doc = IndexFilesETCDocs.indexETCDocs(file, doc, fis, br);
			}
		} catch (Exception e) 
		{
			log.error("excuteIndexThread Error file : ["+file+"]     Class : ["+e.getClass()+"]     Message : ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	public boolean isMeta(String extension)
	{
		if(StorageConstant.UUID_META_NAME.equals(extension)){return true;}
		return false;
	}

}
