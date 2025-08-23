package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.summaryview.renderer.AbstractSummaryViewItemWithIconRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultSummaryViewApprovalStatusRenderer extends AbstractSummaryViewItemWithIconRenderer<ItemModel>
{
    public static final String APPROVAL_STATUS = "approval-status";


    protected boolean canHandle(ItemModel data, DataType dataType)
    {
        return (data instanceof ProductModel && dataType != null);
    }


    protected boolean hasPermission(ItemModel data, DataType dataType)
    {
        return getPermissionFacade().canReadInstanceProperty(data, "approvalStatus");
    }


    protected String getIconStatusSClass(HtmlBasedComponent iconContainer, Attribute attributeConfiguration, ItemModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return getIconStatusSClass("approval-status", ((ProductModel)data).getApprovalStatus().name().toLowerCase());
    }


    protected void renderValue(Div attributeContainer, Attribute attributeConfiguration, ItemModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        attributeContainer.appendChild((Component)createApprovalStatusValue((ProductModel)data));
    }


    protected Label createApprovalStatusValue(ProductModel data)
    {
        return new Label(getLabelService().getObjectLabel(data.getApprovalStatus()));
    }
}
