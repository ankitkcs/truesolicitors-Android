package com.example.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String PACKAGENAME = "com.example.trueclaims";
	private static String DB_PATH = "/data/data/" + PACKAGENAME + "/databases/";
	public static String DB_NAME = "trueclaim.sqlite";
	public static int DELETED = 1;
	public static int UPDATED = 2;
	public static int NEW_RECORD = 3;

	private static final String TAG = "DbHelper";
	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DbHelper(final Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;

	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public final void createDataBase() throws IOException {

		final boolean dbExist = checkDataBase();
		SQLiteDatabase db_Read = null;
		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			// db_Read = this.getReadableDatabase(DB_Internal);
			db_Read = this.getReadableDatabase();
			db_Read.close();

			copyDataBase();

		}
	}

	/**
	 * Restore whole database without any data
	 * 
	 * @throws IOException
	 */
	public final void RestoreDatabase() throws IOException {
		SQLiteDatabase db_Read = this.getReadableDatabase();
		db_Read.close();

		copyDataBase();
		// Log.i(TAG, "Database Restored");
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		final File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * 
	 * @throws IOException
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		final InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		final String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		final OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		final byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public final SQLiteDatabase openDataBase() {
		// Open the database
		final String myPath = DB_PATH + DB_NAME;
		if (myDataBase != null && myDataBase.isOpen()) {
			myDataBase.close();
		}
		return myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public final synchronized void closeDatabase() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(final SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(final SQLiteDatabase arg0, final int arg1,
			final int arg2) {
	}

}
