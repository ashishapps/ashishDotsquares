package com.tamilshout.utils;

import java.io.IOException;

import com.tamilshout.model.UserBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPrefrence {
	private static SharedPreferences appPref;
	private static Editor edit;
	private static String PROFILE = "profile";

	private static boolean remember;
	private static boolean login;

	public static void init(Context ctx) {
		// TODO Auto-generated constructor stub
		appPref = ctx.getSharedPreferences("appPref", 0);
	}

	public static UserBean getProfile(Context ctx) {

		init(ctx);

		String prof = appPref.getString(PROFILE, "");

		UserBean pf = null;

		try {
			pf = (UserBean) ObjectSerializer.deserialize(prof);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pf;

	}

	public static void setProfile(Context ctx, UserBean pf) {

		String prof = "";
		try {
			prof = ObjectSerializer.serialize(pf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		init(ctx);
		edit = appPref.edit();
		edit.putString(PROFILE, prof);
		edit.commit();
	}

	public static void resetPrefs(Context ctx) {
		init(ctx);
		edit = appPref.edit();
		edit.clear();
		edit.commit();

	}

	
	/**
	 * @return the remember
	 */
	public static boolean isRemember(Context ctx) {
		init(ctx);

		return appPref.getBoolean("IsRemember", false);
	}

	/**
	 * @param remember
	 *            the remember to set
	 */
	public static void setRemember(Context ctx, boolean remember) {
		
		init(ctx);
		edit = appPref.edit();
		edit.putBoolean("IsRemember", remember);
		edit.commit();

	}

	/**
	 * @return the login
	 */
	public static boolean isLogin(Context ctx) {
		init(ctx);

		return appPref.getBoolean("isLogin", false);
	}

	/**
	 * @param login the login to set
	 */
	public static void setLogin(Context ctx,boolean login) {
		init(ctx);
		edit = appPref.edit();
		edit.putBoolean("isLogin", login);
		edit.commit();
		
	}

}
