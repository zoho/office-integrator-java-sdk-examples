package com.zoho.officeintegrator.v1.examples.pdfeditor;

import java.util.ArrayList;
import java.util.List;
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
import com.zoho.officeintegrator.v1.CreateDocumentResponse;
import com.zoho.officeintegrator.v1.DocumentDeleteSuccessResponse;
import com.zoho.officeintegrator.v1.EditPdfParameters;
import com.zoho.officeintegrator.v1.InvalidConfigurationException;
import com.zoho.officeintegrator.v1.PdfEditorResponseHandler;
import com.zoho.officeintegrator.v1.V1Operations;

public class DeletePDFDocument {

	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DeletePDFDocument.class.getName());

	public static void main(String args[]) {
		
		try {
			//SDK Initialisation code starts. Move this code to common place and initialise once

			initializeSdk();

			V1Operations sdkOperations = new V1Operations();
			EditPdfParameters editPDFParams = new EditPdfParameters();

			editPDFParams.setUrl("https://demo.office-integrator.com/zdocs/EventForm.pdf");
			
			APIResponse<PdfEditorResponseHandler> response = sdkOperations.editPdf(editPDFParams);
			int responseStatusCode = response.getStatusCode();
			
			if ( responseStatusCode >= 200 && responseStatusCode <= 299 ) {
				CreateDocumentResponse responseObj = (CreateDocumentResponse) response.getObject();

				String documentId = responseObj.getDocumentId();
				
				LOGGER.log(Level.INFO, "PDF Document ID - {0} created to demontrate the pdf document delete api.", new Object[] { documentId }); //No I18N
				
				response = sdkOperations.deletePdfDocument(documentId);
				
				if ( responseStatusCode >= 200 && responseStatusCode <= 299 ) {
					DocumentDeleteSuccessResponse deleteResponseObj = (DocumentDeleteSuccessResponse) response.getObject();
					
					LOGGER.log(Level.INFO, "PDF Document delete status - {0}", new Object[] { deleteResponseObj.getDocumentDeleted() }); //No I18N
				} else {
					InvalidConfigurationException invalidConfiguration = (InvalidConfigurationException) response.getObject();

					String errorMessage = invalidConfiguration.getMessage();
					
					Integer errorCode = invalidConfiguration.getCode();
					String errorKeyName = invalidConfiguration.getKeyName();
					String errorParameterName = invalidConfiguration.getParameterName();
					
					LOGGER.log(Level.INFO, "PDF Document Delete API configuration error - {0} error code - {1} key - {2} param name - {3}", new Object[] { errorMessage, errorCode, errorKeyName, errorParameterName }); //No I18N
				}
				
			} else {
				InvalidConfigurationException invalidConfiguration = (InvalidConfigurationException) response.getObject();

				String errorMessage = invalidConfiguration.getMessage();
				
				Integer errorCode = invalidConfiguration.getCode();
				String errorKeyName = invalidConfiguration.getKeyName();
				String errorParameterName = invalidConfiguration.getParameterName();
				
				LOGGER.log(Level.INFO, "configuration error - {0} error code - {1} key - {2} param name - {3}", new Object[] { errorMessage, errorCode, errorKeyName, errorParameterName }); //No I18N
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Exception in deleting pdf document - ", e); //No I18N
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
			LOGGER.log(Level.INFO, "Exception in deleting pdf document - ", e); //No I18N
		}
		return status;
	}
}
