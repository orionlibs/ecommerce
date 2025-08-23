package de.hybris.platform.warehousingbackoffice.labels.strategy.impl;

import com.hybris.cockpitng.labels.LabelUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.service.PrintMediaService;
import de.hybris.platform.warehousing.process.impl.DefaultConsignmentProcessService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class ConsignmentPrintReturnShippingLabelStrategy implements ConsignmentPrintDocumentStrategy
{
    protected static final String FRONTEND_TEMPLATENAME = "ReturnShippingLabelDocumentTemplate";
    protected static final String POPUP_WIDTH = "700";
    protected static final String POPUP_HEIGHT = "700";
    protected static final String BLOCKED_POPUP_MESSAGE = "blockedpopupmessage";
    private PrintMediaService printMediaService;
    private DefaultConsignmentProcessService consignmentBusinessProcessService;
    private ModelService modelService;


    public void printDocument(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        MediaModel returnShippingLabelMedia = consignment.getReturnLabel();
        if(returnShippingLabelMedia == null)
        {
            returnShippingLabelMedia = getPrintMediaService().getMediaForTemplate("ReturnShippingLabelDocumentTemplate",
                            getConsignmentBusinessProcessService().getConsignmentProcess(consignment));
            consignment.setReturnLabel(returnShippingLabelMedia);
            getModelService().save(consignment);
        }
        String popup = getPrintMediaService().generatePopupScriptForMedia(returnShippingLabelMedia, "700", "700", resolveLabel("blockedpopupmessage"));
        Clients.evalJavaScript(popup);
    }


    protected String resolveLabel(String labelKey)
    {
        String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }


    protected PrintMediaService getPrintMediaService()
    {
        return this.printMediaService;
    }


    @Required
    public void setPrintMediaService(PrintMediaService printMediaService)
    {
        this.printMediaService = printMediaService;
    }


    protected DefaultConsignmentProcessService getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    @Required
    public void setConsignmentBusinessProcessService(DefaultConsignmentProcessService consignmentBusinessProcessService)
    {
        this.consignmentBusinessProcessService = consignmentBusinessProcessService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
