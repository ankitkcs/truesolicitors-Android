package com.example.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.AdapterFaqList;
import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.trueclaims.R;
import com.example.utils.CommonVariable;

public class ViewFAQ extends BaseContainerFragment {
	private ListView listFaq;
	private AdapterFaqList adapterFaqList;
	// Main Fragment
	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private ImageView imgShare;
	private Button btnNextLink;
	private EditText edtSearchView;
	private TextView txtHeaderView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_faq_option, container,
				false);
		initFragmentView(view);
		return view;
	}

	private void initFragmentView(View view) {
		homeFragmentConfigure(view);

		listFaq = (ListView) view.findViewById(R.id.faqQuestion_listView);
		// Getting Question List
		String[] faqQuestionArray = getResources().getStringArray(
				R.array.faq_questionlist);
		adapterFaqList = new AdapterFaqList(getActivity(), faqQuestionArray);
		listFaq.setAdapter(adapterFaqList);

		listFaq.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewFAQDetails fragmentFaqDetail = new ViewFAQDetails();
				Bundle args = new Bundle();
				args.putInt(CommonVariable.PUT_EXTRA_SELECTED_POSITION,
						position);
				fragmentFaqDetail.setArguments(args);
				((BaseContainerFragment) getParentFragment()).replaceFragment(
						fragmentFaqDetail, true);

				getActivity().overridePendingTransition(
						R.anim.push_in_from_left, R.anim.push_out_to_right);
			}
		});
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

	private void homeFragmentConfigure(View view) {
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		txtHeaderView.setText(getResources().getString(R.string.faq_strFaq));
		imgComposeMsg = ((ViewMainFragment) getActivity()).imgComposeMsg;
		imgSearchView = ((ViewMainFragment) getActivity()).imgSearchView;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;

		imgComposeMsg.setVisibility(View.INVISIBLE);
		imgSearchView.setVisibility(View.INVISIBLE);
		imgShare.setVisibility(View.GONE);
		btnNextLink.setVisibility(View.GONE);
		edtSearchView.setVisibility(View.GONE);
	}

}
