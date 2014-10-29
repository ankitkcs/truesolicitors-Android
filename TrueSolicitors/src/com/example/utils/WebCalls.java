package com.example.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.database.Tbl_DocumentTypes;
import com.example.database.Tbl_Documents;
import com.example.database.Tbl_LinkToClaim;
import com.example.database.Tbl_Messages;
import com.example.model.Model_DocumentTypes;
import com.example.model.Model_Documents;
import com.example.model.Model_LinkToClaim;
import com.example.model.Model_Messages;
import com.example.model.Model_WebResponse;

public class WebCalls {
	public static JSONObject jsonObject = new JSONObject();

	/**
	 * Link to claim api called
	 * 
	 * @param claim_number
	 * @param date_of_birth
	 * @param deviceUniqueId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse setLinkToClaim(String claim_number,
			String date_of_birth, String deviceUniqueId)
			throws ClientProtocolException, IOException, JSONException {
		String WebUrl = CommonVariable.API_LINK_TO_CLAIM;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("claim_number", claim_number));
		nameValuePairs.add(new BasicNameValuePair("date_of_birth",
				date_of_birth));
		nameValuePairs.add(new BasicNameValuePair("device_id", deviceUniqueId));
		// Dummy Emulator id
		// nameValuePairs.add(new BasicNameValuePair("device_id",
		// "00000000-54b3-e7c7-0000-000046bffd97"));
		Log.d("tag", "Device id" + deviceUniqueId);
		String response = HttpClient.SendHttpPost(WebUrl,
				CommonVariable.AUTH_PASSWORD, nameValuePairs);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}

		// LinkToClaim object get
		JSONObject jsonObjLinkToClaim = jObject
				.getJSONObject(CommonVariable.RESPONSE_LINKTOCLAIM_ARRAYNAME);
		Model_LinkToClaim modelLinkToClaim = new Model_LinkToClaim();
		modelLinkToClaim.claim_number = jsonObjLinkToClaim
				.getString("claim_number");
		modelLinkToClaim.account_id = jsonObjLinkToClaim
				.getString("account_id");
		modelLinkToClaim.account_name = jsonObjLinkToClaim
				.getString("account_name");
		modelLinkToClaim.customer_name = jsonObjLinkToClaim
				.getString("customer_name");
		modelLinkToClaim.accident_date = jsonObjLinkToClaim
				.getString("accident_date");
		modelLinkToClaim.linked_at = jsonObjLinkToClaim.getString("linked_at");
		modelLinkToClaim.auth_token = jsonObjLinkToClaim
				.getString("auth_token");

		Calendar cal = Calendar.getInstance();
		long milisecond = cal.getTimeInMillis();
		modelLinkToClaim.record_created_at = CommonMethod.getDate(milisecond,
				CommonVariable.DATABASE_DATE_FORMAT);
		modelWs.responseParseObject = modelLinkToClaim;
		Tbl_LinkToClaim.Insert(modelLinkToClaim);
		return modelWs;
	}

	/**
	 * messages api called
	 * 
	 * This method is getting list of message if no record when you are calling
	 * second time then select last record guid and pass in this api and getting
	 * new messages
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse getMessages(String claim_number,
			String cliam_auth_token) throws ClientProtocolException,
			IOException, JSONException {
		String WebUrl;

		// nameValuePairs.add(new BasicNameValuePair("device_id",
		// deviceUniqueId));
		ArrayList<Model_Messages> list = Tbl_Messages.SelectAll();
		if (list == null || list.size() <= 0) {
			WebUrl = CommonVariable.API_GET_MESSAGES;
		} else {

			Tbl_Messages.UpdateMessage();
			Model_Messages doc = list.get(0);
			Log.d("tag", "id is " + doc.id);
			WebUrl = CommonVariable.API_GET_MESSAGES + File.separator
					+ doc.guid;
		}
		String response = HttpClient.getHttpResponse(WebUrl, null,
				cliam_auth_token);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}
		// Message data get
		JSONArray jsonMessageArray = jObject
				.getJSONArray(CommonVariable.RESPONSE_GET_MESSAGES_ARRAYNAME);
		if (jsonMessageArray != null && jsonMessageArray.length() > 0) {

			ArrayList<Model_Messages> listMessages = new ArrayList<Model_Messages>();
			for (int i = 0; i < jsonMessageArray.length(); i++) {
				JSONObject jsonModelObject = jsonMessageArray.getJSONObject(i);
				Model_Messages modelMessage = new Model_Messages();
				modelMessage.guid = jsonModelObject.getString("guid");
				modelMessage.posted_at = jsonModelObject.getString("posted_at");
				modelMessage.from = jsonModelObject.getString("from");
				modelMessage.body = jsonModelObject.getString("body");
				modelMessage.attached_document_guids = "";
				modelMessage.is_delivered = jsonModelObject
						.getString("is_delivered");
				modelMessage.is_to_firm = jsonModelObject
						.getString("is_to_firm");
				modelMessage.is_new_message = "true";
				Calendar cal = Calendar.getInstance();
				long milisecond = cal.getTimeInMillis();
				modelMessage.record_created_at = CommonMethod.getDate(
						milisecond, CommonVariable.DATABASE_DATE_FORMAT);
				modelMessage.claim_number = claim_number;
				if (!CommonMethod.isRecordExist(Tbl_Messages.TableName,
						Tbl_Messages.GUID, modelMessage.guid)) {
					listMessages.add(modelMessage);
				}
			}
			Tbl_Messages.Insert(listMessages);
		}
		return modelWs;
	}

	/**
	 * 
	 * @param strBodyPart
	 * @param auth_token
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse sendNewMessage(String strBodyPart,
			String auth_token) throws ClientProtocolException, IOException,
			JSONException {
		String WebUrl = CommonVariable.API_POST_NEWMESSAGES;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("body", strBodyPart));
		String response = HttpClient.SendHttpPost(WebUrl, auth_token,
				nameValuePairs);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}
		// // LinkToClaim object get
		// JSONObject jsonNewObject = jObject
		// .getJSONObject(CommonVariable.RESPONSE_NEWMESSAGES_ARRAYNAME);
		// Model_Messages modeMessages = new Model_Messages();
		// modeMessages.guid = jsonNewObject.getString("guid");
		// modeMessages.posted_at = jsonNewObject.getString("posted_at");
		// modeMessages.from = jsonNewObject.getString("from");
		// modeMessages.body = jsonNewObject.getString("body");
		// modeMessages.attached_document_guids = "";
		// modeMessages.is_delivered = jsonNewObject.getString("is_delivered");
		// modeMessages.is_to_firm = jsonNewObject.getString("is_to_firm");
		//
		// Tbl_Messages.Insert(modeMessages);

		return modelWs;
	}

	/**
	 * document api called
	 * 
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse getMyFolders(String strClaimNumber,
			String cliam_auth_token) throws ClientProtocolException,
			IOException, JSONException {
		String WebUrl = CommonVariable.API_GET_DOCUMENTS;

		// nameValuePairs.add(new BasicNameValuePair("device_id",
		// deviceUniqueId));

		String response = HttpClient.getHttpResponse(WebUrl, null,
				cliam_auth_token);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}
		// LinkToClaim object get
		JSONArray jsonMessageArray = jObject
				.getJSONArray(CommonVariable.RESPONSE_GET_DOCUMENTS_ARRAYNAME);
		if (jsonMessageArray != null && jsonMessageArray.length() > 0) {
			ArrayList<Model_Documents> listDocuments = new ArrayList<Model_Documents>();
			for (int i = 0; i < jsonMessageArray.length(); i++) {
				JSONObject jsonModelObject = jsonMessageArray.getJSONObject(i);
				Model_Documents modelDocuments = new Model_Documents();
				modelDocuments.guid = jsonModelObject.getString("guid");
				modelDocuments.name = jsonModelObject.getString("name");
				modelDocuments.app_date_read_at = jsonModelObject
						.getString("app_date_read_at");
				modelDocuments.app_date_actioned_at = jsonModelObject
						.getString("app_date_actioned_at");
				modelDocuments.type_code = jsonModelObject
						.getString("type_code");
				modelDocuments.created_at = jsonModelObject
						.getString("created_at");
				modelDocuments.action_performed = jsonModelObject
						.getString("action_performed");

				Calendar cal = Calendar.getInstance();
				long milisecond = cal.getTimeInMillis();
				modelDocuments.record_created_at = CommonMethod.getDate(
						milisecond, CommonVariable.DATABASE_DATE_FORMAT);
				modelDocuments.claim_number = strClaimNumber;

				listDocuments.add(modelDocuments);

			}
			Tbl_Documents.Insert(listDocuments);
		}
		// Calling Document Type guid Webservice call
		getDocumentType(cliam_auth_token);
		return modelWs;
	}

	/**
	 * messages api called
	 * 
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse getDocumentDetailUsingGUID(
			String cliam_auth_token, String docGuid)
			throws ClientProtocolException, IOException, JSONException {
		String WebUrl = CommonVariable.API_GET_DOCUMENT_DETAIL + File.separator
				+ docGuid;

		// nameValuePairs.add(new BasicNameValuePair("device_id",
		// deviceUniqueId));

		String response = HttpClient.getHttpResponse(WebUrl, null,
				cliam_auth_token);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}

		JSONObject jsonModelObject = jObject
				.getJSONObject(CommonVariable.RESPONSE_GET_DOCUMENTSDETAIL_ARRAYNAME);
		String bodyBase64 = jsonModelObject.getString("body");
		modelWs.extraString = bodyBase64;
		return modelWs;
	}

	/**
	 * 
	 * @param strBodyPart
	 * @param auth_token
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 *             The action a user has performed 0=do not agree 1=agree
	 */
	public static Model_WebResponse setDocumentGuidAction(
			String strAppActionTime, String appTypeAction, String documentGUID,
			String strIncludeMessage, String auth_token,
			boolean isIncludeMessage) throws ClientProtocolException,
			IOException, JSONException {
		String WebUrl = CommonVariable.API_POST_DOCUMENT_ACTION
				+ File.separator + documentGUID;
		Log.d("tag", "Document Guid" + documentGUID);
		Log.d("tag", "auth token" + auth_token);

		Log.d("tag", "app_date_actioned_at " + strAppActionTime);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("app_date_actioned_at",
				strAppActionTime));
		nameValuePairs.add(new BasicNameValuePair("app_type_of_action",
				appTypeAction));

