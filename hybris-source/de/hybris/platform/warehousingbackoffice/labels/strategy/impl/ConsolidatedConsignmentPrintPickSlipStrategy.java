package de.hybris.platform.warehousingbackoffice.labels.strategy.impl;

import com.hybris.cockpitng.labels.LabelUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.service.PrintMediaService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsolidatedConsignmentPrintDocumentStrategy;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class ConsolidatedConsignmentPrintPickSlipStrategy implements ConsolidatedConsignmentPrintDocumentStrategy
{
    protected static final String FRONTEND_TEMPLATENAME = "ConsolidatedPickLabelDocumentTemplate";
    protected static final String POPUP_WIDTH = "900";
    protected static final String POPUP_HEIGHT = "800";
    protected static final String BLOCKED_POPUP_MESSAGE = "blockedpopupmessage";
    @Resource
    protected PrintMediaService printMediaService;


    public void printDocument(List<ConsignmentModel> consignmentModels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentModels", consignmentModels);
        MediaModel pickListMedia = this.printMediaService.getMediaForTemplate("ConsolidatedPickLabelDocumentTemplate", generateBusinessProcess(consignmentModels));
        String popup = this.printMediaService.generatePopupScriptForMedia(pickListMedia, "900", "800", resolveLabel("blockedpopupmessage"));
        Clients.evalJavaScript(popup);
    }


    protected BusinessProcessModel generateBusinessProcess(List<ConsignmentModel> consignmentModels)
    {
        ConsignmentProcessModel consignmentProcessModel = new ConsignmentProcessModel();
        BusinessProcessParameterModel businessProcessParameterModel = new BusinessProcessParameterModel();
        consignmentProcessModel.setConsignment(consignmentModels.iterator().next());
        businessProcessParameterModel.setName("ConsolidatedConsignmentModels");
        businessProcessParameterModel.setValue(consignmentModels);
        consignmentProcessModel.setContextParameters(Arrays.asList(new BusinessProcessParameterModel[] {businessProcessParameterModel}));
        return (BusinessProcessModel)consignmentProcessModel;
    }


    protected String resolveLabel(String labelKey)
    {
        String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }
}
