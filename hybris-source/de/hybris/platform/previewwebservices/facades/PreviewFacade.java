package de.hybris.platform.previewwebservices.facades;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import java.net.MalformedURLException;

public interface PreviewFacade
{
    PreviewTicketWsDTO createPreviewTicket(PreviewTicketWsDTO paramPreviewTicketWsDTO) throws MalformedURLException, CMSItemNotFoundException;


    PreviewTicketWsDTO updatePreviewTicket(String paramString, PreviewTicketWsDTO paramPreviewTicketWsDTO);


    PreviewTicketWsDTO getPreviewTicket(String paramString);
}
