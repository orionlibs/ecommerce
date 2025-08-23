package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.model.restrictions.CMSBaseStoreTimeRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CMSBaseStoreTimeRestrictionEvaluator implements CMSRestrictionEvaluator<CMSBaseStoreTimeRestrictionModel>
{
    private CMSTimeRestrictionEvaluator cmsTimeRestrictionEvaluator;
    private BaseStoreService baseStoreService;


    public boolean evaluate(CMSBaseStoreTimeRestrictionModel cmsStoreTimeRestriction, RestrictionData context)
    {
        Collection<BaseStoreModel> baseStores = cmsStoreTimeRestriction.getBaseStores();
        if(CollectionUtils.isEmpty(baseStores) || !baseStores.contains(getBaseStoreService().getCurrentBaseStore()))
        {
            return cmsStoreTimeRestriction.getPassIfStoreDoesntMatch().booleanValue();
        }
        return getCmsTimeRestrictionEvaluator().evaluate((CMSTimeRestrictionModel)cmsStoreTimeRestriction, context);
    }


    protected CMSTimeRestrictionEvaluator getCmsTimeRestrictionEvaluator()
    {
        return this.cmsTimeRestrictionEvaluator;
    }


    @Required
    public void setCmsTimeRestrictionEvaluator(CMSTimeRestrictionEvaluator cmsTimeRestrictionEvaluator)
    {
        this.cmsTimeRestrictionEvaluator = cmsTimeRestrictionEvaluator;
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }
}
