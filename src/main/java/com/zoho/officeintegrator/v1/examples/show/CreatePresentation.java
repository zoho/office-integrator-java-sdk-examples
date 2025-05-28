package com.zoho.officeintegrator.v1.examples.show;

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
import com.zoho.officeintegrator.v1.CreatePresentationParameters;
import com.zoho.officeintegrator.v1.DocumentInfo;
import com.zoho.officeintegrator.v1.InvalidConfigurationException;
import com.zoho.officeintegrator.v1.ShowResponseHandler;
import com.zoho.officeintegrator.v1.UserInfo;
import com.zoho.officeintegrator.v1.V1Operations;
import com.zoho.officeintegrator.v1.ZohoShowEditorSettings;

public class CreatePresentation {

	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(CreatePresentation.class.getName());

	public static void main(String args[]) {
		
		try {
			//SDK Initialisation code starts. Move this code to common place and initialise once

			initializeSdk();
			
			V1Operations sdkOperations = new V1Operations();
			CreatePresentationParameters createPresentationParams = new CreatePresentationParameters();
			
			DocumentInfo documentInfo = new DocumentInfo();
			
			documentInfo.setDocumentName("Untilted Spreadsheet");
			//System time value used to generate unique document everytime. You can replace based on your application.
			documentInfo.setDocumentId("" + System.currentTimeMillis());
			
			createPresentationParams.setDocumentInfo(documentInfo);
			
			UserInfo userInfo = new UserInfo();
			
			userInfo.setUserId("100");
			userInfo.setDisplayName("John");
			
			createPresentationParams.setUserInfo(userInfo);

			ZohoShowEditorSettings showEditorSettings = new ZohoShowEditorSettings();
			
			showEditorSettings.setLanguage("en");

			createPresentationParams.setEditorSettings(showEditorSettings);
			
			Map<String, Object> permissions = new HashMap<String, Object>();
			
            permissions.put("document.edit", true);
            permissions.put("document.export", true);
			permissions.put("document.print", false);
            
			createPresentationParams.setPermissions(permissions);
			
			Map<String, Object> saveUrlParams = new HashMap<String, Object>();
			
			saveUrlParams.put("id", 123456789);
			saveUrlParams.put("auth_token", "oswedf32rk");
			
			Map<String, Object> saveUrlHeaders = new HashMap<String, Object>();
			
			saveUrlHeaders.put("id", 123456789);
			saveUrlHeaders.put("auth_token", "oswedf32rk");
			
			CallbackSettings callbackSettings = new CallbackSettings();

			callbackSettings.setRetries(2);
			callbackSettings.setTimeout(10000);
			callbackSettings.setSaveFormat("pptx");
			callbackSettings.setHttpMethodType("post");
			callbackSettings.setSaveUrlParams(saveUrlParams);
			callbackSettings.setSaveUrlHeaders(saveUrlHeaders);
			callbackSettings.setSaveUrl("https://officeintegrator.zoho.com/v1/api/webhook/savecallback/601e12157123434d4e6e00cc3da2406df2b9a1d84a903c6cfccf92c8286");

			createPresentationParams.setCallbackSettings(callbackSettings);

			APIResponse<ShowResponseHandler> response = sdkOperations.createPresentation(createPresentationParams);
			int responseStatusCode = response.getStatusCode();
			
			if ( responseStatusCode >= 200 && responseStatusCode <= 299 ) {
				CreateDocumentResponse showResponse = (CreateDocumentResponse) response.getObject();

				LOGGER.log(Level.INFO, "Presentation document id - {0}", new Object[] { showResponse.getDocumentId() }); //No I18N
				LOGGER.log(Level.INFO, "Presentation session id - {0}", new Object[] { showResponse.getSessionId() }); //No I18N
				LOGGER.log(Level.INFO, "Presentation session url - {0}", new Object[] { showResponse.getDocumentUrl() }); //No I18N

			} else {
				InvalidConfigurationException invalidConfiguration = (InvalidConfigurationException) response.getObject();

				String errorMessage = invalidConfiguration.getMessage();
				
				Integer errorCode = invalidConfiguration.getCode();
				String errorKeyName = invalidConfiguration.getKeyName();
				String errorParameterName = invalidConfiguration.getParameterName();
				
				LOGGER.log(Level.INFO, "configuration error - {0} error code - {1} key - {2} param name - {3}", new Object[] { errorMessage, errorCode, errorKeyName, errorParameterName }); //No I18N
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception in creating presentation session url - ", e); //No I18N
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
			LOGGER.log(Level.INFO, "Exception in creating document session url - ", e); //No I18N
		}
		return status;
	}
}
