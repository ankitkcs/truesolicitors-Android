package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.database.Tbl_DocumentTypes;
import com.example.model.Model_DocumentTypes;
import com.example.model.Model_Documents;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;

/**
 * Showing List of folder and set adapter
 * 
 * @author Kcspl003
 * 
 */
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
		BitmapDrawable bd = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.tick_doc_icon);

		imageHeight = (bd.getBitmap().getHeight() - ((bd.getBitmap()
				.getHeight() * 15) / 100));
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

			holder.linearAction = (LinearLayout) convertView
					.findViewById(R.id.row_myfolder_linear2);
			holder.linearRead = (LinearLayout) convertView
					.findViewById(R.id.row_myfolder_linear1);
			convertView.setTag(holder);
			

			Log.d("tag", "ImageHeight" + imageHeight);
		} else {
			holder = (ClaimsHolder) convertView.getTag();
		}
		Model_Documents model = listMyFolder.get(position);

		Model_DocumentTypes modelSelect = Tbl_DocumentTypes
				.SelectTypeCodeModel(model.type_code);
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
			FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) holder.chkBoxStatusAction
					.getLayoutParams();

			holder.chkBoxStatusAction.getLayoutParams().height = imageHeight;
			holder.chkBoxStatusAction.getLayoutParams().width = imageHeight;
			holder.chkBoxStatusAction.setLayoutParams(params);
		} else {
			holder.chkBoxStatusAction
					.setImageResource(R.drawable.tick_doc_icon);
		}
		holder.linearRead.setVisibility(View.VISIBLE);
		if (model.app_date_read_at == null
				|| model.app_date_read_at.trim().equalsIgnoreCase("")) {
			holder.chkBoxStatusRead
					.setImageResource(R.drawable.shape_circle_withoutfill);
			FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) holder.chkBoxStatusRead
					.getLayoutParams();

			holder.chkBoxStatusRead.getLayoutParams().height = imageHeight;
			holder.chkBoxStatusRead.getLayoutParams().width = imageHeight;
			holder.chkBoxStatusRead.setLayoutParams(params);
		} else {
			holder.chkBoxStatusRead.setImageResource(R.drawable.tick_doc_icon);
		}
		if (modelSelect.action_prompt == null
				|| modelSelect.action_prompt.equalsIgnoreCase("")) {

			holder.linearAction.setVisibility(View.GONE);
		} else {
			holder.linearAction.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	public class ClaimsHolder {
		private ImageView imgIcon;
		private TextView txtFileName;
		private TextView txtDate;
		private ImageView chkBoxStatusAction;
		private ImageView chkBoxStatusRead;
		private LinearLayout linearAction, linearRead;

	}
}
