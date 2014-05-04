package com.aleros.tastybean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class TastyEndpoint {
	
	public void loginAsync(final String username, final String password, final String scope, final LoginHandler loginHandler) {
		AsyncTask<String, String, TastyUser> process = new AsyncTask<String, String, TastyUser>() {

			@Override
			protected TastyUser doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				TastyAccessToken accessToken;
				try {
					accessToken = login(username, password, scope);
					if (accessToken != null) {
						return me();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(TastyUser result) {
				// TODO Auto-generated method stub
				if (loginHandler != null) {
					loginHandler.onLoggedIn(result);
				}
				super.onPostExecute(result);
			}
			
		};
		process.execute();
	}
	
	public TastyUser me() throws ClientProtocolException, IOException, JSONException {
		String result = "";
		String url = this.endpoint + "/" + this.version + "/me/?format=json&username=" + appId + "&api_key=" + appSecret + "&bearer_token=" + getAccessToken().getAccessToken();
	
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpPost);
	    
		InputStream inputStream = httpResponse.getEntity().getContent();

        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        
        TastyUser user = new TastyUser(new JSONObject(result));
        return user;
	}
	public TastyAccessToken login(String username, String password, String scope) throws ClientProtocolException, IOException {
		String result = "";
		String url = this.endpoint + "/oauth2/access_token";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		StringEntity se = new StringEntity("client_id=" + this.getAppId() + "&client_secret=" + this.getAppSecret() + "&scope=" + scope);
		httpPost.setEntity(se);

        HttpResponse httpResponse = httpClient.execute(httpPost);

        InputStream inputStream = httpResponse.getEntity().getContent();

        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        try {
        	JSONObject obj = new JSONObject(result);
        	TastyAccessToken accessToken = new TastyAccessToken(obj);
        	this.setAccessToken(accessToken);
        	return accessToken;
        } catch (Exception e) {
        	return null;
        }
	}
	private TastyAccessToken accessToken;
	private String appId, appSecret, endpoint, version;
	public String getAppId() {
		return appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public String getVersion() {
		return version;
	}
	/**
	 * Creates a new tasty endpoint
	 * @param appId
	 * @param appSecret
	 * @param endpoint
	 */
	public TastyEndpoint(String version, String appId, String appSecret, String endpoint) {
		this.appId = appId;
		this.appSecret = appSecret;
		this.endpoint = endpoint;
		this.version = version;
	}
	
	/**
	 * Made with help from {@link http://hmkcode.com/android-send-json-data-to-server/ }
	 * @param resource
	 * @param object
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public TastyObject post(String resource, TastyObject object) throws ClientProtocolException, IOException, JSONException {
		String result = "";
		String url = this.endpoint + "/" + this.version + "/" + resource + "/?format=json&username=" + appId + "&api_key=" + appSecret;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String json = object.toJSONObject().toString();
		StringEntity se = new StringEntity(json);
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        if (getAccessToken() != null) {
			httpPost.setHeader("Authorization", "OAuth " + getAccessToken().getAccessToken());
			
		} else {
			httpPost.setHeader("Authorization", "ApiKey " + appId + ":" + appSecret);
		}
        HttpResponse httpResponse = httpClient.execute(httpPost);
     // 9. receive response as inputStream
        InputStream inputStream = httpResponse.getEntity().getContent();

        // 10. convert inputstream to string
        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        
        TastyObject resultObject = new TastyObject(new JSONObject(result));
        return resultObject;
	}
	
	public TastyObject put(String resource, TastyObject object) throws ClientProtocolException, IOException, JSONException {
		String result = "";
		String url = this.endpoint + "/" + this.version + "/" + resource + "/" + object.get("id") + "/?format=json&username=" + appId + "&api_key=" + appSecret;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPost = new HttpPut(url);
		String json = object.toJSONObject().toString();
		StringEntity se = new StringEntity(json);
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        if (getAccessToken() != null) {
			httpPost.setHeader("Authorization", "OAuth " + getAccessToken().getAccessToken());
			
		}
        HttpResponse httpResponse = httpClient.execute(httpPost);
     // 9. receive response as inputStream
        InputStream inputStream = httpResponse.getEntity().getContent();

        // 10. convert inputstream to string
        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        
        TastyObject resultObject = new TastyObject(new JSONObject(result));
        return resultObject;
	}
	
	public TastyResult get(String resource, String query) throws ClientProtocolException, IOException, JSONException {
		String result = "";
		String url = this.endpoint + "/" + this.version + "/" + resource + "/?format=json&username=" + appId + "&api_key=" + appSecret + "&" + query;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		if (getAccessToken() != null) {
			httpGet.setHeader("Authorization", "OAuth " + getAccessToken().getAccessToken());
		}
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
	    
		InputStream inputStream = httpResponse.getEntity().getContent();

        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        
        TastyResult resultObject = new TastyResult(new JSONObject(result));
        return resultObject;
	}
	
	public TastyObject delete(String resource, TastyObject object) throws ClientProtocolException, IOException, JSONException {
		String result = "";
		String url = this.endpoint + "/" + this.version + "/" + resource + "/?format=json&username=" + appId + "&api_key=" + appSecret;
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpPost = new HttpDelete(url);
		HttpResponse httpResponse = httpClient.execute(httpPost);
	    InputStream inputStream = httpResponse.getEntity().getContent();

        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        
        TastyObject resultObject = new TastyObject(new JSONObject(result));
        return resultObject;
	}
	
	/**
	 * @from http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
	 * @param is
	 * @return
	 */
	private static String convertInputStreamToString(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	public TastyAccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(TastyAccessToken accessToken) {
		this.accessToken = accessToken;
	}
}
