package de.hybris.platform.refund.dao.impl;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.refund.dao.RefundDao;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRefundDao extends AbstractItemDao implements RefundDao
{
    public List<RefundEntryModel> getRefunds(ReturnRequestModel request)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("request", request);
        String query = "SELECT {" + Item.PK + "} FROM { RefundEntry} WHERE { returnRequest}=?request ORDER BY {" + Item.PK + "} ASC";
        return getFlexibleSearchService().search(query, params).getResult();
    }
}
