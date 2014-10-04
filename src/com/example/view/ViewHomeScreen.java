package com.example.view;

import com.example.trueclaims.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

/**
 * 
 * @author sanket
 * 
 */
public class ViewHomeScreen extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linkyourclaims);
		init();
	}

	private void init() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
