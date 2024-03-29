package com.tamilshout;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.tamilshout.R;
import com.tamilshout.fragments.BaseContainerFragment;
import com.tamilshout.fragments.FavouriteContainerFragment;
import com.tamilshout.fragments.HomeContainerFragment;
import com.tamilshout.fragments.ItemsforSaleContainerFragment;
import com.tamilshout.fragments.MyItemsContainerFragment;
import com.tamilshout.fragments.ProfileContainerFragment;
import com.tamilshout.fragments.SettingsContainerFragment;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.TouchEffect;
import com.tamilshout.utils.Utils;

public class HomeActivity extends FragmentActivity implements
		OnTabChangeListener {

	public static final TouchEffect TOUCH = new TouchEffect();

	private static final String TAB_1_TAG = "Home";
	private static final String TAB_2_TAG = "My Items";
	private static final String TAB_3_TAG = "Items for Sale";
	private static final String TAB_4_TAG = "Favourite";
	public static final String TAB_5_TAG = "My Profile";
	private static final String TAB_6_TAG = "Settings";
	public static final String TAB_7_TAG = "Home_Fake";

	public static FragmentTabHost mTabHost;

	private JSONObject jsonObject = new JSONObject();
	private String response = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();

		// Thread.setDefaultUncaughtExceptionHandler(new
		// ExceptionHandler(this));

		System.out.println("current country"
				+ HomeActivity.this.getResources().getConfiguration().locale
						.getDisplayCountry());

	}

	/* method to add tabs at starting */
	private void initView() {

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		/*
		 * mTabHost.addTab(mTabHost.newTabSpec(TAB_7_TAG).setIndicator(addTab(R.
		 * drawable.homeselector, "Home")),HomeContainerFragment.class, null);
		 * mTabHost.getTabWidget().getChildAt(6).setVisibility(View.GONE);
		 */
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_1_TAG).setIndicator(
						addTab(R.drawable.homeselector, "Home")),
				HomeContainerFragment.class, null);

		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_2_TAG).setIndicator(
						addTab(R.drawable.myitemsselector, "My Items")),
				MyItemsContainerFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_3_TAG).setIndicator(
						addTab(R.drawable.itemsforsaleselector,
								"Items for Sale")),
				ItemsforSaleContainerFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_4_TAG).setIndicator(
						addTab(R.drawable.favouriteselector, "Favourites")),
				FavouriteContainerFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_5_TAG).setIndicator(
						addTab(R.drawable.myprofileselector, "My Profile")),
				ProfileContainerFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(TAB_6_TAG).setIndicator(
						addTab(R.drawable.settingselector, "Settings")),
				SettingsContainerFragment.class, null);

	}

	/* method to create view and set on tabhonst indicator */
	private View addTab(int drawableId, String text) {

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, mTabHost.getTabWidget(), false);
		// ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		TextView icon = (TextView) tabIndicator.findViewById(R.id.tabText);
		icon.setText(text);
		icon.setCompoundDrawablesWithIntrinsicBounds(0,drawableId, 0, 0);
		

		// icon.setImageResource(drawableId);

		return tabIndicator;
	}

	@Override
	public void onBackPressed() {
		boolean isPopFragment = false;
		String currentTabTag = mTabHost.getCurrentTabTag();
		if (currentTabTag.equals(TAB_1_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_1_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_2_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_2_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_3_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_3_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_4_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_4_TAG)).popFragment();

		} else if (currentTabTag.equals(TAB_5_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_5_TAG)).popFragment();

		} else if (currentTabTag.equals(TAB_6_TAG)) {
			isPopFragment = ((BaseContainerFragment) getSupportFragmentManager()
					.findFragmentByTag(TAB_6_TAG)).popFragment();
		}

		if (!isPopFragment) {
			finish();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		System.out.println("------------- Home Activity------------------");
		super.onActivityResult(requestCode, resultCode, data);

		android.support.v4.app.Fragment frag = null;

		if (getSupportFragmentManager().findFragmentByTag("HOME") != null) {
			frag = getSupportFragmentManager().findFragmentByTag("HOME");
			frag.onActivityResult(requestCode, resultCode, data);
		} else if (getSupportFragmentManager().findFragmentByTag("My Items") != null) {
			frag = getSupportFragmentManager().findFragmentByTag("My Items");
			frag.onActivityResult(requestCode, resultCode, data);
		}

	}

	public static FragmentTabHost getTabHost() {

		return mTabHost;
	}

	@Override
	public void onTabChanged(String tabId) {

		if (TAB_1_TAG.equals(tabId)) {
			initView();
			return;
		}

		// System.out.println("tabid========="+tabId);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (AppPrefrence.isLogin(HomeActivity.this)) {
			Utils.isUserExistsOnServer(HomeActivity.this);
		}
	}

}
