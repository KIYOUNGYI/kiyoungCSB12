package com.kiyoung.cloud.service.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiyoung.cloud.common.dto.DCloudResultSet;

@Service("FileUploadSvc")
public class FileUploadService {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${ROOT_PATH}")
	private String storageRootPath;

//	@Value("${thumb_img_url}")
//	private String thumb_img_url;

	
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

		File uploadFile = null; // kkh
		String path = "/duzon/company1/user1"; // 디비에서 가지고 와서 회사 아이디,유저아이디 가져왔다 치고
		File file = new File(path);

		Map<String, DataHandler> files = in.getAttachments();

		for (String fileKey : files.keySet()) {
			DataHandler data = in.getAttachment(fileKey);
			log.debug("data.getName() >" + data.getName());
			uploadFile = new File(path + "/" + data.getName());

			// 확장자
			String ext = getExt(data);
			// 파일명
			String name = getFileName(data);

			// 중복체크
			File[] listFiles = file.listFiles();
			boolean exists = checkFileExist(uploadFile, listFiles);

			log.debug("exists > " + Boolean.toString(exists));

			if (exists) {
				int i = 1;
				String fileName = name + " (" + i + ")" + ext;
				uploadFile = new File(file.getAbsolutePath() + "/" + fileName);
				// 동일한 폴더가 있을 때
				while (uploadFile.exists()) {
					i++;
					fileName = name + " (" + i + ")" + ext;
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

	private boolean checkFileExist(File file, File[] listFiles) {
		if (null != listFiles && listFiles.length > 0) {
			for (File f : listFiles) {
				if (file.getName().toLowerCase().equals(f.getName().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	

}
