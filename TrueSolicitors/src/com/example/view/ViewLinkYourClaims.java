package com.example.view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.database.Tbl_LinkToClaim;
import com.example.implement.ImplementPopupAction;
import com.example.model.Model_LinkToClaim;
import com.example.model.Model_WebResponse;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;
import com.example.utils.KcsProgressDialog;
import com.example.utils.WebCalls;

/**
 * 
 * @author sanket
 * 
 */
public class ViewLinkYourClaims extends Activity implements OnClickListener,
		ImplementPopupAction {
	private EditText edtClaimNumber;
	private EditText edtClaimDob;
	private Button btnHeaderLink;
	ImageView imgBack;
	String strClaimNumber, strClaimDob;
	private KcsProgressDialog kcsDialog;
	private Handler mHandler = new Handler();
	private int year;
	private int month;
	private int day;
	private DatePicker datePickerDob;
	static final int DATE_DIALOG_ID = 999;

	String strDay = "";
	String strMonth = "";
	String strYear = "";
	ImplementPopupAction implementPopupAction;
	String previousActivity;
	private TextView txtHeader;
	boolean isForgotPasscode;
	private TextView txtResetPasscodeHeader;
	private TextView txtPasscodeDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		setContentView(R.layout.activity_linkyourclaims);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		implementPopupAction = (ImplementPopupAction) ViewLinkYourClaims.this;
	}

	public static boolean validateJavaDate(String strDate) {
		/* Check if date is 'null' */
		if (strDate.trim().equals("")) {
			return true;
		}
		/* Date is not 'null' */
		else {
			/*
			 * Set preferred date format, For example MM-dd-yyyy,
			 * MM.dd.yyyy,dd.MM.yyyy etc.
			 */
			SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
			sdfrmt.setLenient(false);
			/* Create Date object */
			Date javaDate = null;
			/* parse the string into date form */
			try {
				javaDate = sdfrmt.parse(strDate);
				System.out.println("Date after validation: " + javaDate);
			}
			/* Date format is invalid */
			catch (ParseException e) {
				System.out.println("The date you provided is in an "
						+ "invalid date format.");
				return false;
			}
			/* Return 'true' - since date is in valid format */
			return true;
		}
	} // end ValidateJavaDate

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commonview_imgback:
			finish();
			overridePendingTransition(R.anim.activity_close_enter,
					R.anim.activity_close_exit);

			break;
		case R.id.commonview_btnSubmitLink:
			strClaimNumber = edtClaimNumber.getText().toString();
			strClaimDob = edtClaimDob.getText().toString().trim();
			if (!validateJavaDate(strClaimDob)) {
				CommonMethod.showPopupValidation(this, getResources()
						.getString(R.string.linkyourclaim_wrongdate), false);
				return;
			}
			if (!isForgotPasscode) {
				// Disable -ListivewTesting
				if (CommonMethod.isRecordExist(Tbl_LinkToClaim.TableName,
						Tbl_LinkToClaim.CLAIM_NUMBER, strClaimNumber)) {
					CommonMethod.showPopupValidation(
							ViewLinkYourClaims.this,
							getResources().getString(
									R.string.linkyourclaim_claimexists), false);
					return;
				}
				if (!CommonMethod.isInternetAvailable(ViewLinkYourClaims.this)) {
					CommonMethod
							.showPopupValidation(
									ViewLinkYourClaims.this,
									getResources()
											.getString(
													R.string.validation_internetoffline),
									false);
					return;
				}
				WebCallLinkToClaim();
			} else {
				if (CommonMethod.isRecordExist(Tbl_LinkToClaim.TableName,
						Tbl_LinkToClaim.CLAIM_NUMBER, strClaimNumber)) {
					CommonMethod.setSharePrefrenceBoolean(this,
							CommonVariable.PREFS_DIALOG_MYFOLDER_ISOPEN, true);
					CommonMethod.setSharePrefrenceBoolean(this,
							CommonVariable.PREFS_DIALOG_PASSCODE_SAVE_ALREADY,
							false);
					CommonMethod.setSharePrefrenceString(this,
							CommonVariable.PREFS_FOLDER_PASSWORD, "");
					CommonMethod.showPopupSuccessDialog(
							this,
							getResources().getString(
									R.string.dialog_passwordreset_header),
							R.drawable.ic_padlock,
							getResources().getString(
									R.string.dialog_passwordreset_desc), true,
							null);
				}
			}
			break;

		default:
			break;
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

	private void WebCallLinkToClaim() {
		showKcsDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final Model_WebResponse modelResponse = WebCalls
							.setLinkToClaim(
									strClaimNumber,
									strClaimDob,
									CommonMethod
											.getDeviceUniqueID(ViewLinkYourClaims.this));
					mHandler.post(new Runnable() {

						private String strDialogHeader;

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {
								Model_LinkToClaim modelLinkResponse = (Model_LinkToClaim) modelResponse.responseParseObject;
								if (modelLinkResponse != null) {
									int dialogIcon = R.drawable.link_popup_icon;

									strDialogHeader = getResources()
											.getString(
													R.string.dialogsuccess_strMessageHeader)
											+ modelLinkResponse.customer_name
											+ " .";
									String strMessageDetail = CommonVariable.DIALOG_SUCCESS_MESSAGE_DESC

									.replace("CLAIM_NUMBER",
											modelLinkResponse.claim_number);
									CommonMethod.showPopupSuccessDialog(
											ViewLinkYourClaims.this,
											strDialogHeader, dialogIcon,
											strMessageDetail, true,
											implementPopupAction);
								}
							} else {
								CommonMethod.showPopupValidation(
										ViewLinkYourClaims.this,
										modelResponse.responseMessage, false);
							}
						}
					});

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonMethod.showWebServiceCallErrorDialog(
							ViewLinkYourClaims.this, e.getMessage(), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(
							ViewLinkYourClaims.this, e.getMessage(), false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(
							ViewLinkYourClaims.this, e.getMessage(), false);
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

	private void init() {
		btnHeaderLink = (Button) findViewById(R.id.commonview_btnSubmitLink);
		imgBack = (ImageView) findViewById(R.id.commonview_imgback);
		edtClaimNumber = (EditText) findViewById(R.id.linkyourclaim_edtclaimnumber);
		edtClaimDob = (EditText) findViewById(R.id.linkyourclaim_edtclaimdob);
		txtResetPasscodeHeader = (TextView) findViewById(R.id.linkyourclaim_txtResetPasscode);
		edtClaimNumber.addTextChangedListener(new EditTextWatcher(
				edtClaimNumber));
		edtClaimDob.addTextChangedListener(new EditTextWatcher(edtClaimDob));
		txtHeader = (TextView) findViewById(R.id.commonview_txtHeader);
		txtPasscodeDesc = (TextView) findViewById(R.id.linkyourclaim_txtClaimText);
		previousActivity = getIntent().getStringExtra(
				CommonVariable.PUT_EXTRA_LINK_PREVIOUS_CLASS);
		implementPopupAction = (ImplementPopupAction) ViewLinkYourClaims.this;
		imgBack.setOnClickListener(this);
		btnHeaderLink.setOnClickListener(this);
		// Checking Previous activity LinkToClaim or Enter Passcode
		if (previousActivity != null) {
			if (previousActivity
					.equalsIgnoreCase(CommonVariable.VALIDATION_PASSCODE_SCREEN)) {
				txtHeader.setText(getResources().getString(
						R.string.forgotpasscode_header));
				isForgotPasscode = true;
				txtResetPasscodeHeader.setVisibility(View.VISIBLE);
				txtPasscodeDesc.setText(getResources().getString(
						R.string.linkyourclaim_resetsecuritydesc));
				btnHeaderLink.setText(getResources().getString(
						R.string.passcode_strSubmit ));
			} else {
				txtHeader.setText(getResources().getString(
						R.string.passcode_enterpasscode));
				isForgotPasscode = false;

				txtResetPasscodeHeader.setVisibility(View.GONE);
				txtPasscodeDesc.setText(getResources().getString(
						R.string.linkyourclaim_strAccessClaimDesc));
				btnHeaderLink.setText(getResources().getString(
						R.string.linkyourclaim_link));
			}
		} else {

			isForgotPasscode = false;
			txtHeader.setText(getResources().getString(
					R.string.passcode_enterpasscode));
			txtResetPasscodeHeader.setVisibility(View.GONE);
			txtPasscodeDesc.setText(getResources().getString(
					R.string.linkyourclaim_strAccessClaimDesc));
		}
	}

	private class EditTextWatcher implements TextWatcher {
		private EditText mEditText;

		public EditTextWatcher(EditText e) {
			mEditText = e;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void afterTextChanged(Editable s) {

			if (mEditText == edtClaimNumber) {
				if (s.toString().trim().length() <= 0) {
					btnHeaderLink.setVisibility(View.GONE);
					edtClaimNumber.setHintTextColor(getResources().getColor(
							R.color.theme_color));
				} else {
					checkLinkButtonValidation();
				}
			} else if (mEditText == edtClaimDob) {
				if (s.toString().trim().length() <= 0) {
					btnHeaderLink.setVisibility(View.GONE);
					edtClaimDob.setHintTextColor(getResources().getColor(
							R.color.theme_color));
				} else {
					checkLinkButtonValidation();
				}

			}

		}

	}

	/**
	 * Checking Validation if ClaimNumber and Dob Enter then Link button visible
	 * otherwise not show linkbutton
	 */
	private void checkLinkButtonValidation() {
		strClaimNumber = edtClaimNumber.getText().toString().trim();
		strClaimDob = edtClaimDob.getText().toString().trim();
		if (strClaimNumber != null && strClaimDob != null
				&& strClaimNumber.length() > 0 && strClaimDob.length() > 0) {
			btnHeaderLink.setVisibility(View.VISIBLE);
		} else {
			btnHeaderLink.setVisibility(View.GONE);
		}
	}

	private void showKcsDialog() {
		if (isFinishing()) {
			return;
		}
		if (kcsDialog == null)
			kcsDialog = new KcsProgressDialog(this, "", false);
		if (!kcsDialog.isShowing())
			kcsDialog.show();

	}

	private void dismissKcsDialog() {

		if (kcsDialog != null && kcsDialog.isShowing())
			kcsDialog.dismiss();
	}

	/**
	 * Handling Success Popup Ok Button If extra Action perform require
	 * 
	 * @param activity
	 * @param dialog
	 * @param btnOk
	 */
	@Override
	public void onOkClickHandler(Activity activity, Dialog dialog, Button btnOk) {

	}

}
