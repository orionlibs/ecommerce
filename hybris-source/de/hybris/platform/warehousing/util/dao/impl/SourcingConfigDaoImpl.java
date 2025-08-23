package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.warehousing.model.SourcingConfigModel;

public class SourcingConfigDaoImpl extends AbstractWarehousingDao<SourcingConfigModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {SourcingConfig} WHERE {code}=?code";
    }
}
