package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.daos.AddressDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAddressService extends AbstractBusinessService implements AddressService
{
    private AddressDao addressDao;


    public AddressModel createAddressForUser(UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("user", user);
        return createAddressForOwner((ItemModel)user);
    }


    public AddressModel createAddressForOwner(ItemModel owner)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("owner", owner);
        AddressModel address = (AddressModel)getModelService().create(AddressModel.class);
        address.setOwner(owner);
        return address;
    }


    public Collection<AddressModel> getAddressesForOwner(ItemModel owner)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("owner", owner);
        return this.addressDao.findAddressesForOwner(owner);
    }


    public AddressModel cloneAddress(AddressModel address)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("address", address);
        return cloneAddressForOwnerIntern(address, address.getOwner());
    }


    public AddressModel cloneAddressForOwner(AddressModel address, ItemModel owner)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("address", address);
        ServicesUtil.validateParameterNotNullStandardMessage("owner", owner);
        return cloneAddressForOwnerIntern(address, owner);
    }


    private AddressModel cloneAddressForOwnerIntern(AddressModel address, ItemModel owner)
    {
        AddressModel newAddress = (AddressModel)getModelService().clone(address);
        newAddress.setOwner(owner);
        newAddress.setOriginal(address);
        newAddress.setDuplicate(Boolean.TRUE);
        return newAddress;
    }


    @Required
    public void setAddressDao(AddressDao addressDao)
    {
        this.addressDao = addressDao;
    }
}
