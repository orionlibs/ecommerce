package de.hybris.platform.servicelayer.user.daos.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.user.daos.AddressDao;
import java.util.Collection;
import java.util.Collections;

public class DefaultAddressDao extends DefaultGenericDao<AddressModel> implements AddressDao
{
    public DefaultAddressDao()
    {
        super("Address");
    }


    public Collection<AddressModel> findAddressesForOwner(ItemModel owner)
    {
        return find(Collections.singletonMap("owner", owner));
    }
}
