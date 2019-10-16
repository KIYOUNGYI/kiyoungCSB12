package src.test.java;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


import com.google.gson.JsonObject;
import com.kiyoung.cloud.service.storage.StorageService;



public class StorageTest {
	
	private static StorageTest StorageTest;
	
	
	public static StorageTest getInstance() 
	{
		if(StorageTest==null) {StorageTest = new StorageTest();}
		return StorageTest;
	}
	
	public static void main(String[] args) throws Exception 
	{
		StorageTest s = StorageTest.getInstance();
		String keeneye7TokenId = "a1e978c6-4e6c-4482-a937-088f26691533";
		
		String result  = s.create(keeneye7TokenId, "yky2798@gmail.com/dummy/");
		System.out.println(">result:"+result);
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
