package com.tamilshout.fragments;



import com.tamilshout.R;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class BaseContainerFragment extends Fragment {

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	public boolean popFragment() {
		Log.e("test", "pop fragment: "+ getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}

	public void replaceFragment(Fragment fragment, boolean addToBackStack,
			String eventdetail) {

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment, eventdetail);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("------------- BaseContainerFragment------------------");
		Fragment frag = null;

		if (getChildFragmentManager().findFragmentByTag("HOME") != null) {
			frag = getChildFragmentManager().findFragmentByTag("HOME");
			frag.onActivityResult(requestCode, resultCode, data);
		} else if (getChildFragmentManager().findFragmentByTag("My Items") != null) {
			frag = getChildFragmentManager().findFragmentByTag("My Items");
			frag.onActivityResult(requestCode, resultCode, data);
		}
	}
}
