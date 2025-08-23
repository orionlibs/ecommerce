package de.hybris.platform.commercewebservices.core.consent.data;

import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import java.io.Serializable;
import java.util.List;

public class ConsentTemplateDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ConsentTemplateData> consentTemplates;


    public void setConsentTemplates(List<ConsentTemplateData> consentTemplates)
    {
        this.consentTemplates = consentTemplates;
    }


    public List<ConsentTemplateData> getConsentTemplates()
    {
        return this.consentTemplates;
    }
}
