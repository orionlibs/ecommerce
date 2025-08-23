package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundCustomerModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundCustomer";
    public static final String UID = "uid";
    public static final String CONTACTID = "contactId";
    public static final String CUSTOMERID = "customerId";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String TITLE = "title";
    public static final String BASESTORE = "baseStore";
    public static final String OBJTYPE = "objType";
    public static final String ADDRESSUSAGE = "addressUsage";
    public static final String ADDRESSUUID = "addressUUID";
    public static final String COUNTRY = "country";
    public static final String STREET = "street";
    public static final String PHONE = "phone";
    public static final String FAX = "fax";
    public static final String TOWN = "town";
    public static final String POSTALCODE = "postalCode";
    public static final String STREETNUMBER = "streetNumber";
    public static final String REGION = "region";
    public static final String RESPONSESTATUS = "responseStatus";
    public static final String RESPONSEMESSAGE = "responseMessage";
    public static final String SAPCPICONFIG = "sapCpiConfig";


    public SAPCpiOutboundCustomerModel()
    {
    }


    public SAPCpiOutboundCustomerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundCustomerModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "addressUsage", type = Accessor.Type.GETTER)
    public String getAddressUsage()
    {
        return (String)getPersistenceContext().getPropertyValue("addressUsage");
    }


    @Accessor(qualifier = "addressUUID", type = Accessor.Type.GETTER)
    public String getAddressUUID()
    {
        return (String)getPersistenceContext().getPropertyValue("addressUUID");
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
    public String getBaseStore()
    {
        return (String)getPersistenceContext().getPropertyValue("baseStore");
    }


    @Accessor(qualifier = "contactId", type = Accessor.Type.GETTER)
    public String getContactId()
    {
        return (String)getPersistenceContext().getPropertyValue("contactId");
    }


    @Accessor(qualifier = "country", type = Accessor.Type.GETTER)
    public String getCountry()
    {
        return (String)getPersistenceContext().getPropertyValue("country");
    }


    @Accessor(qualifier = "customerId", type = Accessor.Type.GETTER)
    public String getCustomerId()
    {
        return (String)getPersistenceContext().getPropertyValue("customerId");
    }


    @Accessor(qualifier = "fax", type = Accessor.Type.GETTER)
    public String getFax()
    {
        return (String)getPersistenceContext().getPropertyValue("fax");
    }


    @Accessor(qualifier = "firstName", type = Accessor.Type.GETTER)
    public String getFirstName()
    {
        return (String)getPersistenceContext().getPropertyValue("firstName");
    }


    @Accessor(qualifier = "lastName", type = Accessor.Type.GETTER)
    public String getLastName()
    {
        return (String)getPersistenceContext().getPropertyValue("lastName");
    }


    @Accessor(qualifier = "objType", type = Accessor.Type.GETTER)
    public String getObjType()
    {
        return (String)getPersistenceContext().getPropertyValue("objType");
    }


    @Accessor(qualifier = "phone", type = Accessor.Type.GETTER)
    public String getPhone()
    {
        return (String)getPersistenceContext().getPropertyValue("phone");
    }


    @Accessor(qualifier = "postalCode", type = Accessor.Type.GETTER)
    public String getPostalCode()
    {
        return (String)getPersistenceContext().getPropertyValue("postalCode");
    }


    @Accessor(qualifier = "region", type = Accessor.Type.GETTER)
    public String getRegion()
    {
        return (String)getPersistenceContext().getPropertyValue("region");
    }


    @Accessor(qualifier = "responseMessage", type = Accessor.Type.GETTER)
    public String getResponseMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("responseMessage");
    }


    @Accessor(qualifier = "responseStatus", type = Accessor.Type.GETTER)
    public String getResponseStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("responseStatus");
    }


    @Accessor(qualifier = "sapCpiConfig", type = Accessor.Type.GETTER)
    public SAPCpiOutboundConfigModel getSapCpiConfig()
    {
        return (SAPCpiOutboundConfigModel)getPersistenceContext().getPropertyValue("sapCpiConfig");
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
    public String getSessionLanguage()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionLanguage");
    }


    @Accessor(qualifier = "street", type = Accessor.Type.GETTER)
    public String getStreet()
    {
        return (String)getPersistenceContext().getPropertyValue("street");
    }


    @Accessor(qualifier = "streetNumber", type = Accessor.Type.GETTER)
    public String getStreetNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("streetNumber");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return (String)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "town", type = Accessor.Type.GETTER)
    public String getTown()
    {
        return (String)getPersistenceContext().getPropertyValue("town");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "addressUsage", type = Accessor.Type.SETTER)
    public void setAddressUsage(String value)
    {
        getPersistenceContext().setPropertyValue("addressUsage", value);
    }


    @Accessor(qualifier = "addressUUID", type = Accessor.Type.SETTER)
    public void setAddressUUID(String value)
    {
        getPersistenceContext().setPropertyValue("addressUUID", value);
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
    public void setBaseStore(String value)
    {
        getPersistenceContext().setPropertyValue("baseStore", value);
    }


    @Accessor(qualifier = "contactId", type = Accessor.Type.SETTER)
    public void setContactId(String value)
    {
        getPersistenceContext().setPropertyValue("contactId", value);
    }


    @Accessor(qualifier = "country", type = Accessor.Type.SETTER)
    public void setCountry(String value)
    {
        getPersistenceContext().setPropertyValue("country", value);
    }


    @Accessor(qualifier = "customerId", type = Accessor.Type.SETTER)
    public void setCustomerId(String value)
    {
        getPersistenceContext().setPropertyValue("customerId", value);
    }


    @Accessor(qualifier = "fax", type = Accessor.Type.SETTER)
    public void setFax(String value)
    {
        getPersistenceContext().setPropertyValue("fax", value);
    }


    @Accessor(qualifier = "firstName", type = Accessor.Type.SETTER)
    public void setFirstName(String value)
    {
        getPersistenceContext().setPropertyValue("firstName", value);
    }


    @Accessor(qualifier = "lastName", type = Accessor.Type.SETTER)
    public void setLastName(String value)
    {
        getPersistenceContext().setPropertyValue("lastName", value);
    }


    @Accessor(qualifier = "objType", type = Accessor.Type.SETTER)
    public void setObjType(String value)
    {
        getPersistenceContext().setPropertyValue("objType", value);
    }


    @Accessor(qualifier = "phone", type = Accessor.Type.SETTER)
    public void setPhone(String value)
    {
        getPersistenceContext().setPropertyValue("phone", value);
    }


    @Accessor(qualifier = "postalCode", type = Accessor.Type.SETTER)
    public void setPostalCode(String value)
    {
        getPersistenceContext().setPropertyValue("postalCode", value);
    }


    @Accessor(qualifier = "region", type = Accessor.Type.SETTER)
    public void setRegion(String value)
    {
        getPersistenceContext().setPropertyValue("region", value);
    }


    @Accessor(qualifier = "responseMessage", type = Accessor.Type.SETTER)
    public void setResponseMessage(String value)
    {
        getPersistenceContext().setPropertyValue("responseMessage", value);
    }


    @Accessor(qualifier = "responseStatus", type = Accessor.Type.SETTER)
    public void setResponseStatus(String value)
    {
        getPersistenceContext().setPropertyValue("responseStatus", value);
    }


    @Accessor(qualifier = "sapCpiConfig", type = Accessor.Type.SETTER)
    public void setSapCpiConfig(SAPCpiOutboundConfigModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiConfig", value);
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
    public void setSessionLanguage(String value)
    {
        getPersistenceContext().setPropertyValue("sessionLanguage", value);
    }


    @Accessor(qualifier = "street", type = Accessor.Type.SETTER)
    public void setStreet(String value)
    {
        getPersistenceContext().setPropertyValue("street", value);
    }


    @Accessor(qualifier = "streetNumber", type = Accessor.Type.SETTER)
    public void setStreetNumber(String value)
    {
        getPersistenceContext().setPropertyValue("streetNumber", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }


    @Accessor(qualifier = "town", type = Accessor.Type.SETTER)
    public void setTown(String value)
    {
        getPersistenceContext().setPropertyValue("town", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }
}
