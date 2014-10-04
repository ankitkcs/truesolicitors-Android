package com.example.utils;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.example.trueclaims.R;

public class MuPdfReader {
	Activity context;
	Bundle savedInstanceState;
	Uri uri;
	private AlertDialog.Builder mAlertBuilder;
	RelativeLayout layout;
	TextView mPageNumberView;
	private boolean mButtonsVisible;

	public MuPdfReader(Activity context, RelativeLayout layout,
			TextView mPageNumberView, Bundle instanceState, Uri uri,
			boolean mButtonsVisible) {
		// TODO Auto-generated constructor stub
		this.context = context;
		savedInstanceState = instanceState;
		this.uri = uri;
		this.layout = layout;
		this.mPageNumberView = mPageNumberView;
		this.mButtonsVisible = mButtonsVisible;
		ImplementPdfActivity();
	}

	private MuPDFCore core;
	private String mFileName;
	private MuPDFReaderView mDocView;

	private void ImplementPdfActivity() {
		if (core == null) {
			core = (MuPDFCore) context.getLastNonConfigurationInstance();

			if (savedInstanceState != null
					&& savedInstanceState.containsKey("FileName")) {
				mFileName = savedInstanceState.getString("FileName");
			}
		}
		if (core == null) {
			byte buffer[] = null;

			if (uri.toString().startsWith("content://")) {
				// Handle view requests from the Transformer Prime's file
				// manager
				// Hopefully other file managers will use this same scheme,
				// if not
				// using explicit paths.
				Cursor cursor = context.getContentResolver().query(uri,
						new String[] { "_data" }, null, null, null);
				if (cursor.moveToFirst()) {
					String str = cursor.getString(0);
					String reason = null;
					if (str == null) {
						try {
							InputStream is = context.getContentResolver()
									.openInputStream(uri);
							int len = is.available();
							buffer = new byte[len];
							is.read(buffer, 0, len);
							is.close();
						} catch (java.lang.OutOfMemoryError e) {
							System.out
									.println("Out of memory during buffer reading");
							reason = e.toString();
						} catch (Exception e) {
							reason = e.toString();
						}
						if (reason != null) {
							buffer = null;
							Resources res = context.getResources();
							AlertDialog alert = mAlertBuilder.create();
							context.setTitle(String.format(
									res.getString(R.string.cannot_open_document_Reason),
									reason));
							alert.setButton(AlertDialog.BUTTON_POSITIVE,
									context.getString(R.string.dismiss),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											context.finish();
										}
									});
							alert.show();
							return;
						}
					} else {
						uri = Uri.parse(str);
					}
				}
			}
			if (buffer != null) {
				core = openBuffer(buffer);
			} else {
				core = openFile(Uri.decode(uri.getEncodedPath()));
			}
			if (core.countPages() == 0) {
				core = null;
			}
			if (core == null) {
				AlertDialog alert = mAlertBuilder.create();
				alert.setTitle(com.example.trueclaims.R.string.cannot_open_document);
				alert.setButton(AlertDialog.BUTTON_POSITIVE,
						context.getString(R.string.dismiss),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								context.finish();
							}
						});
				alert.show();
				return;
			}

			createUI(savedInstanceState);

		}

	}

	private MuPDFCore openFile(String path) {
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1 ? path
				: path.substring(lastSlashPos + 1));
		System.out.println("Trying to open " + path);
		try {
			core = new MuPDFCore(context, path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	private MuPDFCore openBuffer(byte buffer[]) {
		System.out.println("Trying to open byte buffer");
		try {
			core = new MuPDFCore(context, buffer);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	public void createUI(Bundle savedInstanceState) {
		if (core == null)
			return;

		mDocView = new MuPDFReaderView(context) {

			private AlphaAnimation animation1;

			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setVisibility(View.VISIBLE);
				mPageNumberView.setText(String.format("%d / %d", i + 1,
						core.countPages()));
			
				super.onMoveToChild(i);
			}

			@Override
			protected void onTapMainDocArea() {
				// if (!mButtonsVisible) {
				// mButtonsVisible=true;
				// mPageNumberView.setVisibility(View.VISIBLE);
				// }
			}

			@Override
			protected void onDocMotion() {
				// if (mButtonsVisible) {
				// mButtonsVisible = false;
				// mPageNumberView.setVisibility(View.INVISIBLE);
				// }

			}

		};
		mDocView.setAdapter(new MuPDFPageAdapter(context, core));
		// Reenstate last state if it was recorded
		SharedPreferences prefs = context.getPreferences(Context.MODE_PRIVATE);

		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean("ButtonsHidden", false))
			mPageNumberView.setVisibility(View.VISIBLE);

		// Reenstate last state if it was recorded
		// mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));
		mDocView.setDisplayedViewIndex(0);
		// Stick the document view and the buttons overlay into a parent view

		layout.addView(mDocView);

		// layout.setBackgroundResource(R.drawable.tiled_background);
		// layout.setBackgroundResource(R.color.canvas);

	}
}
