package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.warehousing.model.PackagingInfoModel;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;

public class EditPackageInfoButtonRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    protected static final String CONSIGNMENT_PACKAGINGINFO = "PackagingInfo";
    private PermissionFacade permissionFacade;
    public static final String EDIT_BUTTON = "edititem";
    public static final String DISABLED = "disabled";


    public void render(Listcell listcell, ListColumn columnConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Button button = new Button();
        String buttonClass = "edititem";
        button.setParent((Component)listcell);
        if(!checkEditability((PackagingInfoModel)object))
        {
            button.setDisabled(true);
            buttonClass = buttonClass + " disabled";
        }
        button.setSclass(buttonClass);
        TypeAwareSelectionContext typeAwareSelectionContext = new TypeAwareSelectionContext("PackagingInfo", object, Collections.singletonList(object));
        button.addEventListener("onClick", event -> widgetInstanceManager.sendOutput("referenceSelected", typeAwareSelectionContext));
    }


    protected boolean checkEditability(PackagingInfoModel packageInfo)
    {
        return (!ConsignmentStatus.PICKUP_COMPLETE.equals(packageInfo.getConsignment().getStatus()) &&
                        !ConsignmentStatus.SHIPPED.equals(packageInfo.getConsignment().getStatus()) && packageInfo.getConsignment().getShippingLabel() == null);
    }


    protected boolean checkPermission(DataType dataType, Object object)
    {
        return getPermissionFacade().canReadProperty(dataType.getCode(), ((OrderEntryModel)object).getOrder().getItemtype());
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
