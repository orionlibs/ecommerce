package de.hybris.platform.commercefacades.consent.data;

import java.io.Serializable;

public class AnonymousConsentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String templateCode;
    private int templateVersion;
    private String consentState;


    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode;
    }


    public String getTemplateCode()
    {
        return this.templateCode;
    }


    public void setTemplateVersion(int templateVersion)
    {
        this.templateVersion = templateVersion;
    }


    public int getTemplateVersion()
    {
        return this.templateVersion;
    }


    public void setConsentState(String consentState)
    {
        this.consentState = consentState;
    }


    public String getConsentState()
    {
        return this.consentState;
    }
}
