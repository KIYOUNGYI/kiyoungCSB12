package src.test.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kiyoung.cloud.service.storage.StorageService;



public class StorageTest {
	
	private static StorageTest StorageTest;
	private String delim;
	private String filename;
	
	private int bufSize = 1024;
	
	private static ThreadLocal<String> uploadResult = new ThreadLocal<String>();
	
	public static StorageTest getInstance() 
	{
		if(StorageTest==null) {StorageTest = new StorageTest();}
		return StorageTest;
	}
	
	public static void main(String[] args) throws Exception 
	{
		StorageTest s = StorageTest.getInstance();
//		String keeneye7TokenId = "a1e978c6-4e6c-4482-a937-088f26691533";
//		String result  = s.create("keeneye7TokenId", "yky2798@gmail.com/dummy/hello");
//		System.out.println(">result:"+result);
		String result = s.setStorageUser("e8da03df-cf50-49e8-91a5-5cbbc825e383","dff58a8a-2bcd-4dde-8058-b88eba3f20f2");
		System.out.println(">"+result);
//		s.downloadToTargetDir("d:/", "dff58a8a-2bcd-4dde-8058-b88eba3f20f2@@portal07-767e-4540-98b9-424b0be1a5b5", new String[] {"goatyum@duzon.com/h1/한.zip"}, false);
//		String r = s.downloadToTargetDir("//temp/", "dff58a8a-2bcd-4dde-8058-b88eba3f20f2@@portal07-767e-4540-98b9-424b0be1a5b5", new String[] {"goatyum@duzon.com/h1/한.zip"}, false);
//		System.out.println("r:"+r);
		
	}
	
	public String setStorageUser(String bizConnId,String userConnId) throws Exception 
	{
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/kiyoungCSB12/services/login");
		JsonObject json = new JsonObject();
		
		json.addProperty("businessConnectID", bizConnId);
		json.addProperty("userConnectID", userConnId);
		HttpEntity entity = new StringEntity(json.toString(), "UTF-8");
		String res = "";
		res = sendHttpPost(httpPost, entity, "StorageAdminUser", "setStorageUser");
		
		return res;
	}
	
