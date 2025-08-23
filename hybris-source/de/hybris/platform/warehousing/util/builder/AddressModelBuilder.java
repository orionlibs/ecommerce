package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;

public class AddressModelBuilder
{
    private final AddressModel model;


    private AddressModelBuilder()
    {
        this.model = new AddressModel();
    }


    private AddressModelBuilder(AddressModel model)
    {
        this.model = model;
    }


    private AddressModel getModel()
    {
        return this.model;
    }


    public static AddressModelBuilder aModel()
    {
        return new AddressModelBuilder();
    }


    public static AddressModelBuilder fromModel(AddressModel model)
    {
        return new AddressModelBuilder();
    }


    public AddressModel build()
    {
        return getModel();
    }


    public AddressModelBuilder withStreetNumber(String streetNumber)
    {
        getModel().setStreetnumber(streetNumber);
        return this;
    }


    public AddressModelBuilder withStreetName(String streetName)
    {
        getModel().setStreetname(streetName);
        return this;
    }


    public AddressModelBuilder withFirstName(String firstName)
    {
        getModel().setFirstname(firstName);
        return this;
    }


    public AddressModelBuilder withLastName(String lastName)
    {
        getModel().setLastname(lastName);
        return this;
    }


    public AddressModelBuilder withTown(String town)
    {
        getModel().setTown(town);
        return this;
    }


    public AddressModelBuilder withPostalCode(String postalCode)
    {
        getModel().setPostalcode(postalCode);
        return this;
    }


    public AddressModelBuilder withDuplicate(Boolean duplicate)
    {
        getModel().setDuplicate(duplicate);
        return this;
    }


    public AddressModelBuilder withCountry(CountryModel country)
    {
        getModel().setCountry(country);
        return this;
    }


    public AddressModelBuilder withShippingAddress(Boolean shippingAddress)
    {
        getModel().setShippingAddress(shippingAddress);
        return this;
    }


    public AddressModelBuilder withBillingAddress(Boolean billingAddress)
    {
        getModel().setBillingAddress(billingAddress);
        return this;
    }


    public AddressModelBuilder withContactAddress(Boolean contactAddress)
    {
        getModel().setContactAddress(contactAddress);
        return this;
    }


    public AddressModelBuilder withUnloadingAddress(Boolean unloadingAddress)
    {
        getModel().setUnloadingAddress(unloadingAddress);
        return this;
    }


    public AddressModelBuilder withOwner(ItemModel owner)
    {
        getModel().setOwner(owner);
        return this;
    }


    public AddressModelBuilder withLatitude(Double latitude)
    {
        getModel().setLatitude(latitude);
        return this;
    }


    public AddressModelBuilder withLongitude(Double longitude)
    {
        getModel().setLongitude(longitude);
        return this;
    }


    public AddressModelBuilder withRegion(RegionModel region)
    {
        getModel().setRegion(region);
        return this;
    }
}
