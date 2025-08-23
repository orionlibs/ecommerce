package de.hybris.platform.ordermanagementfacades.returns.data;

import de.hybris.platform.basecommerce.enums.ReturnAction;
import java.io.Serializable;
import java.util.List;

public class ReturnActionDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ReturnAction> returnActions;


    public void setReturnActions(List<ReturnAction> returnActions)
    {
        this.returnActions = returnActions;
    }


    public List<ReturnAction> getReturnActions()
    {
        return this.returnActions;
    }
}
