package org.openmrs.module.smartonfhir.web;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import ca.uhn.fhir.context.FhirContext;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.MacSignatureSignerContext;
import org.keycloak.crypto.SignatureSignerContext;
import org.keycloak.jose.jws.JWSBuilder;
import org.keycloak.representations.JsonWebToken;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
	
	private FhirContext ctx;
	
	public String selectPatient(Map<String, String> claims, String tokenUrl) throws IOException {
		//check tokenUrl in trusted list
		//		if(!isTrustedRedirectUrl(tokenUrl)) {
		//		      throw new BadRequestAlertException("Error constructing redirect URL: untrusted host.", "Patient", "untrustedredirect");
		//		}
		
		// add launch context claims to token
		JsonWebToken tokenSentBack = new JsonWebToken();
		for (Entry<String, String> entry : claims.entrySet()) {
			String decodedValue = URLDecoder.decode(entry.getValue(), "UTF-8");
			tokenSentBack.setOtherClaims(entry.getKey(), decodedValue);
		}
		
		// create token signer
		KeyWrapper keyWrapper = new KeyWrapper();
		keyWrapper.setAlgorithm("HS256");
		SignatureSignerContext signer = new MacSignatureSignerContext(keyWrapper);
		
		// sign and encode launch context token
		String appToken = new JWSBuilder().jsonContent(tokenSentBack).sign(signer);//.hmac256(hmacSecretKeySpec);
		String encodedToken = URLEncoder.encode(appToken, "UTF-8");
		
		// replace placeholder in redirect URL
		String decodedUrl = URLDecoder.decode(tokenUrl, "UTF-8");
		return decodedUrl.replace("{APP_TOKEN}", encodedToken);
	}
}
