package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class CMSContentSlotRemoveInterceptor implements RemoveInterceptor
{
    private CMSAdminContentSlotService adminContentSlotService = null;


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ContentSlotModel)
        {
            for(CMSRelationModel relationModel : getCmsAdminContentSlotService()
                            .getAllRelationsForSlot((ContentSlotModel)model))
            {
                try
                {
                    ctx.getModelService().remove(relationModel);
                }
                catch(ModelRemovalException e)
                {
                    throw new InterceptorException("Could not remove ContentSlot because referencing CMSRelation " + relationModel + " could not be removed.", e);
                }
            }
        }
    }


    @Required
    public void setCmsAdminContentSlotService(CMSAdminContentSlotService adminContentSlotService)
    {
        this.adminContentSlotService = adminContentSlotService;
    }


    public CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.adminContentSlotService;
    }
}
