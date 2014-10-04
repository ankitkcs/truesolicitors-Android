package com.example.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fragment.BaseContainerFragment;
import com.example.fragment.ViewMainFragment;
import com.example.model.Model_WebResponse;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;
import com.example.utils.KcsProgressDialog;
import com.example.utils.MuPdfReader;
import com.example.utils.WebCalls;

/**
 * 
 * @author sanket
 * 
 */
public class ViewMyFolderDetail extends BaseContainerFragment implements
		OnClickListener {
	private WebView webView;
	private EditText edtSearchView;
	private TextView txtHeaderView;
	private String strClaimNumber;
	private ImageView imgShare;
	private Button btnNextLink;
	private Bundle bundleArgument;
	private static String strGuid;
	private KcsProgressDialog kcsDialog;
	private Handler mHandler = new Handler();
	private String strClaimAuthToken;
	RelativeLayout layout;
	TextView txtPageNumber;
	Bundle saveInstnace;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_myfolder_detail,
				container, false);
		init(view);
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundleArgument = getArguments();
		saveInstnace = savedInstanceState;
		if (strGuid == null || strGuid.trim().equalsIgnoreCase("")) {
			strGuid = bundleArgument
					.getString(CommonVariable.PUT_EXTRA_DOCUMENT_GUID);
		} 
	}

	private void init(View view) {
		layout = (RelativeLayout) view.findViewById(R.id.pdf_relative);
		txtPageNumber = (TextView) view.findViewById(R.id.pageNumber);
		// ActionBar Header Item
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		strClaimNumber = ((ViewMainFragment) getActivity()).strClaimNumber;
		strClaimAuthToken = ((ViewMainFragment) getActivity()).strAuthToken;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;

		txtHeaderView.setText(getResources().getString(
				R.string.myfolder_strMyFolderHeader));
		imgShare.setVisibility(View.VISIBLE);
		btnNextLink.setVisibility(View.VISIBLE);
		btnNextLink.setCompoundDrawables(null, null, null, null);
		btnNextLink
				.setText(getResources().getString(R.string.myfolder_strNext));
		btnNextLink.setOnClickListener(this);
		imgShare.setOnClickListener(this);
		if (!CommonMethod.isInternetAvailable(getActivity())) {
			CommonMethod.showPopupValidation(getActivity(), getResources()
					.getString(R.string.validation_internetoffline), false);
			return;
		}
		WebCallGetDocumentDetail();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		if (v == btnNextLink) {
			showPopupConfirmRead();
		} else if (v == imgShare) {

		}
	}

	// open for custom repeat type selected....
	public void showPopupConfirmRead() {
		final Dialog myAlertDialog = new Dialog(getActivity());

		myAlertDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myAlertDialog.setContentView(R.layout.dialog_confirmread);
		myAlertDialog.setCanceledOnTouchOutside(false);
		RadioGroup radioConfirmation = (RadioGroup) myAlertDialog
				.findViewById(R.id.dialog_fileread_radioGroup);
		final Button btnOk = (Button) myAlertDialog
				.findViewById(R.id.dialog_fileread_btnOk);
		TextView txtCusomDialogHeader = (TextView) myAlertDialog
				.findViewById(R.id.dialog_fileread_txtConfirmDoc);
		radioConfirmation
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.dialog_fileread_radioconfirm:
							btnOk.setEnabled(true);
							btnOk.setTextColor(getActivity().getResources()
									.getColor(R.color.color_black));

							break;
						case R.id.dialog_fileread_radioReadLater:

							btnOk.setTextColor(getResources().getColor(
									R.color.color_black));
							btnOk.setEnabled(true);

							break;

						default:
							break;
						}
					}
				});
		btnOk.setTextColor(getResources().getColor(R.color.color_grey));
		btnOk.setEnabled(false);
		// ok button click
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myAlertDialog.dismiss();
				ViewAgreeFile ViewAgreeFileFrag = new ViewAgreeFile();
				Bundle bundle = new Bundle();
				bundle.putString(CommonVariable.PUT_EXTRA_DOCUMENT_GUID,
						strGuid);
				ViewAgreeFileFrag.setArguments(bundle);
				((BaseContainerFragment) getParentFragment()).replaceFragment(
						ViewAgreeFileFrag, true);
			}
		});
		final Button btnCancel = (Button) myAlertDialog
				.findViewById(R.id.dialog_fileread_btnCancel);
		ViewTreeObserver vto = btnCancel.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				btnCancel.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				btnOk.setWidth(btnCancel.getMeasuredWidth());
				btnOk.setHeight(btnCancel.getMeasuredHeight());
			}
		});
		// ok button click
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if no value inserted in editbox then set by default once
				// selected.
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		});
		myAlertDialog.show();
	}

	// Get Document Detail
	// -------------Webservice Call

	/**
	 * call messages api and reloat message tab
	 */
	private void WebCallGetDocumentDetail() {
		showKcsDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final Model_WebResponse modelResponse = WebCalls
							.getDocumentDetailUsingGUID(strClaimAuthToken,
									strGuid);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (modelResponse.responseCode
									.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_SUCCESS)) {
								String base64String = modelResponse.extraString;
								if (base64String != null
										&& (!base64String.equalsIgnoreCase(""))) {
									try {

										loadDocumentDetail(base64String);
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
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

	boolean mButtonsVisible;

	// Document MuPDF Integration
	private void loadDocumentDetail(String base64String) throws IOException {
		String strDecode = CommonMethod.decodeBASE64String(base64String);
		Log.d("tag", "base64    " + strDecode);
		// webView.loadData(strDecode, "text/html", "UTF-8");

		File file = new File(strDecode);
		Uri uri = Uri.parse(file.getAbsolutePath());

		if (uri != null) {
			new MuPdfReader(getActivity(), layout, txtPageNumber, saveInstnace,
					uri, mButtonsVisible);
		}
	}
}
