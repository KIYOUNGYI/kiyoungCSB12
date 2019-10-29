package com.kiyoung.cloud.common.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class ThumbnailCommonService {
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${thum_path}")
	private String thumTempPath;
	 
	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	@Value("${thumb_img_url}")
	private String thumb_img_url;
	
	@Value("${THUMB_USE_MODE}")
	private boolean thumbUseMode;
	
	@Produce
    ProducerTemplate producer;

	public void sendThumbNailDataToThumbImgServer(String filePath) throws IOException {
		String bodyJson = "";
		File thumRoot = new File(thumTempPath);
		String thumPath = thumTempPath + File.separator + "_thum";
		if(!thumRoot.exists()) FileUtils.forceMkdir(thumRoot);
		File thumbFile = new File(filePath);
		String fileName = thumbFile.getName().replaceAll(";", "").replaceAll(" ", "");
		File thumFile = new File(thumPath+ File.separator + fileName);
		String path = thumbFile.getParentFile().getAbsolutePath().replaceAll("\\\\", "/").replaceAll(storageRootPath, "");
		
		if(thumFile.exists()) FileUtils.forceDeleteOnExit(thumFile);
		
		File storageThumbPath = new File(thumTempPath+File.separator+path);
		if(!storageThumbPath.exists()) FileUtils.forceMkdir(storageThumbPath);
		File storageThumbFile = new File(storageThumbPath+File.separator+thumFile.getName());
		log.debug("setThumbPathToRedis filePath : ["+filePath+"]     storageThumbFile : ["+storageThumbFile+"]     storageThumbPath : ["+storageThumbPath+"]     path : ["+path+"]     thumFile : ["+thumFile+"]     fileName : ["+fileName+"]     thumPath : ["+thumPath+"]     isExists : ["+(!storageThumbFile.exists())+"]");
	
		
		HashMap<String, Object> thumbPathMap = new HashMap<String, Object>();
		thumbPathMap.put("method", "createThumb");
		thumbPathMap.put("thumbPath", filePath);
		thumbPathMap.put("thumPath", thumPath);// 이게 왜 있는지 솔직히 잘 모르겠음.  (뒤에서 파일을 만들긴 하는데) 
//		thumbPathMap.put("thumPath", storageThumbFile.getAbsolutePath());
		thumbPathMap.put("storageThumbFile", storageThumbFile.getAbsolutePath());
		bodyJson = new Gson().toJson(thumbPathMap);
		log.debug("bodyJson>>>"+bodyJson);
//		producer.sendBody("direct:rabbitMQThumb",bodyJson);
		
		producer.sendBody("direct:rabbitMQThumb",bodyJson);
		
	}

	
	
}
