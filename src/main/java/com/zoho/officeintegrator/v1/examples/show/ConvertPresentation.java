package com.zoho.officeintegrator.v1.examples.show;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

import com.zoho.api.authenticator.Auth;
import com.zoho.api.authenticator.Token;
import com.zoho.officeintegrator.Initializer;
import com.zoho.officeintegrator.dc.DataCenter;
import com.zoho.officeintegrator.dc.Environment;
import com.zoho.officeintegrator.logger.Logger;
import com.zoho.officeintegrator.logger.Logger.Levels;
import com.zoho.officeintegrator.util.APIResponse;
import com.zoho.officeintegrator.v1.Authentication;
import com.zoho.officeintegrator.v1.ConvertPresentationParameters;
import com.zoho.officeintegrator.v1.FileBodyWrapper;
import com.zoho.officeintegrator.v1.InvalidConfigurationException;
import com.zoho.officeintegrator.v1.ShowResponseHandler;
import com.zoho.officeintegrator.v1.V1Operations;

public class ConvertPresentation {

	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ConvertPresentation.class.getName());

	public static void main(String args[]) {
		
		try {
			//Initializing SDK once is enough. Calling here since code sample will be tested standalone. 
	        //You can place SDK initializer code in you application and call once while your application start-up. 
			initializeSdk();

			V1Operations sdkOperations = new V1Operations();
			ConvertPresentationParameters conversionParameters = new ConvertPresentationParameters();
			
			//Either use url as document source or attach the document in request body use below methods
			conversionParameters.setUrl("https://demo.office-integrator.com/samples/show/Zoho_Show.pptx");
			
			/*
			String inputFilePath = "Absolute input file path"
			File inputFile = new File(inputFilePath);
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			StreamWrapper documentStreamWrapper = new StreamWrapper(fileInputStream);

			conversionParameters.setDocument(documentStreamWrapper); */
			
			conversionParameters.setFormat("pdf");
			
			APIResponse<ShowResponseHandler> response = sdkOperations.convertPresentation(conversionParameters);
			int responseStatusCode = response.getStatusCode();

			if ( responseStatusCode >= 200 && responseStatusCode <= 299 ) {
				FileBodyWrapper fileBodyWrapper = (FileBodyWrapper) response.getObject();
				String outputFilePath = System.getProperty("user.dir") + File.separator + fileBodyWrapper.getFile().getName();
				InputStream inputStream = fileBodyWrapper.getFile().getStream();
				OutputStream outputStream = new FileOutputStream(new File(outputFilePath));
				
				IOUtils.copy(inputStream, outputStream);
				LOGGER.log(Level.INFO, "Converted document saved in output file path - {0}", new Object[] { outputFilePath }); //No I18N
			} else {
				InvalidConfigurationException invalidConfiguration = (InvalidConfigurationException) response.getObject();
				String errorMessage = invalidConfiguration.getMessage();
				
				/*Long errorCode = invalidConfiguration.getCode();
				String errorKeyName = invalidConfiguration.getKeyName();
				String errorParameterName = invalidConfiguration.getParameterName();*/
				
				LOGGER.log(Level.INFO, "Document configuration error - {0}", new Object[] { errorMessage }); //No I18N
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception in creating document session url - ", e); //No I18N
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
