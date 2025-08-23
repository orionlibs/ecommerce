package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.core.model.user.CustomerModel;

public class CustomerDaoImpl extends AbstractWarehousingDao<CustomerModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {User} WHERE {uid}=?" + getCode();
    }
}
