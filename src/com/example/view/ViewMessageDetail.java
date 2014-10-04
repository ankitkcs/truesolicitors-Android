package com.example.view;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.fragment.BaseContainerFragment;
import com.example.trueclaims.R;

/**
 * 
 * @author sanket
 * 
 */
public class ViewMessageDetail extends BaseContainerFragment implements
		OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_message_detail,
				container, false);

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// Log.e("", "OnActivityResult Begin");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
//
//	
}
