package de.hybris.platform.warehousingbackoffice.actions.stockadjustment;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.enums.AsnStatus;

public class StockAdjustmentAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<StockLevelModel, StockLevelModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "stockAdjustmentContext";


    public ActionResult<StockLevelModel> perform(ActionContext<StockLevelModel> actionContext)
    {
        sendOutput("stockAdjustmentContext", actionContext.getData());
        ActionResult<StockLevelModel> actionResult = new ActionResult("success");
        return actionResult;
    }


    public boolean canPerform(ActionContext<StockLevelModel> ctx)
    {
        StockLevelModel stockLevel = (StockLevelModel)ctx.getData();
        boolean physicallyInStock = true;
        if(stockLevel != null && stockLevel.getAsnEntry() != null)
        {
            physicallyInStock = AsnStatus.RECEIVED.equals(stockLevel.getAsnEntry().getAsn().getStatus());
        }
        return (stockLevel != null && stockLevel.getWarehouse() != null && !stockLevel.getWarehouse().isExternal() && physicallyInStock);
    }


    public boolean needsConfirmation(ActionContext<StockLevelModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<StockLevelModel> ctx)
    {
        return null;
    }
}
