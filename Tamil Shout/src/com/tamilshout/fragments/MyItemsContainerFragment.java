package com.tamilshout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamilshout.R;

public class MyItemsContainerFragment extends BaseContainerFragment {
	
	private boolean mIsViewInited;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("test", "tab 2 oncreateview");
		
		
		return inflater.inflate(R.layout.container_fragment, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("test", "tab 2 container oncreate");
//		if (!mIsViewInited) {
//			mIsViewInited = true;
//			initView();
//		}
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 2 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}
	}
	
	private void initView() {
		Log.e("test", "tab 2 init view");
		replaceFragment(new MyItemsFragment(), false);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("------------- MyItemsContainerFragment------------------");
	//	super.onActivityResult(requestCode, resultCode, data);
		
		Fragment frag = getChildFragmentManager().findFragmentByTag("My Items");
		if (frag != null)
			frag.onActivityResult(requestCode, resultCode, data);
	}
	
}