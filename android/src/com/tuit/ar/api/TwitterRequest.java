package com.tuit.ar.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.Handler;

import com.tuit.ar.api.request.Options;


public class TwitterRequest {
	static public enum Method { GET, POST };
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			finishedRequest();
		}
	};
	private Handler handler = new Handler();
	private Options url = null;
	private int statusCode;
	private String response;
	private final TwitterAccount account;

	public Options getUrl() { return url; }
	public void setUrl(Options url) { this.url = url; } 
	public int getStatusCode() { return statusCode; }
	public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
	public String getResponse() { return response; }
	public void setResponse(String response) { this.response = response; }

	private void finishedRequest() {
		Twitter.getInstance().getDefaultAccount().finishedRequest(this);
	}

	public TwitterRequest(TwitterAccount _account, final Options url, final ArrayList <NameValuePair> nvps,
			final Method method) throws Exception {
		account = _account;
		setUrl(url);
		(new Thread() {
			public void run() {
				DefaultHttpClient http = new DefaultHttpClient();

				String full_url = "http" + (Twitter.isSecure ? "s" :"") + "://" + Twitter.BASE_URL + url.toString() + ".json";
				HttpRequestBase request;
				if (method == Method.POST) {
					request = new HttpPost(full_url);
					try {
						((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				else {
					String queryString = "";
					if (nvps != null && nvps.size() > 0) {
						try {
							queryString += "?" + (new UrlEncodedFormEntity(nvps, HTTP.UTF_8)).toString();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					request = new HttpGet(full_url + queryString);
				}

			    final HttpParams params = new BasicHttpParams();
			    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			    HttpProtocolParams.setContentCharset(params, "UTF_8");
			    HttpProtocolParams.setUseExpectContinue(params, false);
				http.setParams(params);	

				try {
					account.getConsumer().sign(request);
					HttpResponse response = http.execute(request);
					HttpEntity resEntity = response.getEntity();
					setStatusCode(response.getStatusLine().getStatusCode());
					
					//if (resEntity != null && statusCode >= 200 && statusCode < 300)
					{
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						resEntity.writeTo(output);
						byte[] bytes = output.toByteArray();
						setResponse(new String(bytes));
					}
				} catch (OAuthMessageSignerException e1) {
					e1.printStackTrace();
				} catch (OAuthExpectationFailedException e1) {
					e1.printStackTrace();
				} catch (OAuthCommunicationException e1) {
					e1.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				handler.post(runnable);
			}
		}).start();
	}
}
