package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.LineOfBusiness;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CompanyModel extends UserGroupModel
{
    public static final String _TYPECODE = "Company";
    public static final String DUNSID = "dunsID";
    public static final String ILNID = "ilnID";
    public static final String BUYERSPECIFICID = "buyerSpecificID";
    public static final String ID = "Id";
    public static final String SUPPLIERSPECIFICID = "supplierSpecificID";
    public static final String ADDRESSES = "addresses";
    public static final String MEDIAS = "medias";
    public static final String SHIPPINGADDRESSES = "shippingAddresses";
    public static final String SHIPPINGADDRESS = "shippingAddress";
    public static final String UNLOADINGADDRESSES = "unloadingAddresses";
    public static final String UNLOADINGADDRESS = "unloadingAddress";
    public static final String BILLINGADDRESSES = "billingAddresses";
    public static final String BILLINGADDRESS = "billingAddress";
    public static final String CONTACTADDRESSES = "contactAddresses";
    public static final String CONTACTADDRESS = "contactAddress";
    public static final String CONTACT = "contact";
    public static final String VATID = "vatID";
    public static final String RESPONSIBLECOMPANY = "responsibleCompany";
    public static final String COUNTRY = "country";
    public static final String LINEOFBUISNESS = "lineOfBuisness";
    public static final String BUYER = "buyer";
    public static final String SUPPLIER = "supplier";
    public static final String MANUFACTURER = "manufacturer";
    public static final String CARRIER = "carrier";
    public static final String PROVIDEDCATALOGS = "providedCatalogs";
    public static final String PURCHASEDCATALOGS = "purchasedCatalogs";


    public CompanyModel()
    {
    }


    public CompanyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompanyModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompanyModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "addresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getPropertyValue("addresses");
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
    public AddressModel getBillingAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("billingAddress");
    }


    @Accessor(qualifier = "billingAddresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getBillingAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "billingAddresses");
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
    public Boolean getBuyer()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("buyer");
    }


    @Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.GETTER)
    public String getBuyerSpecificID()
    {
        return (String)getPersistenceContext().getPropertyValue("buyerSpecificID");
    }


    @Accessor(qualifier = "carrier", type = Accessor.Type.GETTER)
    public Boolean getCarrier()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("carrier");
    }


    @Accessor(qualifier = "contact", type = Accessor.Type.GETTER)
    public UserModel getContact()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("contact");
    }


    @Accessor(qualifier = "contactAddress", type = Accessor.Type.GETTER)
    public AddressModel getContactAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("contactAddress");
    }


    @Accessor(qualifier = "contactAddresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getContactAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "contactAddresses");
    }


    @Accessor(qualifier = "country", type = Accessor.Type.GETTER)
    public CountryModel getCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("country");
    }


    @Accessor(qualifier = "dunsID", type = Accessor.Type.GETTER)
    public String getDunsID()
    {
        return (String)getPersistenceContext().getPropertyValue("dunsID");
    }


    @Accessor(qualifier = "Id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("Id");
    }


    @Accessor(qualifier = "ilnID", type = Accessor.Type.GETTER)
    public String getIlnID()
    {
        return (String)getPersistenceContext().getPropertyValue("ilnID");
    }


    @Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.GETTER)
    public LineOfBusiness getLineOfBuisness()
    {
        return (LineOfBusiness)getPersistenceContext().getPropertyValue("lineOfBuisness");
    }


    @Accessor(qualifier = "manufacturer", type = Accessor.Type.GETTER)
    public Boolean getManufacturer()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("manufacturer");
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getMedias()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("medias");
    }


    @Accessor(qualifier = "providedCatalogs", type = Accessor.Type.GETTER)
    public Collection<CatalogModel> getProvidedCatalogs()
    {
        return (Collection<CatalogModel>)getPersistenceContext().getPropertyValue("providedCatalogs");
    }


    @Accessor(qualifier = "purchasedCatalogs", type = Accessor.Type.GETTER)
    public Collection<CatalogModel> getPurchasedCatalogs()
    {
        return (Collection<CatalogModel>)getPersistenceContext().getPropertyValue("purchasedCatalogs");
    }


    @Accessor(qualifier = "responsibleCompany", type = Accessor.Type.GETTER)
    public CompanyModel getResponsibleCompany()
    {
        return (CompanyModel)getPersistenceContext().getPropertyValue("responsibleCompany");
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
    public AddressModel getShippingAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("shippingAddress");
    }


    @Accessor(qualifier = "shippingAddresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getShippingAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "shippingAddresses");
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
    public Boolean getSupplier()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("supplier");
    }


    @Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.GETTER)
    public String getSupplierSpecificID()
    {
        return (String)getPersistenceContext().getPropertyValue("supplierSpecificID");
    }


    @Accessor(qualifier = "unloadingAddress", type = Accessor.Type.GETTER)
    public AddressModel getUnloadingAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("unloadingAddress");
    }


    @Accessor(qualifier = "unloadingAddresses", type = Accessor.Type.GETTER)
    public Collection<AddressModel> getUnloadingAddresses()
    {
        return (Collection<AddressModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "unloadingAddresses");
    }


    @Accessor(qualifier = "vatID", type = Accessor.Type.GETTER)
    public String getVatID()
    {
        return (String)getPersistenceContext().getPropertyValue("vatID");
    }


    @Accessor(qualifier = "addresses", type = Accessor.Type.SETTER)
    public void setAddresses(Collection<AddressModel> value)
    {
        getPersistenceContext().setPropertyValue("addresses", value);
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
    public void setBillingAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("billingAddress", value);
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
    public void setBuyer(Boolean value)
    {
        getPersistenceContext().setPropertyValue("buyer", value);
    }


    @Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.SETTER)
    public void setBuyerSpecificID(String value)
    {
        getPersistenceContext().setPropertyValue("buyerSpecificID", value);
    }


    @Accessor(qualifier = "carrier", type = Accessor.Type.SETTER)
    public void setCarrier(Boolean value)
    {
        getPersistenceContext().setPropertyValue("carrier", value);
    }


    @Accessor(qualifier = "contact", type = Accessor.Type.SETTER)
    public void setContact(UserModel value)
    {
        getPersistenceContext().setPropertyValue("contact", value);
    }


    @Accessor(qualifier = "contactAddress", type = Accessor.Type.SETTER)
    public void setContactAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("contactAddress", value);
    }


    @Accessor(qualifier = "country", type = Accessor.Type.SETTER)
    public void setCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("country", value);
    }


    @Accessor(qualifier = "dunsID", type = Accessor.Type.SETTER)
    public void setDunsID(String value)
    {
        getPersistenceContext().setPropertyValue("dunsID", value);
    }


    @Accessor(qualifier = "Id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("Id", value);
    }


    @Accessor(qualifier = "ilnID", type = Accessor.Type.SETTER)
    public void setIlnID(String value)
    {
        getPersistenceContext().setPropertyValue("ilnID", value);
    }


    @Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.SETTER)
    public void setLineOfBuisness(LineOfBusiness value)
    {
        getPersistenceContext().setPropertyValue("lineOfBuisness", value);
    }


    @Accessor(qualifier = "manufacturer", type = Accessor.Type.SETTER)
    public void setManufacturer(Boolean value)
    {
        getPersistenceContext().setPropertyValue("manufacturer", value);
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
    public void setMedias(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("medias", value);
    }


    @Accessor(qualifier = "responsibleCompany", type = Accessor.Type.SETTER)
    public void setResponsibleCompany(CompanyModel value)
    {
        getPersistenceContext().setPropertyValue("responsibleCompany", value);
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
    public void setShippingAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("shippingAddress", value);
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
    public void setSupplier(Boolean value)
    {
        getPersistenceContext().setPropertyValue("supplier", value);
    }


    @Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.SETTER)
    public void setSupplierSpecificID(String value)
    {
        getPersistenceContext().setPropertyValue("supplierSpecificID", value);
    }


    @Accessor(qualifier = "unloadingAddress", type = Accessor.Type.SETTER)
    public void setUnloadingAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("unloadingAddress", value);
    }


    @Accessor(qualifier = "vatID", type = Accessor.Type.SETTER)
    public void setVatID(String value)
    {
        getPersistenceContext().setPropertyValue("vatID", value);
    }
}
