package com.example.fragment;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trueclaims.R;
import com.example.view.ViewFAQ;
/**
 * FAQ Container Handling with faq content 
 * @author Kcspl003
 *
 */
public class FragmentFAQ extends BaseContainerFragment {
	private boolean mIsViewInited;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("test", "tab 1 oncreateview");

		View view = inflater.inflate(R.layout.container_fragment, null);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 1 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}

	}

	private void initView() {
		Log.e("test", "tab 1 init view");

		replaceFragment(new ViewFAQ(), false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		replaceFragment(new ViewFAQ(), false);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
