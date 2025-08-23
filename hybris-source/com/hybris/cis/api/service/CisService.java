package com.hybris.cis.api.service;

import com.hybris.cis.api.model.CisResult;

public interface CisService
{
    CisServiceType getType();


    String getId();


    CisResult testConnection();
}
