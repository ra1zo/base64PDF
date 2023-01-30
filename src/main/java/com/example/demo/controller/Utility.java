package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.*;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class Utility {

    private Utility() {

    }

    static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
     public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
     static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
     static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = DemoApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    private static final Logger logger = Logger.getLogger(Utility.class.getName());
     static File getFile(String encodedText) {
         if(encodedText==null) return null;
         byte[] decodedText = Base64.getDecoder().decode(encodedText);
         logger.info("decoding successful");
         File invoice = new File("Invoice.pdf");
         logger.info("Invoice file is created");
         FileOutputStream fileOutputStream = null;
         try {
             fileOutputStream = new FileOutputStream(invoice);
             fileOutputStream.write(decodedText);
             logger.info("Decoded text is converted into pdf");

         } catch (Exception e) {
             logger.warning("Cant Write into File "+e.getMessage());
             return  null;
         } finally {
             try {
                 fileOutputStream.close();
             } catch (IOException e) {
                 logger.warning("Error While closing file"+e.getMessage());
             }
         }
         logger.info("File is ready");
         return invoice;
     }
}
