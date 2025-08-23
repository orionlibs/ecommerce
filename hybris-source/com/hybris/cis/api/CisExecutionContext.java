package com.hybris.cis.api;

import com.hybris.cis.api.service.CisServiceType;
import java.util.Map;

public class CisExecutionContext
{
    private CisServiceType serviceType;
    private Map<String, String> serviceConfig;
    private String userId;
    private String requestId;
    private String clientRefId;


    public CisServiceType getServiceType()
    {
        return this.serviceType;
    }


    public void setServiceType(CisServiceType serviceType)
    {
        this.serviceType = serviceType;
    }


    public Map<String, String> getServiceConfig()
    {
        return this.serviceConfig;
    }


    public void setServiceConfig(Map<String, String> serviceConfig)
    {
        this.serviceConfig = serviceConfig;
    }


    public String getUserId()
    {
        return this.userId;
    }


    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getClientRefId()
    {
        return this.clientRefId;
    }


    public void setClientRefId(String clientRefId)
    {
        this.clientRefId = clientRefId;
    }
}
