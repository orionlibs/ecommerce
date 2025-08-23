package com.hybris.cis.client.rest.common;

import org.springframework.http.ResponseEntity;

public interface CisClient
{
    ResponseEntity<Void> ping(String paramString);
}
