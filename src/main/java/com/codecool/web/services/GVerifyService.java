package com.codecool.web.services;

import com.codecool.web.services.exceptions.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GVerifyService {

    private final JacksonFactory jacksonFactory = new JacksonFactory();
    private final String clientId = "34170676201-s6hhek7s226udra0di1cdnskq2p8b850.apps.googleusercontent.com";

    public String VerifyToken(String token_id) throws GeneralSecurityException, IOException, InvalidTokenException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        GoogleIdToken idToken = verifier.verify(token_id);
        if (idToken != null) {
            return idToken.getPayload().getEmail();
        }else{
            throw new InvalidTokenException("Google token invalid");
        }
    }

    public String getUserName(String token_id) throws GeneralSecurityException, IOException, InvalidTokenException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        GoogleIdToken idToken = verifier.verify(token_id);
        if (idToken != null) {
            String name = (String)idToken.getPayload().get("name");
            System.out.println(idToken.getPayload());
            return name;
        }else{
            throw new InvalidTokenException("Google token invalid");
        }
    }
}
