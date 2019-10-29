package com.kiyoung.cloud.service.storage;
//
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.language.Bean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
//import com.kiyoung.*;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.common.service.CommonStaticService;
import com.kiyoung.cloud.common.service.ThumbImageService;
import com.kiyoung.cloud.service.index.lucene.IndexService;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.FileInfo;
import com.kiyoung.cloud.storage.dto.UserAndBusinessConnectIdDto;
import com.kiyoung.cloud.storage.service.StorageObjectCommonService;
//
@Service("FileUploadSvc")
public class FileUploadService {
//
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
	private ThumbImageService thumbImageService;
	
	@Autowired
	private IndexService indexService;
	
	
	@Bean(ref="bindArgs")
	public Map<String, String> bindArgsBuilder() {
	    return Collections.singletonMap("x-queue-type","classic");
	}
	
	/**
	 * 디렉토리가 없어서 만드는 부분은 구현안함(생략) 
	 * @param exchange
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public DCloudResultSet uploadsAndCreateFolderVer2(Exchange exchange) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		Message in = exchange.getIn();
		HashMap<String, Object> inMap = exchange.getIn().getMandatoryBody(HashMap.class);
		String filePath = inMap.get("FilePath").toString();
		List<File> imageList = new ArrayList<File>(); // log.debug("filePath > " + filePath +"\n"+" in > " + in.toString());
		File uploadFile = null; 
		String account = filePath.substring(0, filePath.indexOf("/"));
		String remain = filePath.substring(filePath.indexOf("/"));
		UserAndBusinessConnectIdDto userDto = commonService.getBizAndUserConnId(account);
		String fullPath = storageRootPath + File.separator + userDto.getBusinessConnectId() + File.separator + userDto.getUserConnectId() + remain;
		File file = new File(fullPath);
		String indexRootPath = commonService.getIndexFilePath(account);
		
		Map<String, DataHandler> files = in.getAttachments();

		for (String fileKey : files.keySet()) {
			FileAccessMeta meta = new FileAccessMeta();
			DataHandler data = in.getAttachment(fileKey);
			uploadFile = new File(fullPath + "/" + data.getName());

			String ext = getExt(data);	// 확장자 (수정 필요 - 추후) 
			String name = getFileName(data);// 파일명 log.debug("ext > "+ext);
			File[] listFiles = file.listFiles();
			boolean exists = commonService.checkFileExist(uploadFile, listFiles);// 중복체크
			String fileName = "";
			
			if (exists) {// 동일한 폴더가 있을 때
				int i = 1;fileName = name + " (" + i + ")" + ext;
				uploadFile = new File(file.getAbsolutePath() + "/" + fileName);
				while (uploadFile.exists()) {i++;fileName = name + " (" + i + ")" + ext;
				uploadFile = new File(file.getAbsolutePath() + "/" + fileName);
				}
			}
			
			FileOutputStream stream = new FileOutputStream(uploadFile);
			long fileSize = IOUtils.copyLarge(data.getInputStream(), stream);// 코어 (파일 업로드 해당)
			stream.close();
			
			String extension = FilenameUtils.getExtension(uploadFile.getName());
			if(StorageConstant.EXT_THUMB_LIST.contains(extension.toLowerCase())) 
			{
				String thumNailPath = thumb_img_url+File.separator+uploadFile.getAbsolutePath();
				meta.setThumbNailPath(thumNailPath);imageList.add(uploadFile);
			}
			commonService.saveFileAccessMeta(uploadFile, account, fullPath, file, meta, fileName);//파일 메타 저장//log.debug("fileSize > " + Long.toString(fileSize));//			log.debug("FileUploadService meta >>> "+meta.toString());
			
			if(indexUseMode) makeIndexData1(uploadFile, indexRootPath, meta);
			
		}

		if(imageList.size()>0 && thumbUseMode) {thumbImageService.convertSaveThumbnail(imageList);}
	
// 	    참고 코드		
//		if(indexUseMode){
			//String indexRootPath = commonService.getIndexFilePath(account);//commonService.setIndexUser(indexRootPath);//어차피 메소드분기 안태워서 필요없는 코드
//			//String indexRootPath = commonService.getIndexFilePath(account);//commonService.setIndexUser(indexRootPath);//어차피 메소드분기 안태워서 필요없는 코드
//			FileInfo fileInfo = CommonStaticService.convertFileToFileInfo(uploadFile, account, commonService, meta);
//			HashMap<String, Object> body = null; 
//			body = new HashMap<String, Object>();
//			body = indexService.getIndexFileMap(indexRootPath, uploadFile, fileInfo);
//			String bodyJson = new Gson().toJson(body);
//			commonService.setIndexData(indexRootPath, bodyJson);
//			//						indexFiles.setQueueData(indexRootPath, body);
//		}
		
		return resultSet;
	}

	private void makeIndexData1(File uploadFile, String indexRootPath, FileAccessMeta meta) throws Exception {
		HashMap<String, Object> body = new HashMap<String, Object>();
		FileInfo fileInfo = CommonStaticService.convertFileToFileInfo(uploadFile,commonService, meta);
		body = indexService.getIndexFileMap(indexRootPath, uploadFile, fileInfo);
		String bodyJson = new Gson().toJson(body);//log.debug("bodyJson >>>"+bodyJson);
		commonService.setIndexData(indexRootPath, bodyJson);
	}

	/**
	 * 디렉토리가 없어서 만드는 부분은 구현안함(생략) 
	 * @param exchange
	 * @return
	 * @throws InvalidPayloadException
	 * @throws IOException
	 */
	@Transactional
	public DCloudResultSet uploadsAndCreateFolder(Exchange exchange) throws InvalidPayloadException, IOException {
		DCloudResultSet resultSet = new DCloudResultSet();
		Message in = exchange.getIn();
		HashMap<String, Object> inMap = exchange.getIn().getMandatoryBody(HashMap.class);
		String filePath = inMap.get("FilePath").toString();
		log.debug("filePath > " + filePath);
		log.debug("in > " + in.toString());

		File uploadFile = null;
		String path = "";
		File file = new File(path);

		Map<String, DataHandler> files = in.getAttachments();

		for (String fileKey : files.keySet()) {
			DataHandler data = in.getAttachment(fileKey);//log.debug("data.getName() >" + data.getName());
			uploadFile = new File(path + "/" + data.getName());

			String ext = getExt(data);// 확장자
			String name = getFileName(data);// 파일명

			
			File[] listFiles = file.listFiles();
			boolean exists = commonService.checkFileExist(uploadFile, listFiles);// 중복체크


			if (exists) {
				int i = 1;
				String fileName = name + " (" + i + ")" + ext;
				uploadFile = new File(file.getAbsolutePath() + "/" + fileName);
				// 동일한 폴더가 있을 때
				while (uploadFile.exists()) {
					i++; fileName = name + " (" + i + ")" + ext;
					uploadFile = new File(file.getAbsolutePath() + "/" + fileName);
				}
			}

			// 코어 (파일 업로드 해당)
			FileOutputStream stream = new FileOutputStream(uploadFile);
			long fileSize = IOUtils.copyLarge(data.getInputStream(), stream);
			stream.close();

			log.debug("fileSize > " + Long.toString(fileSize));
		}

		return resultSet;
	}

	private String getFileName(DataHandler data) {
		String name = data.getName();
		if (data.getName().contains(".")) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}

	private String getExt(DataHandler data) {
		String ext = FilenameUtils.getExtension(data.getName());
		if (org.apache.commons.lang.StringUtils.isNotEmpty(ext)) {
			ext = "." + ext;
		}
		return ext;
	}

//	private boolean checkFileExist(File file, File[] listFiles) {
//		if (null != listFiles && listFiles.length > 0) {
//			for (File f : listFiles) {
//				if (file.getName().toLowerCase().equals(f.getName().toLowerCase())) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	
	
	public static void main(String[] args) {
		File file = new File("");
	} 
}
