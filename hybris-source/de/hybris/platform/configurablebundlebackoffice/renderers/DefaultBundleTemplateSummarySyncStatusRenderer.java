package de.hybris.platform.configurablebundlebackoffice.renderers;

import com.hybris.backoffice.sync.renderers.DefaultSummarySyncStatusRenderer;
import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultBundleTemplateSummarySyncStatusRenderer extends DefaultSummarySyncStatusRenderer
{
    protected HtmlBasedComponent createContainer(Component parent, Attribute attributeConfiguration, ItemModel data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        HtmlBasedComponent container = super.createContainer(parent, attributeConfiguration, data, dataType, widgetInstanceManager);
        UITools.removeSClass(container, "yw-summaryview-container-editable yw-summaryview-sync-status");
        return container;
    }


    protected void onStatusIconClick(ItemModel item, WidgetInstanceManager wim)
    {
    }
}
