package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.util.localization.Localization;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;

public class TaxCommitManualReleaseButtonCellRenderer extends ReleaseButtonCellRenderer implements WidgetComponentRenderer<Listcell, ListColumn, ConsignmentModel>
{
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String HANDLE_MANUAL_TAX_COMMIT_CHOICE = "handleManualCommit";
    protected static final String RELEASE_BUTTON = "releasebutton";
    protected static final String DISABLED = "disabled";
    private WidgetInstanceManager widgetInstanceManager;


    public void render(Listcell parent, ListColumn listColumn, ConsignmentModel consignmentModel, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        setWidgetInstanceManager(widgetInstanceManager);
        Button releaseButton = new Button();
        String releaseButtonClass = "releasebutton";
        releaseButton.setParent((Component)parent);
        releaseButton.addEventListener("onClick", event -> executeManualRelease(consignmentModel));
        releaseButton.setTooltiptext(
                        Localization.getLocalizedString("customersupportbackoffice.tooltip.consignment.taxcommitmanualrelease"));
        if(!canPerformOperation(consignmentModel, ConsignmentStatus.TAX_NOT_COMMITTED))
        {
            releaseButton.setDisabled(true);
            releaseButtonClass = releaseButtonClass + " disabled";
        }
        releaseButton.setSclass(releaseButtonClass);
    }


    protected void executeManualRelease(ConsignmentModel consignmentModel)
    {
        if(canPerformOperation(consignmentModel, ConsignmentStatus.TAX_NOT_COMMITTED))
        {
            triggerBusinessProcessEvent(consignmentModel, "ConsignmentActionEvent", "handleManualCommit");
        }
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return this.widgetInstanceManager;
    }


    public void setWidgetInstanceManager(WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }
}
