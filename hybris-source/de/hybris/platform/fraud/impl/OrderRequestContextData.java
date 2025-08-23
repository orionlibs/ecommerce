package de.hybris.platform.fraud.impl;

import java.util.HashMap;
import java.util.Map;

public class OrderRequestContextData
{
    private String ipAddress;
    private String domain;


    public String getIpAddress()
    {
        return this.ipAddress;
    }


    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }


    public String getDomain()
    {
        return this.domain;
    }


    public void setDomain(String domain)
    {
        this.domain = domain;
    }


    public Map<String, String> asMap()
    {
        Map<String, String> result = new HashMap<>();
        result.put("IP", this.ipAddress);
        result.put("domain", this.domain);
        return result;
    }
}
