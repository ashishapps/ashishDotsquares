package com.tamilshout.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.tamilshout.R;
import com.tamilshout.utils.TouchEffect;

public class BaseFragment extends Fragment implements OnClickListener {
	public static final TouchEffect TOUCH = new TouchEffect();

	SharedPreferences sp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		//new Upgrade(getActivity(), true);
		super.onStart();
	}
	
	public void setHeader(View v, String title) {
		TextView txt = (TextView) v.findViewById(R.id.txtTitle);
		txt.setText("" + title);
	}

	public Button setRightButton(View v,int iconId) {

		Button btnRight=(Button) v.findViewById(R.id.btnTopRight);
		/*TextView topRightText=(TextView) v.findViewById(R.id.txtTopRight);*/
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setBackgroundResource(iconId);
		/*topRightText.setVisibility(View.VISIBLE);
		topRightText.setText(text);*/
		setTouchNClick(v, R.id.btnTopRight);
		
		return btnRight;
	}
	public Button setLeftButton(View v,int iconId) {

		Button btnLeft=(Button) v.findViewById(R.id.btnTopLeft);
		//TextView topLeftText=(TextView) v.findViewById(R.id.txtTopLetf);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setBackgroundResource(iconId);
		//topLeftText.setVisibility(View.VISIBLE);
		//topLeftText.setText(text);
		setTouchNClick(v, R.id.btnTopLeft);
		
		return btnLeft;
	}

	public View setTouchNClick(View v, int id) {

		View vv = v.findViewById(id);
		vv.setOnClickListener(this);
		vv.setOnTouchListener(TOUCH);
		return vv;
	}

	/*public void showUpgradeDialog(){
		
		if(!AppPrefrence.isUpgraded(getActivity())){
			Utils.showDialog(getActivity(), "Upgrade", getResources().getString(R.string.textUpgrade)
					, "Upgrade", "Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Upgrade(getActivity(), false);
						}
					}, null);
		}
	}
	*/
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("Base Fragment", "Base Fragment");
	}
	
	
	
}