		if (isIncludeMessage) {
			nameValuePairs.add(new BasicNameValuePair("optional_message",
					strIncludeMessage));

		}
		String response = HttpClient.SendHttpPUT(WebUrl, auth_token,
				nameValuePairs);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}

		JSONObject jsonActionObj = jObject
				.getJSONObject(CommonVariable.RESPONSE_GET_DOCUMENTSACTION_ARRAYNAME);
		Model_Documents modelDoc = Tbl_Documents.selectDocument(documentGUID);

		modelDoc.name = jsonActionObj.getString("name");
		modelDoc.app_date_read_at = jsonActionObj.getString("app_date_read_at");
		modelDoc.app_date_actioned_at = jsonActionObj
				.getString("app_date_actioned_at");
		modelDoc.type_code = jsonActionObj.getString("type_code");
		modelDoc.created_at = jsonActionObj.getString("created_at");
		modelDoc.action_performed = jsonActionObj.getString("action_performed");
		Tbl_Documents.updateDocument(modelDoc);

		return modelWs;
	}

	/**
	 * setDocumentGuidReadActionPerform
	 * 
	 * @param strBodyPart
	 * @param auth_token
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * 
	 */
	public static Model_WebResponse setDocumentGuidReadActionPerform(
			String strAppReadTime, String documentGUID, String auth_token)
			throws ClientProtocolException, IOException, JSONException {
		String WebUrl = CommonVariable.API_POST_DOCUMENT_ACTION
				+ File.separator + documentGUID;
		Log.d("tag", "Document Guid" + documentGUID);
		Log.d("tag", "auth token" + auth_token);
		Log.d("tag", "app_date_read_at " + strAppReadTime);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("app_date_read_at",
				strAppReadTime));

		String response = HttpClient.SendHttpPUT(WebUrl, auth_token,
				nameValuePairs);
		JSONObject jObject = new JSONObject(response);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}

		JSONObject jsonActionObj = jObject
				.getJSONObject(CommonVariable.RESPONSE_GET_DOCUMENTSACTION_ARRAYNAME);
		Model_Documents modelDoc = Tbl_Documents.selectDocument(documentGUID);

		modelDoc.name = jsonActionObj.getString("name");
		modelDoc.app_date_read_at = jsonActionObj.getString("app_date_read_at");
		modelDoc.app_date_actioned_at = jsonActionObj
				.getString("app_date_actioned_at");
		modelDoc.type_code = jsonActionObj.getString("type_code");
		modelDoc.created_at = jsonActionObj.getString("created_at");
		modelDoc.action_performed = jsonActionObj.getString("action_performed");
		Tbl_Documents.updateDocument(modelDoc);

		return modelWs;
	}

	/**
	 * document api called
	 * 
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Model_WebResponse getDocumentType(String cliam_auth_token)
			throws ClientProtocolException, IOException, JSONException {

		// Model All DocumentTYpe
		String WebURL2 = CommonVariable.API_GET_DOCUMENTTYPE;
		String responseDocumentType = HttpClient.getHttpResponse(WebURL2, null,
				cliam_auth_token);
		JSONObject jObject2 = new JSONObject(responseDocumentType);
		Model_WebResponse modelWs = new Model_WebResponse();
		modelWs.responseCode = jObject2.getString(CommonVariable.RESPONSE_CODE);
		Log.d("tag", "modelWs.responseCode " + modelWs.responseCode);
		modelWs.responseMessage = jObject2
				.getString(CommonVariable.RESPONSE_MESSAGE);
		Log.d("tag", "modelWs.responseMessage " + modelWs.responseMessage);

		if (modelWs.responseCode
				.equalsIgnoreCase(CommonVariable.RESPONSE_CODE_FAILED)) {

			return modelWs;
		}
		// Temp Document Type Saving
		JSONArray jsonDocTypeArray = jObject2
				.getJSONArray(CommonVariable.RESPONSE_GET_DOCUMENTTYPE_ARRAYNAME);
		if (jsonDocTypeArray != null && jsonDocTypeArray.length() > 0) {
			ArrayList<Model_DocumentTypes> listDocumentsType = new ArrayList<Model_DocumentTypes>();
			for (int i = 0; i < jsonDocTypeArray.length(); i++) {
				JSONObject jsonDocObj = jsonDocTypeArray.getJSONObject(i);
				Model_DocumentTypes modelDocumentsType = new Model_DocumentTypes();
				modelDocumentsType.code = jsonDocObj.getString("code");
				modelDocumentsType.name = jsonDocObj.getString("name");
				modelDocumentsType.response_template = jsonDocObj
						.getString("response_template");
				modelDocumentsType.action_prompt = jsonDocObj
						.getString("action_prompt");
				Calendar cal = Calendar.getInstance();
				long milisecond = cal.getTimeInMillis();
				modelDocumentsType.record_created_at = CommonMethod.getDate(
						milisecond, CommonVariable.DATABASE_DATE_FORMAT);
				if (!CommonMethod.isRecordExist(Tbl_DocumentTypes.TableName,
						Tbl_DocumentTypes.CODE, modelDocumentsType.code)) {
					listDocumentsType.add(modelDocumentsType);
				}
			}

			Tbl_DocumentTypes.Insert(listDocumentsType);
		}
		return modelWs;
	}
}
