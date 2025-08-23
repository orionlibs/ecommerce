package de.hybris.platform.warehousingbackoffice.labels.strategy.impl;

import com.hybris.cockpitng.labels.LabelUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.service.PrintMediaService;
import de.hybris.platform.warehousing.process.impl.DefaultConsignmentProcessService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class ConsignmentPrintPackSlipStrategy implements ConsignmentPrintDocumentStrategy
{
    protected static final String FRONTEND_TEMPLATENAME = "PackLabelDocumentTemplate";
    protected static final String POPUP_WIDTH = "950";
    protected static final String POPUP_HEIGHT = "800";
    protected static final String BLOCKED_POPUP_MESSAGE = "blockedpopupmessage";
    private PrintMediaService printMediaService;
    private DefaultConsignmentProcessService consignmentBusinessProcessService;


    public void printDocument(ConsignmentModel consignmentModel)
    {
        ServicesUtil.validateParameterNotNull(consignmentModel, "Consignment cannot be null");
        MediaModel pickListMedia = getPrintMediaService().getMediaForTemplate("PackLabelDocumentTemplate",
                        getConsignmentBusinessProcessService().getConsignmentProcess(consignmentModel));
        String popup = getPrintMediaService().generatePopupScriptForMedia(pickListMedia, "950", "800", resolveLabel("blockedpopupmessage"));
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
}
