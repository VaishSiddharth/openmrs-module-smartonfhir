/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.smartonfhir.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openmrs.module.smartonfhir.web.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelectController {
	
	private PatientService patientService;
	
	@RequestMapping(value = "/ws/fhir2/R4/Patient/{id}")
	public ResponseEntity<String> selectPatient(@PathVariable String id, @RequestParam String token) {
		
		//TODO keycloak location redirect
		Map<String, String> claims = new HashMap<String, String>();
		claims.put("patient", "437a59b2-42ba-4816-a5d3-701000e22917");
		String location;
		try {
			location = patientService.selectPatient(claims, token);
		}
		catch (IOException e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(location, HttpStatus.OK);
	}
}
