package de.hybris.platform.basecommerce.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.BaseStoreSelectorStrategy;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBaseStoreSelectorStrategy implements BaseStoreSelectorStrategy
{
    private BaseSiteService siteService;


    public BaseStoreModel getCurrentBaseStore()
    {
        BaseStoreModel result = null;
        BaseSiteModel currentSite = this.siteService.getCurrentBaseSite();
        if(currentSite != null)
        {
            List<BaseStoreModel> storeModels = currentSite.getStores();
            if(CollectionUtils.isNotEmpty(storeModels))
            {
                result = storeModels.get(0);
            }
        }
        return result;
    }


    @Required
    public void setBaseSiteService(BaseSiteService siteService)
    {
        this.siteService = siteService;
    }
}
