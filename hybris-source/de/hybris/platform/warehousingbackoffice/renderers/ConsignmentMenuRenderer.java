package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.warehousing.labels.strategy.PrintExportFormStrategy;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Button;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class ConsignmentMenuRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsignmentMenuRenderer.class);
    protected static final String ROW_POPUP_STYLE = "ye-inline-editor-row-popup";
    protected static final String SOCKET_OUT_CONTEXT = "reallocateContext";
    protected static final String ACTION_BUTTON = "ye-actiondots--btn ye-actiondots--consignment--btn";
    protected static final String ACTION_CELL = "ye-actiondots ye-actiondots--consignment";
    protected static final String CAN_PRINT_RETURNFORM_KEY = "warehousing.printreturnform.active";
    protected static final String CAN_PRINT_RETURNSHIPPINGLABEL_KEY = "warehousing.printreturnshippinglabel.active";
    protected static final String PICK_ACTION = "Picking";
    protected static final String PACK_ACTION = "Packing";
    protected static final String NON_SELECTABLE_TAGS = "button";
    protected static final String MENU_POPUP_POSITION = "after_end";
    protected static final String CAPTURE_PAYMENT_ON_CONSIGNMENT = "warehousing.capturepaymentonconsignment";
    private static final String EVENT_LOOPBACK = "onLoopbackRequest";
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintPickSlipStrategy;
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintPackSlipStrategy;
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintExportFormStrategy;
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintShippingLabelStrategy;
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintReturnFormStrategy;
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintReturnShippingLabelStrategy;
    @Resource
    private PrintExportFormStrategy printExportFormStrategy;
    @Resource
    private ConfigurationService configurationService;
    @Resource
    private List<ConsignmentStatus> reallocableConsignmentStatuses;


    public void render(Listcell listCell, ListColumn columnConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Button btnPopup = new Button();
        btnPopup.setSclass("ye-actiondots--btn ye-actiondots--consignment--btn");
        Menupopup menupopup = createMenuPopup((WorkflowActionModel)object, widgetInstanceManager);
        btnPopup.appendChild((Component)menupopup);
        btnPopup.addEventListener("onClick", event -> onActionClick(listCell, menupopup));
        listCell.appendChild((Component)btnPopup);
        listCell.setSclass("ye-actiondots ye-actiondots--consignment");
        listCell.addEventListener("onLoopbackRequest", (EventListener)new Object(this, listCell));
        Events.echoEvent("onLoopbackRequest", (Component)listCell, listCell);
    }


    protected Menupopup createMenuPopup(WorkflowActionModel workflowActionModel, WidgetInstanceManager widgetInstanceManager)
    {
        ConsignmentModel consignmentModel = workflowActionModel.getAttachmentItems().get(0);
        Menupopup menuPopup = new Menupopup();
        menuPopup.setSclass("ye-inline-editor-row-popup");
        if(consignmentModel.getFulfillmentSystemConfig() == null)
        {
            boolean captureOnConsignmentReallocationAllowed = true;
            if(getConfigurationService().getConfiguration().getBoolean("warehousing.capturepaymentonconsignment", Boolean.FALSE).booleanValue())
            {
                captureOnConsignmentReallocationAllowed = getReallocableConsignmentStatuses().contains(consignmentModel.getStatus());
            }
            if(consignmentModel.getDeliveryPointOfService() == null && captureOnConsignmentReallocationAllowed)
            {
                Menuitem reallocateConsignment = new Menuitem();
                reallocateConsignment.setLabel(resolveLabel("warehousingbackoffice.taskassignment.consignment.reallocate"));
                reallocateConsignment.addEventListener("onClick", event -> widgetInstanceManager.sendOutput("reallocateContext", consignmentModel));
                menuPopup.appendChild((Component)reallocateConsignment);
            }
            Menuitem printDocument = new Menuitem();
            printDocument.setLabel(resolveLabel("warehousingbackoffice.taskassignment.consignment.print"));
            printDocument.addEventListener("onClick", event -> printDocument(workflowActionModel, consignmentModel));
            menuPopup.appendChild((Component)printDocument);
        }
        else
        {
            Menuitem emptyMenuItem = new Menuitem();
            emptyMenuItem.setLabel(resolveLabel("warehousingbackoffice.taskassignment.consignment.no.action"));
            menuPopup.appendChild((Component)emptyMenuItem);
        }
        return menuPopup;
    }


    protected void onActionClick(Listcell actionColumn, Menupopup menuPopup)
    {
        menuPopup.open((Component)actionColumn, "after_end");
    }


    protected String resolveLabel(String labelKey)
    {
        String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }


    protected void printDocument(WorkflowActionModel workflowActionModel, ConsignmentModel consignmentModel)
    {
        if(workflowActionModel.getName().equals("Picking"))
        {
            getConsignmentPrintPickSlipStrategy().printDocument(consignmentModel);
        }
        else if(workflowActionModel.getName().equals("Packing"))
        {
            getConsignmentPrintPackSlipStrategy().printDocument(consignmentModel);
            printExportSlip(consignmentModel);
            printReturnFormAndLabel(consignmentModel);
        }
        else
        {
            getConsignmentPrintShippingLabelStrategy().printDocument(consignmentModel);
        }
    }


    protected void printReturnFormAndLabel(ConsignmentModel consignmentModel)
    {
        Configuration configuration = getConfigurationService().getConfiguration();
        boolean canPrintReturnForm = false;
        boolean canPrintReturnLabel = false;
        try
        {
            if(configuration != null)
            {
                canPrintReturnForm = configuration.getBoolean("warehousing.printreturnform.active");
                canPrintReturnLabel = configuration.getBoolean("warehousing.printreturnshippinglabel.active");
            }
        }
        catch(ConversionException | java.util.NoSuchElementException e)
        {
            LOGGER.error(String.format("No or incorrect property defined for [%s] or [%s]. Value has to be 'true' or 'false' - any other value will be treated as a false", new Object[] {"warehousing.printreturnform.active", "warehousing.printreturnshippinglabel.active"}));
        }
        if(canPrintReturnForm)
        {
            getConsignmentPrintReturnFormStrategy().printDocument(consignmentModel);
        }
        if(canPrintReturnLabel)
        {
            getConsignmentPrintReturnShippingLabelStrategy().printDocument(consignmentModel);
        }
    }


    protected void printExportSlip(ConsignmentModel consignmentModel)
    {
        if(getPrintExportFormStrategy().canPrintExportForm(consignmentModel))
        {
            getConsignmentPrintExportFormStrategy().printDocument(consignmentModel);
        }
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintPickSlipStrategy()
    {
        return this.consignmentPrintPickSlipStrategy;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintPackSlipStrategy()
    {
        return this.consignmentPrintPackSlipStrategy;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintExportFormStrategy()
    {
        return this.consignmentPrintExportFormStrategy;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintShippingLabelStrategy()
    {
        return this.consignmentPrintShippingLabelStrategy;
    }


    protected PrintExportFormStrategy getPrintExportFormStrategy()
    {
        return this.printExportFormStrategy;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintReturnFormStrategy()
    {
        return this.consignmentPrintReturnFormStrategy;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintReturnShippingLabelStrategy()
    {
        return this.consignmentPrintReturnShippingLabelStrategy;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected List<ConsignmentStatus> getReallocableConsignmentStatuses()
    {
        return this.reallocableConsignmentStatuses;
    }
}
