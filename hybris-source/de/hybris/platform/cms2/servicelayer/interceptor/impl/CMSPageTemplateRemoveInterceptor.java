package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import java.util.List;

public class CMSPageTemplateRemoveInterceptor implements RemoveInterceptor
{
    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof PageTemplateModel)
        {
            List<ContentSlotForTemplateModel> contentSlotRelations = ((PageTemplateModel)model).getContentSlots();
            if(contentSlotRelations != null)
            {
                for(ContentSlotForTemplateModel contentSlotForPageTemplateModel : contentSlotRelations)
                {
                    removeContentSlotForTemplateRelation(ctx, contentSlotForPageTemplateModel);
                }
            }
        }
    }


    protected void removeContentSlotForTemplateRelation(InterceptorContext ctx, ContentSlotForTemplateModel contentSlotForPageTemplateModel) throws InterceptorException
    {
        try
        {
            ctx.getModelService().remove(contentSlotForPageTemplateModel);
        }
        catch(ModelRemovalException e)
        {
            throw new InterceptorException("Could not remove page template because related ContentSlotForTemplate could not be removed.", e);
        }
    }
}
