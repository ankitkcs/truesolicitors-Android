package com.example.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Model_Documents;
import com.example.model.Model_Messages;
import com.example.trueclaims.TrueClaimsApp;

public class Tbl_Documents {

	public static final String TableName = "Documents";
	public static final String ID = "id";
	public static final String GUID = "guid";
	public static final String NAME = "name";
	public static final String APP_DATE_READ_AT = "app_date_read_at";
	public static final String APP_DATE_ACTIONED_AT = "app_date_actioned_at";
	public static final String TYPE_CODE = "type_code";
	public static final String CREATED_AT = "created_at";
	public static final String RECORD_CREATED_AT = "record_created_at";
	public static final String CLAIM_NUMBER = "claim_number";

	public static ArrayList<Model_Documents> SelectAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_Documents> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName;
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_Documents>();
			do {
				Model_Documents model = new Model_Documents();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.guid = (cursor.getString(cursor.getColumnIndex(GUID)));
				model.name = (cursor.getString(cursor.getColumnIndex(NAME)));
				model.app_date_read_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_READ_AT)));
				model.app_date_actioned_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_ACTIONED_AT)));
				model.type_code = (cursor.getString(cursor
						.getColumnIndex(TYPE_CODE)));
				model.created_at = (cursor.getString(cursor
						.getColumnIndex(CREATED_AT)));
				model.record_created_at = (cursor.getString(cursor
						.getColumnIndex(RECORD_CREATED_AT)));
				model.claim_number = (cursor.getString(cursor
						.getColumnIndex(CLAIM_NUMBER)));
				arrModelList.add(model);
			} while (cursor.moveToNext());
			cursor.close();
		}// end if(cursor!=null)
		return arrModelList;
	}

	// Selecting Message claim number wise
	public static ArrayList<Model_Documents> SelectDataClaimWise(
			String strClaimNumber) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_Documents> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName + " where " + CLAIM_NUMBER
				+ " ='" + strClaimNumber + "' ORDER BY id DESC";
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_Documents>();
			do {
				Model_Documents model = new Model_Documents();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.guid = (cursor.getString(cursor.getColumnIndex(GUID)));
				model.name = (cursor.getString(cursor.getColumnIndex(NAME)));
				model.app_date_read_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_READ_AT)));
				model.app_date_actioned_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_ACTIONED_AT)));
				model.type_code = (cursor.getString(cursor
						.getColumnIndex(TYPE_CODE)));
				model.created_at = (cursor.getString(cursor
						.getColumnIndex(CREATED_AT)));
				model.record_created_at = (cursor.getString(cursor
						.getColumnIndex(RECORD_CREATED_AT)));
				model.claim_number = (cursor.getString(cursor
						.getColumnIndex(CLAIM_NUMBER)));
				arrModelList.add(model);
			} while (cursor.moveToNext());
			cursor.close();
		}// end if(cursor!=null)
		return arrModelList;
	}

	public static Model_Documents selectDocument(String strGuid) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;

		Cursor cursor = null;
		String Query = "Select * from " + TableName + " where "
				+ Tbl_Documents.GUID + " = '" + strGuid + "'";
		cursor = sqldb.rawQuery(Query, null);
		Model_Documents model = null;
		if (cursor != null && cursor.moveToFirst()) {

			do {
				model = new Model_Documents();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.guid = (cursor.getString(cursor.getColumnIndex(GUID)));
				model.name = (cursor.getString(cursor.getColumnIndex(NAME)));
				model.app_date_read_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_READ_AT)));
				model.app_date_actioned_at = (cursor.getString(cursor
						.getColumnIndex(APP_DATE_ACTIONED_AT)));
				model.type_code = (cursor.getString(cursor
						.getColumnIndex(TYPE_CODE)));
				model.created_at = (cursor.getString(cursor
						.getColumnIndex(CREATED_AT)));
				model.record_created_at = (cursor.getString(cursor
						.getColumnIndex(RECORD_CREATED_AT)));

				model.claim_number = (cursor.getString(cursor
						.getColumnIndex(CLAIM_NUMBER)));
			} while (cursor.moveToNext());
			cursor.close();
		}
		return model;
	}

	public static void Insert(ArrayList<Model_Documents> arrListModel) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		for (Model_Documents model : arrListModel) {
			ContentValues values = new ContentValues();
			values.put(GUID, model.guid);
			values.put(NAME, model.name);
			values.put(APP_DATE_READ_AT, model.app_date_read_at);
			values.put(APP_DATE_ACTIONED_AT, model.app_date_actioned_at);
			values.put(TYPE_CODE, model.type_code);
			values.put(CREATED_AT, model.created_at);
			values.put(RECORD_CREATED_AT, model.record_created_at);
			values.put(CLAIM_NUMBER, model.claim_number);
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

	public static void updateDocument(Model_Documents modelDoc) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(GUID, modelDoc.guid);
		values.put(NAME, modelDoc.name);
		values.put(APP_DATE_READ_AT, modelDoc.app_date_read_at);
		values.put(APP_DATE_ACTIONED_AT, modelDoc.app_date_actioned_at);
		values.put(TYPE_CODE, modelDoc.type_code);
		values.put(CREATED_AT, modelDoc.created_at);
		values.put(RECORD_CREATED_AT, modelDoc.record_created_at);
		values.put(CLAIM_NUMBER, modelDoc.claim_number);
		sqldb.update(TableName, values, Tbl_Documents.GUID + "= '"
				+ modelDoc.guid + "'", null);
		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}

	public static long totalRecordsInDb() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		Cursor cursor = null;
		String Query = "Select * from " + TableName;
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null) {
			cursor.moveToFirst();
			return cursor.getCount();
		}
		return 0;
	}
}