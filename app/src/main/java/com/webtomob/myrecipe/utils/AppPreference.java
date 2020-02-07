package com.webtomob.myrecipe.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreference {
	private SharedPreferences prefs;
	private static String PREFS_FILE;
	private static AppPreference prefManager;
	
	
	private AppPreference(Context context) {
		PREFS_FILE = context.getPackageName();
		prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
	}
	
	public static AppPreference getInstance(Context context) {
		if(prefManager == null) {
			prefManager = new AppPreference(context);
		}
		return prefManager;
	}
	
	public void clear(){
		if(prefs != null) {
			prefs.edit().clear().commit();
		}
	}
	
	public boolean contains(String key){
		return prefs.contains(key);
	}
	
	public void remove(String key) {
		if(prefs != null) {
			prefs.edit().remove(key).commit();
		}
	}
	
	public void putInt(String key, int value) {
		if(prefs != null) {
			prefs.edit().putInt(key, value).commit();
		}
	}
	
	public int getInt(String key, int... defValue) {
		int value = defValue.length > 0 ? defValue[0] : 0;
		if(prefs != null) {
			return prefs.getInt(key, value);
		}
		return value;
	}
	
	public void putFloat(String key, float value) {
		if(prefs != null) {
			prefs.edit().putFloat(key, value).commit();
		}
	}
	
	public float getFloat(String key, float... defValue) {
		float value = defValue.length > 0 ? defValue[0] : 0;
		if(prefs != null) {
			return prefs.getFloat(key, value);
		}
		return value;
	}
	
	public void putLong(String key, long value) {
		if(prefs != null) {
			prefs.edit().putLong(key, value).commit();
		}
	}
	
	public long getLong(String key, long... defValue) {
		long value = defValue.length > 0 ? defValue[0] : 0;
		if(prefs != null) {
			return prefs.getLong(key, value);
		}
		return value;
	}
	
	public void putString(String key, String value) {
		if(prefs != null) {
			prefs.edit().putString(key, value).commit();
		}
	}
	
	public String getString(String key, String... defValue) {
		String value = defValue.length > 0 ? defValue[0] : null;
		if(prefs != null) {
			return prefs.getString(key, value);
		}
		return value;
	}
	
	public void putBoolean(String key, boolean value) {
		if(prefs != null) {
			prefs.edit().putBoolean(key, value).commit();
		}
	}
	
	public boolean getBoolean(String key, boolean... defValue) {
		boolean value = defValue.length > 0 ? defValue[0] : false;
		if(prefs != null) {
			return prefs.getBoolean(key, value);
		}
		return value;
	}
}
