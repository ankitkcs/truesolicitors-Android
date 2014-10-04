package com.example.utils;

public class CommonVariable {

	/**
	 * Webservice Url
	 */
	// Base Url
	public static final String BASE_WEBSERVICE_URL = "http://82.196.11.49/api/";
	// UserName
	public static final String AUTH_USERNAME = "true_solicitors";
	public static final String AUTH_PASSWORD = "3bf73122ecbd29c667e599d679ad2ef4";
	// LinkToClaim
	public static final String API_LINK_TO_CLAIM = BASE_WEBSERVICE_URL
			+ "accounts/true_solicitors/claims/link";
	public static final String RESPONSE_LINKTOCLAIM_ARRAYNAME = "linked_claim";
	public static final String RESPONSE_CODE = "response_code";
	public static final String RESPONSE_MESSAGE = "response_message";
	// ---------------------Messages Api ---------------------//
	public static final String LINKTOCLAIM_SUCCESS_MESSAGE = "You have successfully linked your Claim, number CLAIM_NUMBER  to this app.";
	public static final String API_GET_MESSAGES = BASE_WEBSERVICE_URL
			+ "messages";
	public static final String RESPONSE_GET_MESSAGES_ARRAYNAME = "messages";
	// ---------------------New Messages Api ---------------------//
	public static final String API_POST_NEWMESSAGES = BASE_WEBSERVICE_URL
			+ "messages";
	public static final String RESPONSE_NEWMESSAGES_ARRAYNAME = "message";
	// ---------------------Document Api ---------------------//
	public static final String API_GET_DOCUMENTS = BASE_WEBSERVICE_URL
			+ "documents";
	public static final String RESPONSE_GET_DOCUMENTS_ARRAYNAME = "documents";

	public static final String PUT_EXTRA_DOCUMENT_GUID = "documents_guid";
	// ---------------------Document Detail Api ---------------------//
	public static final String API_GET_DOCUMENT_DETAIL = BASE_WEBSERVICE_URL
			+ "documents";
	public static final String RESPONSE_GET_DOCUMENTSDETAIL_ARRAYNAME = "document";
	// --------------------Document Action(Read,Action)Detail Api ------//
	public static final String API_POST_DOCUMENT_ACTION = BASE_WEBSERVICE_URL
			+ "documents";
	public static final String RESPONSE_GET_DOCUMENTSACTION_ARRAYNAME = "document";

	public static final String PUT_EXTRA_DOCUMENT_READTIME = "app_date_read_at";
	public static final String PUT_EXTRA_DOCUMENT_ACTIONTIME = "app_date_actioned_at";
	// --------------------Document Type Api ------//
	public static final String API_GET_DOCUMENTTYPE = BASE_WEBSERVICE_URL
			+ "document_types";
	public static final String RESPONSE_GET_DOCUMENTTYPE_ARRAYNAME = "document_types";

	// ---------------------Webservice Api End ---------------------//
	public static String DIALOG_SUCCESS_MESSAGE_DESC = "You have successfully linked \nyour Claim, number CLAIM_NUMBER\n to this app.\n\n You can now track the current\nstatus of your TRUE Claim.";

	public static final String NA_STRING = "";
	// DATE FORMAT
	public static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATABASE_DATE_FORMAT_WITHOUT_TIME = "yyyy-MM-dd";
	public static final String MESSAGE_DISPLAY_DATE_FORMAT = "dd-MMM-yyyy HH.mm a";
	public static final String MESSAGE_DISPLAY_DATE_FORMAT_WITHOUTDATE = "HH.mm a";
	public static final String DISPLAY_DATE_FORMAT_WITH_DAY = "E, MMM dd, yyyy";
	
	
	// Checking First Time Dialog Is Open or not
	public static final String PREFS_DIALOG_MYFOLDER_ISOPEN = "prefs_myfolder_dialog";
	public static final String PREFS_DIALOG_MESSAGE_ISOPEN = "prefs_message_dialog";
	public static final String PREFS_DIALOG_PASSCODE_SAVE_ALREADY = "prefs_myfolder_passcode";
	public static final String PREFS_FIRSTTIME_HOMESCREEN = "prefs_homescreen";
	public static final String PREFS_FOLDER_PASSWORD = "prefs_password";
	public static final String PUT_EXTRA_CLAIM_NO = "claim_number";

	public static String PUT_EXTRA_ClAIM_ACCOUNT_ID = "account_id";
	public static final String PUT_EXTRA_CLAIM_AUTH_TOKEN = "auth_token";
	public static final String SHARE_APP_SUBJECT = "TrueClaims App";
	public static final String SHARE_APP_TEXT = "TrueClaims App Link\n http://www.play.google.com";

	public static String EXIT_APPTEXT = "Are you sure to exit the application?";
	public static String RESPONSE_CODE_SUCCESS = "1";
	public static String RESPONSE_CODE_FAILED = "0";

	// Faq Question Bundle
	public static final String PUT_EXTRA_SELECTED_POSITION = "listPosition";
	public static final String PUT_EXTRA_MORE_HEADER = "more_desc_header";
	// Tab No by default selected
	public static final String PUT_EXTRA_TAB_NO = "TabNumber";

}
