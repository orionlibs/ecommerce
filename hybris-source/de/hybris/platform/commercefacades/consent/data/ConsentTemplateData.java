package de.hybris.platform.commercefacades.consent.data;

import java.io.Serializable;

public class ConsentTemplateData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private Integer version;
    private boolean exposed;
    private ConsentData consentData;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setVersion(Integer version)
    {
        this.version = version;
    }


    public Integer getVersion()
    {
        return this.version;
    }


    public void setExposed(boolean exposed)
    {
        this.exposed = exposed;
    }


    public boolean isExposed()
    {
        return this.exposed;
    }


    public void setConsentData(ConsentData consentData)
    {
        this.consentData = consentData;
    }


    public ConsentData getConsentData()
    {
        return this.consentData;
    }
}