	/**
	 * 폴더 생성 
	 * @param tokenId
	 * @param directoryPath
	 * @return
	 * @throws Exception
	 */
	public String create(String tokenId, String directoryPath) throws Exception {
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/kiyoungCSB12/services/login");
		JsonObject json = new JsonObject();
		json.addProperty("TokenID", tokenId);
		json.addProperty("DirectoryPath", directoryPath);
		HttpEntity entity = new StringEntity(json.toString(), "UTF-8");

		String res = "";
		try {
			res = sendHttpPost(httpPost, entity, "StorageSvc", "create");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
    }
	
	
	public String downloadToTargetDir(String targetDir, String tokenId, String[] filePath, boolean recursive) throws Exception 
	{
		
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/kiyoungCSB12/services/login");
 		try {
 			httpPost.setHeader("service", "FileDownloadSvc");
 			httpPost.setHeader("method", "fileDownloadDummy");
 			httpPost.setHeader("clientId", "dummy");
 			
 			Gson gson = new Gson();
 			String filePathJson = gson.toJson(filePath);
 			
 			JsonObject json = new JsonObject();
 			json.addProperty("TokenID", tokenId);
 			json.addProperty("FilePath", filePathJson);
 			json.addProperty("Recursive", recursive);
 			
 			HttpEntity entity = new StringEntity(json.toString(), "UTF-8");
			httpPost.setEntity(entity);
			
 			HttpClient httpClient = new DefaultHttpClient();
 			HttpResponse execute = httpClient.execute(httpPost);
 			HttpEntity resEntity = execute.getEntity();
 			
 			File targetFile = new File("/Users/yky1990/temp/foo.txt");

 			if (entity != null) {
 			    InputStream inputStream = entity.getContent();
 			    OutputStream outputStream = new FileOutputStream(targetFile);
 			    IOUtils.copy(inputStream, outputStream);
 			    outputStream.close();
 			}
 			
 			
 			
 			System.out.println(resEntity.getContentLength());
 			
 			InputStream input = resEntity.getContent();
 			System.out.println("input:"+input.toString());
 			
 			System.out.println("available:"+input.available());
 			BufferedInputStream in = null;
 			in = new BufferedInputStream(input);
 			System.out.println("in:"+in.toString());
 			try {
 				parse(targetDir, in);
 			} catch (IOException e) {
 				throw e;
 			}
 			
 			in.close();
 			input.close();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		String returnVal = uploadResult.get();
 		uploadResult.remove();
 		
		return returnVal;
	}
	
	
	/*
	 * method no. 10
	 */
	private void parse(String targetDir, BufferedInputStream in) throws IOException {
		String line = null;

		try {
			/* 첫줄을 읽어서 delimeter로 저장한다. */
			delim = readLine(in).trim();
			System.out.println("delim:"+delim.toString());
			if(delim.startsWith("{")) {
				uploadResult.set(delim);
				return;
			}
			/*
			 * 첫번째 파라미터 처리 파라미터가 파일에 관련한 것이면 processUploading()으로 파일을 업로드 시키며
			 * 그렇지 않은 경우 parseValue()로 파라미터와 value를 파싱한다.
			 */
			line = readLine(in).trim();
			
//			if ((line.indexOf("Content-Type: application/octet-stream")) != -1)
			if ((line.indexOf("Content-Type: text/plain; charset=UTF-8")) == -1)
				{processDownloading(targetDir, in);}
			else
				{
					parseValue(line, in);
				}

			/* 이어지는 파라미터들 처리. 과정은 위와 같다. */
			while ((line = readLine(in)) != null) {
				line = line.trim();
				if (line.equals(delim)) {
					line = readLine(in).trim();
//					if ((line.indexOf("Content-Type: application/octet-stream")) != -1)
					if ((line.indexOf("Content-Type: text/plain; charset=UTF-8")) == -1) {
						System.out.println("processDownloading ");
						processDownloading(targetDir, in);
					}
					else {
						parseValue(line, in);
					}
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	
	/**
	 * 포스트 전송 공통 모듈
	 * @param httpPost
	 * @param entity
	 * @param service
	 * @param method
	 * @return
	 * @throws Exception
	 */
	private static String sendHttpPost(HttpPost httpPost, HttpEntity entity, String service, String method) throws Exception {
		String res;
		httpPost.setEntity(entity);
		httpPost.setHeader("service", service);
		httpPost.setHeader("method", method);
		httpPost.setHeader("clientId", "dum");
		httpPost.setHeader("Accept-Charset", "UTF-8");

		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse execute = httpClient.execute(httpPost);
		HttpEntity resEntity = execute.getEntity();
		InputStream content = resEntity.getContent();

		StringWriter writer = new StringWriter();
		IOUtils.copy(content, writer, "UTF-8");
		res = writer.toString();
		writer.close();
		return res;
	}
	
	/*
	 * method no. 13
	 */
	private String readLine(BufferedInputStream in) throws IOException {
		byte[] buffer = new byte[bufSize];
		int count = 0;
		int rByte;

		try {
			if ((rByte = in.read()) == -1)
				return null;
			while (rByte != '\n') {
				buffer[count++] = (byte) rByte;
				if (count == buffer.length - 1)
					return new String(buffer, 0, buffer.length);
				if (rByte == -1)
					break;
				rByte = in.read();
			}
		} catch (IOException e) {
			throw e;
		}

		if (count > 0)
			return new String(buffer, 0, count);
		else
			return "";
	}
	
	
	/*
	 * method no. 14
	 */
	private void processDownloading(String targetDir, BufferedInputStream in) throws IOException {
		readLine(in);
		String line = readLine(in).trim();
		
		if (parseFilename(line)) {
			File f = new File(targetDir, filename);
			FileOutputStream fout = null;
			BufferedOutputStream out = null;

			try { /* FileOutputStream 생성 */
				fout = new FileOutputStream(f);
				out = new BufferedOutputStream(fout);
			} catch (IOException e) {
				throw e;
			}

			try {
				/* 버퍼. 기본 버퍼사이즈는 bufSize로 결정하며, 여기서는 1024byte로 정했다. */
				byte[] buffer = new byte[bufSize];

				/* 현재의 버퍼 크기. 버퍼 크기가 변동되므로 버퍼 크기를 추적해야 한다. */
				int currentBufSize = bufSize;

				/* count는 버퍼내에서 저장된 데이타의 마지막 인덱스. bufferResizingCount는 버퍼 증가 회수 */
				int count = 0, bufferResizingCount = 0;

				int rByte;

				readLine(in);
				readLine(in);

				while ((rByte = in.read()) != -1) {
					buffer[count++] = (byte) rByte;

					if (count == buffer.length) { // 버퍼가 다 찼을 경우
						/* 버퍼를 2배로 늘린다. 메모리를 지나치게 사용할 수 있으므로 5회까지 늘릴 수 있다. */
						if (bufferResizingCount < 5) {
							byte[] tempArray = new byte[currentBufSize * 2];
							System.arraycopy(buffer, 0, tempArray, 0,
									currentBufSize);
							currentBufSize *= 2;
							buffer = tempArray;
						} else {
							/* 버퍼 리사이징이 5회가 넘어가면 출력한다. */
							out.write(buffer, 0, count);
							out.flush();

							/* 버퍼를 최초 크기로 복원한다. */
							count = 0;
							currentBufSize = bufSize;
							buffer = new byte[bufSize];
							bufferResizingCount = 0;
						}
					}

					/* \n 문자를 만났을 경우 다음 라인을 읽어서 파일의 끝인지 판별한다. */
					if (rByte == '\n') {

						/*
						 * 다음 라인이 Delimeter인지 확인하기 위해 현재 위치를 mark하고 다음 라인을 읽은 후
						 * 그 위치를 reset한다.
						 */
						in.mark(70);
						String temp = readDelimeter(in);
						in.reset();

						/* 파일의 끝인 경우 \n 전까지 출력한다. */
						if ((temp.indexOf(delim)) != -1) {
							out.write(buffer, 0, count - 2);
							out.flush();
							out.close();
							return;
						} else { /* 아닌 경우 바로 출력한다. */
							out.write(buffer, 0, count);
							out.flush();
							/* 버퍼를 최초 크기로 복원한다. */
							count = 0;
							bufferResizingCount = 0;
							currentBufSize = bufSize;
							buffer = new byte[bufSize];
						} // end of if
					} // end of if
				} // end of while
				out.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	/*
	 * method no. 16
	 */
	private String readDelimeter(BufferedInputStream in) throws IOException {
		byte[] buffer = new byte[64];
		int count = 0;
		int rByte;

		try {
			if ((rByte = in.read()) == -1)
				return null;
			while (rByte != '\n') {
				buffer[count++] = (byte) rByte;
				if (count == buffer.length - 1)
					return new String(buffer, 0, buffer.length);
				if (rByte == -1)
					break;
				rByte = in.read();
			}
		} catch (IOException e) {
			throw e;
		}

		if (count > 0)
			return new String(buffer, 0, count);
		else
			return "";
	}
	
	private void parseValue(String line, BufferedInputStream in) throws IOException {
		String value = null;
		try {
			readLine(in);
			readLine(in);
			readLine(in);

			value = readLine(in);
			uploadResult.set(value);

		} catch (IOException e) {
			throw e;
		}
	}
	
	/*
	 * method no. 17
	 */
	private boolean parseFilename(String line) {
		/*
		 * 토크나이징해서 필드명을 얻어낸다. 마지막 토큰이 파일명이 된다. 토크나이징 구분자는 ", /, \ 를 사용한다.
		 */
		if (line.indexOf("filename=\"\"") == -1) { // 전송된 파일이 있는 경우
			String token = null;
			StringTokenizer st = new StringTokenizer(line, "\"\\/");
			while (st.hasMoreTokens())
				token = st.nextToken();
			try {
				filename = java.net.URLDecoder.decode(token, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return true;
		} else { // 전송된 파일이 없는 경우
			return false;
		}
	}
}
