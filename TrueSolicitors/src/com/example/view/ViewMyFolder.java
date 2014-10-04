package com.example.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.AdapterMyFolderList;
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
 * @author sanket
 * 
 */
public class ViewMyFolder extends BaseContainerFragment implements
		OnClickListener {

	boolean isMyFolderDialogOpen = true;
	private LinearLayout linearEmptyScreen;
	private ListView listViewFolderFiles;
	private TextView txtHeaderView;
	private EditText edtSearchView;
	private ImageView imgComposeMsg;
	private ImageView imgSearchView;
	private ImageView imgShare;
	private Button btnNextLink;
	private KcsProgressDialog kcsDialog;
	private String strClaimNumber;
	private String strClaimAuthToken = "";
	private Handler mHandler = new Handler();
	private AdapterMyFolderList adapter;
	private ArrayList<Model_Documents> listOfDocuments;
	final static int VIEWFOLDER_RESULT_CODE = 1110;
	public final static String MYFOLDER_UPDATE = "MYFOLDER_UIUPDATE";
	private View onCreateView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_myfolder, container,
				false);
		onCreateView = view;
		init(view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		listViewFolderFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("tag", "Call Fragmet MyFoldeR");
				ViewMyFolderDetail viewMyFolderDetailFragment = new ViewMyFolderDetail();
				Bundle bundle = new Bundle();
				Model_Documents modelDocuments = listOfDocuments.get(position);
				if (modelDocuments.app_date_read_at == null
						|| modelDocuments.app_date_read_at.trim()
								.equalsIgnoreCase("")) {
					Calendar cal = Calendar.getInstance();
					long milisecond = cal.getTimeInMillis();
					modelDocuments.app_date_read_at = CommonMethod.getDate(
							milisecond, CommonVariable.DATABASE_DATE_FORMAT);
					Tbl_Documents.updateDocument(modelDocuments);

				}
				bundle.putString(CommonVariable.PUT_EXTRA_DOCUMENT_GUID,
						modelDocuments.guid);
				viewMyFolderDetailFragment.setArguments(bundle);
				((BaseContainerFragment) getParentFragment()).replaceFragment(
						viewMyFolderDetailFragment, true);
			}
		});
		super.onActivityCreated(savedInstanceState);
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


	private void init(View view) {
		// TODO Auto-generated method stub
		// SetActionBar Button Hidden
		edtSearchView = ((ViewMainFragment) getActivity()).edtSearchView;
		TextView txtClaimView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		strClaimNumber = ((ViewMainFragment) getActivity()).strClaimNumber;
		strClaimAuthToken = ((ViewMainFragment) getActivity()).strAuthToken;
		txtHeaderView = ((ViewMainFragment) getActivity()).txtActionBarHeader;
		imgComposeMsg = ((ViewMainFragment) getActivity()).imgComposeMsg;
		imgSearchView = ((ViewMainFragment) getActivity()).imgSearchView;
		btnNextLink = ((ViewMainFragment) getActivity()).btnNextLink;
		imgShare = ((ViewMainFragment) getActivity()).imgShare;

		imgComposeMsg.setVisibility(View.INVISIBLE);
		imgSearchView.setVisibility(View.INVISIBLE);
		imgShare.setVisibility(View.GONE);
		btnNextLink.setVisibility(View.GONE);
		edtSearchView.setVisibility(View.GONE);
		txtHeaderView.setText(getResources().getString(
				R.string.myfolder_strMyFolderHeader));
		// ActionBar Hidden Item End
		// Checking First Time My Folder is Already Open Or Not
		linearEmptyScreen = (LinearLayout) view
				.findViewById(R.id.myfolder_linearEmptyScreenContent);
		listViewFolderFiles = (ListView) view
				.findViewById(R.id.myfolder_listViewContent);

		listViewFolderFiles.setVisibility(View.GONE);
		Log.d("tag", "OnResume Called");
		showDialogMyFolder(onCreateView);
		
	}

	private void showDialogMyFolder(View view) {
		isMyFolderDialogOpen = CommonMethod.getSharePrefrenceBoolean(
				getActivity(), CommonVariable.PREFS_DIALOG_MYFOLDER_ISOPEN);
		if (!isMyFolderDialogOpen) {
			// If Not Then Showing First Time Dialog and Passing Next Activity
			String strDialogHeader = view.getResources().getString(
					R.string.dialogsuccess_strFolderHeader);
			int dialogIcon = R.drawable.folder_popup_icon;

			String strMessageDetail = view.getResources().getString(
					R.string.dialogsuccess_strFolderDesc);
			Intent intent = new Intent(getActivity(), ViewEnterPasscode.class);
			intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_NO, strClaimNumber);
			showPopupSuccessDialog(getActivity(), strDialogHeader, dialogIcon,
					strMessageDetail,
					CommonVariable.PREFS_DIALOG_MYFOLDER_ISOPEN, intent);
			listViewFolderFiles.setVisibility(View.GONE);
			linearEmptyScreen.setVisibility(View.VISIBLE);
		} else {

			Intent intent = new Intent(getActivity(), ViewEnterPasscode.class);
			intent.putExtra(CommonVariable.PUT_EXTRA_CLAIM_NO, strClaimNumber);
			getParentFragment().startActivityForResult(intent,
					VIEWFOLDER_RESULT_CODE);

		}
	}

	// -------------Webservice Call

	/**
	 * call messages api and reloat message tab
	 */
	private void WebCallGetFolders() {
		showKcsDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final Model_WebResponse modelResponse = WebCalls
							.getMyFolders(strClaimNumber, strClaimAuthToken);
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
							dismissKcsDialog();

						}
					});
				}

			}
		}).start();
		;

	}

	private void reloadListviewData() {
		listOfDocuments = Tbl_Documents.SelectAll();
		if (listOfDocuments != null && listOfDocuments.size() > 0) {
			if (listViewFolderFiles == null) {
				listViewFolderFiles = (ListView) onCreateView
						.findViewById(R.id.myfolder_listViewContent);
			}
			linearEmptyScreen.setVisibility(View.GONE);
			listViewFolderFiles.setVisibility(View.VISIBLE);
			adapter = new AdapterMyFolderList(getActivity(), listOfDocuments);
			listViewFolderFiles.setAdapter(adapter);
		} else {
			linearEmptyScreen.setVisibility(View.VISIBLE);
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

	/**
	 * Temp Showing Success Dialog Use in MyFolder,Message,Passcode Time
	 * 
	 * @param activity
	 * @param message
	 * @param b
	 * @param prefsDialogMyfolderIsopen
	 * @return
	 */
	public Dialog showPopupSuccessDialog(final Activity activity, String title,
			int icon, String message, final String sharedPrefKey,
			final Intent intent) {

		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().setBackgroundDrawable(
		// activity.getResources().getDrawable(
		// R.drawable.shape_box_transparancy));
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_popup_background)));
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

				if (intent != null) {

					getParentFragment().startActivityForResult(intent,
							VIEWFOLDER_RESULT_CODE);
				}

			}
		});

		dialog.show();
		return dialog;
	}

	public void refreshListViewData() {
		if (!CommonMethod.isInternetAvailable(getActivity())) {
			CommonMethod.showPopupValidation(getActivity(), getResources()
					.getString(R.string.validation_internetoffline), false);
			return;
		}
		WebCallGetFolders();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("tag", "Call ViewMyFolderTab");
	}
}
