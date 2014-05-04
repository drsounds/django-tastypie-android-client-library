package com.aleros.tastybean;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Hashtable;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.os.AsyncTask;

public class TastyQuery extends Hashtable<String, String> {
	private TastyEndpoint endpoint = Tasty.DefaultEndpoint;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2557382001852061257L;
	public TastyQuery(String resource) {
	}
	public TastyQuery(String resource, TastyEndpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	@SuppressWarnings("deprecation")
	public TastyResult get() throws ClientProtocolException, IOException, JSONException {
		String query = "";
		while(this.keys().hasMoreElements()) {
			String key= this.keys().nextElement();
			String value = String.valueOf(this.get(key));
			query += key + "=" + URLEncoder.encode(value) + "&";
			
		}
		TastyResult result = endpoint.get("product", query);
		return result;
	}
	@SuppressWarnings("deprecation")
	public void getInBackground(final String id, final FindCallback callback) {
		String query = "";
		while(this.keys().hasMoreElements()) {
			String key= this.keys().nextElement();
			String value = String.valueOf(this.get(key));
			query += key + "=" + URLEncoder.encode(value) + "&";
			
		}
		final String query2 = query;
		AsyncTask<String, String, TastyResult> task = new AsyncTask<String, String, TastyResult>() {

			@Override
			protected TastyResult doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					TastyResult result = endpoint.get(id, query2);
					return result;
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
			protected void onPostExecute(TastyResult result) {
				// TODO Auto-generated method stub
				callback.done(result, null);
				super.onPostExecute(result);
			}
			
		};
		task.execute();
	}
	public void whereEqualTo(String key, String val) {
		this.put(key, val);
	}
}
