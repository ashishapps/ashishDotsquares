package com.tamilshout.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.tamilshout.model.ItemPictureBean;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Environment;

public class Constant {

	public static Context appContext;
	public static String SELECTED_COUNTRY_NAME=null;
	public static String COUNTRY_CODE=null;
	public static String CURRENT_COUNTRY=null;
	public static Location location=null;
	public static Address address=null;
	public static double LAT=0.0;
	public static double LONG=0.0;
	public static int SELECTED_FILTER=0;
	public static int SELECTED_CATEGORYID=0;
	public static int SELECTED_OFFER_FILTER=0;
	public static int SELECTED_MY_ITEMS_FILTER=0;
	public static final String IMAGES="images";
	public static ArrayList<String> imagePathList=null;
	public static TreeMap<Integer, String> imagePathHashMap=null ;
	public static TreeMap<Integer, String> newAddedImagePath=null ;
	public static ArrayList<ItemPictureBean> itemImageList ;

	
	
	
	public static final File CACHE_DIR;
	public static final File ROOT_DIR;
	public static final File WEB_DIR;
	
	
	

		
		// supported file formats
		public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg","png");


	static {

		ROOT_DIR = new File(Environment.getExternalStorageDirectory(), "Directory");
		if (!ROOT_DIR.exists()) {
			ROOT_DIR.mkdirs();
			Utils.createNoMediaFile(ROOT_DIR);
		}
		WEB_DIR = new File(ROOT_DIR, "web");
		if (!WEB_DIR.exists())
			WEB_DIR.mkdirs();

		CACHE_DIR = new File(ROOT_DIR, "Cache");
		if (!CACHE_DIR.exists())
			CACHE_DIR.mkdirs();

	}
}
