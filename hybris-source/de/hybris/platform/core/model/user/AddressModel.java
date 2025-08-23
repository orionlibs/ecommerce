package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class AddressModel extends ItemModel
{
    public static final String _TYPECODE = "Address";
    public static final String _USER2ADDRESSES = "User2Addresses";
    public static final String ORIGINAL = "original";
    public static final String DUPLICATE = "duplicate";
    public static final String APPARTMENT = "appartment";
    public static final String BUILDING = "building";
    public static final String CELLPHONE = "cellphone";
    public static final String COMPANY = "company";
    public static final String COUNTRY = "country";
    public static final String DEPARTMENT = "department";
    public static final String DISTRICT = "district";
    public static final String EMAIL = "email";
    public static final String FAX = "fax";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String MIDDLENAME = "middlename";
    public static final String MIDDLENAME2 = "middlename2";
    public static final String PHONE1 = "phone1";
    public static final String PHONE2 = "phone2";
    public static final String POBOX = "pobox";
    public static final String POSTALCODE = "postalcode";
    public static final String REGION = "region";
    public static final String STREETNAME = "streetname";
    public static final String STREETNUMBER = "streetnumber";
    public static final String TITLE = "title";
    public static final String TOWN = "town";
    public static final String GENDER = "gender";
    public static final String DATEOFBIRTH = "dateOfBirth";
    public static final String REMARKS = "remarks";
    public static final String PUBLICKEY = "publicKey";
    public static final String URL = "url";
    public static final String TYPEQUALIFIER = "typeQualifier";
    public static final String SHIPPINGADDRESS = "shippingAddress";
    public static final String UNLOADINGADDRESS = "unloadingAddress";
    public static final String BILLINGADDRESS = "billingAddress";
    public static final String CONTACTADDRESS = "contactAddress";
    public static final String LINE1 = "line1";
    public static final String LINE2 = "line2";
    public static final String DELIVERYADDRESSS2CARTTOORDERCRONJOB = "deliveryAddresss2CartToOrderCronJob";
    public static final String PAYMENTADDRESSS2CARTTOORDERCRONJOB = "paymentAddresss2CartToOrderCronJob";
    public static final String SAPADDRESSUUID = "sapAddressUUID";
    public static final String VISIBLEINADDRESSBOOK = "visibleInAddressBook";
    public static final String SAPCUSTOMERID = "sapCustomerID";
    public static final String SAPADDRESSUSAGE = "sapAddressUsage";
    public static final String SAPADDRESSUSAGECOUNTER = "sapAddressUsageCounter";
    public static final String SAPMESSAGEFUNCTION = "sapMessageFunction";


    public AddressModel()
    {
    }


    public AddressModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AddressModel(AddressModel _original, ItemModel _owner)
    {
        setOriginal(_original);
        setOwner(_owner);
    }


    @Accessor(qualifier = "appartment", type = Accessor.Type.GETTER)
    public String getAppartment()
    {
        return (String)getPersistenceContext().getPropertyValue("appartment");
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
    public Boolean getBillingAddress()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("billingAddress");
    }


    @Accessor(qualifier = "building", type = Accessor.Type.GETTER)
    public String getBuilding()
    {
        return (String)getPersistenceContext().getPropertyValue("building");
    }


    @Accessor(qualifier = "cellphone", type = Accessor.Type.GETTER)
    public String getCellphone()
    {
        return (String)getPersistenceContext().getPropertyValue("cellphone");
    }


    @Accessor(qualifier = "company", type = Accessor.Type.GETTER)
    public String getCompany()
    {
        return (String)getPersistenceContext().getPropertyValue("company");
    }


    @Accessor(qualifier = "contactAddress", type = Accessor.Type.GETTER)
    public Boolean getContactAddress()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("contactAddress");
    }


    @Accessor(qualifier = "country", type = Accessor.Type.GETTER)
    public CountryModel getCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("country");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Date getDateofbirth()
    {
        return getDateOfBirth();
    }


    @Accessor(qualifier = "dateOfBirth", type = Accessor.Type.GETTER)
    public Date getDateOfBirth()
    {
        return (Date)getPersistenceContext().getPropertyValue("dateOfBirth");
    }


    @Accessor(qualifier = "deliveryAddresss2CartToOrderCronJob", type = Accessor.Type.GETTER)
    public Collection<CartToOrderCronJobModel> getDeliveryAddresss2CartToOrderCronJob()
    {
        return (Collection<CartToOrderCronJobModel>)getPersistenceContext().getPropertyValue("deliveryAddresss2CartToOrderCronJob");
    }


    @Accessor(qualifier = "department", type = Accessor.Type.GETTER)
    public String getDepartment()
    {
        return (String)getPersistenceContext().getPropertyValue("department");
    }


    @Accessor(qualifier = "district", type = Accessor.Type.GETTER)
    public String getDistrict()
    {
        return (String)getPersistenceContext().getPropertyValue("district");
    }


    @Accessor(qualifier = "duplicate", type = Accessor.Type.GETTER)
    public Boolean getDuplicate()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("duplicate");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.GETTER)
    public String getEmail()
    {
        return (String)getPersistenceContext().getPropertyValue("email");
    }


    @Accessor(qualifier = "fax", type = Accessor.Type.GETTER)
    public String getFax()
    {
        return (String)getPersistenceContext().getPropertyValue("fax");
    }


    @Accessor(qualifier = "firstname", type = Accessor.Type.GETTER)
    public String getFirstname()
    {
        return (String)getPersistenceContext().getPropertyValue("firstname");
    }


    @Accessor(qualifier = "gender", type = Accessor.Type.GETTER)
    public Gender getGender()
    {
        return (Gender)getPersistenceContext().getPropertyValue("gender");
    }


    @Accessor(qualifier = "lastname", type = Accessor.Type.GETTER)
    public String getLastname()
    {
        return (String)getPersistenceContext().getPropertyValue("lastname");
    }


    @Accessor(qualifier = "line1", type = Accessor.Type.GETTER)
    public String getLine1()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "line1");
    }


    @Accessor(qualifier = "line2", type = Accessor.Type.GETTER)
    public String getLine2()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "line2");
    }


    @Accessor(qualifier = "middlename", type = Accessor.Type.GETTER)
    public String getMiddlename()
    {
        return (String)getPersistenceContext().getPropertyValue("middlename");
    }


    @Accessor(qualifier = "middlename2", type = Accessor.Type.GETTER)
    public String getMiddlename2()
    {
        return (String)getPersistenceContext().getPropertyValue("middlename2");
    }


    @Accessor(qualifier = "original", type = Accessor.Type.GETTER)
    public AddressModel getOriginal()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("original");
    }


    @Accessor(qualifier = "paymentAddresss2CartToOrderCronJob", type = Accessor.Type.GETTER)
    public Collection<CartToOrderCronJobModel> getPaymentAddresss2CartToOrderCronJob()
    {
        return (Collection<CartToOrderCronJobModel>)getPersistenceContext().getPropertyValue("paymentAddresss2CartToOrderCronJob");
    }


    @Accessor(qualifier = "phone1", type = Accessor.Type.GETTER)
    public String getPhone1()
    {
        return (String)getPersistenceContext().getPropertyValue("phone1");
    }


    @Accessor(qualifier = "phone2", type = Accessor.Type.GETTER)
    public String getPhone2()
    {
        return (String)getPersistenceContext().getPropertyValue("phone2");
    }


    @Accessor(qualifier = "pobox", type = Accessor.Type.GETTER)
    public String getPobox()
    {
        return (String)getPersistenceContext().getPropertyValue("pobox");
    }


    @Accessor(qualifier = "postalcode", type = Accessor.Type.GETTER)
    public String getPostalcode()
    {
        return (String)getPersistenceContext().getPropertyValue("postalcode");
    }


    @Accessor(qualifier = "publicKey", type = Accessor.Type.GETTER)
    public String getPublicKey()
    {
        return (String)getPersistenceContext().getPropertyValue("publicKey");
    }


    @Accessor(qualifier = "region", type = Accessor.Type.GETTER)
    public RegionModel getRegion()
    {
        return (RegionModel)getPersistenceContext().getPropertyValue("region");
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
    public String getRemarks()
    {
        return (String)getPersistenceContext().getPropertyValue("remarks");
    }


    @Accessor(qualifier = "sapAddressUsage", type = Accessor.Type.GETTER)
    public String getSapAddressUsage()
    {
        return (String)getPersistenceContext().getPropertyValue("sapAddressUsage");
    }


    @Accessor(qualifier = "sapAddressUsageCounter", type = Accessor.Type.GETTER)
    public String getSapAddressUsageCounter()
    {
        return (String)getPersistenceContext().getPropertyValue("sapAddressUsageCounter");
    }


    @Accessor(qualifier = "sapAddressUUID", type = Accessor.Type.GETTER)
    public String getSapAddressUUID()
    {
        return (String)getPersistenceContext().getPropertyValue("sapAddressUUID");
    }


    @Accessor(qualifier = "sapCustomerID", type = Accessor.Type.GETTER)
    public String getSapCustomerID()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCustomerID");
    }


    @Accessor(qualifier = "sapMessageFunction", type = Accessor.Type.GETTER)
    public String getSapMessageFunction()
    {
        return (String)getPersistenceContext().getPropertyValue("sapMessageFunction");
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
    public Boolean getShippingAddress()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("shippingAddress");
    }


    @Accessor(qualifier = "streetname", type = Accessor.Type.GETTER)
    public String getStreetname()
    {
        return (String)getPersistenceContext().getPropertyValue("streetname");
    }


    @Accessor(qualifier = "streetnumber", type = Accessor.Type.GETTER)
    public String getStreetnumber()
    {
        return (String)getPersistenceContext().getPropertyValue("streetnumber");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public TitleModel getTitle()
    {
        return (TitleModel)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "town", type = Accessor.Type.GETTER)
    public String getTown()
    {
        return (String)getPersistenceContext().getPropertyValue("town");
    }


    @Accessor(qualifier = "typeQualifier", type = Accessor.Type.GETTER)
    public String getTypeQualifier()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "typeQualifier");
    }


    @Accessor(qualifier = "unloadingAddress", type = Accessor.Type.GETTER)
    public Boolean getUnloadingAddress()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("unloadingAddress");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "visibleInAddressBook", type = Accessor.Type.GETTER)
    public Boolean getVisibleInAddressBook()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("visibleInAddressBook");
    }


    @Accessor(qualifier = "appartment", type = Accessor.Type.SETTER)
    public void setAppartment(String value)
    {
        getPersistenceContext().setPropertyValue("appartment", value);
    }


    @Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
    public void setBillingAddress(Boolean value)
    {
        getPersistenceContext().setPropertyValue("billingAddress", value);
    }


    @Accessor(qualifier = "building", type = Accessor.Type.SETTER)
    public void setBuilding(String value)
    {
        getPersistenceContext().setPropertyValue("building", value);
    }


    @Accessor(qualifier = "cellphone", type = Accessor.Type.SETTER)
    public void setCellphone(String value)
    {
        getPersistenceContext().setPropertyValue("cellphone", value);
    }


    @Accessor(qualifier = "company", type = Accessor.Type.SETTER)
    public void setCompany(String value)
    {
        getPersistenceContext().setPropertyValue("company", value);
    }


    @Accessor(qualifier = "contactAddress", type = Accessor.Type.SETTER)
    public void setContactAddress(Boolean value)
    {
        getPersistenceContext().setPropertyValue("contactAddress", value);
    }


    @Accessor(qualifier = "country", type = Accessor.Type.SETTER)
    public void setCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("country", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setDateofbirth(Date value)
    {
        setDateOfBirth(value);
    }


    @Accessor(qualifier = "dateOfBirth", type = Accessor.Type.SETTER)
    public void setDateOfBirth(Date value)
    {
        getPersistenceContext().setPropertyValue("dateOfBirth", value);
    }


    @Accessor(qualifier = "deliveryAddresss2CartToOrderCronJob", type = Accessor.Type.SETTER)
    public void setDeliveryAddresss2CartToOrderCronJob(Collection<CartToOrderCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("deliveryAddresss2CartToOrderCronJob", value);
    }


    @Accessor(qualifier = "department", type = Accessor.Type.SETTER)
    public void setDepartment(String value)
    {
        getPersistenceContext().setPropertyValue("department", value);
    }


    @Accessor(qualifier = "district", type = Accessor.Type.SETTER)
    public void setDistrict(String value)
    {
        getPersistenceContext().setPropertyValue("district", value);
    }


    @Accessor(qualifier = "duplicate", type = Accessor.Type.SETTER)
    public void setDuplicate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("duplicate", value);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.SETTER)
    public void setEmail(String value)
    {
        getPersistenceContext().setPropertyValue("email", value);
    }


    @Accessor(qualifier = "fax", type = Accessor.Type.SETTER)
    public void setFax(String value)
    {
        getPersistenceContext().setPropertyValue("fax", value);
    }


    @Accessor(qualifier = "firstname", type = Accessor.Type.SETTER)
    public void setFirstname(String value)
    {
        getPersistenceContext().setPropertyValue("firstname", value);
    }


    @Accessor(qualifier = "gender", type = Accessor.Type.SETTER)
    public void setGender(Gender value)
    {
        getPersistenceContext().setPropertyValue("gender", value);
    }


    @Accessor(qualifier = "lastname", type = Accessor.Type.SETTER)
    public void setLastname(String value)
    {
        getPersistenceContext().setPropertyValue("lastname", value);
    }


    @Accessor(qualifier = "line1", type = Accessor.Type.SETTER)
    public void setLine1(String value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "line1", value);
    }


    @Accessor(qualifier = "line2", type = Accessor.Type.SETTER)
    public void setLine2(String value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "line2", value);
    }


    @Accessor(qualifier = "middlename", type = Accessor.Type.SETTER)
    public void setMiddlename(String value)
    {
        getPersistenceContext().setPropertyValue("middlename", value);
    }


    @Accessor(qualifier = "middlename2", type = Accessor.Type.SETTER)
    public void setMiddlename2(String value)
    {
        getPersistenceContext().setPropertyValue("middlename2", value);
    }


    @Accessor(qualifier = "original", type = Accessor.Type.SETTER)
    public void setOriginal(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("original", value);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        super.setOwner(value);
    }


    @Accessor(qualifier = "paymentAddresss2CartToOrderCronJob", type = Accessor.Type.SETTER)
    public void setPaymentAddresss2CartToOrderCronJob(Collection<CartToOrderCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("paymentAddresss2CartToOrderCronJob", value);
    }


    @Accessor(qualifier = "phone1", type = Accessor.Type.SETTER)
    public void setPhone1(String value)
    {
        getPersistenceContext().setPropertyValue("phone1", value);
    }


    @Accessor(qualifier = "phone2", type = Accessor.Type.SETTER)
    public void setPhone2(String value)
    {
        getPersistenceContext().setPropertyValue("phone2", value);
    }


    @Accessor(qualifier = "pobox", type = Accessor.Type.SETTER)
    public void setPobox(String value)
    {
        getPersistenceContext().setPropertyValue("pobox", value);
    }


    @Accessor(qualifier = "postalcode", type = Accessor.Type.SETTER)
    public void setPostalcode(String value)
    {
        getPersistenceContext().setPropertyValue("postalcode", value);
    }


    @Accessor(qualifier = "publicKey", type = Accessor.Type.SETTER)
    public void setPublicKey(String value)
    {
        getPersistenceContext().setPropertyValue("publicKey", value);
    }


    @Accessor(qualifier = "region", type = Accessor.Type.SETTER)
    public void setRegion(RegionModel value)
    {
        getPersistenceContext().setPropertyValue("region", value);
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
    public void setRemarks(String value)
    {
        getPersistenceContext().setPropertyValue("remarks", value);
    }


    @Accessor(qualifier = "sapAddressUsage", type = Accessor.Type.SETTER)
    public void setSapAddressUsage(String value)
    {
        getPersistenceContext().setPropertyValue("sapAddressUsage", value);
    }


    @Accessor(qualifier = "sapAddressUsageCounter", type = Accessor.Type.SETTER)
    public void setSapAddressUsageCounter(String value)
    {
        getPersistenceContext().setPropertyValue("sapAddressUsageCounter", value);
    }


    @Accessor(qualifier = "sapAddressUUID", type = Accessor.Type.SETTER)
    public void setSapAddressUUID(String value)
    {
        getPersistenceContext().setPropertyValue("sapAddressUUID", value);
    }


    @Accessor(qualifier = "sapCustomerID", type = Accessor.Type.SETTER)
    public void setSapCustomerID(String value)
    {
        getPersistenceContext().setPropertyValue("sapCustomerID", value);
    }


    @Accessor(qualifier = "sapMessageFunction", type = Accessor.Type.SETTER)
    public void setSapMessageFunction(String value)
    {
        getPersistenceContext().setPropertyValue("sapMessageFunction", value);
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
    public void setShippingAddress(Boolean value)
    {
        getPersistenceContext().setPropertyValue("shippingAddress", value);
    }


    @Accessor(qualifier = "streetname", type = Accessor.Type.SETTER)
    public void setStreetname(String value)
    {
        getPersistenceContext().setPropertyValue("streetname", value);
    }


    @Accessor(qualifier = "streetnumber", type = Accessor.Type.SETTER)
    public void setStreetnumber(String value)
    {
        getPersistenceContext().setPropertyValue("streetnumber", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(TitleModel value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }


    @Accessor(qualifier = "town", type = Accessor.Type.SETTER)
    public void setTown(String value)
    {
        getPersistenceContext().setPropertyValue("town", value);
    }


    @Accessor(qualifier = "unloadingAddress", type = Accessor.Type.SETTER)
    public void setUnloadingAddress(Boolean value)
    {
        getPersistenceContext().setPropertyValue("unloadingAddress", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }


    @Accessor(qualifier = "visibleInAddressBook", type = Accessor.Type.SETTER)
    public void setVisibleInAddressBook(Boolean value)
    {
        getPersistenceContext().setPropertyValue("visibleInAddressBook", value);
    }
}
