package de.hybris.platform.personalizationservices.configuration.impl;

import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.LinkedList;

public class CxConfigValidationInterceptor implements ValidateInterceptor<CxConfigModel>
{
    public void onValidate(CxConfigModel model, InterceptorContext context) throws InterceptorException
    {
        if(model != null && model.getConsentTemplates() != null)
        {
            LinkedList<String> nonmatchingConsentTemplateNames = new LinkedList<>();
            for(ConsentTemplateModel consentTemplate : model.getConsentTemplates())
            {
                if(model.getBaseSites() == null || consentTemplate.getBaseSite() == null ||
                                !model.getBaseSites().contains(consentTemplate.getBaseSite()))
                {
                    nonmatchingConsentTemplateNames.add(getConsentTemplateFriendlyName(consentTemplate));
                }
            }
            if(!nonmatchingConsentTemplateNames.isEmpty())
            {
                throw new InterceptorException("ConsentTemplates: [" + String.join(", ", nonmatchingConsentTemplateNames) + "] do not match any BaseSite for this CxConfig.");
            }
        }
    }


    protected String getConsentTemplateFriendlyName(ConsentTemplateModel consentTemplate)
    {
        return consentTemplate.getId() + " - " + consentTemplate.getId();
    }
}
