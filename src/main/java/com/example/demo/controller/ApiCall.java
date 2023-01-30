package com.example.demo.controller;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.example.demo.controller.Utility.*;


@Service
public class ApiCall {

    public String getSheetsData(int userId) throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1UTp7A3PPeGGPA_udl06H1G--ZNawS_jF5vloYhNIK4A";
        final String range = "Sheet1!A" + (userId + 1) + ":C" + (userId + 1);
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();


        List<Object> row = values.get(0);
        if (row.size() > 2 && row.get(2) != null) {
            return row.get(1).toString() + row.get(2).toString();
        } else {
            return row.get(1).toString();
        }


    }

}
