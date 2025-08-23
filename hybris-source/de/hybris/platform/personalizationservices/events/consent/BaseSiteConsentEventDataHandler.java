package de.hybris.platform.personalizationservices.events.consent;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventDataConsumer;
import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventDataProvider;
import de.hybris.platform.site.BaseSiteService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseSiteConsentEventDataHandler implements AnonymousConsentChangeEventDataProvider, AnonymousConsentChangeEventDataConsumer
{
    private static final String BASE_SITE_KEY = "baseSite";
    private BaseSiteService baseSiteService;


    public Map<String, String> getData()
    {
        Map<String, String> data = new HashMap<>();
        Optional.<BaseSiteService>of(this.baseSiteService)
                        .map(BaseSiteService::getCurrentBaseSite)
                        .map(BaseSiteModel::getUid)
                        .ifPresent(uid -> data.put("baseSite", uid));
        return data;
    }


    public void process(Map<String, String> data)
    {
        if(data != null && data.get("baseSite") != null)
        {
            String baseSiteUid = data.get("baseSite");
            BaseSiteModel currentBaseSite = this.baseSiteService.getCurrentBaseSite();
            if(currentBaseSite == null || !baseSiteUid.equals(currentBaseSite.getUid()))
            {
                this.baseSiteService.setCurrentBaseSite(baseSiteUid, true);
            }
        }
    }


    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }
}
