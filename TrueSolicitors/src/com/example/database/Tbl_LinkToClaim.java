package com.example.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Model_LinkToClaim;
import com.example.trueclaims.TrueClaimsApp;

public class Tbl_LinkToClaim {

	public static final String TableName = "LinkToClaim";
	public static final String ID = "id";
	public static final String CLAIM_NUMBER = "claim_number";
	public static final String ACCOUNT_ID = "account_id";
	public static final String ACCOUNT_NAME = "account_name";
	public static final String CUSTOMER_NAME = "customer_name";
	public static final String ACCIDENT_DATE = "accident_date";
	public static final String LINKED_AT = "linked_at";
	public static final String AUTH_TOKEN = "auth_token";
	public static final String RECORD_CREATED_AT = "record_created_at";

	public static ArrayList<Model_LinkToClaim> SelectAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_LinkToClaim> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName;
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_LinkToClaim>();
			do {
				Model_LinkToClaim model = new Model_LinkToClaim();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.claim_number = (cursor.getString(cursor
						.getColumnIndex(CLAIM_NUMBER)));
				model.account_id = (cursor.getString(cursor
						.getColumnIndex(ACCOUNT_ID)));
				model.account_name = (cursor.getString(cursor
						.getColumnIndex(ACCOUNT_NAME)));
				model.customer_name = (cursor.getString(cursor
						.getColumnIndex(CUSTOMER_NAME)));
				model.accident_date = (cursor.getString(cursor
						.getColumnIndex(ACCIDENT_DATE)));
				model.linked_at = (cursor.getString(cursor
						.getColumnIndex(LINKED_AT)));
				model.auth_token = (cursor.getString(cursor
						.getColumnIndex(AUTH_TOKEN)));
				model.record_created_at = (cursor.getString(cursor
						.getColumnIndex(RECORD_CREATED_AT)));
				arrModelList.add(model);
			} while (cursor.moveToNext());
			cursor.close();
		}// end if(cursor!=null)
		return arrModelList;
	}

	public static Model_LinkToClaim SelectRecordUsingClaimNumber(
			String claimNumber) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		Cursor cursor = null;
		String Query = "Select * from " + TableName;
		cursor = sqldb.rawQuery(Query, null);
		Model_LinkToClaim model = null;
		if (cursor != null && cursor.moveToFirst()) {

			model = new Model_LinkToClaim();
			model.id = (cursor.getString(cursor.getColumnIndex(ID)));
			model.claim_number = (cursor.getString(cursor
					.getColumnIndex(CLAIM_NUMBER)));
			model.account_id = (cursor.getString(cursor
					.getColumnIndex(ACCOUNT_ID)));
			model.account_name = (cursor.getString(cursor
					.getColumnIndex(ACCOUNT_NAME)));
			model.customer_name = (cursor.getString(cursor
					.getColumnIndex(CUSTOMER_NAME)));
			model.accident_date = (cursor.getString(cursor
					.getColumnIndex(ACCIDENT_DATE)));
			model.linked_at = (cursor.getString(cursor
					.getColumnIndex(LINKED_AT)));
			model.auth_token = (cursor.getString(cursor
					.getColumnIndex(AUTH_TOKEN)));
			model.record_created_at = (cursor.getString(cursor
					.getColumnIndex(RECORD_CREATED_AT)));
			cursor.close();

			return model;

		}// end if(cursor!=null)
		return model;

	}

	public static void Insert(ArrayList<Model_LinkToClaim> arrListModel) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		for (Model_LinkToClaim model : arrListModel) {
			ContentValues values = new ContentValues();
			values.put(CLAIM_NUMBER, model.claim_number);
			values.put(ACCOUNT_ID, model.account_id);
			values.put(ACCOUNT_NAME, model.account_name);
			values.put(CUSTOMER_NAME, model.customer_name);
			values.put(ACCIDENT_DATE, model.accident_date);
			values.put(LINKED_AT, model.linked_at);
			values.put(AUTH_TOKEN, model.auth_token);
			values.put(RECORD_CREATED_AT, model.record_created_at);
			sqldb.insert(TableName, null, values);
		}
		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}// End insert method

	public static void Insert(Model_LinkToClaim model) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(CLAIM_NUMBER, model.claim_number);
		values.put(ACCOUNT_ID, model.account_id);
		values.put(ACCOUNT_NAME, model.account_name);
		values.put(CUSTOMER_NAME, model.customer_name);
		values.put(ACCIDENT_DATE, model.accident_date);
		values.put(LINKED_AT, model.linked_at);
		values.put(AUTH_TOKEN, model.auth_token);
		values.put(RECORD_CREATED_AT, model.record_created_at);

		sqldb.insert(TableName, null, values);

		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}// End insert method

	public static int deleteAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		int row = sqldb.delete(TableName, null, null);
		return row;
	}
}