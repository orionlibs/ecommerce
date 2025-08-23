package de.hybris.platform.commercefacades.consent.data;

import java.io.Serializable;
import java.util.Date;

public class ConsentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private Date consentGivenDate;
    private Date consentWithdrawnDate;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setConsentGivenDate(Date consentGivenDate)
    {
        this.consentGivenDate = consentGivenDate;
    }


    public Date getConsentGivenDate()
    {
        return this.consentGivenDate;
    }


    public void setConsentWithdrawnDate(Date consentWithdrawnDate)
    {
        this.consentWithdrawnDate = consentWithdrawnDate;
    }


    public Date getConsentWithdrawnDate()
    {
        return this.consentWithdrawnDate;
    }
}
