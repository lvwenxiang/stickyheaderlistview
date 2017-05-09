package com.example.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;


import com.example.bean.DateEntity;
import com.example.extra.MyLog;

public class HttpConnectUtil {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	private static final int TIME_OUT = 10 * 1000; // ��ʱʱ��
	private static final String CHARSET = "utf-8"; // ���ñ���

	/**
	 * 
	 * ˵�� :Https
	 * 
	 * @author ���� ���ٷ�:
	 * @version ����ʱ�䣺2016��12��9�� ����1:30:35
	 * @param uri
	 * @param pairs
	 * @param method
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
//	public static HttpEntity getEntity(String uri,
//			ArrayList<NameValuePair> pairs, int method)
//			throws ClientProtocolException, IOException {
////		HttpEntity entity = null;
////		HttpParams httpParameters = new BasicHttpParams();
////		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
////		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
////		HttpClient httpclient = HttpsRequestUtil.getHttpClient(httpParameters);
////		HttpUriRequest request = null;
////		switch (method) {
////		case METHOD_GET:
////			StringBuffer sb = new StringBuffer(uri);
////			if (pairs != null && !pairs.isEmpty()) {
////				sb.append("?");
////				for (int i = 0; i < pairs.size(); i++) {
////					sb.append(pairs.get(i).getName()).append("=")
////							.append(pairs.get(i).getValue()).append("&");
////				}
////				sb.deleteCharAt(sb.length() - 1);
////			}
////			request = new HttpGet(sb.toString());
////			break;
////
////		case METHOD_POST:
////			request = new HttpPost();
////			if (pairs != null && !pairs.isEmpty()) {
////				HttpEntity reqEntity = new UrlEncodedFormEntity(pairs);
////				((HttpPost) request).setEntity(reqEntity);
////			}
////			break;
////		}
////		HttpResponse response = httpclient.execute(request);
////		entity = response.getEntity();
////		return entity;
//	}

	public static InputStream getsInputStream(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = null;
		if (entity != null) {
			in = entity.getContent();
		}
		return in;
	}

	public static long getLength(HttpEntity entity) {
		long length = -1;
		if (entity != null) {
			length = entity.getContentLength();
		}
		return length;
	}

	/**
	 * 
	 * ˵�� :Https�ϴ�ͼƬ��������
	 * 
	 * @author ���� ���ٷ�:
	 * @version ����ʱ�䣺2016��11��30�� ����4:10:58
	 * @param file
	 * @param fileKey
	 * @param RequestURL
	 * @param pairs
	 * @return
	 */
	public static String uploadFile(File file, String fileKey,
			String RequestURL, ArrayList<NameValuePair> pairs) {

		int res = 0;
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������

		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
//			sslContext.init(null,
//					new TrustManager[] { new TrustAnyTrustManager() },
//					new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
//			HttpsURLConnection
//					.setDefaultHostnameVerifier(new TrustAnyHostnameVerifier());
			HttpsURLConnection conn = (HttpsURLConnection) new URL(RequestURL)
					.openConnection();

			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null) {
				/**
				 * ���ļ���Ϊ��ʱִ���ϴ�
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb2 = new StringBuffer();
				for (int i = 0; i < pairs.size(); i++) {
					sb2.append(PREFIX);
					sb2.append(BOUNDARY);
					sb2.append(LINE_END);

					sb2.append("Content-Disposition: form-data; name=\""
							+ pairs.get(i).getName() + "\"" + LINE_END
							+ LINE_END);
					sb2.append(pairs.get(i).getValue());
					sb2.append(LINE_END);
				}
				dos.write(sb2.toString().getBytes());

				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺��
				 */

				sb.append("Content-Disposition: form-data; name=\"" + fileKey
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);

				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
				 */
				res = conn.getResponseCode();
				MyLog.println("response code:" + res);
				if (res == 200) {
					MyLog.println("request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					MyLog.println("result : " + result);
				} else {
					MyLog.println("request error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			result = new String(result.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * https����
	 */
	public static String getResult(String uri, ArrayList<NameValuePair> pairs,
			int method) throws Exception {
		String result = null;

		HttpParams params = new BasicHttpParams();
		// �������ӳ�ʱ�� Socket ��ʱ���Լ� Socket �����С
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		HttpConnectionParams.setSocketBufferSize(params, 16 * 1024);
//		HttpClient httpClient = HttpsPostThread.getHttpClient(params);

		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			StringBuffer sb = new StringBuffer(uri);
			if (pairs != null && !pairs.isEmpty()) {
				sb.append("?");
				for (int i = 0; i < pairs.size(); i++) {
					sb.append(pairs.get(i).getName()).append("=")
							.append(pairs.get(i).getValue()).append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			request.addHeader("charset", HTTP.UTF_8);
			break;
		case METHOD_POST:
			request = new HttpPost(uri);
			if (pairs != null && !pairs.isEmpty()) {
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(pairs,
						HTTP.UTF_8));
			}
			break;
		}
//		HttpResponse response = httpClient.execute(request);
//		MyLog.println("response.getStatusLine().getStatusCode()="
//				+ response.getStatusLine().getStatusCode());
//		if (response.getStatusLine().getStatusCode() == 200) {
//
//			result = EntityUtils.toString(response.getEntity());
//			MyLog.println("response.result=" + result);
//		}
//		params = null;
//		httpClient = null;
//		request = null;
//		response = null;
		return result;
	}

	/*
	 * ��Cookieͷ��Https����
	 */
	public static String getHttpPost(String url,
			List<NameValuePair> valuePairs, Context context) {
		String result = null;
		try {
			String cookie = null;
			HttpPost httpPost = new HttpPost(url);

			httpPost.addHeader("Pragma", "no-cache");
			httpPost.addHeader("Cache-Control", "no-cache");
			httpPost.addHeader("Cookie", PrefShare.getInstance(context)
					.getString("cookie"));
			MyLog.println("PrefShare.getInstance(context).getString="
					+ PrefShare.getInstance(context).getString("cookie"));

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);// ��������ʱ10��
			HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000); // ���õȴ����ݳ�ʱ10��
			HttpConnectionParams.setSocketBufferSize(httpParams, 16 * 1024);
//			HttpClient httpClient = HttpsPostThread.getHttpClient(httpParams);
//
//			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, HTTP.UTF_8));
//			HttpResponse response = httpClient.execute(httpPost);
//
//			Header header = response.getFirstHeader("Set-Cookie");
//			if (header != null) {
//				cookie = header.getValue();
//				if (PrefShare.getInstance(context).getString("cookie") == null) {
//					PrefShare.getInstance(context).saveString("cookie", cookie);
//				}
//			}
//			if (response.getStatusLine().getStatusCode() == 200) {
//				result = EntityUtils.toString(response.getEntity());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ��������Ա���������
	// {"attenceinfo":{"USER_ID":"548","KC_DDATA":"2015-04-14","KC_DAYORDER":"288","ORDERS":31},"result":"true"}

	public static String getTodayCompareYesterday(String result)
			throws Exception {
		String s = null;
		JSONObject jsonObject = new JSONObject(result);
		boolean jResult = jsonObject.getBoolean("result");
		if (!jResult)
			return null;
		String attenceinfo = jsonObject.getString("attenceinfo");
		JSONObject object = new JSONObject(attenceinfo);
		s = object.getInt("KC_DAYORDER") + "/" + object.getInt("ORDERS");
		return s;

	}

	// �ܳ�����
	// {"attenceinfo":{"USER_ID":"548","WEEK_ORDER":"275","WEEK_JIABAN":"212","WEEK_CHUQIN":"90"},"result":"true"}
	public static String getWeekRecentRanking(String result) throws Exception {
		String s = null;
		JSONObject jsonObject = new JSONObject(result);
		boolean jResult = jsonObject.getBoolean("result");
		if (!jResult)
			return null;
		String attenceinfo = jsonObject.getString("attenceinfo");
		JSONObject object = new JSONObject(attenceinfo);
		s = object.getInt("WEEK_ORDER") + "/" + object.getInt("WEEK_JIABAN")
				+ "/" + object.getInt("WEEK_CHUQIN");
		return s;

	}

	// {"attenceinfo":[{"USER_ID":"784","USER_NAME":"\u6c88\u9633","IN_DATE":"2014-07-12","WORK_BEGINTIME":"",
	// "WORK_ENDTIME":"","QINGJIA_BEGINTIME":"","QINGJIA_ENDTIME":"","QINGJIA_TYPE":"","WAICHU_BEGINTIME":"",
	// "WAICHU_ENDTIME":"","REMARK":"\u672a\u5165\u804c","WORK_JIABAN_BEGIN":"","WORK_JIABAN_END":""},
	// {"USER_ID":"784","USER_NAME":"\u6c88\u9633","IN_DATE":"2014-07-13","WORK_BEGINTIME":"",
	// "WORK_ENDTIME":"","QINGJIA_BEGINTIME":"","QINGJIA_ENDTIME":"","QINGJIA_TYPE":"","WAICHU_BEGINTIME":"",
	// "WAICHU_ENDTIME":"","REMARK":"\u672a\u5165\u804c","WORK_JIABAN_BEGIN":"","WORK_JIABAN_END":""},
	// {"USER_ID":"784","USER_NAME":"\u6c88\u9633","IN_DATE":"2014-07-14","WORK_BEGINTIME":"",
	// "WORK_ENDTIME":"","QINGJIA_BEGINTIME":"","QINGJIA_ENDTIME":"","QINGJIA_TYPE":"","WAICHU_BEGINTIME":"",
	// "WAICHU_ENDTIME":"","REMARK":"\u672a\u5165\u804c","WORK_JIABAN_BEGIN":"","WORK_JIABAN_END":""},
	// {"USER_ID":"784","USER_NAME":"\u6c88\u9633","IN_DATE":"2014-07-15","WORK_BEGINTIME":"2014-07-15 09:00:00",
	// "WORK_ENDTIME":"2014-07-15 20:12:28","QINGJIA_BEGINTIME":"","QINGJIA_ENDTIME":"","QINGJIA_TYPE":"",
	// "WAICHU_BEGINTIME":"","WAICHU_ENDTIME":"","REMARK":"\u65b0\u5165\u804c\uff0c\u672a\u5f55\u6307\u7eb9",
	// "WORK_JIABAN_BEGIN":"2014-07-15 18:00:00","WORK_JIABAN_END":"2014-07-15 20:12:28"}],"result":"true"}
	public static ArrayList<DateEntity> getDateEntities(String result)
			throws Exception {
		ArrayList<DateEntity> dateEntities = null;
		JSONObject object = new JSONObject(result);
		if (object.getBoolean("result")) {
			JSONArray array = new JSONArray(object.getString("attenceinfo"));
			dateEntities = new ArrayList<DateEntity>();
			DateEntity d = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				d = new DateEntity(Integer.parseInt(object.getString("IN_DATE")
						.split("-")[0]), Integer.parseInt(object.getString(
						"IN_DATE").split("-")[1]), Integer.parseInt(object
						.getString("IN_DATE").split("-")[2]));
				d.setDate(object.getString("IN_DATE"));
//				if (Config.getStringFromLongDate(System.currentTimeMillis())
//						.equals(object.getString("IN_DATE"))) {
//					d.setToday(true);
//				} else {
//					d.setToday(false);
//				}
//				d.setWeek(Config.getDateWeek(object.getString("IN_DATE")));
				if (object.getString("REMARK") != null
						&& !"".equals(object.getString("REMARK"))) {
					d.setMark(object.getString("REMARK"));
				}
				if (object.getString("WORK_BEGINTIME") != null
						&& !"".equals(object.getString("WORK_BEGINTIME"))) {
					if (object.getString("WORK_BEGINTIME").contains("-")) {
						d.setStartTime(object.getString("WORK_BEGINTIME")
								.split(" ")[1].substring(0, 5));
					} else {
						d.setStartTime(object.getString("WORK_BEGINTIME")
								.substring(0, 5));
					}

					if (object.getString("WORK_ENDTIME") != null
							&& !"".equals(object.getString("WORK_ENDTIME"))) {
						MyLog.println("object.getString(WORK_ENDTIME)="
								+ object.getString("WORK_ENDTIME") + "/");
						if (object.getString("WORK_ENDTIME").contains("-")) {
							d.setEndTime(object.getString("WORK_ENDTIME")
									.split(" ")[1].substring(0, 5));
						} else {
							d.setEndTime(object.getString("WORK_ENDTIME")
									.substring(0, 5));
						}
					}
				}
				if (object.getString("QINGJIA_BEGINTIME") != null
						&& !"".equals(object.getString("QINGJIA_BEGINTIME"))) {
					d.setLeaveDate(object.getString("QINGJIA_BEGINTIME").split(
							" ")[1].substring(0, 5)
							+ "-"
							+ object.getString("QINGJIA_ENDTIME").split(" ")[1]
									.substring(0, 5));
				}
				if (object.getString("WAICHU_BEGINTIME") != null
						&& !"".equals(object.getString("WAICHU_BEGINTIME"))) {
					d.setOutWorkDate(object.getString("WAICHU_BEGINTIME")
							.split(" ")[1].substring(0, 5)
							+ "-"
							+ object.getString("WAICHU_ENDTIME").split(" ")[1]
									.substring(0, 5));
				}
				if (object.getString("WORK_JIABAN_BEGIN") != null
						&& !"".equals(object.getString("WORK_JIABAN_BEGIN"))) {
					d.setOverWorkDate(object.getString("WORK_JIABAN_BEGIN")
							.split(" ")[1].substring(0, 5)
							+ "-"
							+ object.getString("WORK_JIABAN_END").split(" ")[1]
									.substring(0, 5));
				}
				// if (object.getInt("TODAY_WORK") == 0) {
				if ("0".equals(object.getString("TODAY_WORK"))) {
					d.setWork(false);
				} else {
					d.setWork(true);
				}
				if (object.getInt("WORK_LATER") == 0) {
					d.setLate(false);
				} else {
					d.setLate(true);

				}
				if (object.getInt("WORK_EARLY") == 0) {
					d.setEarly(false);
				} else {
					d.setEarly(true);

				}
				if (object.getInt("ABNORMAL") == 0) {
					d.setNormal(false);
				} else {
					d.setNormal(true);
				}
				dateEntities.add(d);
			}

			object = null;
			array = null;
			d = null;
		}
		return dateEntities;
	}
}
