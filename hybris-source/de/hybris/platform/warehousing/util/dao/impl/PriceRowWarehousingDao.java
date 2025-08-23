package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.europe1.model.PriceRowModel;

public class PriceRowWarehousingDao extends AbstractWarehousingDao<PriceRowModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {PriceRow} WHERE {productId}=?productId";
    }
}
