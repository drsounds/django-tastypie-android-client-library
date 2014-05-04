package com.aleros.tastybean;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

public class TastyUser extends TastyObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TastyUser(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}
	public TastyUser(Parcel parcel) {
		super(parcel);
		// TODO Auto-generated constructor stub
	}

}
