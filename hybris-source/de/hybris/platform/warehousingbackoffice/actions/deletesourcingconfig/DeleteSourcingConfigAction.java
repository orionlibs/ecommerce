package de.hybris.platform.warehousingbackoffice.actions.deletesourcingconfig;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import de.hybris.platform.warehousing.model.SourcingConfigModel;
import org.apache.commons.collections4.CollectionUtils;

public class DeleteSourcingConfigAction extends DeleteAction
{
    public boolean canPerform(ActionContext<Object> ctx)
    {
        boolean allowed = false;
        if(ctx.getData() != null && ctx
                        .getData() instanceof SourcingConfigModel &&
                        CollectionUtils.isEmpty(((SourcingConfigModel)ctx.getData()).getBaseStores()))
        {
            allowed = true;
        }
        return allowed;
    }
}
