package com.example.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adapter.AdapterFaqList;
import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.trueclaims.R;
import com.example.utils.CommonVariable;
/**
 * Set More Tab Data  Load static data and different option data
 * @author Kcspl003
 *
 */
public class ViewMore extends BaseContainerFragment implements OnClickListener {
	private ListView listFaq;
	private AdapterFaqList adapterFaqList;
	// Main Fragment
	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private ImageView imgShare;
	private Button btnNextLink;
	private EditText edtSearchView;
	private TextView txtHeaderView;
	// Relative Layout List Handler
	private RelativeLayout rltvAboutTrue;
	private RelativeLayout rltvWhyTrue;
	private RelativeLayout rltvTypeOfInjuries;
	private RelativeLayout rltvContacts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_more_option, container,
				false);
		initFragmentView(view);
		return view;
	}

	private void initFragmentView(View view) {
		homeFragmentConfigure(view);
		rltvAboutTrue = (RelativeLayout) view
				.findViewById(R.id.moreoption_rltvAboutTrue);
		rltvWhyTrue = (RelativeLayout) view
				.findViewById(R.id.moreoption_rltvWhyTrue);
		rltvTypeOfInjuries = (RelativeLayout) view
				.findViewById(R.id.moreoption_rltvTypeofInjuries);
		rltvContacts = (RelativeLayout) view
				.findViewById(R.id.moreoption_rltvContact);
		rltvAboutTrue.setOnClickListener(this);
		rltvWhyTrue.setOnClickListener(this);
		rltvTypeOfInjuries.setOnClickListener(this);
		rltvContacts.setOnClickListener(this);
	}

	private void homeFragmentConfigure(View view) {
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		txtHeaderView.setText(getResources().getString(R.string.faq_strFaq));
		imgComposeMsg = ((ViewMainFragment) getActivity()).imgComposeMsg;
		imgSearchView = ((ViewMainFragment) getActivity()).imgSearchView;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;
		txtHeaderView
				.setText(getResources().getString(R.string.more_strHeader));
//		imgComposeMsg.setVisibility(View.INVISIBLE);
//		imgSearchView.setVisibility(View.INVISIBLE);
		imgComposeMsg.setVisibility(View.GONE);
		imgSearchView.setVisibility(View.GONE);
		
		imgShare.setVisibility(View.GONE);
		btnNextLink.setVisibility(View.GONE);
		edtSearchView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v == rltvAboutTrue) {
			openIntentMoreDetail(getResources().getString(
					R.string.more_abouttrue));
		} else if (v == rltvWhyTrue) {

			openIntentMoreDetail(getResources()
					.getString(R.string.more_whytrue));
		} else if (v == rltvTypeOfInjuries) {

			openIntentMoreDetail(getResources().getString(
					R.string.more_typeofinjuries));
		} else if (v == rltvContacts) {
			ViewMoreDescContact fragmentMoreDetail = new ViewMoreDescContact();
			Bundle args = new Bundle();
			args.putString(CommonVariable.PUT_EXTRA_MORE_HEADER, getResources()
					.getString(R.string.more_contactdetail_contactTrue));
			fragmentMoreDetail.setArguments(args);
			((BaseContainerFragment) getParentFragment()).replaceFragment(
					fragmentMoreDetail, true);

			getActivity().overridePendingTransition(R.anim.push_in_from_left,
					R.anim.push_out_to_right);
		}
	}

	public void openIntentMoreDetail(String strHeader) {
		// TODO Auto-generated method stub
		ViewMoreDetailDesc fragmentMoreDetail = new ViewMoreDetailDesc();
		Bundle args = new Bundle();
		args.putString(CommonVariable.PUT_EXTRA_MORE_HEADER, strHeader);
		fragmentMoreDetail.setArguments(args);
		((BaseContainerFragment) getParentFragment()).replaceFragment(
				fragmentMoreDetail, true);

		getActivity().overridePendingTransition(R.anim.push_in_from_left,
				R.anim.push_out_to_right);
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

}
