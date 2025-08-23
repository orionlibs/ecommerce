package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPreviewTicketDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;

public class DefaultCMSPreviewTicketDao extends AbstractItemDao implements CMSPreviewTicketDao
{
    public List<CMSPreviewTicketModel> findPreviewTicketsForId(String ticketId)
    {
        String query = "SELECT {pk} FROM {CMSPreviewTicket} WHERE {id} = ?ticketId";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {CMSPreviewTicket} WHERE {id} = ?ticketId", Collections.singletonMap("ticketId", ticketId));
        SearchResult<CMSPreviewTicketModel> result = search(fQuery);
        return result.getResult();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
