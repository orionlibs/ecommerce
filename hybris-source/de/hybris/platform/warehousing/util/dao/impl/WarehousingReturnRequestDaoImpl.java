package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.returns.model.ReturnRequestModel;

public class WarehousingReturnRequestDaoImpl extends AbstractWarehousingDao<ReturnRequestModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {ReturnRequest} WHERE {code}=?code";
    }
}
