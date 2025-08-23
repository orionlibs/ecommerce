package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class HostedOrderPageRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String requestId;
    private String requestUrl;
    private String responseUrl;
    private String siteName;


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setRequestUrl(String requestUrl)
    {
        this.requestUrl = requestUrl;
    }


    public String getRequestUrl()
    {
        return this.requestUrl;
    }


    public void setResponseUrl(String responseUrl)
    {
        this.responseUrl = responseUrl;
    }


    public String getResponseUrl()
    {
        return this.responseUrl;
    }


    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }


    public String getSiteName()
    {
        return this.siteName;
    }
}
