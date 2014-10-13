package com.example.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Model_Messages;
import com.example.trueclaims.TrueClaimsApp;

public class Tbl_Messages {

	public static final String TableName = "Messages";
	public static final String ID = "id";
	public static final String GUID = "guid";
	public static final String POSTED_AT = "posted_at";
	public static final String FROM = "_from";
	public static final String BODY = "body";
	public static final String ATTACHED_DOCUMENT_GUIDS = "attached_document_guids";
	public static final String IS_DELIVERED = "is_delivered";
	public static final String IS_TO_FIRM = "is_to_firm";

	public static final String IS_NEW_MESSAGE = "is_new_message";
	public static final String RECORD_CREATED_AT = "record_created_at";
	public static final String CLAIM_NUMBER = "claim_number";

	public static ArrayList<Model_Messages> SelectAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_Messages> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName + " ORDER BY id DESC";
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_Messages>();
			do {
				Model_Messages model = new Model_Messages();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.guid = (cursor.getString(cursor.getColumnIndex(GUID)));
				model.posted_at = (cursor.getString(cursor
						.getColumnIndex(POSTED_AT)));
				model.from = (cursor.getString(cursor.getColumnIndex(FROM)));
				model.body = (cursor.getString(cursor.getColumnIndex(BODY)));
				model.attached_document_guids = (cursor.getString(cursor
						.getColumnIndex(ATTACHED_DOCUMENT_GUIDS)));
				model.is_delivered = (cursor.getString(cursor
						.getColumnIndex(IS_DELIVERED)));
				model.is_to_firm = (cursor.getString(cursor
						.getColumnIndex(IS_TO_FIRM)));
				model.is_new_message = (cursor.getString(cursor
						.getColumnIndex(IS_NEW_MESSAGE)));
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
	public static ArrayList<Model_Messages> SelectDataClaimWise(
			String strClaimNumber) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		ArrayList<Model_Messages> arrModelList = null;
		Cursor cursor = null;
		String Query = "Select * from " + TableName + " where " + CLAIM_NUMBER
				+ " ='" + strClaimNumber + "' ORDER BY id DESC";
		cursor = sqldb.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {
			arrModelList = new ArrayList<Model_Messages>();
			do {
				Model_Messages model = new Model_Messages();
				model.id = (cursor.getString(cursor.getColumnIndex(ID)));
				model.guid = (cursor.getString(cursor.getColumnIndex(GUID)));
				model.posted_at = (cursor.getString(cursor
						.getColumnIndex(POSTED_AT)));
				model.from = (cursor.getString(cursor.getColumnIndex(FROM)));
				model.body = (cursor.getString(cursor.getColumnIndex(BODY)));
				model.attached_document_guids = (cursor.getString(cursor
						.getColumnIndex(ATTACHED_DOCUMENT_GUIDS)));
				model.is_delivered = (cursor.getString(cursor
						.getColumnIndex(IS_DELIVERED)));
				model.is_to_firm = (cursor.getString(cursor
						.getColumnIndex(IS_TO_FIRM)));
				model.is_new_message = (cursor.getString(cursor
						.getColumnIndex(IS_NEW_MESSAGE)));
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

	public static void Insert(ArrayList<Model_Messages> arrListModel) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		for (Model_Messages model : arrListModel) {
			ContentValues values = new ContentValues();
			values.put(GUID, model.guid);
			values.put(POSTED_AT, model.posted_at);
			values.put(FROM, model.from);
			values.put(BODY, model.body);
			values.put(ATTACHED_DOCUMENT_GUIDS, model.attached_document_guids);
			values.put(IS_DELIVERED, model.is_delivered);
			values.put(IS_TO_FIRM, model.is_to_firm);
			values.put(IS_NEW_MESSAGE, model.is_new_message);
			values.put(RECORD_CREATED_AT, model.record_created_at);

			values.put(CLAIM_NUMBER, model.claim_number);
			
			sqldb.insert(TableName, null, values);
		}
		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}// End insert method

	public static void Insert(Model_Messages model) {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(GUID, model.guid);
		values.put(POSTED_AT, model.posted_at);
		values.put(FROM, model.from);
		values.put(BODY, model.body);
		values.put(ATTACHED_DOCUMENT_GUIDS, model.attached_document_guids);
		values.put(IS_DELIVERED, model.is_delivered);
		values.put(IS_TO_FIRM, model.is_to_firm);
		values.put(IS_NEW_MESSAGE, model.is_new_message);
		values.put(RECORD_CREATED_AT, model.record_created_at);
		values.put(CLAIM_NUMBER, model.claim_number);
		sqldb.insert(TableName, null, values);

		sqldb.setTransactionSuccessful();
		sqldb.endTransaction();
	}// End insert method
		// Update newmessage value to old message

	public static void UpdateMessage() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.execSQL("UPDATE " + TableName + " SET " + IS_NEW_MESSAGE
				+ " = 'false'");

	}// End insert method

	public static int deleteAll() {
		SQLiteDatabase sqldb = TrueClaimsApp.sqLiteDatabase;
		sqldb.beginTransaction();
		int row = sqldb.delete(TableName, null, null);
		sqldb.endTransaction();
		return row;
	}

}