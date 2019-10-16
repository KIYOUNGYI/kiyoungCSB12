package com.kiyoung.cloud.common;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.StreamCache;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.component.http.HttpHeaderFilterStrategy;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.component.http.helper.CamelFileDataSource;
import org.apache.camel.component.http.helper.HttpHelper;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.GZIPHelper;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.MessageHelper;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class MultipartHttpBindingWeb extends org.apache.camel.component.http.DefaultHttpBinding {

	@Value("${tmpPath}")
	private String tmpPath;
	
	private boolean useReaderForPayload;
	private HeaderFilterStrategy headerFilterStrategy;
	private HttpEndpoint endpoint;
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	public MultipartHttpBindingWeb() {
		this(new HttpHeaderFilterStrategy());
	}
	
	public MultipartHttpBindingWeb(HeaderFilterStrategy headerFilterStrategy) {
		this.headerFilterStrategy = headerFilterStrategy;
	}
	
	public MultipartHttpBindingWeb(HttpEndpoint endpoint) {
		this.endpoint = endpoint;
		this.headerFilterStrategy = endpoint.getHeaderFilterStrategy();
	}
	
	@Override
	public void readRequest(final HttpServletRequest request,
			final HttpMessage message) {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			setMessageHeader(request, message);
			doMulitpart(request, message);
		}else{
	        // lets force a parse of the body and headers
	        message.getBody();
	        setMessageHeader(request, message);

	        Object body = message.getBody();
	        // reset the stream cache if the body is the instance of StreamCache
	        if (body instanceof StreamCache) {
	        	((StreamCache)body).reset();
	        }
	        // if content type is serialized java object, then de-serialize it to a Java object
	        if (request.getContentType() != null && HttpConstants.CONTENT_TYPE_JAVA_SERIALIZED_OBJECT.equals(request.getContentType())) {
	            try {
	                InputStream is = endpoint.getCamelContext().getTypeConverter().mandatoryConvertTo(InputStream.class, body);
	                Object object = HttpHelper.deserializeJavaObjectFromStream(is);
	                if (object != null) {
	                    message.setBody(object);
	                }
	            } catch (Exception e) {
	                throw new RuntimeCamelException("Cannot deserialize body to Java object", e);
	            }
	        }
	        populateAttachments(request, message);
		}
	}

	private void setMessageHeader(final HttpServletRequest request,
			final HttpMessage message) {
		// populate the headers from the request
		Map<String, Object> headers = message.getHeaders();
		
		//apply the headerFilterStrategy
		Enumeration names = request.getHeaderNames();
		while (names.hasMoreElements()) {
		    String name = (String)names.nextElement();
		    Object value = request.getHeader(name);
		    // mapping the content-type 
		    if (name.toLowerCase().equals("content-type")) {
		        name = Exchange.CONTENT_TYPE;
		    }
		    if (headerFilterStrategy != null
		        && !headerFilterStrategy.applyFilterToExternalHeaders(name, value, message.getExchange())) {
		        headers.put(name, value);
		    }
		}
		        
		if (request.getCharacterEncoding() != null) {
		    headers.put(Exchange.HTTP_CHARACTER_ENCODING, request.getCharacterEncoding());
		    message.getExchange().setProperty(Exchange.CHARSET_NAME, request.getCharacterEncoding());
		}
		
		// store the method and query and other info in headers
		headers.put(Exchange.HTTP_METHOD, request.getMethod());
		headers.put(Exchange.HTTP_QUERY, request.getQueryString());
		headers.put(Exchange.HTTP_URL, request.getRequestURL());
		headers.put(Exchange.HTTP_URI, request.getRequestURI());
		headers.put(Exchange.HTTP_PATH, request.getPathInfo());
		headers.put(Exchange.CONTENT_TYPE, request.getContentType());
		
		try {
			populateRequestParameters(request, message);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeCamelException("Cannot read request parameters due " + e.getMessage(), e);
		}
	}

	private void doMulitpart(final HttpServletRequest request, HttpMessage message) {
		try {
			if(log.isDebugEnabled()) log.debug("doMulitpart request getHeader: "+request.getHeader("FilePath"));
			
			// header의 Filepath 정보가 tempMetaMongoDB 에 저장이 되어있는지 확인한다.
			boolean isContinueUpload = false;
			if("goatyum1@duzon.com/test".equals(request.getHeader("FilePath"))){
				// tempMeta에 있으면 MD5 값을 비교한다.
				// TODO

				// MD5 값이 동일하다면 이어받기를 한다.
				// TODO
				isContinueUpload = true;
				
				// MD5값이 다르다면 새로생성한다. 기존로직 사용.
			}
			
			// Create a new file upload handler
			final ServletFileUpload upload = new ServletFileUpload();
			// Parse the request
			final FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				final FileItemStream item = iter.next();
				final String name = item.getFieldName();
				String fileName = "";
				if(item.getName() != null){
					if(item.getName().contains("%")) {
						StringBuffer sb = new StringBuffer(item.getName());
						fileName = replacer(sb);
					} else {
						fileName = URLDecoder.decode(item.getName(), "UTF-8");
					}
				}
				log.debug("FileName: {}", fileName);
				
				final InputStream stream = item.openStream();
				if (item.isFormField()) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					IOHelper.copy(stream, bos); 
					message.setBody(bos);
				} else {
					//message.addAttachment(name, new DataHandler(new ByteArrayDataSource(stream, item.getContentType())));
				    
				    boolean memory = true;
                    File tmpFile = null;
                    File otherTmpPath = new File(tmpPath); // kkh 추가된 소스

                    OutputStream os = new ByteArrayOutputStream();
                    byte buff[] = new byte[4096];
                    int i = -1;
                    int totalLength = 0;
                    while( ( i = stream.read(buff))!= -1){
                        os.write(buff, 0, i);
                        totalLength += i;
                        if( memory && totalLength > 64 * 1024 ){
                        	ByteArrayOutputStream bos = (ByteArrayOutputStream)os;
                        	tmpFile = FileUtil.createTempFile("cip.", ".tmp");
                        	if(otherTmpPath.exists()){
                        		tmpFile = new File(otherTmpPath, tmpFile.getName()); // kkh 추가된 소스
                        	}
                        	os = new BufferedOutputStream( new FileOutputStream(tmpFile)); // 원본 소스
                        	bos.writeTo(os);
                        	memory = false;
                        }
                    }
                    os.flush();
                    os.close();
                    
                    if(memory){
                        /* 소용량 파일(64k 미만)
                         * 메모리 상태로 처리
                         */
                    	ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(((ByteArrayOutputStream)os).toByteArray(), item.getContentType());
                    	byteArrayDataSource.setName(fileName);
                        message.addAttachment(name, new DataHandler(byteArrayDataSource));
                    }else{
                        /* 대용량 파일(64k 이상) 
                         * 임시 파일을 생성해서 처리 
                         */
//                        message.addAttachment(name, new DataHandler(new CamelFileDataSource(tmpFile, name)));
                        message.addAttachment(name, new DataHandler(new CamelFileDataSource(tmpFile, fileName)));
                    }
                   
                    if(isContinueUpload){
    					// 테스트를 위해서 임의로 temp폴더에 저장. 실제로는 사용하지 않는다.
                    	File oriTempFile = new File("/duzon/643cc9e1-a49b-4627-8cc0-c7888b1abcab/test/1.ppt_temp");
                    	if(!oriTempFile.exists()) {
                    		if(log.isDebugEnabled()) log.debug("doMulitpart !oriTempFile.exists() : "+oriTempFile);
                    		FileUtils.copyFile(tmpFile, oriTempFile);
                    	}else{
                    		// 전송하다 끊어진 파일다음부터 이어서 작성함.
                    		RandomAccessFile rf = new RandomAccessFile(oriTempFile.getAbsolutePath(), "rw");
                    		rf.seek(oriTempFile.length()); // 파일의 해당 부분부터 이어서 생성.

                    		// 끊어진 파일 이후의 정보가 들어옴.
                    		FileInputStream inputStream = new FileInputStream(tmpFile.getAbsolutePath()); 
                    		log.debug("oriTempFile : ["+oriTempFile.getAbsolutePath()+"]     oriTempFile size : ["+oriTempFile.length()+"]");
                    		log.debug("tmpFile : ["+tmpFile.getAbsolutePath()+"]     tmpFile size : ["+tmpFile.length()+"]");
                    		// temp파일에 합치기 위해서 byte[] 로 변환 
                    		byte[] inFileBytes = FileUtils.readFileToByteArray(tmpFile);
                    		// temp파일에 byte 합침.
                    		rf.write(inFileBytes);
                    		
                    	    rf.close();
                    		inputStream.close();
                    		
                    		// temp파일을 지정되어진 path로 복사.
                    		oriTempFile.renameTo(new File("/duzon/643cc9e1-a49b-4627-8cc0-c7888b1abcab/test/1.ppt"));
                    		// temp파일 삭제
//                    		oriTempFile.delete();
                    		// 이어전송 파일 삭제
//                    		tmpFile.delete();
                    	}
    				}
				}
			}
		} catch (final FileUploadException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void populateRequestParameters(HttpServletRequest request, HttpMessage message) throws UnsupportedEncodingException {
        //we populate the http request parameters without checking the request method
        Map<String, Object> headers = message.getHeaders();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String)names.nextElement();
            Object value = request.getParameter(name);
            if (headerFilterStrategy != null
                && !headerFilterStrategy.applyFilterToExternalHeaders(name, value, message.getExchange())) {
                headers.put(name, value);
            }
        }
        
        if (request.getMethod().equals("POST") && request.getContentType() != null
                && request.getContentType().startsWith(HttpConstants.CONTENT_TYPE_WWW_FORM_URLENCODED)) {
            String charset = request.getCharacterEncoding();
            if (charset == null) {
                charset = "UTF-8";
            }
            // Push POST form params into the headers to retain compatibility with DefaultHttpBinding
            String body = message.getBody(String.class);
            for (String param : body.split("&")) {
                String[] pair = param.split("=", 2);
                String name = URLDecoder.decode(pair[0], charset);
                String value = URLDecoder.decode(pair[1], charset);
                if (headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToExternalHeaders(name, value, message.getExchange())) {
                    headers.put(name, value);
                }
            }
        }
    }
    
    @Override
	protected void populateAttachments(HttpServletRequest request, HttpMessage message) {
        // check if there is multipart files, if so will put it into DataHandler
        Enumeration names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object object = request.getAttribute(name);
            if (object instanceof File) {
                String fileName = request.getParameter(name);
                message.addAttachment(fileName, new DataHandler(new CamelFileDataSource((File)object, fileName)));
            }
        }
    }

    @Override
	public void writeResponse(Exchange exchange, HttpServletResponse response) throws IOException {
    	//fileUpload메서드에서 온 exchange라면 String 응답값만을 보내기 위해 att를 지워준다.
//    	Object property = exchange.getProperty(StorageConstant.METHOD_FILEUPLOAD);
    	Object property = exchange.getProperty("fileUpload");
    	if(ObjectHelper.isNotEmpty(property) && (Boolean)property){
    		exchange.getOut().setAttachments(null);
    	}
    	
        if (exchange.isFailed()) {
            if (exchange.getException() != null) {
                doWriteExceptionResponse(exchange.getException(), response);
            } else {
                // it must be a fault, no need to check for the fault flag on the message
                doWriteFaultResponse(exchange.getOut(), response, exchange);
            }
        } else {
            // just copy the protocol relates header
            copyProtocolHeaders(exchange.getIn(), exchange.getOut());
            Message out = exchange.getOut();            
            if (out != null) {
                doWriteResponse(out, response, exchange);
            }
        }
    }

    private void copyProtocolHeaders(Message request, Message response) {
        if (request.getHeader(Exchange.CONTENT_ENCODING) != null) {
            String contentEncoding = request.getHeader(Exchange.CONTENT_ENCODING, String.class);
            response.setHeader(Exchange.CONTENT_ENCODING, contentEncoding);
        }        
        if (checkChunked(response, response.getExchange())) {
            response.setHeader(Exchange.TRANSFER_ENCODING, "chunked");
        }
    }

    @Override
	public void doWriteExceptionResponse(Throwable exception, HttpServletResponse response) throws IOException {
        // 500 for internal server error
        response.setStatus(500);

        if (endpoint != null && endpoint.isTransferException()) {
            // transfer the exception as a serialized java object
            HttpHelper.writeObjectToServletResponse(response, exception);
        } else {
            // write stacktrace as plain text
            response.setContentType("text/plain");
            PrintWriter pw = response.getWriter();
            exception.printStackTrace(pw);
            pw.flush();
        }
    }

    @Override
	public void doWriteFaultResponse(Message message, HttpServletResponse response, Exchange exchange) throws IOException {
        doWriteResponse(message, response, exchange);
    }

    @Override
	public void doWriteResponse(Message message, HttpServletResponse response, Exchange exchange) throws IOException {
        // set the status code in the response. Default is 200.
        if (message.getHeader(Exchange.HTTP_RESPONSE_CODE) != null) {
            int code = message.getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
            response.setStatus(code);
        }
        // set the content type in the response.
        String contentType = MessageHelper.getContentType(message);
        if (MessageHelper.getContentType(message) != null) {
            response.setContentType(contentType);
        }

        // append headers
        // must use entrySet to ensure case of keys is preserved
        for (Map.Entry<String, Object> entry : message.getHeaders().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToCamelHeaders(key, value, exchange)) {
                response.setHeader(key, value.toString());
            }
        }

        // write the body.
        if (message.getBody() != null) {
            if (GZIPHelper.isGzip(message)) {
                doWriteGZIPResponse(message, response, exchange);
            } else {
                doWriteDirectResponse(message, response, exchange);
            }
        }
    }

    @Override
	protected void doWriteDirectResponse(Message message, HttpServletResponse response, Exchange exchange) throws IOException {
        // if content type is serialized Java object, then serialize and write it to the response
        String contentType = message.getHeader(Exchange.CONTENT_TYPE, String.class);
        if (contentType != null && HttpConstants.CONTENT_TYPE_JAVA_SERIALIZED_OBJECT.equals(contentType)) {
            try {
                Object object = message.getMandatoryBody(Serializable.class);
                HttpHelper.writeObjectToServletResponse(response, object);
                // object is written so return
                return;
            } catch (InvalidPayloadException e) {
                throw IOHelper.createIOException(e);
            }
        }

        if(message.getAttachmentNames().isEmpty()){
	        // other kind of content type
	        InputStream is = null;
	        if (checkChunked(message, exchange)) {
	            is = message.getBody(InputStream.class);
	        }
	        if (is != null) {
	            ServletOutputStream os = response.getOutputStream();
	            try {
	                // copy directly from input stream to output stream
	                IOHelper.copy(is, os);
	            } finally {
	                IOHelper.close(os);
	                IOHelper.close(is);
	            }
	        } else {
	            // not convertable as a stream so try as a String
	            String data = message.getBody(String.class);
	            if (data != null) {
	                // set content length before we write data
	                response.setContentLength(data.length());
	                response.getWriter().print(data);
	                response.getWriter().flush();
	            }
	        }
        }else{
        	ServletOutputStream os = response.getOutputStream();
        	InputStream is = null;
	        if (checkChunked(message, exchange)) {
	            is = message.getBody(InputStream.class);
	        }
            try {
                IOHelper.copy(is, os);
            } finally {
                IOHelper.close(is);
            }
            os.println();
        	Set<String> name = message.getAttachmentNames();
        	for (Iterator<String> iterator = name.iterator(); iterator.hasNext();) {
				String attachName = iterator.next();
				DataHandler dh = message.getAttachment(attachName);
	            try {
	            	InputStream inputStream = dh.getInputStream();
	                IOHelper.copy(inputStream, os);
	                IOHelper.close(inputStream);
	            } finally {
	                IOHelper.close(is);
	            }
			}
        	os.flush();
        	IOHelper.close(os);
        	
        }
    }

    @Override
	protected boolean checkChunked(Message message, Exchange exchange) {
        boolean answer = true;
        if (message.getHeader(Exchange.HTTP_CHUNKED) == null) {
            // check the endpoint option
            Endpoint endpoint = exchange.getFromEndpoint();
            if (endpoint instanceof HttpEndpoint) {
                answer = ((HttpEndpoint)endpoint).isChunked();
            }
        } else {
            answer = message.getHeader(Exchange.HTTP_CHUNKED, boolean.class);
        }
        return answer;
    }

        byte[] bytes;
        @Override
		protected void doWriteGZIPResponse(Message message, HttpServletResponse response, Exchange exchange) throws IOException {
        try {
            bytes = message.getMandatoryBody(byte[].class);
        } catch (InvalidPayloadException e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }

        byte[] data = GZIPHelper.compressGZIP(bytes);
        ServletOutputStream os = response.getOutputStream();
        try {
            response.setContentLength(data.length);
            os.write(data);
            os.flush();
        } finally {
            IOHelper.close(os);
        }
    }

    @Override
	public Object parseBody(HttpMessage httpMessage) throws IOException {
        // lets assume the body is a reader
        HttpServletRequest request = httpMessage.getRequest();
        // Need to handle the GET Method which has no inputStream
        if ("GET".equals(request.getMethod())) {
            return null;
        }
        if (isUseReaderForPayload()) {
            // use reader to read the response body
            return request.getReader();
        } else {
            // reade the response body from servlet request
            return HttpHelper.readRequestBodyFromServletRequest(request, httpMessage.getExchange());
        }
    }

    @Override
	public boolean isUseReaderForPayload() {
        return useReaderForPayload;
    }

    @Override
	public void setUseReaderForPayload(boolean useReaderForPayload) {
        this.useReaderForPayload = useReaderForPayload;
    }

    @Override
	public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    @Override
	public void setHeaderFilterStrategy(HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }
    
    public static String replacer(StringBuffer outBuffer) {
        String data = outBuffer.toString();
        try {
           data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
           data = data.replaceAll("\\+", "%2B");
           if(!data.equals(outBuffer.toString())) {
        	   data = URLDecoder.decode(data, "utf-8");
           }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return data;
     }

}