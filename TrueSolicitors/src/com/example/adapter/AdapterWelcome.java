package com.example.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.trueclaims.R;

public class AdapterWelcome extends PagerAdapter {

	Context context;
	private LayoutInflater inflater;
	int[] imageArray;

	public AdapterWelcome(Context context, int[] arrayListPromoImages) {
		this.context = context;
		this.imageArray = arrayListPromoImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return imageArray.length;
	}

	public Object instantiateItem(View collection, int position) {
		Log.i("PAGE", position + "");
		View view = inflater.inflate(R.layout.row_welcome_pager, null);
		ImageView imgView = (ImageView) view
				.findViewById(R.id.xml_menu_viewpager_bgImage);

		imgView.setImageResource(imageArray[position]);

		((ViewPager) collection).addView(view, 0);

		return view;
	}

	@Override
	public void destroyItem(View container, int arg1, Object object) {
		
		((ViewPager) container).removeView(container);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
