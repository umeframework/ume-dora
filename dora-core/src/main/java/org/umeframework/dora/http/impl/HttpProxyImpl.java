/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.http.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.exception.DataAccessException;
import org.umeframework.dora.http.HttpParam;
import org.umeframework.dora.http.HttpProcessProgress;
import org.umeframework.dora.http.HttpProxy;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.util.StringUtil;

/**
 * HttpProxy2Impl
 *
 * @author Yue MA
 */
public class HttpProxyImpl extends BaseComponent implements HttpProxy {
	/**
	 * split char of URL
	 */
	private static final String URL_SPLIT_CHAR = "/";
	/**
	 * parameter split char of URL
	 */
	private static final String URL_PARAM_SPLIT_CHAR = "?";
	/**
	 * parameter concat char of URL
	 */
	private static final String URL_PARAM_CONCAT_CHAR = "&";
	/**
	 * equal string
	 */
	private static final String EQUAL_STR = "=";
	/**
	 * application content type
	 */
	private String appContentType;
	/**
	 * string entity content type
	 */
	private String entityContentType;
	/**
	 * char set of entity data
	 */
	private String entityCharset;
	/**
	 * char set of entity data
	 */
	private String paramCharset;
	/**
	 * httpClient
	 */
	private HttpClient httpClient;

	/**
	 * use HTTPs connection
	 */
	private boolean useSSL;

