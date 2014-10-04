package com.example.view;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.AdapterMessageList;
import com.example.database.Tbl_LinkToClaim;
import com.example.database.Tbl_Messages;
import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.model.Model_LinkToClaim;
import com.example.model.Model_Messages;
import com.example.model.Model_WebResponse;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;
import com.example.utils.KcsProgressDialog;
import com.example.utils.WebCalls;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 
 * @author sanket
 * 
 */
public class ViewMessage extends BaseContainerFragment implements
		OnClickListener {
	private PullToRefreshListView listViewMessage;
	private EditText edtSearchView;
	private AdapterMessageList adapter;
	private ArrayList<Model_Messages> listOfMessages;
	private TextView txtHeaderView;

	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private KcsProgressDialog kcsDialog;
	private String strClaimNumber = "";
	private String strClaimAuthToken = "";
	private Handler mHandler = new Handler();
	private String strAuthToken;
	private String strDialogHeader = "";
	private Button btnNextLink;
	private ImageView imgShare;
	private Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_message, container,
				false);
		activity = getActivity();
		initFragmentView(view);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean isMessageOpen = CommonMethod.getSharePrefrenceBoolean(
				getActivity(), CommonVariable.PREFS_DIALOG_MESSAGE_ISOPEN);
		
		if (!isMessageOpen) {
			Model_LinkToClaim model = Tbl_LinkToClaim
					.SelectRecordUsingClaimNumber(strClaimNumber);
			if (model != null) {
				strDialogHeader = getResources().getString(
						R.string.dialogsuccess_strMessageHeader)
						+ model.customer_name + " .";
			}

			int dialogIcon = R.drawable.link_popup_icon;
			String strClaimString = "Claim ";
			String strMessageDetail = CommonVariable.DIALOG_SUCCESS_MESSAGE_DESC

			.replace("CLAIM_NUMBER", strClaimNumber);
			CommonMethod.showPopupSuccessDialog(getActivity(), strDialogHeader,
					dialogIcon, strMessageDetail,
					CommonVariable.PREFS_DIALOG_MESSAGE_ISOPEN, null, false);
		}
		WebCallGetMessages(false);
		// Set a listener to be invoked when the list should be refreshed.
		((PullToRefreshListView) listViewMessage)
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						// Do work to refresh the list here.
						if (CommonMethod.isInternetAvailable(getActivity())) {

							WebCallGetMessages(true);
							// connectWithHttpGet();

						}
					}
				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// Log.e("", "OnActivityResult Begin");
	}

	// Initialization data with Fragment
	private void initFragmentView(View view) {
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		strClaimNumber = ((ViewMainFragment) getActivity()).strClaimNumber;
		strClaimAuthToken = ((ViewMainFragment) getActivity()).strAuthToken;
		imgComposeMsg = ((ViewMainFragment) getActivity()).imgComposeMsg;
		imgSearchView = ((ViewMainFragment) getActivity()).imgSearchView;
		txtHeaderView.setText(strClaimNumber);
		imgComposeMsg.setVisibility(View.VISIBLE);
		imgSearchView.setVisibility(View.VISIBLE);
		listViewMessage = (PullToRefreshListView) view
				.findViewById(R.id.message_listMessages);
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;
		btnNextLink.setVisibility(View.GONE);
		imgShare.setVisibility(View.GONE);

		edtSearchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				adapter.getFilter().filter(s.toString());
			}
		});
	}

	/**
	 * call messages api and reloat message tab
	 * 
	 * @param isPullToRefreshMode
	 */
	private void WebCallGetMessages(final boolean isPullToRefreshMode) {
		if (!isPullToRefreshMode) {
			showKcsDialog();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final Model_WebResponse modelResponse = WebCalls
							.getMessages(strClaimNumber, strClaimAuthToken);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {

								reloadListviewData();
							} else {
								CommonMethod.showPopupValidation(getActivity(),
										modelResponse.responseMessage, false);
							}
						}

					});

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonMethod.showWebServiceCallErrorDialog(getActivity(),
							e.getMessage(), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(getActivity(),
							e.getMessage(), false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(getActivity(),
							e.getMessage(), false);
				} finally {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (!isPullToRefreshMode) {
								dismissKcsDialog();
							}
							((PullToRefreshListView) listViewMessage)
									.onRefreshComplete();
						}
					});
				}

			}
		}).start();
		;

	}

	private void reloadListviewData() {
		listOfMessages = Tbl_Messages.SelectDataClaimWise(strClaimNumber);
		if (listOfMessages != null && listOfMessages.size() > 0) {
			adapter = new AdapterMessageList(activity, listOfMessages);
			listViewMessage.setAdapter(adapter);
		}
	}

	private void showKcsDialog() {
		if (getActivity().isFinishing()) {
			return;
		}
		if (kcsDialog == null)
			kcsDialog = new KcsProgressDialog(getActivity(), "", false);
		if (!kcsDialog.isShowing())
			kcsDialog.show();

	}

	private void dismissKcsDialog() {

		if (kcsDialog != null && kcsDialog.isShowing())
			kcsDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
