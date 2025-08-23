package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.RelationBetweenComponentsService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.apache.log4j.Logger;

public class CMSAbstractComponentRemoveInterceptor implements RemoveInterceptor
{
    private static final Logger LOG = Logger.getLogger(CMSAbstractComponentRemoveInterceptor.class);
    private RelationBetweenComponentsService relationBetweenComponentsService;


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        getRelationBetweenComponentsService().removeRelationBetweenComponentsOnModel((AbstractCMSComponentModel)model);
        ctx.getModelService().saveAll();
    }


    public RelationBetweenComponentsService getRelationBetweenComponentsService()
    {
        return this.relationBetweenComponentsService;
    }


    public void setRelationBetweenComponentsService(RelationBetweenComponentsService relationBetweenComponentsService)
    {
        this.relationBetweenComponentsService = relationBetweenComponentsService;
    }
}
