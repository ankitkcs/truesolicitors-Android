package com.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.implement.ImplementPopupAction;
import com.example.trueclaims.R;
import com.example.trueclaims.TrueClaimsApp;

@SuppressLint("NewApi")
public class CommonMethod {

	public static int getDeviceWidth(Activity activity) {
		int deviceWidth = 0;

		Point size = new Point();
		WindowManager windowManager = activity.getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			windowManager.getDefaultDisplay().getSize(size);
			deviceWidth = size.x;
		} else {
			Display display = windowManager.getDefaultDisplay();
			deviceWidth = display.getWidth();
		}
		return deviceWidth;
	}

	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Getting value in Prefs Keywise
	 * 
	 * @param activity
	 * @param KEY
	 * @return
	 */
	public static String getSharePrefrence(Context activity, String KEY) {
		SharedPreferences prefs = activity.getSharedPreferences(
				activity.getPackageName(), activity.MODE_PRIVATE);
		return prefs.getString(KEY, CommonVariable.NA_STRING);
	}

	/**
	 * Set Save Prefs Value Key wise
	 * 
	 * @param activity
	 * @param KEY
	 * @param value
	 */
	public static void setSharePrefrenceBoolean(Context activity, String KEY,
			Boolean value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				activity.getPackageName(), activity.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(KEY, value);
		editor.commit();
		editor = null;
		prefs = null;
	}

	/**
	 * Set Save Prefs Value Key wise
	 * 
	 * @param activity
	 * @param KEY
	 * @param value
	 */
	public static void setSharePrefrenceString(Context activity, String KEY,
			String value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				activity.getPackageName(), activity.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(KEY, value);
		editor.commit();
		editor = null;
		prefs = null;
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		// DateFormat formatter = new SimpleDateFormat(dateFormat);
		DateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	/**
	 * getting shredPref boolean value
	 * 
	 * @param activity
	 * @param KEY
	 * @return
	 */
	public static boolean getSharePrefrenceBoolean(Context activity, String KEY) {
		SharedPreferences prefs = activity.getSharedPreferences(
				activity.getPackageName(), activity.MODE_PRIVATE);
		return prefs.getBoolean(KEY, false);
	}

	public static String converDateFormate(String oldpattern,
			String newPattern, String dateString) {
		// SimpleDateFormat sdf = new SimpleDateFormat(oldpattern);
		SimpleDateFormat sdf = new SimpleDateFormat(oldpattern);
		Date testDate = null;
		try {
			testDate = sdf.parse(dateString);
			SimpleDateFormat formatter = new SimpleDateFormat(newPattern);
			String newFormat = formatter.format(testDate);
			System.out.println("" + newFormat);
			return newFormat;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	public static void dialogShareApp(Activity activity, String subject,
			String bodypart) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, subject);
		share.putExtra(Intent.EXTRA_TEXT, bodypart);
		activity.startActivity(Intent.createChooser(share, "Share App"));
	}

	/**
	 * 
	 * @param activity
	 * @param subject
	 * @param bodypart
	 * @param path
	 */
	public static void dialogFileShareApp(Activity activity, String subject,
			String bodypart, String path) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, subject);
		share.putExtra(Intent.EXTRA_TEXT, bodypart);
		if (path != null && path.trim().length() > 0 && new File(path).exists()) {
			share.putExtra(Intent.EXTRA_STREAM, new File(path));
		}
		activity.startActivity(Intent.createChooser(share, activity
				.getResources().getString(R.string.app_name)));
	}

	public static void showPopupConfirm(final Activity activity, String message) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.xml_popup_confirm_message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		TextView txtMessage = (TextView) dialog
				.findViewById(R.id.xml_popupconfirm_txtMessage);
		txtMessage.setText(message);
		final Button btnCancel = (Button) dialog
				.findViewById(R.id.xml_popupconfirm_btnCancel);
		final Button btnOk = (Button) dialog
				.findViewById(R.id.xml_popupconfirm_btnOk);
		final RelativeLayout relCancel = (RelativeLayout) dialog
				.findViewById(R.id.xml_popupconfirm_relCancel);
		final RelativeLayout relOk = (RelativeLayout) dialog
				.findViewById(R.id.xml_popupconfirm_relOk);

		relOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnOk.performClick();
			}
		});
		relCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnCancel.performClick();

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				activity.finish();
			}
		});
		dialog.show();
	}

	public static String getDeviceUniqueID(Activity activity) {
		// String device_unique_id = Secure.getString(
		// activity.getContentResolver(), Secure.ANDROID_ID);
		TelephonyManager mngr = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);

		return mngr.getDeviceId();
	}

	// database Operation
	public static void copyDataBase(Context mActivity) throws IOException {
		InputStream myInput = new FileInputStream(new File("/data/data/"
				+ mActivity.getPackageName() + "/databases/"
				+ "trueclaim.sqlite"));
		File files = new File(Environment.getExternalStorageDirectory()
				+ "/files/");
		files.mkdirs();
		String outFileName = Environment.getExternalStorageDirectory()
				+ "/files/trueclaim.sqlite";
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int bufferLength;
		while ((bufferLength = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, bufferLength);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	static Handler handler = new Handler();

	public static void showWebServiceCallErrorDialog(final Activity activity,
			final String message, final boolean isFinish) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				showPopupValidation(activity, message, isFinish);
			}
		});
	}

	public static void showPopupValidation(final Activity activity,
			String message, final boolean isActivityFinish) {

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
				if (isActivityFinish) {
					activity.finish();
				}
			}
		});
		if (!activity.isFinishing()) {
			dialog.show();
		}
	}

	public static void showPopupValidationInterface(final Activity activity,
			String message, final boolean isActivityFinish,
			final ImplementPopupAction implementPopupAction) {

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
				implementPopupAction.onOkClickHandler(activity, dialog, btnOk);

			}
		});
		if (!activity.isFinishing()) {
			dialog.show();
		}
	}

	/**
	 * 
	 * @param TableName
	 * @param columnName
	 * @param value
	 * @return
	 */

	// Check Record Exists or not
	public static boolean isRecordExist(String TableName, String columnName,
			String value) {

		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		String Query = "Select * from " + TableName + " where " + columnName
				+ "='" + value + "'";
		Cursor cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.getCount() <= 0) {
			cursor.close();
			return false;

		}
		return true;

	}

	// Decode Base64 string
	public static String decodeBASE64String(String base64) throws IOException {
		// Decode data on other side, by processing encoded data

		byte[] pdfAsBytes = Base64.decode(base64.toString(), 0);

		File filePath = new File(Environment.getExternalStorageDirectory()
				+ "/dummy.pdf");
		FileOutputStream os = new FileOutputStream(filePath, true);
		os.write(pdfAsBytes);
		os.flush();
		os.close();

		return filePath.getAbsolutePath();
	}

	public static String DateFormatParseDisplay(String date)
			throws ParseException {

		DateFormat inDF = new SimpleDateFormat(
				CommonVariable.DATABASE_DATE_FORMAT); // inputFormat
		DateFormat TodayDF = new SimpleDateFormat(
				CommonVariable.MESSAGE_DISPLAY_DATE_FORMAT_WITHOUTDATE); // OutputFormat
		// For today and
		// yesterday
		DateFormat FullDF = new SimpleDateFormat(
				CommonVariable.MESSAGE_DISPLAY_DATE_FORMAT); // Outputformat
		// long

		Date inDate = inDF.parse(date);
		// calendar for inputday
		Calendar inCal = new GregorianCalendar();
		inCal.setTime(inDate);
		// startOfToday
		Calendar cStartOfDate = new GregorianCalendar();
		cStartOfDate.set(Calendar.HOUR_OF_DAY, 0);
		cStartOfDate.set(Calendar.MINUTE, 0);
		cStartOfDate.set(Calendar.SECOND, 0);
		cStartOfDate.set(Calendar.MILLISECOND, 0);
		// endOfToday
		Calendar cEndOfDate = new GregorianCalendar();
		cEndOfDate.set(Calendar.HOUR_OF_DAY, 23);
		cEndOfDate.set(Calendar.MINUTE, 59);
		cEndOfDate.set(Calendar.SECOND, 59);

		// startOfYesterday
		Calendar cStartOfYesterday = new GregorianCalendar();
		cStartOfYesterday.set(Calendar.HOUR_OF_DAY, 0);
		cStartOfYesterday.set(Calendar.MINUTE, 0);
		cStartOfYesterday.set(Calendar.SECOND, 0);
		cStartOfYesterday.set(Calendar.MILLISECOND, 0);

		// endOfYesterday
		Calendar cEndOfYesterday = new GregorianCalendar();
		cEndOfYesterday.set(Calendar.HOUR_OF_DAY, 23);
		cEndOfYesterday.set(Calendar.MINUTE, 59);
		cEndOfYesterday.set(Calendar.SECOND, 59);

		if (cStartOfDate.before(inCal) && cEndOfDate.after(inCal)) {
			System.out.println("Today " + TodayDF.format(inDate));
			return "Today " + TodayDF.format(inDate);
		} else if (cStartOfYesterday.before(inCal)
				&& cEndOfYesterday.after(inCal)) {
			System.out.println("Yesterday" + TodayDF.format(inDate));
			return "Yesterday ," + TodayDF.format(inDate);
		} else {

			return FullDF.format(inDate);
		}

	}

	/**
	 * Showing Success Dialog Use in MyFolder,Message,Passcode Time
	 * 
	 * @param activity
	 * @param message
	 * @param b
	 * @param prefsDialogMyfolderIsopen
	 * @return
	 */
	public static Dialog showPopupSuccessDialog(final Activity activity,
			String title, int icon, String message,
			final boolean isFinishActivity,
			final ImplementPopupAction implementPopupAction) {

		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().setBackgroundDrawable(
		// activity.getResources().getDrawable(
		// R.drawable.shape_box_transparancy));
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(activity.getResources().getColor(
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
				if (implementPopupAction != null) {
					implementPopupAction.onOkClickHandler(activity, dialog,
							btnOk);
				}
				if (isFinishActivity) {
					activity.finish();
				}

			}
		});

		dialog.show();
		return dialog;
	}

	/**
	 * Set Font Style
	 * 
	 * @param activity
	 * @param arrTextView
	 */
	public static void setFontRobotoRegular(Activity activity,
			TextView... arrTextView) {
		Typeface font = Typeface.createFromAsset(activity.getAssets(),
				"Roboto-Light_0.ttf");
		for (TextView textView : arrTextView) {
			textView.setTypeface(font);
		}

	}

}
