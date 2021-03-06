package com.example.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.adapter.AdapterWelcome;
import com.example.trueclaims.R;
import com.example.utils.CommonMethod;
import com.example.utils.CommonVariable;

/**
 * 
 * @author sanket
 * 
 */
public class ViewWelcome extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {
	private PagerAdapter myAdapter;
	private ViewPager pagerWelcomeImage;
	private AdapterWelcome adapterWelcome;
	private int[] imageArray = { R.drawable.android_instant_message_image,
			R.drawable.android_instant_message_image,
			R.drawable.android_updates_image };
	private LinearLayout linearBulletView;
	private Button btnGetStarted;
private RelativeLayout rltvGetStarted;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		init();
	}
	

	private void init() {
		pagerWelcomeImage = (ViewPager) findViewById(R.id.welcome_pager);
//		btnGetStarted = (Button) findViewById(R.id.welcome_btnGetStarted);
		linearBulletView = (LinearLayout) findViewById(R.id.welcome_linearview_bullet);
		adapterWelcome = new AdapterWelcome(ViewWelcome.this, imageArray);
		rltvGetStarted=(RelativeLayout) findViewById(R.id.welcome_rltvgetstarted);
		pagerWelcomeImage.setAdapter(adapterWelcome);
		for (int i = 0; i < imageArray.length; i++) {
			ImageView imgBullet = new ImageView(ViewWelcome.this);
			imgBullet.setImageResource(R.drawable.shape_circle_white);
			int padding = (int) getResources().getDimension(R.dimen.margin_5dp);
			imgBullet.setPadding(padding, 0, padding, 0);
			imgBullet.setScaleType(ScaleType.CENTER);
			imgBullet.setId(i);
			if (i == 0) {

				imgBullet.setImageResource(R.drawable.shape_fill_circle_black);
			}
			linearBulletView.addView(imgBullet);
		}
		rltvGetStarted.setOnClickListener(this);
		pagerWelcomeImage.setCurrentItem(0);
		pagerWelcomeImage.setCurrentItem(1);
		pagerWelcomeImage.setCurrentItem(0);
		pagerWelcomeImage.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == rltvGetStarted) {
			Intent intent = new Intent(this, ViewHomeScreen.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_in_from_left,
					R.anim.push_out_to_right);
			finish();
		}
	}

	int index;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		CommonMethod.showPopupConfirm(ViewWelcome.this,
				CommonVariable.EXIT_APPTEXT);
	}

	@Override
	public void onPageSelected(int pos) {
		index = pos;
		if (imageArray == null || imageArray.length <= 0) {
			return;
		}
		for (int i = 0; i < imageArray.length; i++) {
			ImageView img = ((ImageView) linearBulletView.findViewById(i));

			if (i == pos) {
				img.setImageResource(R.drawable.shape_fill_circle_black);
			} else {
				img.setImageResource(R.drawable.shape_circle_white);
			}

		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

}