	/**
	 * getHttpClient
	 */
	protected HttpClient getHttpClient() {
		if (httpClient == null) {
			if (useSSL) {
				try {
					SSLContext sslcontext = SSLContext.getInstance("TLS");
					X509TrustManager tm = new X509TrustManager() {
						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						        throws java.security.cert.CertificateException {
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						        throws java.security.cert.CertificateException {
						}

						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}
					};
					sslcontext.init(null, new TrustManager[] { tm }, null);
					@SuppressWarnings("deprecation")
					SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
					Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create().register("https", ssf).build();
					HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
					httpClient = HttpClients.custom().setConnectionManager(cm).build();
				} catch (Exception e) {
					throw new ApplicationException(e, "Failed to create SSL HttpClient instance.");
				}
			} else {
				httpClient = HttpClientBuilder.create().build();
			}
		}
		return httpClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#doPost(java.lang.String, java.lang.String)
	 */
	@Override
	public String doPost(String url, String msg) {
		HttpParam param = new HttpParam(msg);
		return doPost(url, param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#doPost(java.lang.String,
	 * org.umeframework.dora.http.HttpParam)
	 */
	@Override
	public String doPost(String url, HttpParam param) {
		String resStr = null;
		HttpPost httpPost = null;
		Map<String, String> headers = param.getHeaders();
		Map<String, String> parameters = param.getParameters();
		String bodyMessage = param.getMessage();
		try {

			// create parameter as URL parameters format
			StringBuilder paramStr = new StringBuilder();
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				String key = e.getKey();
				String value = e.getValue();
				if (paramCharset != null) {
					value = encode(value, paramCharset);
				}
				paramStr.append(URL_PARAM_CONCAT_CHAR);
				paramStr.append(key);
				paramStr.append(EQUAL_STR);
				paramStr.append(value);
			}
			if (!url.contains(URL_PARAM_SPLIT_CHAR) && paramStr.length() > 0) {
				url = url + URL_PARAM_SPLIT_CHAR;
			}
			// concat parameter string into url
			url = url + paramStr;

			httpPost = new HttpPost(url);
			if (appContentType != null) {
				httpPost.addHeader(HTTP.CONTENT_TYPE, appContentType);
			}
			for (Map.Entry<String, String> e : headers.entrySet()) {
				httpPost.addHeader(e.getKey(), e.getValue());
			}

			// create HTTP string entity object
			if (bodyMessage != null) {
				StringEntity strEntity = null;
				if (entityCharset != null) {
					strEntity = new StringEntity(bodyMessage, entityCharset);
				} else {
					strEntity = new StringEntity(bodyMessage);
				}
				if (entityContentType != null) {
					strEntity.setContentType(entityContentType);
				}
				httpPost.setEntity(strEntity);
			}

			// log the post message (parameter format is encode before)
			getLogger().info("Start Http Post:", url, param, entityCharset, appContentType, entityContentType);

			HttpResponse res = getHttpClient().execute(httpPost);
			param.setResponse(res);
			int statusCode = res.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				resStr = EntityUtils.toString(entity, entityCharset);
				if (StringUtil.isEmpty(resStr)) {
					getLogger().warn("No data received from post request.");
				}
				httpPost.completed();
			}

			getLogger().info("End Http Post:", url, resStr);
		} catch (Exception e) {
			if (httpPost != null) {
				httpPost.abort();
			}
			throw new DataAccessException(e, "Http Post failed:" + url);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}

		}
		return resStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#doGet(java.lang.String, java.lang.String)
	 */
	@Override
	public String doGet(String url) {
		return doGet(url, new HttpParam());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#doGet(java.lang.String, java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public String doGet(String url, HttpParam param) {
		String resStr = null;
		HttpGet httpGet = null;
		Map<String, String> headers = param.getHeaders();
		Map<String, String> parameters = param.getParameters();
		String bodyMessage = param.getMessage();
		try {
			// create parameter as URL parameters format
			StringBuilder paramStr = new StringBuilder();
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				String key = e.getKey();
				String value = e.getValue();
				if (paramCharset != null) {
					value = encode(value, paramCharset);
				}
				paramStr.append(URL_PARAM_CONCAT_CHAR);
				paramStr.append(key);
				paramStr.append(EQUAL_STR);
				paramStr.append(value);
			}
			if (!url.contains(URL_PARAM_SPLIT_CHAR) && paramStr.length() > 0) {
				url = url + URL_PARAM_SPLIT_CHAR + paramStr.substring(1);
			} else {
				url = url + paramStr;
			}

			if (bodyMessage != null) {
				if (entityCharset != null) {
					bodyMessage = encode(bodyMessage, entityCharset);
				}
				url = url.endsWith(URL_SPLIT_CHAR) ? url : url + URL_SPLIT_CHAR;
				url = url + bodyMessage;
			}

			httpGet = new HttpGet(url);
			for (Map.Entry<String, String> e : headers.entrySet()) {
				httpGet.addHeader(e.getKey(), e.getValue());
			}

			getLogger().info("Start Http Get:", url, param.toString(), entityCharset, appContentType, entityContentType);

			HttpResponse res = getHttpClient().execute(httpGet);
			int statusCode = res.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				resStr = EntityUtils.toString(entity, entityCharset);
				httpGet.completed();
			}
			getLogger().info("End Http Get:", url, resStr);

		} catch (Exception e) {
			if (httpGet != null) {
				httpGet.abort();
			}
			throw new DataAccessException(e, "Http Get failed:" + url);
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return resStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#doDownload(java.lang.String,
	 * java.lang.String, java.util.Map, org.umeframework.dora.http.HttpProcessProgress)
	 */
	@Override
	public long doDownload(String url, String saveToPath, Map<String, String> headParams, HttpProcessProgress downloadProgress) {
		long contentLength = -1;
		CloseableHttpClient cloHttpclient = null;
		try {
			cloHttpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			if (headParams != null) {
				for (String key : headParams.keySet()) {
					httpGet.addHeader(key, headParams.get(key));
				}
			}
			CloseableHttpResponse response = cloHttpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			contentLength = entity.getContentLength();
			InputStream is = entity.getContent();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int size = 0;
			long totalRead = 0;
			while ((size = is.read(buffer)) > 0) {
				bos.write(buffer, 0, size);
				totalRead += size;
				if (downloadProgress != null) {
					downloadProgress.onProgress((int) (totalRead * 100 / contentLength));
				}
			}
			FileOutputStream fos = new FileOutputStream(saveToPath);
			bos.writeTo(fos);
			bos.flush();
			bos.close();
			fos.close();
			EntityUtils.consume(entity);
			response.close();
		} catch (Exception e) {
			throw new DataAccessException(e, "Http Download failed:" + url);
		} finally {
			if (cloHttpclient != null) {
				try {
					cloHttpclient.close();
				} catch (IOException e) {
					throw new DataAccessException(e, "CloseableHttpClient close error ");
				}
			}
		}
		return contentLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.HttpProxy#uploadFileImpl(java.lang.String,
	 * java.lang.String, java.lang.String, java.util.Map)
	 */
	public String doUpload(String url, String localFile, String serverFieldName, Map<String, String> params) {
		String resStr = null;
		CloseableHttpClient cloHttpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			FileBody binFileBody = new FileBody(new File(localFile));
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.addPart(serverFieldName, binFileBody);
			if (params != null) {
				for (String key : params.keySet()) {
					multipartEntityBuilder.addPart(key, new StringBody(params.get(key), ContentType.TEXT_PLAIN));
				}
			}
			HttpEntity reqEntity = multipartEntityBuilder.build();
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = cloHttpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			resStr = EntityUtils.toString(entity, entityCharset);
			httpPost.completed();
		} catch (Exception e) {
			throw new DataAccessException(e, "Http Upload failed:" + url);
		} finally {
			if (cloHttpclient != null) {
				try {
					cloHttpclient.close();
				} catch (IOException e) {
					throw new DataAccessException(e, "CloseableHttpClient close error ");
				}
			}
		}
		return resStr;
	}

	/**
	 * encode URL message
	 *
	 * @param msg
	 * @param charset
	 * @return
	 */
	protected String encode(String msg, String charset) {
		try {
			msg = URLEncoder.encode(msg, charset);
		} catch (Exception e) {
			throw new ApplicationException(e, "URL encode error:" + msg);
		}
		return msg;
	}

	/**
	 * @return the appContentType
	 */
	@Override
	public String getAppContentType() {
		return appContentType;
	}

	/**
	 * @param appContentType
	 *            the appContentType to set
	 */
	@Override
	public void setAppContentType(String appContentType) {
		this.appContentType = appContentType;
	}

	/**
	 * @return the entityContentType
	 */
	@Override
	public String getEntityContentType() {
		return entityContentType;
	}

	/**
	 * @param entityContentType
	 *            the entityContentType to set
	 */
	@Override
	public void setEntityContentType(String entityContentType) {
		this.entityContentType = entityContentType;
	}

	/**
	 * @return the entityCharset
	 */
	@Override
	public String getEntityCharset() {
		return entityCharset;
	}

	/**
	 * @param entityCharset
	 *            the entityCharset to set
	 */
	@Override
	public void setEntityCharset(String entityCharset) {
		this.entityCharset = entityCharset;
	}

	/**
	 * @return the paramCharset
	 */
	@Override
	public String getParamCharset() {
		return paramCharset;
	}

	/**
	 * @param paramCharset
	 *            the paramCharset to set
	 */
	@Override
	public void setParamCharset(String paramCharset) {
		this.paramCharset = paramCharset;
	}

	/**
	 * @return the useSSL
	 */
	@Override
	public boolean isUseSSL() {
		return useSSL;
	}

	/**
	 * @param useSSL
	 *            the useSSL to set
	 */
	@Override
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
}
