package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.model.Model_Documents;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;

public class AdapterMyFolderList extends BaseAdapter {

	private Context context;
	private ArrayList<Model_Documents> listMyFolder;
	private LayoutInflater inflater;
	private int imageHeight;

	public AdapterMyFolderList(Context ctx,
			ArrayList<Model_Documents> listFolder) {

		this.context = ctx;
		this.listMyFolder = listFolder;
		Log.d("tag", "List folder size" + listFolder.size());
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMyFolder.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listMyFolder.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ClaimsHolder holder;
		if (convertView == null) {
			holder = new ClaimsHolder();
			convertView = inflater.inflate(R.layout.row_myfolder, null);
			holder.imgIcon = (ImageView) convertView
					.findViewById(R.id.row_myfolder_imgDocIcon);
			holder.txtDate = (TextView) convertView
					.findViewById(R.id.row_myfolder_txtDate);
			holder.txtFileName = (TextView) convertView
					.findViewById(R.id.row_myfolder_txtFileName);

			holder.chkBoxStatusAction = (ImageView) convertView
					.findViewById(R.id.row_myfolder_imgStatusAction);
			holder.chkBoxStatusRead = (ImageView) convertView
					.findViewById(R.id.row_myFolder_imgStatusRead);

			holder.imgDummy = (ImageView) convertView.findViewById(R.id.row_myFolder_img1);
			holder.linearAction = (LinearLayout) convertView
					.findViewById(R.id.row_myfolder_linear2);
			holder.linearRead = (LinearLayout) convertView
					.findViewById(R.id.row_myfolder_linear1);
			convertView.setTag(holder);
			BitmapDrawable bd = (BitmapDrawable) context.getResources()
					.getDrawable(R.drawable.tick_doc_icon);
			
			imageHeight = bd.getBitmap().getHeight();
		} else {
			holder = (ClaimsHolder) convertView.getTag();
		}
		Model_Documents model = listMyFolder.get(position);

		holder.txtDate.setText(CommonMethod.converDateFormate(
				CommonVariable.DATABASE_DATE_FORMAT,
				CommonVariable.DISPLAY_DATE_FORMAT_WITH_DAY, model.created_at));// Dummy
																				// date
																				// becz
																				// i
																				// don't
		// know which value set
		// as date
		holder.txtFileName.setText(model.name);
		holder.linearAction.setVisibility(View.VISIBLE);
		if (model.app_date_actioned_at == null
				|| model.app_date_actioned_at.trim().equalsIgnoreCase("")) {
			holder.chkBoxStatusAction
					.setImageResource(R.drawable.shape_circle_withoutfill);
			holder.chkBoxStatusAction.getLayoutParams().height = imageHeight;
			holder.chkBoxStatusAction.getLayoutParams().width = imageHeight;
		} else {
			holder.chkBoxStatusAction
					.setImageResource(R.drawable.tick_doc_icon);
		}
		holder.linearRead.setVisibility(View.VISIBLE);
		if (model.app_date_read_at == null
				|| model.app_date_read_at.trim().equalsIgnoreCase("")) {
			holder.chkBoxStatusRead
					.setImageResource(R.drawable.shape_circle_withoutfill);
			holder.chkBoxStatusRead.getLayoutParams().height = imageHeight;
			holder.chkBoxStatusRead.getLayoutParams().width = imageHeight;
			Log.d("tag", "Image Height " + imageHeight);
		} else {
			holder.chkBoxStatusRead.setImageResource(R.drawable.tick_doc_icon);
		}

		return convertView;
	}

	// Set Status If read or action true then showing view otherwise not show
	// view
	private void setCheckImageAndLayout(View convertView,
			LinearLayout linearView, String isFlag) {
		if (isFlag == null || isFlag.trim().equalsIgnoreCase("")) {
			linearView.setVisibility(View.GONE);

		} else {
			linearView.setVisibility(View.VISIBLE);
		}
	}

	public class ClaimsHolder {
		private ImageView imgIcon;
		private TextView txtFileName;
		private TextView txtDate;
		private ImageView chkBoxStatusAction;
		private ImageView chkBoxStatusRead;
		private LinearLayout linearAction, linearRead;
		private ImageView imgDummy;

	}
}
