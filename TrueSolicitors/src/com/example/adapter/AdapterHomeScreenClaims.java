package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.model.Model_LinkToClaim;
import com.example.trueclaims.R;

public class AdapterHomeScreenClaims extends BaseAdapter {
	private Context context;
	private ArrayList<Model_LinkToClaim> listClaims;
	private LayoutInflater inflater;

	public AdapterHomeScreenClaims(Context ctx,
			ArrayList<Model_LinkToClaim> listClaims) {

		this.context = ctx;
		this.listClaims = listClaims;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listClaims.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listClaims.get(position);
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
			convertView = inflater.inflate(R.layout.row_homescreen_listclaims,
					null);
			holder.btnClaims = (TextView) convertView
					.findViewById(R.id.row_homescreen_btnClaimsNo);
			convertView.setTag(holder);

		} else {
			holder = (ClaimsHolder) convertView.getTag();
		}
		Model_LinkToClaim model = listClaims.get(position);
		
		holder.btnClaims.setText(model.claim_number);

		return convertView;
	}

	public class ClaimsHolder {
		TextView btnClaims;
	}
}
