package com.example.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Model_DocumentTypes;
import com.example.trueclaims.TrueClaimsApp;

public class Tbl_DocumentTypes {

	public static final String TableName = "DocumentTypes";
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String RESPONSE_TEMPLATE = "response_template";
	public static final String ACTION_PROMPT = "action_prompt";
	public static final String RECORD_CREATED_AT = "record_created_at";

	public static ArrayList<Model_DocumentTypes> SelectAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_DocumentTypes> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName;
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_DocumentTypes>();
			do {
				Model_DocumentTypes model = new Model_DocumentTypes();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.code = (cursor.getString(cursor.getColumnIndex(CODE)));
				model.name = (cursor.getString(cursor.getColumnIndex(NAME)));
				model.response_template = (cursor.getString(cursor
						.getColumnIndex(RESPONSE_TEMPLATE)));
				model.action_prompt = (cursor.getString(cursor
						.getColumnIndex(ACTION_PROMPT)));
				model.record_created_at = (cursor.getString(cursor
						.getColumnIndex(RECORD_CREATED_AT)));
				arrModelList.add(model);
			} while (cursor.moveToNext());
			cursor.close();
		}// end if(cursor!=null)
		return arrModelList;
	}

	public static void Insert(ArrayList<Model_DocumentTypes> arrListModel) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		for (Model_DocumentTypes model : arrListModel) {
			ContentValues values = new ContentValues();
			values.put(CODE, model.code);
			values.put(NAME, model.name);
			values.put(RESPONSE_TEMPLATE, model.response_template);
			values.put(ACTION_PROMPT, model.action_prompt);
			values.put(RECORD_CREATED_AT, model.record_created_at);
			sqldb.insert(TableName, null, values);
		}
		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}// End insert method

	public static int deleteAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		int row = sqldb.delete(TableName, null, null);
		return row;
	}
}