package com.example.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trueclaims.R;

public class ViewAboutus extends Activity implements OnClickListener {
	private ImageView imgBack;
	private TextView txtDesc;
	private TextView txtHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_aboutus);
		init();
	}

	private void init() {
		imgBack = (ImageView) findViewById(R.id.commonview_imgback);
		txtHeader = (TextView) findViewById(R.id.commonview_txtHeader);
		txtHeader
				.setText(getResources().getString(R.string.aboutus_strAboutus));
		txtDesc = (TextView) findViewById(R.id.homeabout_txtDesc);
		txtDesc.setText(Html.fromHtml(getResources().getString(
				R.string.more_abouttrue_desc)));

		imgBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == imgBack) {
			onBackPressed();

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_close_enter,
				R.anim.activity_close_exit);

	}
}
