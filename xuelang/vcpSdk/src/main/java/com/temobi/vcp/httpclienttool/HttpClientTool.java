package com.temobi.vcp.httpclienttool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.temobi.vcp.protocal.MVPCommand;

import android.util.Log;

public class HttpClientTool {
	private HttpParams httpParams;
	private HttpClient httpClient;

	public String doPost(String url, String postData, String sessionId,
			String x_ipcsn, String cameraId, String operId) {
		/* 建立HTTPPost对象 */
		HttpPost httpRequest = new HttpPost(url);
		String strResult = "doPostError";
		try {
			/* 添加请求参数到请求对象 */

			httpRequest.setHeader(MVPCommand.SESSIONID_FIELD, sessionId);
			httpRequest.setHeader(MVPCommand.IPCSN_FIELD, x_ipcsn);
			httpRequest.setHeader(MVPCommand.CAMERA_ID, cameraId);
			httpRequest.setHeader(MVPCommand.OPERID, operId);

			HttpEntity entity = new StringEntity(postData, "UTF-8");
			// httpRequest.setEntity(new UrlEncodedFormEntity(postData,
			// HTTP.UTF_8));
			httpRequest.setEntity(entity);

			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			if (e != null) {

				if (e.getMessage() != null) {
					strResult = e.getMessage().toString();
					e.printStackTrace();
				}

			}
			strResult = "error";

		} catch (Exception e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		}
		Log.v("strResult", strResult);

		return strResult;
	}

	public HttpClient getHttpClient() {
		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
		this.httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 创建一个 HttpClient 实例
		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}
}
