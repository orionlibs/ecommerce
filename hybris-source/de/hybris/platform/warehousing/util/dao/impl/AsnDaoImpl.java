package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;

public class AsnDaoImpl extends AbstractWarehousingDao<AdvancedShippingNoticeModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {AdvancedShippingNotice} WHERE {internalId}=?" + getCode();
    }
}
