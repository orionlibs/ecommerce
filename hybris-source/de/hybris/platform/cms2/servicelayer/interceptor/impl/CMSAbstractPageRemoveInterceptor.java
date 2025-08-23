package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CMSAbstractPageRemoveInterceptor implements RemoveInterceptor
{
    private static final Logger LOG = Logger.getLogger(CMSAbstractPageRemoveInterceptor.class);
    private CMSContentSlotService cmsContentSlotService;


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof AbstractPageModel)
        {
            List<ContentSlotForPageModel> contentSlotRelations = ((AbstractPageModel)model).getContentSlots();
            if(contentSlotRelations != null)
            {
                for(ContentSlotForPageModel contentSlotForPageModel : contentSlotRelations)
                {
                    removeContentSlot((AbstractPageModel)model, ctx, contentSlotForPageModel);
                    removeContentSlotForPageRelation(ctx, contentSlotForPageModel);
                }
            }
        }
    }


    protected void removeContentSlot(AbstractPageModel pageModel, InterceptorContext ctx, ContentSlotForPageModel contentSlotForPageModel)
    {
        ContentSlotModel contentSlot = contentSlotForPageModel.getContentSlot();
        if(contentSlot != null && !isAssignedToOtherPages(contentSlot, pageModel))
        {
            try
            {
                ctx.getModelService().remove(contentSlot);
            }
            catch(ModelRemovalException e)
            {
                LOG.error("Could not remove content slot " + contentSlot + ".", (Throwable)e);
            }
        }
    }


    protected void removeContentSlotForPageRelation(InterceptorContext ctx, ContentSlotForPageModel contentSlotForPageModel) throws InterceptorException
    {
        if(!ctx.getModelService().isRemoved(contentSlotForPageModel))
        {
            try
            {
                ctx.getModelService().remove(contentSlotForPageModel);
            }
            catch(ModelRemovalException e)
            {
                throw new InterceptorException("Page can not be removed because related ContentSlotForPage relation " + contentSlotForPageModel + " could not be removed.", e);
            }
        }
    }


    protected boolean isAssignedToOtherPages(ContentSlotModel contentSlot, AbstractPageModel page)
    {
        Collection<AbstractPageModel> pagesForContentSlot = this.cmsContentSlotService.getPagesForContentSlot(contentSlot);
        if(CollectionUtils.isNotEmpty(pagesForContentSlot))
        {
            if(pagesForContentSlot.contains(page))
            {
                return (pagesForContentSlot.size() > 1);
            }
            return true;
        }
        return false;
    }


    @Required
    public void setCmsContentSlotService(CMSContentSlotService cmsContentSlotService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
    }
}
