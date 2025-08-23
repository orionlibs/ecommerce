package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class NavigationNodeRemoveInterceptor implements RemoveInterceptor
{
    private ModelService modelService;


    public void onRemove(Object model, InterceptorContext interceptorContext) throws InterceptorException
    {
        CMSNavigationNodeModel node = (CMSNavigationNodeModel)model;
        node.getEntries().forEach(entry -> {
            entry.setNavigationNode(null);
            getModelService().save(entry);
        });
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
