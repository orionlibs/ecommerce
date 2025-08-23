package de.hybris.platform.servicelayer.user.daos;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import java.util.Collection;

public interface AddressDao
{
    Collection<AddressModel> findAddressesForOwner(ItemModel paramItemModel);
}
