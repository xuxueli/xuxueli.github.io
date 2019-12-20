### HttpClient简介
HttpClient 是 Apache Jakarta Common 下的子项目，可以用来提供高效的、最新的、功能丰富的支持 HTTP 协议的客户端编程工具包，并且它支持 HTTP 协议最新的版本和建议。

httpclient项目从commons子项目挪到了HttpComponents子项目下，httpclient3.1和httpcilent4.0无法做到代码向后兼容
httpclient官网（HttpComponents）：http://hc.apache.org/


### maven依赖
```
<!-- httpcomponents子项目（新版本4）：httpclient-4.3.6/httpcore-4.3.3/commons-codec-1.6/commons-logging-1.1.3 -->
<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpclient</artifactId>
	<version>4.3.6</version>
</dependency>
```

### httpclient工具类
```
package com.xxl.util.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http util to send data
 * 
 * @author xuxueli
 * @version 2015-11-28 15:30:59
 */
public class HttpClientUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * http post request
	 * @param url
	 * @param params
	 * @return	[0]=responseMsg, [1]=exceptionMsg
	 */
	public static String post(String url, Map<String, String> params){
		HttpPost httpPost = null;
		CloseableHttpClient httpClient = null;
		try{
			// httpPost config
			httpPost = new HttpPost(url);
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,String> entry : params.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			httpPost.setConfig(requestConfig);
			
			// httpClient = HttpClients.createDefault();	// default retry 3 times
			// httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
			httpClient = HttpClients.custom().disableAutomaticRetries().build();
			
			// parse response
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String responseMsg = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return responseMsg;
				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			/*StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			callback.setMsg(out.toString());*/
			return e.getMessage();
		} finally{
			if (httpPost!=null) {
				httpPost.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * http get request
	 * @param url
	 * @param queryString
	 * @return
	 */
	public static String get(String url, String queryString){
		// fill params
		if (queryString != null && queryString.trim().length()>0) {
			url = url + "?" + queryString;
		}

		// httpGet config
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);

		CloseableHttpClient httpClient = null;
		try{
			// httpClient = HttpClients.createDefault();	// default retry 3 times
			// httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// parse response
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String responseMsg = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return responseMsg;
				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			/*StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			callback.setMsg(out.toString());*/
			return e.getMessage();
		} finally{
			if (httpGet!=null) {
				httpGet.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(get("https://www.hao123.com/", null));
	}
	
}
```




