package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundAddressModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundAddress";
    public static final String _SAPCPIOUTBOUNDORDER2SAPCPIOUTBOUNDADDRESS = "SAPCpiOutboundOrder2SAPCpiOutboundAddress";
    public static final String ORDERID = "orderId";
    public static final String DOCUMENTADDRESSID = "documentAddressId";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String MIDDLENAME = "middleName";
    public static final String MIDDLENAME2 = "middleName2";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String BUILDING = "building";
    public static final String APARTMENT = "apartment";
    public static final String POBOX = "pobox";
    public static final String FAXNUMBER = "faxNumber";
    public static final String TITLECODE = "titleCode";
    public static final String TELNUMBER = "telNumber";
    public static final String HOUSENUMBER = "houseNumber";
    public static final String POSTALCODE = "postalCode";
    public static final String REGIONISOCODE = "regionIsoCode";
    public static final String COUNTRYISOCODE = "countryIsoCode";
    public static final String EMAIL = "email";
    public static final String LANGUAGEISOCODE = "languageIsoCode";
    public static final String SAPCPIOUTBOUNDORDER = "sapCpiOutboundOrder";


    public SAPCpiOutboundAddressModel()
    {
    }


    public SAPCpiOutboundAddressModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundAddressModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "apartment", type = Accessor.Type.GETTER)
    public String getApartment()
    {
        return (String)getPersistenceContext().getPropertyValue("apartment");
    }


    @Accessor(qualifier = "building", type = Accessor.Type.GETTER)
    public String getBuilding()
    {
        return (String)getPersistenceContext().getPropertyValue("building");
    }


    @Accessor(qualifier = "city", type = Accessor.Type.GETTER)
    public String getCity()
    {
        return (String)getPersistenceContext().getPropertyValue("city");
    }


    @Accessor(qualifier = "countryIsoCode", type = Accessor.Type.GETTER)
    public String getCountryIsoCode()
    {
        return (String)getPersistenceContext().getPropertyValue("countryIsoCode");
    }


    @Accessor(qualifier = "district", type = Accessor.Type.GETTER)
    public String getDistrict()
    {
        return (String)getPersistenceContext().getPropertyValue("district");
    }


    @Accessor(qualifier = "documentAddressId", type = Accessor.Type.GETTER)
    public String getDocumentAddressId()
    {
        return (String)getPersistenceContext().getPropertyValue("documentAddressId");
    }


    @Accessor(qualifier = "email", type = Accessor.Type.GETTER)
    public String getEmail()
    {
        return (String)getPersistenceContext().getPropertyValue("email");
    }


    @Accessor(qualifier = "faxNumber", type = Accessor.Type.GETTER)
    public String getFaxNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("faxNumber");
    }


    @Accessor(qualifier = "firstName", type = Accessor.Type.GETTER)
    public String getFirstName()
    {
        return (String)getPersistenceContext().getPropertyValue("firstName");
    }


    @Accessor(qualifier = "houseNumber", type = Accessor.Type.GETTER)
    public String getHouseNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("houseNumber");
    }


    @Accessor(qualifier = "languageIsoCode", type = Accessor.Type.GETTER)
    public String getLanguageIsoCode()
    {
        return (String)getPersistenceContext().getPropertyValue("languageIsoCode");
    }


    @Accessor(qualifier = "lastName", type = Accessor.Type.GETTER)
    public String getLastName()
    {
        return (String)getPersistenceContext().getPropertyValue("lastName");
    }


    @Accessor(qualifier = "middleName", type = Accessor.Type.GETTER)
    public String getMiddleName()
    {
        return (String)getPersistenceContext().getPropertyValue("middleName");
    }


    @Accessor(qualifier = "middleName2", type = Accessor.Type.GETTER)
    public String getMiddleName2()
    {
        return (String)getPersistenceContext().getPropertyValue("middleName2");
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.GETTER)
    public String getOrderId()
    {
        return (String)getPersistenceContext().getPropertyValue("orderId");
    }


    @Accessor(qualifier = "pobox", type = Accessor.Type.GETTER)
    public String getPobox()
    {
        return (String)getPersistenceContext().getPropertyValue("pobox");
    }


    @Accessor(qualifier = "postalCode", type = Accessor.Type.GETTER)
    public String getPostalCode()
    {
        return (String)getPersistenceContext().getPropertyValue("postalCode");
    }


    @Accessor(qualifier = "regionIsoCode", type = Accessor.Type.GETTER)
    public String getRegionIsoCode()
    {
        return (String)getPersistenceContext().getPropertyValue("regionIsoCode");
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.GETTER)
    public SAPCpiOutboundOrderModel getSapCpiOutboundOrder()
    {
        return (SAPCpiOutboundOrderModel)getPersistenceContext().getPropertyValue("sapCpiOutboundOrder");
    }


    @Accessor(qualifier = "street", type = Accessor.Type.GETTER)
    public String getStreet()
    {
        return (String)getPersistenceContext().getPropertyValue("street");
    }


    @Accessor(qualifier = "telNumber", type = Accessor.Type.GETTER)
    public String getTelNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("telNumber");
    }


    @Accessor(qualifier = "titleCode", type = Accessor.Type.GETTER)
    public String getTitleCode()
    {
        return (String)getPersistenceContext().getPropertyValue("titleCode");
    }


    @Accessor(qualifier = "apartment", type = Accessor.Type.SETTER)
    public void setApartment(String value)
    {
        getPersistenceContext().setPropertyValue("apartment", value);
    }


    @Accessor(qualifier = "building", type = Accessor.Type.SETTER)
    public void setBuilding(String value)
    {
        getPersistenceContext().setPropertyValue("building", value);
    }


    @Accessor(qualifier = "city", type = Accessor.Type.SETTER)
    public void setCity(String value)
    {
        getPersistenceContext().setPropertyValue("city", value);
    }


    @Accessor(qualifier = "countryIsoCode", type = Accessor.Type.SETTER)
    public void setCountryIsoCode(String value)
    {
        getPersistenceContext().setPropertyValue("countryIsoCode", value);
    }


    @Accessor(qualifier = "district", type = Accessor.Type.SETTER)
    public void setDistrict(String value)
    {
        getPersistenceContext().setPropertyValue("district", value);
    }


    @Accessor(qualifier = "documentAddressId", type = Accessor.Type.SETTER)
    public void setDocumentAddressId(String value)
    {
        getPersistenceContext().setPropertyValue("documentAddressId", value);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.SETTER)
    public void setEmail(String value)
    {
        getPersistenceContext().setPropertyValue("email", value);
    }


    @Accessor(qualifier = "faxNumber", type = Accessor.Type.SETTER)
    public void setFaxNumber(String value)
    {
        getPersistenceContext().setPropertyValue("faxNumber", value);
    }


    @Accessor(qualifier = "firstName", type = Accessor.Type.SETTER)
    public void setFirstName(String value)
    {
        getPersistenceContext().setPropertyValue("firstName", value);
    }


    @Accessor(qualifier = "houseNumber", type = Accessor.Type.SETTER)
    public void setHouseNumber(String value)
    {
        getPersistenceContext().setPropertyValue("houseNumber", value);
    }


    @Accessor(qualifier = "languageIsoCode", type = Accessor.Type.SETTER)
    public void setLanguageIsoCode(String value)
    {
        getPersistenceContext().setPropertyValue("languageIsoCode", value);
    }


    @Accessor(qualifier = "lastName", type = Accessor.Type.SETTER)
    public void setLastName(String value)
    {
        getPersistenceContext().setPropertyValue("lastName", value);
    }


    @Accessor(qualifier = "middleName", type = Accessor.Type.SETTER)
    public void setMiddleName(String value)
    {
        getPersistenceContext().setPropertyValue("middleName", value);
    }


    @Accessor(qualifier = "middleName2", type = Accessor.Type.SETTER)
    public void setMiddleName2(String value)
    {
        getPersistenceContext().setPropertyValue("middleName2", value);
    }


    @Accessor(qualifier = "orderId", type = Accessor.Type.SETTER)
    public void setOrderId(String value)
    {
        getPersistenceContext().setPropertyValue("orderId", value);
    }


    @Accessor(qualifier = "pobox", type = Accessor.Type.SETTER)
    public void setPobox(String value)
    {
        getPersistenceContext().setPropertyValue("pobox", value);
    }


    @Accessor(qualifier = "postalCode", type = Accessor.Type.SETTER)
    public void setPostalCode(String value)
    {
        getPersistenceContext().setPropertyValue("postalCode", value);
    }


    @Accessor(qualifier = "regionIsoCode", type = Accessor.Type.SETTER)
    public void setRegionIsoCode(String value)
    {
        getPersistenceContext().setPropertyValue("regionIsoCode", value);
    }


    @Accessor(qualifier = "sapCpiOutboundOrder", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundOrder(SAPCpiOutboundOrderModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundOrder", value);
    }


    @Accessor(qualifier = "street", type = Accessor.Type.SETTER)
    public void setStreet(String value)
    {
        getPersistenceContext().setPropertyValue("street", value);
    }


    @Accessor(qualifier = "telNumber", type = Accessor.Type.SETTER)
    public void setTelNumber(String value)
    {
        getPersistenceContext().setPropertyValue("telNumber", value);
    }


    @Accessor(qualifier = "titleCode", type = Accessor.Type.SETTER)
    public void setTitleCode(String value)
    {
        getPersistenceContext().setPropertyValue("titleCode", value);
    }
}
