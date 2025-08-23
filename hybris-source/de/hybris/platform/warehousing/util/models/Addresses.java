package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.daos.AddressDao;
import de.hybris.platform.warehousing.util.builder.AddressModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Addresses extends AbstractItems<AddressModel>
{
    public static final Double LATITUDE_MONTREAL = Double.valueOf(45.501633D);
    public static final Double LONGITUDE_MONTREAL = Double.valueOf(-73.574003D);
    public static final Double LATITUDE_MONTREAL_NANCY_HOME = Double.valueOf(45.502794D);
    public static final Double LONGITUDE_MONTREAL_NANCY_HOME = Double.valueOf(-73.571472D);
    public static final Double LATITUDE_BOSTON = Double.valueOf(42.351941D);
    public static final Double LONGITUDE_BOSTON = Double.valueOf(-71.047847D);
    public static final String STREET_NUMBER_MONTREAL_MAISONNEUVE = "999";
    public static final String STREET_NAME_MONTREAL_MAISONNEUVE = "De Maisonneuve";
    public static final String POSTAL_CODE_MONTREAL_MAISONNEUVE = "H3A 3L4";
    public static final String STREET_NUMBER_MONTREAL_DUKE = "111";
    public static final String STREET_NAME_MONTREAL_DUKE = "Duke";
    public static final String POSTAL_CODE_MONTREAL_DUKE = "H3C 2M1";
    public static final String STREET_NUMBER_MONTREAL_NANCY_HOME = "705";
    public static final String STREET_NAME_MONTREAL_NANCY_HOME = "Ste-Catherine";
    public static final String POSTAL_CODE_MONTREAL_NANCY_HOME = "H3B 4G5";
    public static final String STREET_NUMBER_BOSTON = "33-41";
    public static final String STREET_NAME_BOSTON = "Farnsworth";
    public static final String POSTAL_CODE_BOSTON = "02210";
    public static final String NAME_MONTREAL = "Montreal";
    public static final String NAME_BOSTON = "Boston";
    private AddressDao addressDao;
    private Countries countries;
    private Users users;
    private Regions regions;


    public AddressModel MontrealDeMaisonneuvePos()
    {
        return (AddressModel)getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner((ItemModel)getUsers().ManagerMontrealMaisonneuve()),
                        () -> AddressModelBuilder.aModel().withStreetNumber("999").withStreetName("De Maisonneuve").withTown("Montreal").withPostalCode("H3A 3L4").withCountry(getCountries().Canada()).withDuplicate(Boolean.FALSE).withBillingAddress(Boolean.FALSE).withContactAddress(Boolean.FALSE)
                                        .withUnloadingAddress(Boolean.FALSE).withShippingAddress(Boolean.TRUE).withOwner((ItemModel)getUsers().ManagerMontrealMaisonneuve()).withLatitude(LATITUDE_MONTREAL).withLongitude(LONGITUDE_MONTREAL).withRegion(getRegions().quebecRegion()).build());
    }


    public AddressModel MontrealDukePos()
    {
        return (AddressModel)getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner((ItemModel)getUsers().ManagerMontrealDuke()),
                        () -> AddressModelBuilder.aModel().withStreetNumber("111").withStreetName("Duke").withTown("Montreal").withPostalCode("H3C 2M1").withCountry(getCountries().Canada()).withDuplicate(Boolean.FALSE).withBillingAddress(Boolean.FALSE).withContactAddress(Boolean.FALSE)
                                        .withUnloadingAddress(Boolean.FALSE).withShippingAddress(Boolean.TRUE).withOwner((ItemModel)getUsers().ManagerMontrealDuke()).withRegion(getRegions().quebecRegion()).build());
    }


    public AddressModel MontrealNancyHome()
    {
        return (AddressModel)getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner((ItemModel)getUsers().Nancy()),
                        () -> AddressModelBuilder.fromModel(ShippingAddress()).withStreetNumber("705").withStreetName("Ste-Catherine").withTown("Montreal").withPostalCode("H3B 4G5").withCountry(getCountries().Canada()).withOwner((ItemModel)getUsers().Nancy())
                                        .withLatitude(LATITUDE_MONTREAL_NANCY_HOME).withLongitude(LONGITUDE_MONTREAL_NANCY_HOME).withRegion(getRegions().quebecRegion()).build());
    }


    public AddressModel Boston()
    {
        return (AddressModel)getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner((ItemModel)getUsers().Bob()),
                        () -> AddressModelBuilder.fromModel(ShippingAddress()).withStreetNumber("33-41").withStreetName("Farnsworth").withTown("Boston").withPostalCode("02210").withCountry(getCountries().UnitedStates()).withDuplicate(Boolean.FALSE).withBillingAddress(Boolean.TRUE)
                                        .withContactAddress(Boolean.FALSE).withUnloadingAddress(Boolean.FALSE).withShippingAddress(Boolean.TRUE).withOwner((ItemModel)getUsers().Bob()).withLatitude(LATITUDE_BOSTON).withLongitude(LONGITUDE_BOSTON).withRegion(getRegions().massachusettsRegion())
                                        .build());
    }


    protected AddressModel ShippingAddress()
    {
        return AddressModelBuilder.aModel().withDuplicate(Boolean.FALSE).withBillingAddress(Boolean.FALSE)
                        .withContactAddress(Boolean.FALSE).withUnloadingAddress(Boolean.FALSE).withShippingAddress(Boolean.TRUE).build();
    }


    public AddressDao getAddressDao()
    {
        return this.addressDao;
    }


    @Required
    public void setAddressDao(AddressDao addressDao)
    {
        this.addressDao = addressDao;
    }


    public Countries getCountries()
    {
        return this.countries;
    }


    @Required
    public void setCountries(Countries countries)
    {
        this.countries = countries;
    }


    public Users getUsers()
    {
        return this.users;
    }


    @Required
    public void setUsers(Users users)
    {
        this.users = users;
    }


    protected Regions getRegions()
    {
        return this.regions;
    }


    @Required
    public void setRegions(Regions regions)
    {
        this.regions = regions;
    }
}
