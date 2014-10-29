package com.example.fragment;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trueclaims.R;
import com.example.view.ViewMyFolder;

/**
 * Container Folder Fragment Folder Tab-Getting List Of Document Related
 * ClaimNumber
 * 
 * @author Kcspl003
 * 
 */
public class FragmentMyFolder extends BaseContainerFragment {

	private boolean mIsViewInited;
	public static final int REQUESTCODE_FRAG_MYFOLDER = 1000;
	public static FragmentMyFolder instance;
	ViewMyFolder viewMyFolderFrag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("test", "tab 1 oncreateview");
		instance = this;
		return inflater.inflate(R.layout.container_fragment, null);
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
		Log.e("test", "tab 1 init view");
		if (viewMyFolderFrag == null) {
			viewMyFolderFrag = new ViewMyFolder();
		}
		replaceFragment(viewMyFolderFrag, false);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (viewMyFolderFrag == null) {
			viewMyFolderFrag = new ViewMyFolder();
		}
		replaceFragment(viewMyFolderFrag, false);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (viewMyFolderFrag != null) {
				viewMyFolderFrag.refreshListViewData();
			}
		} else {
			((ViewMainFragment) getActivity()).actionbar
					.setSelectedNavigationItem(0);
		}
	}

}
