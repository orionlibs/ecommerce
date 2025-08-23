package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class ContentSlotForPageRemoveInterceptor implements RemoveInterceptor<ContentSlotForPageModel>
{
    public RelatedPagePrepareInterceptor relatedPagePrepareInterceptor;


    public void onRemove(ContentSlotForPageModel contentSlotForPageModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        getRelatedPagePrepareInterceptor().onPrepare((ItemModel)contentSlotForPageModel, interceptorContext);
    }


    public RelatedPagePrepareInterceptor getRelatedPagePrepareInterceptor()
    {
        return this.relatedPagePrepareInterceptor;
    }


    @Required
    public void setRelatedPagePrepareInterceptor(RelatedPagePrepareInterceptor relatedPagePrepareInterceptor)
    {
        this.relatedPagePrepareInterceptor = relatedPagePrepareInterceptor;
    }
}
