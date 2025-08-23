package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.util.Map;

public class PaymentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, String> parameters;
    private String postUrl;
    private Map<String, String> mappingLabels;


    public void setParameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, String> getParameters()
    {
        return this.parameters;
    }


    public void setPostUrl(String postUrl)
    {
        this.postUrl = postUrl;
    }


    public String getPostUrl()
    {
        return this.postUrl;
    }


    public void setMappingLabels(Map<String, String> mappingLabels)
    {
        this.mappingLabels = mappingLabels;
    }


    public Map<String, String> getMappingLabels()
    {
        return this.mappingLabels;
    }
}
