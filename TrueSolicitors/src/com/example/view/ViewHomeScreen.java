package com.example.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adapter.AdapterHomeScreenClaims;
import com.example.database.Tbl_LinkToClaim;
import com.example.fragment.ViewMainFragment;
import com.example.model.Model_LinkToClaim;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;

/**
 * 
 * @author sanket
 * 
 */
public class ViewHomeScreen extends Activity implements OnClickListener,
		OnTouchListener, OnItemClickListener {
	private TextView txtLinkYourClaimDesc;
	private Button btnLinkToNewClaims, btnLinkReportClaim;
	private ArrayList<Model_LinkToClaim> listClaims;
	private ListView listViewClaims;
	private LinearLayout linearShareDesc;
	private TextView txtWelcome;
	private AdapterHomeScreenClaims adapter;
	private Button txtAboutUs, btnFAQ, txtShareApp;
	private RelativeLayout layoutHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		listClaims = Tbl_LinkToClaim.SelectAll();
		
		checkClaimExistsOrNot();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// Disable Welcome Screen
		CommonMethod.showPopupConfirm(ViewHomeScreen.this,
				CommonVariable.EXIT_APPTEXT);
	}

	@Override
	public void onClick(View v) {
		if (v == btnLinkToNewClaims) {

			Intent intent = new Intent(this, ViewLinkYourClaims.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_enter,
					R.anim.activity_open_exit);
		} else if (v == btnLinkReportClaim) {

			Intent intent = new Intent(this, ViewLinkYourClaims.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_enter,
					R.anim.activity_open_exit);
			// unSelectorView(v);
		} else if (v == txtAboutUs) {
			Intent intent = new Intent(this, ViewAboutus.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_enter,
					R.anim.activity_open_exit);
		} else if (v == btnFAQ) {
			CommonVariable.IS_ONLYFAQ=true;
			Intent intent = new Intent(this, ViewMainFragment.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_enter,
					R.anim.activity_open_exit);
		} else if (v == txtShareApp) {
			CommonMethod.dialogShareApp(ViewHomeScreen.this,
					CommonVariable.SHARE_APP_SUBJECT,
					CommonVariable.SHARE_APP_TEXT);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return false;
	}

	private void init() {

		// NewClaim
		btnLinkToNewClaims = (Button) findViewById(R.id.homescreen_btnLinkToClaim);
		// Report Claims
		btnLinkReportClaim = (Button) findViewById(R.id.homescreen_btnReportClaim);
		txtAboutUs = (Button) findViewById(R.id.linkyourclaims_txtAboutus);
		txtShareApp = (Button) findViewById(R.id.linkyourclaims_txtShareApp);
		btnFAQ = (Button) findViewById(R.id.linkyourclaims_txtFAQ);
		listViewClaims = (ListView) findViewById(R.id.homescreen_listclims);
		linearShareDesc = (LinearLayout) findViewById(R.id.homescreen_sharedesc);
		txtWelcome = (TextView) findViewById(R.id.homescreen_txtWelcome);
		txtLinkYourClaimDesc = (TextView) findViewById(R.id.homescreen_txtTrueSolicitors);
		txtLinkYourClaimDesc.setText(Html.fromHtml(getResources().getString(
				R.string.homescreen_strToGetApp)));
		btnLinkToNewClaims.setOnClickListener(this);
		btnLinkReportClaim.setOnClickListener(this);
		CommonMethod.setSharePrefrenceBoolean(ViewHomeScreen.this,
				CommonVariable.PREFS_FIRSTTIME_HOMESCREEN, true);
		// boolean isFirstTimeOpenWelcome =
		// CommonMethod.getSharePrefrenceBoolean(
		// ViewHomeScreen.this, CommonVariable.PREFS_FIRSTTIME_HOMESCREEN);

		// setDummyViewToMainView();
		layoutHeader = (RelativeLayout) findViewById(R.id.homescreen_rltvList);

		btnLinkReportClaim.setOnTouchListener(this);
		btnLinkToNewClaims.setOnTouchListener(this);
		txtAboutUs.setOnClickListener(this);
		txtShareApp.setOnClickListener(this);
		btnFAQ.setOnClickListener(this);

	}

	private void checkClaimExistsOrNot() {
		// TODO Auto-generated method stub
		if (listClaims == null || listClaims.size() == 0) {
			listViewClaims.setVisibility(View.GONE);
			txtWelcome.setVisibility(View.VISIBLE);
			txtLinkYourClaimDesc.setVisibility(View.VISIBLE);
			// CommonMethod.setSharePrefrenceBoolean(ViewHomeScreen.this,
			// CommonVariable.PREFS_FIRSTTIME_HOMESCREEN, true);

		} else {
			txtWelcome.setVisibility(View.GONE);
			txtLinkYourClaimDesc.setVisibility(View.GONE);
			linearShareDesc.setVisibility(View.GONE);
			listViewClaims.setVisibility(View.VISIBLE);
			if (listClaims != null && listClaims.size() > 0) {

				layoutHeader.setGravity(Gravity.CENTER);

				adapter = new AdapterHomeScreenClaims(this, listClaims);
				listViewClaims.setAdapter(adapter);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
					listViewClaims.setOnItemClickListener(this);
				}
				setListViewHeightBasedOnChildren(listClaims, listViewClaims);
			}

		}

	}

	public void setListViewHeightBasedOnChildren(
			ArrayList<Model_LinkToClaim> listClaims2, ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;

		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		totalHeight += listItem.getMeasuredHeight();

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		if (listClaims2.size() == 1) {

			params.height = height + 10;

		} else {
			params.height = (height + height);
			listView.setLayoutParams(params);
		}

		listView.requestLayout();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ViewHomeScreen.this, ViewMainFragment.class);
		CommonVariable.IS_ONLYFAQ=false;
		intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_NO,
				listClaims.get(position).claim_number);
		intent.putExtra(CommonVariable.PUT_EXTRA_ClAIM_ACCOUNT_ID,
				listClaims.get(position).account_id);
		intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_AUTH_TOKEN,
				listClaims.get(position).auth_token);
		startActivity(intent);
	}

}
