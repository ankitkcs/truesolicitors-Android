package com.example.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.trueclaims.R;

/**
 * 
 * @author admin LeftToright =0, RightToLeft=1 ,NoAnimation Any of Int
 */
public class BaseContainerFragment extends Fragment {
	 public void replaceFragment(Fragment fragment, boolean addToBackStack) {
	        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
	        if (addToBackStack) {
	            transaction.addToBackStack(null);
	        }
	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        transaction.replace(R.id.container_framelayout, fragment);
	        transaction.commit();
	        getChildFragmentManager().executePendingTransactions();
	    }

	

	public boolean popFragment() {
		Log.e("test", "pop fragment: "
				+ getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}
}
