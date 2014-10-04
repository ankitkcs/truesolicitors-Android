package com.example.view;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Model_WebResponse;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;
import com.example.utils.KcsProgressDialog;
import com.example.utils.TextDrawable;
import com.example.utils.WebCalls;

/**
 * 
 * @author sanket
 * 
 */
public class ViewNewMessage extends Activity implements OnClickListener {
	private EditText edtTo, edtMessage;
	private Button btnHeaderLink;
	private TextView txtHeaderTitle;
	private ImageView btnBack;
	private String strClaimNumber = "";
	private String strClaimAuthToken = "";
	private Bundle bundleLink;
	private Handler mHandler = new Handler();
	private KcsProgressDialog kcsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_new);

		init();
	}
	

	private void init() {
		// Getting auth_token and claim number in bundle
		bundleLink = getIntent().getExtras();
		strClaimNumber = bundleLink
				.getString(CommonVariable.PUT_EXTRA_CLAIM_NO);
		strClaimAuthToken = bundleLink
				.getString(CommonVariable.PUT_EXTRA_CLAIM_AUTH_TOKEN);
		Log.d("TOKEN", "ViewNewMessage token is"+strClaimAuthToken);
		btnHeaderLink = (Button) findViewById(R.id.commonview_btnSubmitLink);
		edtTo = (EditText) findViewById(R.id.newmessage_edtToNumber);
		edtMessage = (EditText) findViewById(R.id.newmessage_edtBodyPart);
		txtHeaderTitle = (TextView) findViewById(R.id.commonview_txtHeader);
		txtHeaderTitle.setText(getResources().getString(
				R.string.newMessage_strheader));
		btnBack = (ImageView) findViewById(R.id.commonview_imgback);
		// Set Static part
		String strToString = getResources()
				.getString(R.string.newMessage_strTo) + " ";
		edtTo.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(
				strToString), null, null, null);
		edtTo.setCompoundDrawablePadding(strToString.length() * 10);
		btnHeaderLink.setText(getResources().getString(
				R.string.newMessage_strSend));
		btnBack.setOnClickListener(this);
		btnHeaderLink.setVisibility(View.VISIBLE);
		btnHeaderLink.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commonview_btnSubmitLink:
			String strMessages = edtMessage.getText().toString().trim();
			if (strMessages == null || strMessages.equalsIgnoreCase("")) {
				return;
			}
			if (!CommonMethod.isInternetAvailable(ViewNewMessage.this)) {
				CommonMethod.showPopupValidation(
						ViewNewMessage.this,
						getResources().getString(
								R.string.validation_internetoffline), false);
			}
			WebCallLinkToClaim(strMessages);

			break;
		case R.id.commonview_imgback:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/**
	 * Send New Message Using New Message Api. Passing Body parth with claim
	 * auth token in WS.
	 * 
	 * @param strMessages
	 */
	private void WebCallLinkToClaim(final String strMessages) {
		showKcsDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final Model_WebResponse modelResponse = WebCalls
							.sendNewMessage(strMessages, strClaimAuthToken);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {
								CommonMethod
										.showPopupValidation(
												ViewNewMessage.this,
												getResources()
														.getString(
																R.string.message_validation_newmessagesuccess),
												true);

							} else {
								CommonMethod.showPopupValidation(
										ViewNewMessage.this,
										modelResponse.responseMessage, false);
							}
						}
					});

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonMethod.showWebServiceCallErrorDialog(
							ViewNewMessage.this, e.getMessage(), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(
							ViewNewMessage.this, e.getMessage(), false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					CommonMethod.showWebServiceCallErrorDialog(
							ViewNewMessage.this, e.getMessage(), false);
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
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
}
