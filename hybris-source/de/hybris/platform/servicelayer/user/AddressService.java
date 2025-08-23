package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface AddressService
{
    AddressModel createAddressForUser(UserModel paramUserModel);


    AddressModel createAddressForOwner(ItemModel paramItemModel);


    Collection<AddressModel> getAddressesForOwner(ItemModel paramItemModel);


    AddressModel cloneAddress(AddressModel paramAddressModel);


    AddressModel cloneAddressForOwner(AddressModel paramAddressModel, ItemModel paramItemModel);
}
