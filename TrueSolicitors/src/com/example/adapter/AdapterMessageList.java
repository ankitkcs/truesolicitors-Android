package com.example.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Model_Messages;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;

/**
 * Showing List of Message With Search Filter
 * 
 * @author sanket
 * 
 */
public class AdapterMessageList extends BaseAdapter implements Filterable {

	private Context context;
	private ArrayList<Model_Messages> listMessage;
	private List<Model_Messages> filteredData = null;
	private LayoutInflater inflater;
	private ItemFilter mFilter = new ItemFilter();

	public AdapterMessageList(Context ctx, ArrayList<Model_Messages> listMessage) {

		this.context = ctx;
		this.filteredData = listMessage;
		this.listMessage = listMessage;

		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filteredData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ClaimsHolder holder;
		if (convertView == null) {
			holder = new ClaimsHolder();
			convertView = inflater.inflate(R.layout.row_message_claims, null);
			holder.imgIcon = (ImageView) convertView
					.findViewById(R.id.row_message_imgIcon);
			holder.txtDate = (TextView) convertView
					.findViewById(R.id.row_message_txtDate);
			holder.txtMessage = (TextView) convertView
					.findViewById(R.id.row_message_txtMessageDesc);
			holder.txtNewMessage = (TextView) convertView
					.findViewById(R.id.row_message_txtNew);

			convertView.setTag(holder);

		} else {
			holder = (ClaimsHolder) convertView.getTag();
		}
		Model_Messages model = filteredData.get(position);
		if (!model.is_to_firm.equalsIgnoreCase("true")) {
			holder.imgIcon.setImageResource(R.drawable.ic_true_messages);
		} else {
			holder.imgIcon.setImageResource(R.drawable.ic_user_messages);
		}
		try {
			holder.txtDate
					.setText(CommonMethod.DateFormatParseDisplay(filteredData
							.get(position).posted_at));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.txtMessage.setText(filteredData.get(position).body);
		if (model.is_new_message.equalsIgnoreCase("true")) {
			holder.txtNewMessage.setVisibility(View.VISIBLE);
		} else {
			holder.txtNewMessage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			final List<Model_Messages> list = listMessage;

			final ArrayList<Model_Messages> nlist = new ArrayList<Model_Messages>();

			String filterableString;
			for (int i = 0; i < listMessage.size(); i++) {
				filterableString = listMessage.get(i).body;
				if (filterableString.toLowerCase().contains(filterString)) {
					nlist.add(list.get(i));
				}
			}
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (ArrayList<Model_Messages>) results.values;
			notifyDataSetChanged();
		}

	}

	public class ClaimsHolder {
		private ImageView imgIcon;
		private TextView txtMessage;
		private TextView txtDate;
		private TextView txtNewMessage;
	}
}
