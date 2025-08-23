package de.hybris.platform.warehousing.asn.dao.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.asn.dao.AsnDao;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

public class DefaultAsnDao extends AbstractItemDao implements AsnDao
{
    protected static final String ASNENTRIES_QUERY_PARAM = "asnEntries";
    protected static final String STOCKLEVEL_FOR_ASNENTRY_QUERY = "Select {pk} FROM {StockLevel} WHERE {asnEntry} IN (?asnEntries)";
    protected static final String ASN_FOR_INTERNALID = "Select {pk} FROM {AdvancedShippingNotice} WHERE {internalId} = ?internalId";


    public List<StockLevelModel> getStockLevelsForAsn(AdvancedShippingNoticeModel asn)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("asn", asn);
        Assert.isTrue(CollectionUtils.isNotEmpty(asn.getAsnEntries()), "No ASN Entries found to find the stock level");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("Select {pk} FROM {StockLevel} WHERE {asnEntry} IN (?asnEntries)");
        fQuery.addQueryParameter("asnEntries", asn.getAsnEntries());
        SearchResult result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    public AdvancedShippingNoticeModel getAsnForInternalId(String internalId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("internalId", internalId);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("Select {pk} FROM {AdvancedShippingNotice} WHERE {internalId} = ?internalId");
        fQuery.addQueryParameter("internalId", internalId);
        SearchResult asnResult = getFlexibleSearchService().search(fQuery);
        List<AdvancedShippingNoticeModel> asns = asnResult.getResult();
        if(asns.isEmpty())
        {
            throw new UnknownIdentifierException("AdvancedShippingNotice with internal id: [" + internalId + "] not found!");
        }
        if(asns.size() > 1)
        {
            throw new AmbiguousIdentifierException("AdvancedShippingNotice with internal id: [" + internalId + "] is not unique, " + asns
                            .size() + " AdvancedShippingNotices found!");
        }
        return asns.get(0);
    }
}
