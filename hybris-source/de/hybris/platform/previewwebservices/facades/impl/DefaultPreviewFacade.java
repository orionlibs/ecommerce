package de.hybris.platform.previewwebservices.facades.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.previewwebservices.facades.PreviewFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.net.MalformedURLException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPreviewFacade extends AbstractBaseFacade implements PreviewFacade
{
    private CMSPreviewService cmsPreviewService;
    private Converter<CMSPreviewTicketModel, PreviewTicketWsDTO> previewTicketConverter;
    private Converter<PreviewTicketWsDTO, PreviewDataModel> previewTicketReverseConverter;


    public PreviewTicketWsDTO createPreviewTicket(PreviewTicketWsDTO previewTicketWsDTO) throws MalformedURLException, CMSItemNotFoundException
    {
        PreviewDataModel previewDataModel = (PreviewDataModel)this.previewTicketReverseConverter.convert(previewTicketWsDTO);
        CMSPreviewTicketModel cmsPreviewTicketModel = this.cmsPreviewService.createPreviewTicket(previewDataModel);
        return (PreviewTicketWsDTO)this.previewTicketConverter.convert(cmsPreviewTicketModel);
    }


    public PreviewTicketWsDTO updatePreviewTicket(String ticketId, PreviewTicketWsDTO previewTicketWsDTO)
    {
        CMSPreviewTicketModel cmsPreviewTicketModel = this.cmsPreviewService.getPreviewTicket(ticketId);
        if(Objects.isNull(cmsPreviewTicketModel))
        {
            throw createNotFoundException("CMSPreviewTicket", ticketId);
        }
        PreviewDataModel previewDataModel = (PreviewDataModel)this.previewTicketReverseConverter.convert(previewTicketWsDTO, cmsPreviewTicketModel.getPreviewData());
        getModelService().save(previewDataModel);
        cmsPreviewTicketModel.setPreviewData(previewDataModel);
        getModelService().save(cmsPreviewTicketModel);
        return (PreviewTicketWsDTO)this.previewTicketConverter.convert(cmsPreviewTicketModel);
    }


    public PreviewTicketWsDTO getPreviewTicket(String ticketId)
    {
        CMSPreviewTicketModel cmsPreviewTicketModel = this.cmsPreviewService.getPreviewTicket(ticketId);
        if(Objects.isNull(cmsPreviewTicketModel))
        {
            throw createNotFoundException("CMSPreviewTicket", ticketId);
        }
        return (PreviewTicketWsDTO)this.previewTicketConverter.convert(cmsPreviewTicketModel);
    }


    protected CMSPreviewService getCmsPreviewService()
    {
        return this.cmsPreviewService;
    }


    @Required
    public void setCmsPreviewService(CMSPreviewService cmsPreviewService)
    {
        this.cmsPreviewService = cmsPreviewService;
    }


    protected Converter<CMSPreviewTicketModel, PreviewTicketWsDTO> getPreviewTicketConverter()
    {
        return this.previewTicketConverter;
    }


    @Required
    public void setPreviewTicketConverter(Converter<CMSPreviewTicketModel, PreviewTicketWsDTO> previewTicketConverter)
    {
        this.previewTicketConverter = previewTicketConverter;
    }


    protected Converter<PreviewTicketWsDTO, PreviewDataModel> getPreviewTicketReverseConverter()
    {
        return this.previewTicketReverseConverter;
    }


    @Required
    public void setPreviewTicketReverseConverter(Converter<PreviewTicketWsDTO, PreviewDataModel> previewTicketReverseConverter)
    {
        this.previewTicketReverseConverter = previewTicketReverseConverter;
    }
}
