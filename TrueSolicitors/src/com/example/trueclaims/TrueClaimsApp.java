package com.example.trueclaims;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.database.DbHelper;
import com.example.utils.CommonMethod;

public class TrueClaimsApp extends Application {
	public Context mContext;
	public static SQLiteDatabase sqLiteDatabase;
	private DbHelper dbHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		dbHelper = new DbHelper(this);

		try {
			dbHelper.createDataBase();
			sqLiteDatabase = dbHelper.openDataBase();
			// Log.i("sqldb", "sqldb" + sqLiteDatabase.isOpen());
			CommonMethod.copyDataBase(mContext);
		} catch (IOException e) {
			e.printStackTrace();
		}

		

	}

	public void onDestroy() {
		dbHelper.closeDatabase();
	}
}
