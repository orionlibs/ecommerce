package de.hybris.platform.adaptivesearchbackoffice.actions.deletesearchconfiguration;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;

public class DeleteSearchConfigurationAction extends DeleteAction
{
    @Resource
    private ModelService modelService;


    public boolean canPerform(ActionContext<Object> ctx)
    {
        Object data = ctx.getData();
        if(data == null || this.modelService.isNew(data))
        {
            return false;
        }
        return super.canPerform(ctx);
    }
}
