package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import java.util.List;

public interface CMSPreviewTicketDao
{
    List<CMSPreviewTicketModel> findPreviewTicketsForId(String paramString);
}
