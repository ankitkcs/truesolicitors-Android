package com.example.view;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.database.Tbl_LinkToClaim;
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
public class ViewLinkYourClaims extends Activity implements OnClickListener {
	private EditText edtClaimNumber;
	private TextView txtClaimDob;
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
			strClaimDob = txtClaimDob.getText().toString().trim();
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
				CommonMethod.showPopupValidation(
						ViewLinkYourClaims.this,
						getResources().getString(
								R.string.validation_internetoffline), false);
				return;
			}
			WebCallLinkToClaim();
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

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {
								CommonMethod
										.showPopupValidation(
												ViewLinkYourClaims.this,
												CommonVariable.LINKTOCLAIM_SUCCESS_MESSAGE
														.replace(
																"CLAIM_NUMBER",
																strClaimNumber),
												true);

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
		txtClaimDob = (TextView) findViewById(R.id.linkyourclaim_txtclaimdob);
		edtClaimNumber.addTextChangedListener(new EditTextWatcher(
				edtClaimNumber));

		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);

		long milisecond = cal.getTimeInMillis();
		String strCurrentDate = CommonMethod.getDate(milisecond,
				CommonVariable.DATABASE_DATE_FORMAT_WITHOUT_TIME);
		// set current date into textview
		txtClaimDob.setText(strCurrentDate);

		txtClaimDob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("tag", "OnClick Called");
				datePickerDialog(ViewLinkYourClaims.this);
			}
		});

		imgBack.setOnClickListener(this);
		btnHeaderLink.setOnClickListener(this);
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
			}
		}

	}

	/**
	 * Checking Validation if ClaimNumber and Dob Enter then Link button visible
	 * otherwise not show linkbutton
	 */
	private void checkLinkButtonValidation() {
		strClaimNumber = edtClaimNumber.getText().toString().trim();
		strClaimDob = txtClaimDob.getText().toString().trim();
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

	public void datePickerDialog(Activity activity) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.row_datepicker_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		datePickerDob = (DatePicker) dialog
				.findViewById(R.id.linkyourclaim_datepickerdob);
		TextView btnDone = (TextView) dialog
				.findViewById(R.id.dialog_spinner_txtDone);
		TextView btnCancel = (TextView) dialog
				.findViewById(R.id.dialog_spinner_txtCancel);

		// set selected date into datepicker also
		datePickerDob.init(year, month, day, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int selectedYear,
					int selectedMonth, int selectedDay) {
				// TODO Auto-generated method stub
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;
				strYear = year + "";
				strMonth = month <= 8 ? "0" + (month + 1) : String
						.valueOf(month + 1);
				strDay = checkDigit(day);
			}
		});

		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				txtClaimDob.setText(new StringBuilder().append(year)
						.append("-").append(strMonth).append("-")
						.append(strDay));
				checkLinkButtonValidation();
				dialog.dismiss();
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}
}
