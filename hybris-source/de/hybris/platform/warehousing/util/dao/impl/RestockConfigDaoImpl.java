package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.warehousing.model.RestockConfigModel;

public class RestockConfigDaoImpl extends AbstractWarehousingDao<RestockConfigModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {RestockConfig}";
    }
}
