package com.example.view;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Tbl_Documents;
import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.model.Model_Documents;
import com.example.model.Model_WebResponse;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;
import com.example.utils.KcsProgressDialog;
import com.example.utils.WebCalls;

/**
 * 
 * This class using agree perticular document and updating webservice 
 * Action Type and action time.
 * @author sanket
 * 
 */
public class ViewAgreeFile extends BaseContainerFragment implements
		OnClickListener {
	private EditText edtSearchView;
	private TextView txtHeaderView;
	private String strClaimNumber;
	private Button btnNextLink;
	private ImageView imgShare;
	private Bundle bundleArgument;
	private String strGuid;
	private KcsProgressDialog kcsDialog;
	private String strClaimAuthToken;
	private Handler mHandler = new Handler();
	private String strAppReadTime;
	private String strAppActionTime;
	public static boolean isBackHandle = false;
	public ImageView imgBackView;
	private Model_Documents modelSelectedDoc;
	private RadioGroup radioActionType;
	private View viewAgreeInstance;
	private int radioSelectedPosition;
	private String strAppTypeAction;
	private String strDocFilePath;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_agree_file, container,
				false);
		viewAgreeInstance = view;
		initFragmentView(view);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundleArgument = getArguments();
		strGuid = bundleArgument
				.getString(CommonVariable.PUT_EXTRA_DOCUMENT_GUID);
		strDocFilePath = bundleArgument
				.getString(CommonVariable.PUT_EXTRA_DOCUMENT_FILE_PATH);
		modelSelectedDoc = Tbl_Documents.selectDocument(strGuid);
		strAppReadTime = modelSelectedDoc.app_date_read_at;
		if (strAppReadTime == null || strAppReadTime.trim().length() < 0) {
			Calendar cal = Calendar.getInstance();
			long milisecond = cal.getTimeInMillis();
			strAppReadTime = CommonMethod.getDate(milisecond,
					CommonVariable.DATABASE_DATE_FORMAT);
		}

	}

	private void initFragmentView(View view) {

		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		strClaimNumber = ((ViewMainFragment) getActivity()).strClaimNumber;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		strClaimAuthToken = ((ViewMainFragment) getActivity()).strAuthToken;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;
		txtHeaderView.setText(getResources().getString(
				R.string.myfolder_strMyFolderHeader));
		imgShare.setVisibility(View.VISIBLE);
		btnNextLink.setVisibility(View.VISIBLE);
		radioActionType = (RadioGroup) view
				.findViewById(R.id.agreefile_radioGroup);
		btnNextLink.setCompoundDrawables(null, null, null, null);
		btnNextLink
				.setText(getResources().getString(R.string.myfolder_strNext));
		btnNextLink.setOnClickListener(this);
		imgShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnNextLink) {
			int isSelectedId = radioActionType.getCheckedRadioButtonId();
			if (isSelectedId == -1) {
				CommonMethod.showPopupValidation(getActivity(), getResources()
						.getString(R.string.validation_agreedissagree), false);
				return;
			}

			if (!CommonMethod.isInternetAvailable(getActivity())) {
				CommonMethod.showPopupValidation(
						getActivity(),
						getActivity().getResources().getString(
								R.string.validation_internetoffline), false);
			} else {
				radioSelectedPosition = radioActionType
						.indexOfChild(viewAgreeInstance
								.findViewById(isSelectedId));
				switch (radioSelectedPosition) {
				case 0:
					strAppTypeAction = "1";
					break;
				case 1:
					strAppTypeAction = "0";
					break;
				}

				WebCallDocumentActionPerform();
			}
		} else if (v == imgShare) {
			File mDocumentFile = new File(strDocFilePath);
			if (mDocumentFile != null && mDocumentFile.exists()) {
				CommonMethod.dialogFileShareApp(getActivity(), strGuid, "",
						mDocumentFile.getAbsolutePath());
			} else {
				CommonMethod.showPopupValidation(getActivity(), getResources()
						.getString(R.string.validation_docnotfound), false);
			}
		}
	}

	// -------------Webservice Call

	/**
	 * call messages api and reloat message tab
	 */
	private void WebCallDocumentActionPerform() {
		if (strAppTypeAction == null || strAppTypeAction.trim().length() < 0) {
			CommonMethod.showPopupValidation(getActivity(), getResources()
					.getString(R.string.validation_agreedissagree), false);
			return;
		}
		showKcsDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Calendar cal = Calendar.getInstance();
					long milisecond = cal.getTimeInMillis();
					strAppActionTime = CommonMethod.getDate(milisecond,
							CommonVariable.DATABASE_DATE_FORMAT);
					// Appreadtime,appactiontime,docguid,claimauthtoken
					final Model_WebResponse modelResponse = WebCalls
							.setDocumentGuidAction(strAppReadTime,
									strAppActionTime, strAppTypeAction,
									strGuid, strClaimAuthToken);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {
								showPopupValidation(
										getActivity(),
										getActivity()
												.getResources()
												.getString(
														R.string.validation_agree_message));

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
							dismissKcsDialog();

						}
					});
				}

			}
		}).start();
		;

	}

	private void showKcsDialog() {
		if (getActivity() == null) {
			ViewMyFolder myFolder = new ViewMyFolder();

		}
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

	public void showPopupValidation(final Activity activity, String message) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.xml_popup_validation_message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		TextView txtMessage = (TextView) dialog
				.findViewById(R.id.xml_popup_validation_txtMessage);
		txtMessage.setText(message);

		final Button btnOk = (Button) dialog
				.findViewById(R.id.xml_popupvalidation_btnOk);

		final RelativeLayout relOk = (RelativeLayout) dialog
				.findViewById(R.id.xml_popupvalidation_relOk);

		relOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOk.performClick();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				// ((BaseContainerFragment)
				// getParentFragment()).replaceFragment(
				// new FragmentMyFolder(), true);
				((ViewMainFragment) getActivity()).actionbar
						.selectTab(((ViewMainFragment) getActivity()).messageTab);

				// ((ViewMainFragment) getActivity()).actionbar
				// .selectTab(((ViewMainFragment) getActivity()).myFolderTab);
			}
		});
		if (!activity.isFinishing()) {
			dialog.show();
		}
	}
}
