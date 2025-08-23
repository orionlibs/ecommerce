package de.hybris.platform.warehousingbackoffice.labels.strategy.impl;

import com.hybris.cockpitng.labels.LabelUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.service.PrintMediaService;
import de.hybris.platform.warehousing.process.impl.DefaultConsignmentProcessService;
import de.hybris.platform.warehousing.shipping.strategy.DeliveryTrackingIdStrategy;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class ConsignmentPrintShippingLabelStrategy implements ConsignmentPrintDocumentStrategy
{
    protected static final String FRONTEND_TEMPLATENAME = "ShippingLabelDocumentTemplate";
    protected static final String POPUP_WIDTH = "600";
    protected static final String POPUP_HEIGHT = "600";
    protected static final String BLOCKED_POPUP_MESSAGE = "blockedpopupmessage";
    private PrintMediaService printMediaService;
    private DefaultConsignmentProcessService consignmentBusinessProcessService;
    private DeliveryTrackingIdStrategy deliveryTrackingIdStrategy;
    private ModelService modelService;


    public void printDocument(ConsignmentModel consignmentModel)
    {
        ServicesUtil.validateParameterNotNull(consignmentModel, "Consignment cannot be null");
        MediaModel shippingLabelMedia = consignmentModel.getShippingLabel();
        if(shippingLabelMedia == null)
        {
            if(consignmentModel.getTrackingID() == null || consignmentModel.getTrackingID().isEmpty())
            {
                String trackingId = getDeliveryTrackingIdStrategy().generateTrackingId(consignmentModel);
                consignmentModel.setTrackingID(trackingId);
                getModelService().save(consignmentModel);
            }
            shippingLabelMedia = getPrintMediaService().getMediaForTemplate("ShippingLabelDocumentTemplate",
                            getConsignmentBusinessProcessService().getConsignmentProcess(consignmentModel));
            consignmentModel.setShippingLabel(shippingLabelMedia);
            getModelService().save(consignmentModel);
        }
        String popup = getPrintMediaService().generatePopupScriptForMedia(shippingLabelMedia, "600", "600", resolveLabel("blockedpopupmessage"));
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


    protected DeliveryTrackingIdStrategy getDeliveryTrackingIdStrategy()
    {
        return this.deliveryTrackingIdStrategy;
    }


    @Required
    public void setDeliveryTrackingIdStrategy(DeliveryTrackingIdStrategy deliveryTrackingIdStrategy)
    {
        this.deliveryTrackingIdStrategy = deliveryTrackingIdStrategy;
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
