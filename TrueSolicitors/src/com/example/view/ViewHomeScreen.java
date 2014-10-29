package com.example.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
	private LinearLayout listViewClaims;
	private LinearLayout linearShareDesc;
	private TextView txtWelcome;
	private AdapterHomeScreenClaims adapter;
	private Button txtAboutUs, btnFAQ, txtShareApp;
	private RelativeLayout layoutHeader;
	private View convertView;
	private LayoutInflater inflater;

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
			openGmailIntent();
			// Intent intent = new Intent(this, ViewLinkYourClaims.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.activity_open_enter,
			// R.anim.activity_open_exit);
			// unSelectorView(v);
		} else if (v == txtAboutUs) {
			Intent intent = new Intent(this, ViewAboutus.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_enter,
					R.anim.activity_open_exit);
		} else if (v == btnFAQ) {
			CommonVariable.IS_ONLYFAQ = true;
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

	private void openGmailIntent() {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		String[] TO = { CommonVariable.REPORT_CLAIM_MAIL };
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				CommonVariable.REPORT_CLAIM_SUBJECT);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_TITLE,
				CommonVariable.REPORT_CLAIM_TITLE);
		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent,
				0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches)
			if (info.activityInfo.packageName.endsWith(".gm")
					|| info.activityInfo.name.toLowerCase().contains("gmail"))
				best = info;
		if (best != null)
			emailIntent.setClassName(best.activityInfo.packageName,
					best.activityInfo.name);
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(emailIntent);
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
		listViewClaims = (LinearLayout) findViewById(R.id.homescreen_listclims);
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
				fillLayout();
				// adapter = new AdapterHomeScreenClaims(this, listClaims);
				// listViewClaims.setAdapter(adapter);
				// if (adapter != null) {
				// adapter.notifyDataSetChanged();
				// listViewClaims.setOnItemClickListener(this);
				// }
				// setListViewHeightBasedOnChildren(listClaims, listViewClaims);
			}

		}

	}
	 ClaimsHolder holder;
	private void fillLayout() {
		if (listClaims != null && listClaims.size() > 0) {
			Log.d("Tag", "ListView Size " + listClaims.size());
			listViewClaims.removeAllViews();
			inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < listClaims.size(); i++) {

				  holder = new ClaimsHolder();
				convertView = inflater.inflate(
						R.layout.row_homescreen_listclaims, null);
				holder.btnClaims = (TextView) convertView
						.findViewById(R.id.row_homescreen_btnClaimsNo);
				convertView.setTag(holder);

				Model_LinkToClaim model = listClaims.get(i);
				holder.btnClaims.setTag(listClaims.get(i));

				holder.btnClaims.setText(getResources().getString(
						R.string.homescreen_strClaimNumber)
						+ " " + model.claim_number);
				LinearLayout.LayoutParams linkParams = (LayoutParams) btnLinkToNewClaims
						.getLayoutParams();
				
				
				ViewTreeObserver vto = btnLinkToNewClaims.getViewTreeObserver(); 
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
				    @Override 
				    public void onGlobalLayout() { 
				        btnLinkToNewClaims.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
				        int width  = btnLinkToNewClaims.getMeasuredWidth();
				        int height = btnLinkToNewClaims.getMeasuredHeight(); 
				        android.view.ViewGroup.LayoutParams layoutParams = holder.btnClaims
								.getLayoutParams();
						layoutParams.height = height;
				        holder.btnClaims.setLayoutParams(layoutParams);
				    } 
				});
				listViewClaims.addView(convertView);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ClaimsHolder holderSelected = (ClaimsHolder) convertView
								.getTag();
						Model_LinkToClaim modelSelected = (Model_LinkToClaim) holderSelected.btnClaims
								.getTag();
						Log.d("tag", "Holder Selected CLaim Number"
								+ modelSelected.claim_number);
						Log.d("tag", "Holder Selected AccountId "
								+ modelSelected.account_id);
						Log.d("tag", "Holder Selected AuthToken "
								+ modelSelected.auth_token);
						Intent intent = new Intent(ViewHomeScreen.this,
								ViewMainFragment.class);
						CommonVariable.IS_ONLYFAQ = false;
						intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_NO,
								modelSelected.claim_number);
						intent.putExtra(
								CommonVariable.PUT_EXTRA_ClAIM_ACCOUNT_ID,
								modelSelected.account_id);
						intent.putExtra(
								CommonVariable.PUT_EXTRA_CLAIM_AUTH_TOKEN,
								modelSelected.auth_token);
						startActivity(intent);
					}
				});

			}
		}
	}

	public void setListViewHeightBasedOnChildren(
			ArrayList<Model_LinkToClaim> listClaims2, LinearLayout listView) {

		int totalHeight = 0;

		View listItem = listViewClaims.getChildAt(0);
		listItem.measure(0, 0);
		totalHeight += listItem.getMeasuredHeight();

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listViewClaims.getHeight() * (listView.getChildCount() - 1));
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

	}

	public class ClaimsHolder {
		TextView btnClaims;
	}
}
