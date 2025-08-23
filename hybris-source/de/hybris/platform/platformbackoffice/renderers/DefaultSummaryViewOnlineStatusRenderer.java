package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.summaryview.renderer.AbstractSummaryViewItemWithIconRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.time.Instant;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultSummaryViewOnlineStatusRenderer extends AbstractSummaryViewItemWithIconRenderer<ItemModel>
{
    private static final String ICON_GROUP = "online-status";


    protected void renderValue(Div attributeContainer, Attribute attributeConfiguration, ItemModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Label label = createOnlineStatusLabel((ProductModel)data, attributeConfiguration, data, dataAttribute, dataType
                        .getCode(), widgetInstanceManager);
        attributeContainer.appendChild((Component)label);
    }


    protected Label createOnlineStatusLabel(ProductModel productModel, Attribute attributeConfiguration, ItemModel data, DataAttribute dataAttribute, String typeCode, WidgetInstanceManager widgetInstanceManager)
    {
        OnlineStatus productStatus = getOnlineStatus(productModel);
        String productStatusLabel = Labels.getLabel(productStatus.getLabelKey());
        Label status = new Label(productStatusLabel);
        return status;
    }


    protected boolean canHandle(ItemModel data, DataType dataType)
    {
        return (data instanceof ProductModel && dataType != null);
    }


    protected boolean hasPermission(ItemModel data, DataType dataType)
    {
        boolean canReadOnlineDate = getPermissionFacade().canReadInstanceProperty(data, "onlineDate");
        boolean canReadOfflineDate = getPermissionFacade().canReadInstanceProperty(data, "offlineDate");
        return (canReadOnlineDate && canReadOfflineDate);
    }


    protected String getIconStatusSClass(HtmlBasedComponent iconContainer, Attribute attributeConfiguration, ItemModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        ProductModel productModel = (ProductModel)data;
        OnlineStatus productStatus = getOnlineStatus(productModel);
        return getIconStatusSClass("online-status", productStatus.name().toLowerCase());
    }


    protected OnlineStatus getOnlineStatus(ProductModel productModel)
    {
        if(areBothDatesAbsent(productModel))
        {
            return OnlineStatus.OFFLINE;
        }
        if(isOnlyOnlineDateAvailable(productModel))
        {
            Instant instant = productModel.getOnlineDate().toInstant();
            return Instant.now().isAfter(instant) ? OnlineStatus.ONLINE : OnlineStatus.OFFLINE;
        }
        if(isOnlyOfflineDateAvailable(productModel))
        {
            Instant instant = productModel.getOfflineDate().toInstant();
            return Instant.now().isBefore(instant) ? OnlineStatus.ONLINE : OnlineStatus.OFFLINE;
        }
        Instant now = Instant.now();
        Instant onlineFrom = productModel.getOnlineDate().toInstant();
        Instant onlineTo = productModel.getOfflineDate().toInstant();
        return (now.isAfter(onlineFrom) && now.isBefore(onlineTo)) ? OnlineStatus.ONLINE : OnlineStatus.OFFLINE;
    }


    private static boolean isOnlyOfflineDateAvailable(ProductModel productModel)
    {
        return (productModel.getOnlineDate() == null && productModel.getOfflineDate() != null);
    }


    private static boolean isOnlyOnlineDateAvailable(ProductModel productModel)
    {
        return (productModel.getOnlineDate() != null && productModel.getOfflineDate() == null);
    }


    private static boolean areBothDatesAbsent(ProductModel productModel)
    {
        return (productModel.getOnlineDate() == null && productModel.getOfflineDate() == null);
    }
}
