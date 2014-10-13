package com.example.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;

/**
 * This class used in Document -When click the Folder tab 
 * Everytime is call when open 2 tab.
 * @author sanket
 * 
 */
public class ViewEnterPasscode extends Activity implements OnClickListener {
	private TextView txtCommonHeader;
	private Button btnCommonSave, btnCommonBack;
	private Spanned spanned2;
	private boolean isPasscodeSaveAlready;
	private ImageView imgBack;
	private Bundle bundle;
	private String strHeader;
	private EditText edtText;
	private ImageView view1;
	private ImageView view2;
	private ImageView view3;
	private ImageView view4;

	private String savePassword;
	private TextView txtForgottenPassword;
	private RelativeLayout rltvRememberMe;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		savePassword = CommonMethod.getSharePrefrence(ViewEnterPasscode.this,
				CommonVariable.PREFS_FOLDER_PASSWORD);
		if (savePassword != null && savePassword.trim().length() > 0) {

			rltvRememberMe.setVisibility(View.GONE);
			txtForgottenPassword.setVisibility(View.VISIBLE);
		} else {
			txtForgottenPassword.setVisibility(View.GONE);
			rltvRememberMe.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		setContentView(R.layout.activity_enter_passcode);
		bundle = getIntent().getExtras();
		init();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnCommonSave) {

			if (!isPasscodeSaveAlready) {
				CommonMethod.setSharePrefrenceString(ViewEnterPasscode.this,
						CommonVariable.PREFS_FOLDER_PASSWORD, edtText.getText()
								.toString());
				String strDialogHeader = getResources().getString(
						R.string.dialog_strPasscodeSaved);
				int dialogIcon = R.drawable.tick_popup_icon;
				String strMessageDetail = getResources().getString(
						R.string.dialog_strPasscodeDesc);
				showPopupSuccessDialog(ViewEnterPasscode.this, strDialogHeader,
						dialogIcon, strMessageDetail,
						CommonVariable.PREFS_DIALOG_PASSCODE_SAVE_ALREADY);
			} else {

				if (savePassword.equals(edtText.getText().toString())) {
					Intent intent = new Intent();
					intent.putExtra("tab", "FOLDERTAB");
					setResult(RESULT_OK, intent);
					finish();
				} else {
					view1.setBackgroundResource(R.drawable.passcode_no_fill);
					view2.setBackgroundResource(R.drawable.passcode_no_fill);
					view3.setBackgroundResource(R.drawable.passcode_no_fill);
					view4.setBackgroundResource(R.drawable.passcode_no_fill);

					showPopupValidation(ViewEnterPasscode.this, getResources()
							.getString(R.string.dialog_strValidPassword),
							false, edtText);

					// Intent intent = new Intent();
					// intent.putExtra("tab", "FOLDERTAB");
					// setResult(RESULT_CANCELED, intent);
					// finish();
				}

			}
		} else if (v == imgBack) {
			CommonMethod.showPopupValidation(ViewEnterPasscode.this,
					getResources().getString(R.string.validation_password),
					false);
			return;
		}
	}

	public void showPopupValidation(final Activity activity, String message,
			final boolean isActivityFinish, final EditText edtText2) {

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
				edtText.setText("");
				edtText.setFocusable(true);

			}
		});
		if (!activity.isFinishing()) {
			dialog.show();
		}
	}

	public void onBackPressed() {

//		Intent intent = new Intent();
//		intent.putExtra("tab", "FOLDERTAB");
//		setResult(RESULT_CANCELED, intent);
//		finish();
		CommonMethod.showPopupValidation(ViewEnterPasscode.this,
				getResources().getString(R.string.validation_password),
				false);
	}

	private void init() {
		strHeader = bundle.getString(CommonVariable.PUT_EXTRA_CLAIM_NO);
		isPasscodeSaveAlready = CommonMethod.getSharePrefrenceBoolean(
				ViewEnterPasscode.this,
				CommonVariable.PREFS_DIALOG_PASSCODE_SAVE_ALREADY);

		txtCommonHeader = (TextView) findViewById(R.id.commonview_txtHeader);
		btnCommonSave = (Button) findViewById(R.id.commonview_btnSubmitLink);
		imgBack = (ImageView) findViewById(R.id.commonview_imgback);
		imgBack.setOnClickListener(this);
		// int EditTextWidth = (CommonMethod.getDeviceWidth(this) / 4) - 20;
		edtText = (EditText) findViewById(R.id.editText1);

		view1 = (ImageView) findViewById(R.id.passcode_edtpassword1);
		view2 = (ImageView) findViewById(R.id.passcode_edtpassword2);
		view3 = (ImageView) findViewById(R.id.passcode_edtpassword3);
		view4 = (ImageView) findViewById(R.id.passcode_edtpassword4);
		edtText.requestFocus();
		edtText.setClickable(false);
		txtForgottenPassword = (TextView) findViewById(R.id.passcode_txtForgottenPassword);
		rltvRememberMe = (RelativeLayout) findViewById(R.id.passcode_linearremember);
		edtText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

		edtText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 0) {
					Log.d("tag", "Length is" + s.length());
					setBackgroundResource(s.length());
				}
			}
		});
		edtText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				edtText.post(new Runnable() {
					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(edtText,
								InputMethodManager.SHOW_IMPLICIT);
					}
				});
			}
		});

		// TempSet
		txtCommonHeader.setText(getResources().getString(
				R.string.passcode_strCreatePasscode));
	}

	private void setBackgroundResource(int i) {
		ShapeDrawable d = new ShapeDrawable(new OvalShape());
		d.setIntrinsicHeight(10);
		d.setIntrinsicWidth(10);
		d.getPaint().setColor(getResources().getColor(R.color.color_red));

		if (i == 0) {
			view1.setBackgroundResource(R.drawable.passcode_no_fill);
			view2.setBackgroundResource(R.drawable.passcode_no_fill);
			view3.setBackgroundResource(R.drawable.passcode_no_fill);
			view4.setBackgroundResource(R.drawable.passcode_no_fill);
		} else if (i == 1) {
			view1.setBackgroundResource(R.drawable.passcode_filled);
			view2.setBackgroundResource(R.drawable.passcode_no_fill);
			view3.setBackgroundResource(R.drawable.passcode_no_fill);
			view4.setBackgroundResource(R.drawable.passcode_no_fill);

			btnCommonSave.setVisibility(View.GONE);
		} else if (i == 2) {

			view1.setBackgroundResource(R.drawable.passcode_filled);

			view2.setBackgroundResource(R.drawable.passcode_filled);
			view3.setBackgroundResource(R.drawable.passcode_no_fill);
			view4.setBackgroundResource(R.drawable.passcode_no_fill);

			btnCommonSave.setVisibility(View.GONE);
		} else if (i == 3) {
			view1.setBackgroundResource(R.drawable.passcode_filled);

			view2.setBackgroundResource(R.drawable.passcode_filled);
			view3.setBackgroundResource(R.drawable.passcode_filled);
			view4.setBackgroundResource(R.drawable.passcode_no_fill);

			btnCommonSave.setVisibility(View.GONE);
		} else if (i == 4) {

			view1.setBackgroundResource(R.drawable.passcode_filled);

			view2.setBackgroundResource(R.drawable.passcode_filled);
			view3.setBackgroundResource(R.drawable.passcode_filled);
			view4.setBackgroundResource(R.drawable.passcode_filled);
			txtCommonHeader.setText(strHeader);
			btnCommonSave.setVisibility(View.VISIBLE);
			Drawable img = this.getResources().getDrawable(
					R.drawable.ic_action_andorid_tick_icon);
			// btnCommonSave.setCompoundDrawablesWithIntrinsicBounds(img,
			// null, null, null);
			btnCommonSave.setOnClickListener(this);

			if (!isPasscodeSaveAlready) {

				btnCommonSave.setText(getResources().getString(
						R.string.passcode_strSave));
			} else {

				btnCommonSave.setText(getResources().getString(
						R.string.passcode_strSubmit));
			}
		}
	}

	/**
	 * Temp Showing Success Dialog Use in MyFolder,Message,Passcode Time
	 * 
	 * @param activity
	 * @param message
	 * @param b
	 * @param prefsDialogMyfolderIsopen
	 *            showPopupSuccessDialog(ViewEnterPasscode.this,
	 *            strDialogHeader, dialogIcon, strMessageDetail,
	 *            CommonVariable.PREFS_DIALOG_PASSCODE_SAVE_ALREADY);
	 * @return
	 */
	public Dialog showPopupSuccessDialog(final Activity activity, String title,
			int icon, String message, final String sharedPrefKey) {

		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().setBackgroundDrawable(
		// activity.getResources().getDrawable(
		// R.drawable.shape_box_transparancy));
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#D8D8D8")));
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_common_success_msg);
		dialog.setCanceledOnTouchOutside(false);
		// WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// lp.dimAmount=0.5f;
		// dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		final TextView txtHeader = (TextView) dialog
				.findViewById(R.id.dialogsuccess_txtHeader);
		final ImageView imgIcon = (ImageView) dialog
				.findViewById(R.id.dialogsuccess_imgIcon);
		final TextView txtMessge = (TextView) dialog
				.findViewById(R.id.dialogsuccess_txtDesc);
		txtHeader.setText(title);
		txtMessge.setText(message);
		imgIcon.setImageResource(icon);
		// txtMessage.setText(message);
		final Button btnOk = (Button) dialog
				.findViewById(R.id.dialogsuccess_btnOk);

		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				CommonMethod.setSharePrefrenceBoolean(activity, sharedPrefKey,
						true);
				Intent intent = new Intent();
				intent.putExtra("tab", "FOLDERTAB");
				setResult(RESULT_OK, intent);
				finish();

			}
		});

		dialog.show();
		return dialog;
	}

}