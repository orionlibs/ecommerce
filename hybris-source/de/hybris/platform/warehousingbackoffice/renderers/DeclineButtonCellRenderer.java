package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;

public class DeclineButtonCellRenderer implements WidgetComponentRenderer<Listcell, ListColumn, ConsignmentEntryModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "consignmentEntryContext";


    public void render(Listcell parent, ListColumn columnConfiguration, ConsignmentEntryModel object, DataType dataType, WidgetInstanceManager widgetInstanceManger)
    {
        Button button = new Button();
        button.setSclass("declineitem");
        button.setParent((Component)parent);
        if(object.getConsignment().getDeliveryMode() instanceof de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel || object.getQuantityPending().longValue() == 0L)
        {
            button.setDisabled(true);
        }
        button.addEventListener("onClick", (EventListener)new Object(this, widgetInstanceManger, object));
    }
}
