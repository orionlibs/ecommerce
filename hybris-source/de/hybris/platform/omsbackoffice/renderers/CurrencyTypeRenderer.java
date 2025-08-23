package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.core.model.order.OrderEntryModel;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Listcell;

public class CurrencyTypeRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private PermissionFacade permissionFacade;


    public void render(Listcell listcell, ListColumn columnConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(dataType == null || checkPermission(dataType))
        {
            listcell.setLabel(getCurrencyLabelForOrderEntry((OrderEntryModel)object));
        }
    }


    protected boolean checkPermission(DataType dataType)
    {
        return getPermissionFacade().canReadProperty(dataType.getCode(), "Order");
    }


    protected String getCurrencyLabelForOrderEntry(OrderEntryModel orderEntry)
    {
        return orderEntry.getOrder().getCurrency().getIsocode();
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
