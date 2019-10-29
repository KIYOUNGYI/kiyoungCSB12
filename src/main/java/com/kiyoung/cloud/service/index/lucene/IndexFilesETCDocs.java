package com.kiyoung.cloud.service.index.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.mozilla.universalchardet.UniversalDetector;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Service;


@Service
public class IndexFilesETCDocs {

	
	public Document indexETCDocs(File file, Document doc, FileInputStream fis, BufferedReader br) throws IOException 
	{
		String encoding = findFileEncoding(new File(file.getAbsolutePath()));
		doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, encoding))));
		br = new BufferedReader(new InputStreamReader(fis, encoding));
		StringBuffer sb = new StringBuffer();
		String data = "";
		while((data = br.readLine()) !=null){
			sb.append(data+"\n");
		}
		if((sb.toString().trim()).equals("")){
			sb.append("unkwon");
		}
		doc.add(new TextField("contents", sb.toString(), Field.Store.YES));
		
		
		return doc;
	}
	
	public String findFileEncoding(File file) throws IOException {
		byte[] buf = new byte[4096];
		FileInputStream fis = null;
		UniversalDetector detector = null;
		String encoding = "";
		try{
			fis = new FileInputStream(file);
			detector = new UniversalDetector(null);
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();
			encoding = detector.getDetectedCharset();
			if (encoding != null) {
				if(encoding.equals("WINDOWS-1252")) encoding="EUC-KR";
			} else {
				encoding="EUC-KR";
			}
			detector.reset();
			fis.close();		
		}catch(Exception e){
			
			detector.reset();
			fis.close();			
		}finally{
			try {
				if(fis!=null) fis.close();
			} catch (Exception e2) {
				
			}
		}
		return encoding;
	}
}
