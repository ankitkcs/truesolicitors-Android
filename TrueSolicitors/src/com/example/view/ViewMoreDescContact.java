package com.example.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.trueclaims.R;
import com.example.utils.CommonVariable;

/**
 * More Tab Select option wise detail open
 * 
 * @author sanket
 * 
 */
public class ViewMoreDescContact extends BaseContainerFragment implements
		OnClickListener {

	private TextView txtMoreDesc;

	private String intBundleMoreHeader;
	// Main Fragment
	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private TextView txtHeaderView;
	private ImageView imgShare;
	private Button btnNextLink;
	private EditText edtSearchView;
	private RelativeLayout layoutCall;
	private RelativeLayout layoutMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_moredetail_contact,
				container, false);

		initFragment(view);
		return view;
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		switch (transit) {
		case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(),
						android.R.anim.fade_in);
			} else {
				return AnimationUtils.loadAnimation(getActivity(),
						android.R.anim.fade_out);
			}

		case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_close_enter);
			} else {
				return AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_close_exit);
			}

		case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
		default:
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_open_enter);
			} else {
				return AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_open_exit);
			}
		}

	}

	private void initFragment(View view) {
		intBundleMoreHeader = getArguments().getString(
				CommonVariable.PUT_EXTRA_MORE_HEADER);
		Log.d("tag", "View More Detail" + intBundleMoreHeader);
		homeFragmentConfigure(view);
		txtMoreDesc = (TextView) view
				.findViewById(R.id.moredetail_txtcontactdesc);
		layoutCall = (RelativeLayout) view
				.findViewById(R.id.moredetail_contentCall);
		layoutMessage = (RelativeLayout) view
				.findViewById(R.id.moredetail_contactMessage);
		setContactDetail();
		// txtMoreDesc.setText("");
		layoutCall.setOnClickListener(this);
		layoutMessage.setOnClickListener(this);

	}

	private void setContactDetail() {
		Log.d("tag", "int b" + intBundleMoreHeader);
		if (intBundleMoreHeader.equalsIgnoreCase(getResources().getString(
				R.string.more_contactdetail_contactTrue))) {
			txtMoreDesc.setText(Html.fromHtml(getResources().getString(
					R.string.more_contact_desc)));
		}
	}

	private void homeFragmentConfigure(View view) {
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;

		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		txtHeaderView.setText(intBundleMoreHeader);
		txtHeaderView.setVisibility(View.VISIBLE);
		imgComposeMsg = ((ViewMainFragment) getActivity()).imgComposeMsg;
		imgSearchView = ((ViewMainFragment) getActivity()).imgSearchView;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;

		// imgComposeMsg.setVisibility(View.INVISIBLE);
		// imgSearchView.setVisibility(View.INVISIBLE);
		imgComposeMsg.setVisibility(View.GONE);
		imgSearchView.setVisibility(View.GONE);

		imgShare.setVisibility(View.GONE);
		btnNextLink.setVisibility(View.GONE);
		edtSearchView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v == layoutCall) {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:08448547000"));

			startActivity(intent);
		} else if (v == layoutMessage) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
					+ "08448547000")));
		}
	}
}
