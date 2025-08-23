package de.hybris.platform.warehousingbackoffice.actions.deleteatpformula;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import org.apache.commons.collections4.CollectionUtils;

public class DeleteAtpFormulaAction extends DeleteAction
{
    public boolean canPerform(ActionContext<Object> ctx)
    {
        boolean allowed = false;
        if(ctx.getData() != null && ctx
                        .getData() instanceof AtpFormulaModel &&
                        CollectionUtils.isEmpty(((AtpFormulaModel)ctx.getData()).getBaseStores()))
        {
            allowed = true;
        }
        return allowed;
    }
}
