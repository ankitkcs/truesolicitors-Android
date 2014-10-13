package com.example.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class HttpClient {
	/********** #region Variables **********/
	private final static String TAG_1 = "HttpClient";
	public static final String TAG = "WebCalls: ";

	private static String responses;

	/********** #endregion Variables **********/

	/**
	 * 
	 * SendHttpPost
	 * 
	 * @param URL
	 * @param jsonObjSend
	 * @return String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String SendHttpPost(String URL, JSONObject jsonObjSend)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPostRequest = new HttpPost(URL);

		// Set HTTP parameters
		/*
		 * StringEntity se; se = new StringEntity(jsonObjSend.toString());
		 */
		jsonObjSend.length();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				jsonObjSend.length());
		nameValuePairs.add(new BasicNameValuePair("json", jsonObjSend
				.toString()));
		// Log.i("jsonObjSend.toString()","jsonObjSend.toString()"+jsonObjSend.toString());

		Log.i(TAG, "URL: " + URL);
		Log.i(TAG, "Request: " + jsonObjSend.toString());
		UrlEncodedFormEntity en = new UrlEncodedFormEntity(nameValuePairs);
		en.getContent();
		httpPostRequest
				.setHeader("accept", "application/vnd.truesolicitors.v1");
		httpPostRequest.getParams().setParameter("http.socket.timeout",
				new Integer(600000));

		httpPostRequest.setEntity(en);
		long t = System.currentTimeMillis();
		HttpResponse response = (HttpResponse) httpclient
				.execute(httpPostRequest);
		Log.i(TAG_1, "HTTPResponse received in ["
				+ (System.currentTimeMillis() - t) + "ms]");
		Log.i(TAG_1, httpPostRequest.getRequestLine().getProtocolVersion()
				.toString());
		responses = EntityUtils.toString(response.getEntity());
		// responses = convertEntityToString(response.getEntity(), "UTF-8");
		Log.i(TAG, "Responce: " + responses);
		Log.i("HTTPPOST", "******************");
		// Log.i("Encoding",response.getEntity().getContentEncoding().getName());

		return responses;
	}

	/**
	 * 
	 * convertEntityToString
	 * 
	 * @param entity
	 * @param defaultCharset
	 * @return String
	 */
	public static String convertEntityToString(HttpEntity entity,
			String defaultCharset) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 * 
		 * (c) public domain:
		 * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/
		 * 11/a-simple-restful-client-at-android/
		 */

		String StringResponse = "";

		String line = null;
		try {
			StringResponse = EntityUtils.toString(entity, HTTP.UTF_8);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return StringResponse;
	}

	// Passing Namevalue pair
	/**
	 * 
	 * SendHttpPost
	 * 
	 * @param URL
	 * @param jsonObjSend
	 * @return String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String SendHttpPost(String URL, String token,
			List<NameValuePair> nameValuePairs) throws ClientProtocolException,
			IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPostRequest = new HttpPost(URL);

		// Set HTTP parameters
		/*
		 * StringEntity se; se = new StringEntity(jsonObjSend.toString());
		 */

		// Log.i("jsonObjSend.toString()","jsonObjSend.toString()"+jsonObjSend.toString());

		Log.i(TAG, "URL: " + URL);

		httpPostRequest
				.setHeader("accept", "application/vnd.truesolicitors.v1");
		httpPostRequest.getParams().setParameter("http.socket.timeout",
				new Integer(600000));

		String base64EncodedCredentials = "Basic "
				+ Base64.encodeToString(
						(CommonVariable.AUTH_USERNAME + ":" + token).getBytes(),
						Base64.NO_WRAP);
		httpPostRequest.setHeader("Authorization", base64EncodedCredentials);
		httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		long t = System.currentTimeMillis();
		HttpResponse response = (HttpResponse) httpclient
				.execute(httpPostRequest);
		Log.i(TAG_1, "HTTPResponse received in ["
				+ (System.currentTimeMillis() - t) + "ms]");
		Log.i(TAG_1, httpPostRequest.getRequestLine().getProtocolVersion()
				.toString());
		responses = EntityUtils.toString(response.getEntity());
		// responses = convertEntityToString(response.getEntity(), "UTF-8");
		Log.i(TAG, "Responce: " + responses);
		Log.i("HTTPPOST", "******************");
		// Log.i("Encoding",response.getEntity().getContentEncoding().getName());

		return responses;
	}

	// Passing Namevalue pair
	/**
	 * 
	 * SendHttpPut
	 * 
	 * @param URL
	 * @param jsonObjSend
	 * @return String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String SendHttpPUT(String URL, String token,
			List<NameValuePair> nameValuePairs) throws ClientProtocolException,
			IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPut httpPostRequest = new HttpPut(URL);

		// Set HTTP parameters
		/*
		 * StringEntity se; se = new StringEntity(jsonObjSend.toString());
		 */

		// Log.i("jsonObjSend.toString()","jsonObjSend.toString()"+jsonObjSend.toString());

		Log.i(TAG, "URL: " + URL);

		httpPostRequest
				.setHeader("accept", "application/vnd.truesolicitors.v1");
		httpPostRequest.getParams().setParameter("http.socket.timeout",
				new Integer(600000));

		String base64EncodedCredentials = "Basic "
				+ Base64.encodeToString(
						(CommonVariable.AUTH_USERNAME + ":" + token).getBytes(),
						Base64.NO_WRAP);
		httpPostRequest.setHeader("Authorization", base64EncodedCredentials);
		httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		long t = System.currentTimeMillis();
		HttpResponse response = (HttpResponse) httpclient
				.execute(httpPostRequest);
		Log.i(TAG_1, "HTTPResponse received in ["
				+ (System.currentTimeMillis() - t) + "ms]");
		Log.i(TAG_1, httpPostRequest.getRequestLine().getProtocolVersion()
				.toString());
		responses = EntityUtils.toString(response.getEntity());
		// responses = convertEntityToString(response.getEntity(), "UTF-8");
		Log.i(TAG, "Responce: " + responses);
		Log.i("HTTPPOST", "******************");
		// Log.i("Encoding",response.getEntity().getContentEncoding().getName());

		return responses;
	}

	public static String getHttpResponse(String URL,
			List<NameValuePair> nameValuePairs, String token)
			throws ClientProtocolException, IOException, ParseException {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(URL);
		Log.i(TAG, "URL: " + URL);
		String base64EncodedCredentials = "Basic "
				+ Base64.encodeToString(
						(CommonVariable.AUTH_USERNAME + ":" + token).getBytes(),
						Base64.NO_WRAP);
		httpGet.setHeader("Authorization", base64EncodedCredentials);

		httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");

		long t = System.currentTimeMillis();
		HttpResponse response = (HttpResponse) httpclient.execute(httpGet);
		Log.i(TAG_1, "HTTPResponse received in ["
				+ (System.currentTimeMillis() - t) + "ms]");

		responses = EntityUtils.toString(response.getEntity());
		// responses = convertEntityToString(response.getEntity(), "UTF-8");
		Log.i(TAG, "Responce: " + responses);
		Log.i("HTTPPOST", "******************");
		// Log.i("Encoding",response.getEntity().getContentEncoding().getName());

		return responses;
	}

}
