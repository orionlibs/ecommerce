package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCompany extends UserGroup
{
    public static final String DUNSID = "dunsID";
    public static final String ILNID = "ilnID";
    public static final String BUYERSPECIFICID = "buyerSpecificID";
    public static final String ID = "Id";
    public static final String SUPPLIERSPECIFICID = "supplierSpecificID";
    public static final String ADDRESSES = "addresses";
    public static final String MEDIAS = "medias";
    public static final String SHIPPINGADDRESS = "shippingAddress";
    public static final String UNLOADINGADDRESS = "unloadingAddress";
    public static final String BILLINGADDRESS = "billingAddress";
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
    protected static final OneToManyHandler<Catalog> PROVIDEDCATALOGSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.CATALOG, false, "supplier", null, false, true, 0);
    protected static final OneToManyHandler<Catalog> PURCHASEDCATALOGSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.CATALOG, false, "buyer", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("dunsID", Item.AttributeMode.INITIAL);
        tmp.put("ilnID", Item.AttributeMode.INITIAL);
        tmp.put("buyerSpecificID", Item.AttributeMode.INITIAL);
        tmp.put("Id", Item.AttributeMode.INITIAL);
        tmp.put("supplierSpecificID", Item.AttributeMode.INITIAL);
        tmp.put("medias", Item.AttributeMode.INITIAL);
        tmp.put("shippingAddress", Item.AttributeMode.INITIAL);
        tmp.put("unloadingAddress", Item.AttributeMode.INITIAL);
        tmp.put("billingAddress", Item.AttributeMode.INITIAL);
        tmp.put("contactAddress", Item.AttributeMode.INITIAL);
        tmp.put("contact", Item.AttributeMode.INITIAL);
        tmp.put("vatID", Item.AttributeMode.INITIAL);
        tmp.put("responsibleCompany", Item.AttributeMode.INITIAL);
        tmp.put("country", Item.AttributeMode.INITIAL);
        tmp.put("lineOfBuisness", Item.AttributeMode.INITIAL);
        tmp.put("buyer", Item.AttributeMode.INITIAL);
        tmp.put("supplier", Item.AttributeMode.INITIAL);
        tmp.put("manufacturer", Item.AttributeMode.INITIAL);
        tmp.put("carrier", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Address> getAddresses()
    {
        return getAddresses(getSession().getSessionContext());
    }


    public void setAddresses(Collection<Address> value)
    {
        setAddresses(getSession().getSessionContext(), value);
    }


    public Address getBillingAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "billingAddress");
    }


    public Address getBillingAddress()
    {
        return getBillingAddress(getSession().getSessionContext());
    }


    public void setBillingAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "billingAddress", value);
    }


    public void setBillingAddress(Address value)
    {
        setBillingAddress(getSession().getSessionContext(), value);
    }


    public Boolean isBuyer(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "buyer");
    }


    public Boolean isBuyer()
    {
        return isBuyer(getSession().getSessionContext());
    }


    public boolean isBuyerAsPrimitive(SessionContext ctx)
    {
        Boolean value = isBuyer(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isBuyerAsPrimitive()
    {
        return isBuyerAsPrimitive(getSession().getSessionContext());
    }


    public void setBuyer(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "buyer", value);
    }


    public void setBuyer(Boolean value)
    {
        setBuyer(getSession().getSessionContext(), value);
    }


    public void setBuyer(SessionContext ctx, boolean value)
    {
        setBuyer(ctx, Boolean.valueOf(value));
    }


    public void setBuyer(boolean value)
    {
        setBuyer(getSession().getSessionContext(), value);
    }


    public String getBuyerSpecificID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "buyerSpecificID");
    }


    public String getBuyerSpecificID()
    {
        return getBuyerSpecificID(getSession().getSessionContext());
    }


    public void setBuyerSpecificID(SessionContext ctx, String value)
    {
        setProperty(ctx, "buyerSpecificID", value);
    }


    public void setBuyerSpecificID(String value)
    {
        setBuyerSpecificID(getSession().getSessionContext(), value);
    }


    public Boolean isCarrier(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "carrier");
    }


    public Boolean isCarrier()
    {
        return isCarrier(getSession().getSessionContext());
    }


    public boolean isCarrierAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCarrier(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCarrierAsPrimitive()
    {
        return isCarrierAsPrimitive(getSession().getSessionContext());
    }


    public void setCarrier(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "carrier", value);
    }


    public void setCarrier(Boolean value)
    {
        setCarrier(getSession().getSessionContext(), value);
    }


    public void setCarrier(SessionContext ctx, boolean value)
    {
        setCarrier(ctx, Boolean.valueOf(value));
    }


    public void setCarrier(boolean value)
    {
        setCarrier(getSession().getSessionContext(), value);
    }


    public User getContact(SessionContext ctx)
    {
        return (User)getProperty(ctx, "contact");
    }


    public User getContact()
    {
        return getContact(getSession().getSessionContext());
    }


    public void setContact(SessionContext ctx, User value)
    {
        setProperty(ctx, "contact", value);
    }


    public void setContact(User value)
    {
        setContact(getSession().getSessionContext(), value);
    }


    public Address getContactAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "contactAddress");
    }


    public Address getContactAddress()
    {
        return getContactAddress(getSession().getSessionContext());
    }


    public void setContactAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "contactAddress", value);
    }


    public void setContactAddress(Address value)
    {
        setContactAddress(getSession().getSessionContext(), value);
    }


    public Country getCountry(SessionContext ctx)
    {
        return (Country)getProperty(ctx, "country");
    }


    public Country getCountry()
    {
        return getCountry(getSession().getSessionContext());
    }


    public void setCountry(SessionContext ctx, Country value)
    {
        setProperty(ctx, "country", value);
    }


    public void setCountry(Country value)
    {
        setCountry(getSession().getSessionContext(), value);
    }


    public String getDunsID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dunsID");
    }


    public String getDunsID()
    {
        return getDunsID(getSession().getSessionContext());
    }


    public void setDunsID(SessionContext ctx, String value)
    {
        setProperty(ctx, "dunsID", value);
    }


    public void setDunsID(String value)
    {
        setDunsID(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "Id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "Id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public String getIlnID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ilnID");
    }


    public String getIlnID()
    {
        return getIlnID(getSession().getSessionContext());
    }


    public void setIlnID(SessionContext ctx, String value)
    {
        setProperty(ctx, "ilnID", value);
    }


    public void setIlnID(String value)
    {
        setIlnID(getSession().getSessionContext(), value);
    }


    public EnumerationValue getLineOfBuisness(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "lineOfBuisness");
    }


    public EnumerationValue getLineOfBuisness()
    {
        return getLineOfBuisness(getSession().getSessionContext());
    }


    public void setLineOfBuisness(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "lineOfBuisness", value);
    }


    public void setLineOfBuisness(EnumerationValue value)
    {
        setLineOfBuisness(getSession().getSessionContext(), value);
    }


    public Boolean isManufacturer(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "manufacturer");
    }


    public Boolean isManufacturer()
    {
        return isManufacturer(getSession().getSessionContext());
    }


    public boolean isManufacturerAsPrimitive(SessionContext ctx)
    {
        Boolean value = isManufacturer(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isManufacturerAsPrimitive()
    {
        return isManufacturerAsPrimitive(getSession().getSessionContext());
    }


    public void setManufacturer(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "manufacturer", value);
    }


    public void setManufacturer(Boolean value)
    {
        setManufacturer(getSession().getSessionContext(), value);
    }


    public void setManufacturer(SessionContext ctx, boolean value)
    {
        setManufacturer(ctx, Boolean.valueOf(value));
    }


    public void setManufacturer(boolean value)
    {
        setManufacturer(getSession().getSessionContext(), value);
    }


    public Collection<Media> getMedias(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "medias");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getMedias()
    {
        return getMedias(getSession().getSessionContext());
    }


    public void setMedias(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "medias", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setMedias(Collection<Media> value)
    {
        setMedias(getSession().getSessionContext(), value);
    }


    public Collection<Catalog> getProvidedCatalogs(SessionContext ctx)
    {
        return PROVIDEDCATALOGSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Catalog> getProvidedCatalogs()
    {
        return getProvidedCatalogs(getSession().getSessionContext());
    }


    public Collection<Catalog> getPurchasedCatalogs(SessionContext ctx)
    {
        return PURCHASEDCATALOGSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Catalog> getPurchasedCatalogs()
    {
        return getPurchasedCatalogs(getSession().getSessionContext());
    }


    public Company getResponsibleCompany(SessionContext ctx)
    {
        return (Company)getProperty(ctx, "responsibleCompany");
    }


    public Company getResponsibleCompany()
    {
        return getResponsibleCompany(getSession().getSessionContext());
    }


    public void setResponsibleCompany(SessionContext ctx, Company value)
    {
        setProperty(ctx, "responsibleCompany", value);
    }


    public void setResponsibleCompany(Company value)
    {
        setResponsibleCompany(getSession().getSessionContext(), value);
    }


    public Address getShippingAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "shippingAddress");
    }


    public Address getShippingAddress()
    {
        return getShippingAddress(getSession().getSessionContext());
    }


    public void setShippingAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "shippingAddress", value);
    }


    public void setShippingAddress(Address value)
    {
        setShippingAddress(getSession().getSessionContext(), value);
    }


    public Boolean isSupplier(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "supplier");
    }


    public Boolean isSupplier()
    {
        return isSupplier(getSession().getSessionContext());
    }


    public boolean isSupplierAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSupplier(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSupplierAsPrimitive()
    {
        return isSupplierAsPrimitive(getSession().getSessionContext());
    }


    public void setSupplier(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "supplier", value);
    }


    public void setSupplier(Boolean value)
    {
        setSupplier(getSession().getSessionContext(), value);
    }


    public void setSupplier(SessionContext ctx, boolean value)
    {
        setSupplier(ctx, Boolean.valueOf(value));
    }


    public void setSupplier(boolean value)
    {
        setSupplier(getSession().getSessionContext(), value);
    }


    public String getSupplierSpecificID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "supplierSpecificID");
    }


    public String getSupplierSpecificID()
    {
        return getSupplierSpecificID(getSession().getSessionContext());
    }


    public void setSupplierSpecificID(SessionContext ctx, String value)
    {
        setProperty(ctx, "supplierSpecificID", value);
    }


    public void setSupplierSpecificID(String value)
    {
        setSupplierSpecificID(getSession().getSessionContext(), value);
    }


    public Address getUnloadingAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "unloadingAddress");
    }


    public Address getUnloadingAddress()
    {
        return getUnloadingAddress(getSession().getSessionContext());
    }


    public void setUnloadingAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "unloadingAddress", value);
    }


    public void setUnloadingAddress(Address value)
    {
        setUnloadingAddress(getSession().getSessionContext(), value);
    }


    public String getVatID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "vatID");
    }


    public String getVatID()
    {
        return getVatID(getSession().getSessionContext());
    }


    public void setVatID(SessionContext ctx, String value)
    {
        setProperty(ctx, "vatID", value);
    }


    public void setVatID(String value)
    {
        setVatID(getSession().getSessionContext(), value);
    }


    public abstract Collection<Address> getAddresses(SessionContext paramSessionContext);


    public abstract void setAddresses(SessionContext paramSessionContext, Collection<Address> paramCollection);
}
