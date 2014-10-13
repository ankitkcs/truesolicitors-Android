package com.example.fragment;

import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trueclaims.R;
import com.example.utils.CommonVariable;
import com.example.view.ViewNewMessage;
/**
 * Main Fragment Handling Tab and Container 
 * 
 * @author Kcspl003
 *
 */
public class ViewMainFragment extends ActionBarActivity implements
		OnClickListener {
	public static Context appContext;
	private boolean mIsViewInited;
	int mSortMode = -1;

	public boolean isOpenSearchView = false;
	// ActionBar
	public ActionBar actionbar;
	public ImageView imgSearchView;
	public ImageView imgComposeMsg;
	public EditText edtSearchView;
	public TextView txtActionBarHeader;
	private Bundle bundle;
	public String strClaimNumber = "";
	public String strAuthToken = "";
	public Button btnNextLink;
	public ImageView imgShare;
	public ImageView imgBack;
	public ActionBar.Tab messageTab, myFolderTab, faqTab, moreTab;
	private int intTabNo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container_fragment);
		bundle = this.getIntent().getExtras();
		init(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

	private void init(Bundle savedInstanceState) {
		appContext = getApplicationContext();
		// ActionBar
		actionbar = getSupportActionBar();
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.common_header);
		// actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(false);
		//
	
		actionbar.setIcon(R.drawable.ic_action_android_back_arrow_icon);
		actionbar.setBackgroundDrawable(new ColorDrawable(color.white));
		imgComposeMsg = (ImageView) findViewById(R.id.commonview_imgComposeMessage);
		imgSearchView = (ImageView) findViewById(R.id.commonview_imgComposeSearch);
		btnNextLink = (Button) findViewById(R.id.commonview_btnSubmitLink);
		imgShare = (ImageView) findViewById(R.id.commonview_imgComposeShare);
		imgBack = (ImageView) findViewById(R.id.commonview_imgback);
		edtSearchView = (EditText) actionbar.getCustomView().findViewById(
				R.id.commonview_btnSearchView);
		imgBack.setOnClickListener(this);
		// edtSearchView.setCompoundDrawablesWithIntrinsicBounds(getResources()
		// .getDrawable(R.drawable.android_searching_icon), null, null,
		// null);

		// Tab Number get

		txtActionBarHeader = (TextView) findViewById(R.id.commonview_txtHeader);
		imgComposeMsg.setVisibility(View.VISIBLE);
		imgSearchView.setVisibility(View.VISIBLE);
		imgComposeMsg.setOnClickListener(this);
		imgSearchView.setOnClickListener(this);

		// actionbar.show();
		actionbar.setLogo(null); // forgot why this one but it helped

		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		((View) homeIcon).setVisibility(View.GONE);

		actionbar.setDisplayShowTitleEnabled(false);
		//Checking Directly open faq tab or not 
		if (CommonVariable.IS_ONLYFAQ) {
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			Fragment faqFragment = new FragmentFAQ();
			if (faqFragment != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();

				fragmentManager.beginTransaction()
						.replace(R.id.container_framelayout, faqFragment)
						.commit();
				return;
			}
		}
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		messageTab = actionbar.newTab()
				.setIcon(R.drawable.ic_action_android_messages_icon)
				.setTag("MESSAGETAB");
		myFolderTab = actionbar.newTab()
				.setIcon(R.drawable.ic_action_android_my_folder_icon)
				.setTag("FolderTAB");
		faqTab = actionbar.newTab()
				.setIcon(R.drawable.ic_action_android_info_icon)
				.setTag("FAQTAB");
		moreTab = actionbar.newTab()
				.setIcon(R.drawable.ic_action_android_more_icon)
				.setTag("MORETAB");

		Fragment messageFragment = new FragmentMessage();
		Fragment myFolderFragment = new FragmentMyFolder();
		Fragment faqFragment = new FragmentFAQ();
		Fragment moreOptFragment = new FragmentMoreOption();

		messageTab.setTabListener(new MyTabsListener(messageFragment));
		myFolderTab.setTabListener(new MyTabsListener(myFolderFragment));
		faqTab.setTabListener(new MyTabsListener(faqFragment));
		moreTab.setTabListener(new MyTabsListener(moreOptFragment));

		if (bundle != null
				&& bundle.getString(CommonVariable.PUT_EXTRA_CLAIM_NO) != null) {
			strClaimNumber = bundle
					.getString(CommonVariable.PUT_EXTRA_CLAIM_NO);
		}
		strAuthToken = bundle
				.getString(CommonVariable.PUT_EXTRA_CLAIM_AUTH_TOKEN);
		Log.d("TOKEN", "ViewMainFragment token is" + strAuthToken);
		actionbar.addTab(messageTab);
		actionbar.addTab(myFolderTab);

		actionbar.addTab(faqTab);
		actionbar.addTab(moreTab);

	}

	@Override
	public void onBackPressed() {
		if (!returnBackStackImmediate(getSupportFragmentManager())) {

			super.onBackPressed();
			overridePendingTransition(R.anim.push_in_from_right,
					R.anim.push_out_to_left);
		}
	}

	// HACK: propagate back button press to child fragments.
	// This might not work properly when you have multiple fragments adding
	// multiple children to the backstack.
	// (in our case, only one child fragments adds fragments to the
	// backstack,
	// so we're fine with this)
	private boolean returnBackStackImmediate(FragmentManager fm) {
		List<Fragment> fragments = fm.getFragments();
		if (fragments != null && fragments.size() > 0) {
			for (Fragment fragment : fragments) {
				if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
					if (fragment.getChildFragmentManager()
							.popBackStackImmediate()) {
						Log.d("tag", "CalledInside");

						return true;
					} else {

						return returnBackStackImmediate(fragment
								.getChildFragmentManager());
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == imgComposeMsg) {
			Intent intent = new Intent(appContext, ViewNewMessage.class);
			intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_NO, strClaimNumber);
			intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_AUTH_TOKEN,
					strAuthToken);
			startActivity(intent);

		} else if (v == imgSearchView) {
			if (!isOpenSearchView) {
				txtActionBarHeader.setVisibility(View.GONE);
				edtSearchView.setVisibility(View.VISIBLE);
				isOpenSearchView = true;
			} else {
				txtActionBarHeader.setVisibility(View.VISIBLE);
				edtSearchView.setVisibility(View.GONE);
				isOpenSearchView = false;
			}
		} else if (v == imgBack) {

			onBackPressed();
		}
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (tab != null) {
				int i = tab.getPosition();
				setIconInTab(i);
			}
			ft.replace(R.id.container_framelayout, fragment);

		}

		private void setIconInTab(int i) {

			if (i == 0) {

				txtActionBarHeader.setVisibility(View.VISIBLE);
				messageTab
						.setIcon(R.drawable.ic_action_android_messages_coloured_icon);
				myFolderTab
						.setIcon(R.drawable.ic_action_android_my_folder_icon);
				faqTab.setIcon(R.drawable.ic_action_android_info_icon);
				moreTab.setIcon(R.drawable.ic_action_android_more_icon);
			} else if (i == 1) {
				txtActionBarHeader.setVisibility(View.VISIBLE);
				messageTab.setIcon(R.drawable.ic_action_android_messages_icon);
				myFolderTab
						.setIcon(R.drawable.ic_action_android_my_folder_coloured_icon);
				faqTab.setIcon(R.drawable.ic_action_android_info_icon);
				moreTab.setIcon(R.drawable.ic_action_android_more_icon);
			} else if (i == 2) {

				txtActionBarHeader.setVisibility(View.VISIBLE);
				messageTab.setIcon(R.drawable.ic_action_android_messages_icon);
				myFolderTab
						.setIcon(R.drawable.ic_action_android_my_folder_icon);
				faqTab.setIcon(R.drawable.ic_action_android_info_coloured_icon);
				moreTab.setIcon(R.drawable.ic_action_android_more_icon);
			} else if (i == 3) {
				txtActionBarHeader.setVisibility(View.VISIBLE);
				messageTab.setIcon(R.drawable.ic_action_android_messages_icon);
				myFolderTab
						.setIcon(R.drawable.ic_action_android_my_folder_icon);
				faqTab.setIcon(R.drawable.ic_action_android_info_icon);
				moreTab.setIcon(R.drawable.ic_action_android_more_coloured_icon);

			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (fragment != null) {
				ft.remove(fragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
	}

}