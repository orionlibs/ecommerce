package de.hybris.platform.admincockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.order.CalculationService;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class RecalculateOrderAction extends AbstractListViewAction
{
    private CalculationService calculationService;


    public String getImageURI(ListViewAction.Context context)
    {
        return isAvailable(context) ? "admincockpit/images/icon_func_calculate_complete.png" : null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    protected boolean isAvailable(ListViewAction.Context context)
    {
        TypedObject typedObject = context.getItem();
        if(typedObject != null)
        {
            return (ItemModel)typedObject.getObject() instanceof de.hybris.platform.core.model.order.AbstractOrderModel;
        }
        return false;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("ba.recalc_order_tooltip");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }
}
