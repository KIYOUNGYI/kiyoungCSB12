package com.kiyoung.cloud.common.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kiyoung.cloud.common.StorageConstant;

@Service("thumbImageService")
public class ThumbImageService {
	@Value("${thum_path}")
	private String thumTempPath;
	 
	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	@Value("${thumb_img_url}")
	private String thumb_img_url;
	
	@Value("${THUMB_USE_MODE}")
	private boolean thumbUseMode;

	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	@Autowired
	private ThumbnailCommonService thumbnailCommonService;
	
	public void convertSaveThumbnail(List<File> imageList) throws IOException {
		// TODO Auto-generated method stub
		for(File f : imageList) 
		{
			if(f.exists()) thumbnailCommonService.sendThumbNailDataToThumbImgServer(f.getAbsolutePath());
		}
	}
	
	public void getThumbNail(String receiveMessage) throws Exception 
	{
		log.error("message >> "+receiveMessage);
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		JsonObject jo = (JsonObject)parser.parse(receiveMessage);
		HashMap<String, Object> thumbPathMap = gson.fromJson(jo, HashMap.class);
		String method = thumbPathMap.get("method").toString();
		String thumbPath = thumbPathMap.get("thumbPath").toString();
		String thumPath = thumbPathMap.get("thumPath").toString();
		String storageThumbFilePath = thumbPathMap.get("storageThumbFile").toString();
		if(method.equals("createThumb")) 
		{
			File thumbFile = new File(thumbPath);
			File storageThumbFile = new File(storageThumbFilePath);
			log.debug("thumbPathMap >>"+thumbPathMap.toString());
			sendThumImageServer(thumbFile, thumPath, storageThumbFile);
		}
	}
	
	/**
	 * 썸네일을 생성해서 이미지 서버로 전송 
	 * 
	 * @param imageList
	 * @throws Exception
	 */
	public void sendThumImageServer(File imageFile, String thumPath, File storageThumbFile) throws Exception {
//		if(thumbUseMode) {
		if(thumbUseMode) {
				try {
					createImg(StorageConstant.THUMNAIL_WIDTH, StorageConstant.THUMNAIL_WIDTH, thumPath, imageFile, storageThumbFile);
				} catch(Exception e) {
					log.error("error:",e);
				}
		}
	}
	
	private void createImg(int width, int height, String path, File orgnFullFile, File outThumFile) throws IOException, InterruptedException{
		 if(thumbUseMode){
			 BufferedImage resizedImage = null;
			 Graphics2D graphics2D = null;
			 try{
				 File dir = new File(path);
				 if(log.isDebugEnabled()) log.debug("testImg path : ["+path+"]     thumFullFileNm : ["+outThumFile+"]     orgnFullFilNm : ["+orgnFullFile+"]");
				 if (!dir.exists()) dir.mkdir();

				 if (outThumFile.exists()) {
					 return;
				 }
				 BufferedImage originalImage = null;
				 originalImage = ImageIO.read(orgnFullFile);
				 String ext = FilenameUtils.getExtension(orgnFullFile.getName());
				 
				 log.debug("ext >>"+ext);
				 
				 int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

				 log.debug("type >>"+type);
				 
				 resizedImage = new BufferedImage(width, height, type);
				 graphics2D = resizedImage.createGraphics();
				 graphics2D.drawImage(originalImage, 0, 0, width, height, null);
				 graphics2D.dispose();
				 outThumFile.createNewFile();
				 ImageIO.write(resizedImage, ext, outThumFile); 

				 Thread.sleep(1000);
			 } catch(Exception e) {
//				 e.printStackTrace();
				 log.error("error:",e);
			 }  finally {
				 if(resizedImage!=null) resizedImage.flush();
				 if(graphics2D!=null) graphics2D.dispose();
			 }
		 }
	 }
	
}
