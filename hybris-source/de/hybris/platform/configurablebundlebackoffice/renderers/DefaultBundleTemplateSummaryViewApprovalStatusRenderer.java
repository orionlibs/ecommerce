package de.hybris.platform.configurablebundlebackoffice.renderers;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.summaryview.renderer.AbstractSummaryViewItemWithIconRenderer;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultBundleTemplateSummaryViewApprovalStatusRenderer extends AbstractSummaryViewItemWithIconRenderer<BundleTemplateModel>
{
    public static final String APPROVAL_STATUS = "approval-status";


    protected boolean hasPermission(BundleTemplateModel data, DataType dataType)
    {
        return getPermissionFacade().canReadInstanceProperty(data, "status");
    }


    protected String getIconStatusSClass(HtmlBasedComponent iconContainer, Attribute attributeConfiguration, BundleTemplateModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return getIconStatusSClass("approval-status", data.getStatus().getStatus().name().toLowerCase(Locale.ENGLISH));
    }


    protected void renderValue(Div attributeContainer, Attribute attributeConfiguration, BundleTemplateModel data, DataAttribute dataAttribute, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        attributeContainer.appendChild((Component)createApprovalStatusValue(data));
    }


    protected Label createApprovalStatusValue(BundleTemplateModel data)
    {
        return new Label(getLabelService().getObjectLabel(data.getStatus().getStatus()));
    }
}
