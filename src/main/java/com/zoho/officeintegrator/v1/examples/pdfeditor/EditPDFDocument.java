//$Id$
package com.zoho.officeintegrator.v1.examples.pdfeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.zoho.api.authenticator.Auth;
import com.zoho.api.authenticator.Token;
import com.zoho.officeintegrator.Initializer;
import com.zoho.officeintegrator.dc.DataCenter;
import com.zoho.officeintegrator.dc.Environment;
import com.zoho.officeintegrator.logger.Logger;
import com.zoho.officeintegrator.logger.Logger.Levels;
import com.zoho.officeintegrator.util.APIResponse;
import com.zoho.officeintegrator.v1.Authentication;
import com.zoho.officeintegrator.v1.CallbackSettings;
import com.zoho.officeintegrator.v1.CreateDocumentResponse;
import com.zoho.officeintegrator.v1.DocumentInfo;
import com.zoho.officeintegrator.v1.EditPdfParameters;
import com.zoho.officeintegrator.v1.InvalidConfigurationException;
import com.zoho.officeintegrator.v1.PdfEditorResponseHandler;
import com.zoho.officeintegrator.v1.PdfEditorSettings;
import com.zoho.officeintegrator.v1.PdfEditorUiOptions;
import com.zoho.officeintegrator.v1.UserInfo;
import com.zoho.officeintegrator.v1.V1Operations;
import com.zoho.officeintegrator.v1.examples.sheet.CoEditSpreadsheet;

public class EditPDFDocument {

	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(CoEditSpreadsheet.class.getName());

	public static void main(String args[]) {
		
		try {
			//SDK Initialisation code starts. Move this code to common place and initialise once

			initializeSdk();

			V1Operations sdkOperations = new V1Operations();
			EditPdfParameters editPDFParams = new EditPdfParameters();

			editPDFParams.setUrl("https://demo.office-integrator.com/zdocs/EventForm.pdf");
			
			DocumentInfo documentInfo = new DocumentInfo();

			documentInfo.setDocumentName("EventForm.pdf");
			documentInfo.setDocumentId("" + System.currentTimeMillis());

			editPDFParams.setDocumentInfo(documentInfo);
			
			PdfEditorSettings editorSettings = new PdfEditorSettings();
			
			editorSettings.setUnit("in");
			editorSettings.setLanguage("en");

			editPDFParams.setEditorSettings(editorSettings);
			
			UserInfo userInfo = new UserInfo();
			
			userInfo.setUserId("" + System.currentTimeMillis());
			userInfo.setDisplayName("User 1");

			editPDFParams.setUserInfo(userInfo);
			
			PdfEditorUiOptions uiOptions = new PdfEditorUiOptions();
			
			uiOptions.setFileMenu("show");
			uiOptions.setSaveButton("show");
			
			//editPDFParams.setUiOptions(uiOptions);
			
			CallbackSettings callbackSettings = new CallbackSettings();
			Map<String, Object> saveUrlHeaders = new HashMap<>();
			Map<String, Object> saveUrlParams = new HashMap<>();
			
			callbackSettings.setRetries(2);
			callbackSettings.setTimeout(100000);
			callbackSettings.setSaveFormat("pdf");
			callbackSettings.setHttpMethodType("post");
			callbackSettings.setSaveUrlParams(saveUrlParams);
			callbackSettings.setSaveUrlHeaders(saveUrlHeaders);
			callbackSettings.setSaveUrl("https://officeintegrator.zoho.com/v1/api/webhook/savecallback/601e12157a25e63fc4dfd4e6e00cc3da3f9242bb2916325bcb00576beed123bb");
			
			editPDFParams.setCallbackSettings(callbackSettings);
			
			APIResponse<PdfEditorResponseHandler> response = sdkOperations.editPdf(editPDFParams);
			int responseStatusCode = response.getStatusCode();
			
			if ( responseStatusCode >= 200 && responseStatusCode <= 299 ) {
				CreateDocumentResponse pdfSessionResponse = (CreateDocumentResponse) response.getObject();
				
				LOGGER.log(Level.INFO, "PDF document id - {0}", new Object[] { pdfSessionResponse.getDocumentId() }); //No I18N
				LOGGER.log(Level.INFO, "PDF document session id - {0}", new Object[] { pdfSessionResponse.getSessionId() }); //No I18N
				LOGGER.log(Level.INFO, "PDF document session url - {0}", new Object[] { pdfSessionResponse.getDocumentUrl() }); //No I18N
			} else {
				InvalidConfigurationException invalidConfiguration = (InvalidConfigurationException) response.getObject();

				String errorMessage = invalidConfiguration.getMessage();
				
				Integer errorCode = invalidConfiguration.getCode();
				String errorKeyName = invalidConfiguration.getKeyName();
				String errorParameterName = invalidConfiguration.getParameterName();
				
				LOGGER.log(Level.INFO, "PDF Editor configuration error - {0} error code - {1} key - {2} param name - {3}", new Object[] { errorMessage, errorCode, errorKeyName, errorParameterName }); //No I18N
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception in creating pdf session url - ", e); //No I18N
		}
	}
	
	//Initialize SDK on service start up once before making any api call to office integrator sdk.
	public static boolean initializeSdk() {
		boolean status = false;

		try {

			//Sdk application log configuration
			Logger logger = new Logger.Builder()
			        .level(Levels.INFO)
			        //.filePath("<file absolute path where logs would be written>") //No I18N
			        .build();

			List<Token> tokens = new ArrayList<Token>();
			Auth auth = new Auth.Builder()
				.addParam("apikey", "2ae438cf864488657cc9754a27daa480") //Update this apikey with your own apikey signed up in office inetgrator service
				.authenticationSchema(new Authentication.TokenFlow())
				.build();
			
			tokens.add(auth);

			Environment environment = new DataCenter.Production("https://api.office-integrator.com"); // Refer this help page for api end point domain details -  https://www.zoho.com/officeintegrator/api/v1/getting-started.html

			new Initializer.Builder()
				.environment(environment)
				.tokens(tokens)
				.logger(logger)
				.initialize();
			
			status = true;
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Exception in creating pdf document session url - ", e); //No I18N
		}
		return status;
	}
}
