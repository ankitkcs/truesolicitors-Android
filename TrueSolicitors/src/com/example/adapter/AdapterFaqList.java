package com.example.adapter;

import com.example.trueclaims.R;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterFaqList extends BaseAdapter {
	Activity activity;
	String faqQuestionList[];
	private LayoutInflater inflater;

	public AdapterFaqList(Activity faqActivity, String[] faqQuestionList) {
		this.activity = faqActivity;
		this.faqQuestionList = faqQuestionList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return faqQuestionList.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FaqHolder holder;
		if (convertView == null) {
			holder = new FaqHolder();
			convertView = inflater.inflate(R.layout.row_faq_question, null);

			holder.txtFaqQuestion = (TextView) convertView
					.findViewById(R.id.faqquestion_txtFaqQuestion);

			convertView.setTag(holder);
		} else {
			holder = (FaqHolder) convertView.getTag();
		}

		holder.txtFaqQuestion.setText(Html.fromHtml(faqQuestionList[position]));
		return convertView;
	}

	public class FaqHolder {
		private TextView txtFaqQuestion;

	}
}
