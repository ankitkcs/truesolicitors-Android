package com.example.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.trueclaims.R;
import com.example.utils.CommonVariable;

/**
 * 
 * @author sanket
 * 
 */
public class ViewFAQDetails extends BaseContainerFragment {
	private TextView txtFaqQuestion;
	private TextView txtFaqAnswer;

	private int intBundlePosition;
	// Main Fragment
	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private TextView txtHeaderView;
	private ImageView imgShare;
	private Button btnNextLink;
	private EditText edtSearchView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_faq_detail, container,
				false);

		initFragment(view);
		return view;
	}

	private void initFragment(View view) {
		homeFragmentConfigure(view);
		intBundlePosition = getArguments().getInt(
				CommonVariable.PUT_EXTRA_SELECTED_POSITION);
		txtFaqQuestion = (TextView) view
				.findViewById(R.id.faqdetail_txtFaqQuestion);
		txtFaqAnswer = (TextView) view
				.findViewById(R.id.faqdetail_txtFaqAnswer);
		String[] questionList = getResources().getStringArray(
				R.array.faq_questionlist);
		String[] answerList = getResources().getStringArray(
				R.array.faq_answerlist);
		txtFaqQuestion.setText(Html.fromHtml(questionList[intBundlePosition]));
		txtFaqAnswer.setText(Html.fromHtml(answerList[intBundlePosition]));
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
