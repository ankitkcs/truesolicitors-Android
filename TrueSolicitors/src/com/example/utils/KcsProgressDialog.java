package com.example.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.example.trueclaims.R;

public class KcsProgressDialog extends Dialog {

	public KcsProgressDialog(Context context, String Message,
			boolean isCancelable) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kcs_progress_dialog);
		setCancelable(isCancelable);
		setCanceledOnTouchOutside(false);

		TextView txtMsg = (TextView) findViewById(R.id.kcs_customdialog_txtMessage);

		txtMsg.setText(Message);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);

	}

}
