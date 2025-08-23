package de.hybris.platform.warehousing.labels.service.impl;

import de.hybris.platform.acceleratorservices.document.service.DocumentGenerationService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.service.PrintMediaService;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPrintMediaService implements PrintMediaService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPrintMediaService.class);
    public static final String DOCUMENT_BODY_ENCODING = "UTF-8";
    private ImpersonationService impersonationService;
    private DocumentGenerationService documentGenerationService;
    private MediaService mediaService;
    private ModelService modelService;


    public MediaModel getMediaForTemplate(String frontendTemplateName, BusinessProcessModel businessProcessModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("frontendTemplateName", frontendTemplateName);
        ServicesUtil.validateParameterNotNullStandardMessage("businessProcessModel", businessProcessModel);
        LOG.info("Generating media for template: [{}] and item associated with business process: [{}]", frontendTemplateName, businessProcessModel
                        .getClass().getSimpleName());
        OrderModel order = getOrder(businessProcessModel);
        ImpersonationContext context = new ImpersonationContext();
        context.setOrder((AbstractOrderModel)order);
        context.setSite(order.getSite());
        context.setUser(order.getUser());
        context.setCatalogVersions(Collections.emptyList());
        return (MediaModel)getImpersonationService()
                        .executeInContext(context, () -> getDocumentGenerationService().generate(frontendTemplateName, businessProcessModel));
    }


    public String generatePopupScriptForMedia(MediaModel mediaModel, String width, String height, String blockedPopupMessage)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("width", width);
        ServicesUtil.validateParameterNotNullStandardMessage("height", height);
        ServicesUtil.validateParameterNotNullStandardMessage("blockedPopupMessage", blockedPopupMessage);
        String formattedHTML = StringEscapeUtils.escapeEcmaScript(generateHtmlMediaTemplate(mediaModel));
        return "var myWindow = window.open('','','width=" + width + ",height=" + height + ",scrollbars=yes'); try { myWindow.document.write(\"" + formattedHTML + "\") } catch (e) { alert(\"" + blockedPopupMessage + "\") }";
    }


    public String generateHtmlMediaTemplate(MediaModel mediaModel)
    {
        String htmlTemplate;
        ServicesUtil.validateParameterNotNullStandardMessage("mediaModel", mediaModel);
        try
        {
            htmlTemplate = new String(getMediaService().getDataFromMedia(mediaModel), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            htmlTemplate = new String(getMediaService().getDataFromMedia(mediaModel));
            LOG.warn("document content - UnsupportedEncodingException");
        }
        return htmlTemplate;
    }


    protected OrderModel getOrder(BusinessProcessModel businessProcessModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("businessProcessModel", businessProcessModel);
        if(businessProcessModel instanceof OrderProcessModel)
        {
            return ((OrderProcessModel)businessProcessModel).getOrder();
        }
        if(businessProcessModel instanceof ConsignmentProcessModel)
        {
            return (OrderModel)((ConsignmentProcessModel)businessProcessModel).getConsignment().getOrder();
        }
        if(businessProcessModel instanceof ReturnProcessModel)
        {
            return ((ReturnProcessModel)businessProcessModel).getReturnRequest().getOrder();
        }
        if(businessProcessModel.getContextParameters().iterator().hasNext())
        {
            BusinessProcessParameterModel param = businessProcessModel.getContextParameters().iterator().next();
            List<ConsignmentModel> consignmentList = (List<ConsignmentModel>)param.getValue();
            if(CollectionUtils.isNotEmpty(consignmentList))
            {
                return (OrderModel)((ConsignmentModel)consignmentList.iterator().next()).getOrder();
            }
        }
        LOG.info("Unsupported BusinessProcess type [{}] for item [{}]", businessProcessModel.getClass().getSimpleName(), businessProcessModel);
        return null;
    }


    protected ImpersonationService getImpersonationService()
    {
        return this.impersonationService;
    }


    @Required
    public void setImpersonationService(ImpersonationService impersonationService)
    {
        this.impersonationService = impersonationService;
    }


    protected DocumentGenerationService getDocumentGenerationService()
    {
        return this.documentGenerationService;
    }


    @Required
    public void setDocumentGenerationService(DocumentGenerationService documentGenerationService)
    {
        this.documentGenerationService = documentGenerationService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
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
