package src.test.java;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FileUploadServiceTest {
	
	
	
	/**
	 * 다중 파일 업로드
	 * @param tokenId
	 * @param localFqpn
	 * @param filePath
	 * @param overwrite
	 * @param createDate
	 * @return
	 * @throws Exception
	 */
	public static String uploadsAndCreateFolder2(String tokenId, String[] localFqpn, String filePath, boolean overwrite, String createDate) throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:8080/kiyoungCSB12/services/storageWeb");//url을 
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		
		for(int i=0; i<localFqpn.length; i++)
		{
			entity.addPart("file_name_"+(i+1), new FileBody(new File(localFqpn[i])));
		}

		JsonObject json = new JsonObject();
		json.addProperty("TokenID", tokenId);
		json.addProperty("FilePath", filePath);
		json.addProperty("Overwrite", overwrite);
//		json.addProperty("CreateDate", createDate);
		entity.addPart("formField", new StringBody(json.toString(), "text/plain", Charset.forName("UTF-8")));
		System.out.println("json:"+json.toString());
		String res = "";
		try {
			res = sendHttpPost(httpPost, entity, "FileUploadSvc", "uploadsAndCreateFolderVer2");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	/**
	 * 단일 파일 업로드
	 * @param tokenId
	 * @param fromFqpn
	 * @param filePath
	 * @param overwrite
	 * @param createDate
	 * @return
	 * @throws Exception
	 */
	public static String uploadsAndCreateFolder(String tokenId, File fromFqpn, String filePath, boolean overwrite, String createDate) throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:8080/kiyoungCSB12/services/storageWeb");//url을 
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		entity.addPart("file_name_1", new FileBody(fromFqpn));
		JsonObject json = new JsonObject();
		json.addProperty("TokenID", tokenId);
		json.addProperty("FilePath", filePath);
		json.addProperty("Overwrite", overwrite);
//		json.addProperty("CreateDate", createDate);
		entity.addPart("formField", new StringBody(json.toString(), "text/plain", Charset.forName("UTF-8")));
		System.out.println("json:"+json.toString());
		String res = "";
		try {
			res = sendHttpPost(httpPost, entity, "FileUploadSvc", "uploadsAndCreateFolder");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
//		단일 파일 업로드 	
//		uploadsAndCreateFolder("token",new File("/Users/yky1990/hello"),"yky1990/",false,"");

//		다중 파일 업로드		
//		String result = uploadsAndCreateFolder("ab8299a3-563c-4d4a-b234-38b2eca053d6", new File("/Users/yky1990/get-pip.py"), "goatyum@duzon.com/", false, "1387870799000");
//		String[] localFqpn = {"/Users/yky1990/get-pip.py","/Users/yky1990/hello.txt","/Users/yky1990/Downloads/sample_input.txt","/Users/yky1990/Downloads/sample_output.txt"};
//		String[] localFqpn = {"/Users/yky1990/get-pip.py"};
		String[] localFqpn = {"/Users/yky1990/dummy1.txt"};
//		String[] localFqpn = {"/Users/yky1990/Documents/dummyThumb001.jpg"};
//		String result2 = uploadsAndCreateFolder2("ab8299a3-563c-4d4a-b234-38b2eca053d6", localFqpn, "yky2798@gmail.com/", false, "1387870799000");
		String result3 = uploadsAndCreateFolder2("ab8299a3-563c-4d4a-b234-38b2eca053d6", localFqpn, "dummy1@gmail.com/1027/", false, "1387870799000");
		System.out.println("result3:"+result3);
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
}
