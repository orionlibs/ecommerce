package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ReferenceDivisionMappingModel extends ItemModel
{
    public static final String _TYPECODE = "ReferenceDivisionMapping";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DIVISION = "division";
    public static final String DIVISIONNAME = "divisionName";
    public static final String REFDIVISIONCONDITIONS = "refDivisionConditions";
    public static final String REFDIVISIONCONDITIONSNAME = "refDivisionConditionsName";
    public static final String REFDIVISIONCUSTOMER = "refDivisionCustomer";
    public static final String REFDIVISIONCUSTOMERNAME = "refDivisionCustomerName";


    public ReferenceDivisionMappingModel()
    {
    }


    public ReferenceDivisionMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReferenceDivisionMappingModel(String _division, String _refDivisionConditions, String _refDivisionCustomer, String _salesOrganization)
    {
        setDivision(_division);
        setRefDivisionConditions(_refDivisionConditions);
        setRefDivisionCustomer(_refDivisionCustomer);
        setSalesOrganization(_salesOrganization);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReferenceDivisionMappingModel(String _division, ItemModel _owner, String _refDivisionConditions, String _refDivisionCustomer, String _salesOrganization)
    {
        setDivision(_division);
        setOwner(_owner);
        setRefDivisionConditions(_refDivisionConditions);
        setRefDivisionCustomer(_refDivisionCustomer);
        setSalesOrganization(_salesOrganization);
    }


    @Accessor(qualifier = "division", type = Accessor.Type.GETTER)
    public String getDivision()
    {
        return (String)getPersistenceContext().getPropertyValue("division");
    }


    @Accessor(qualifier = "divisionName", type = Accessor.Type.GETTER)
    public String getDivisionName()
    {
        return getDivisionName(null);
    }


    @Accessor(qualifier = "divisionName", type = Accessor.Type.GETTER)
    public String getDivisionName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("divisionName", loc);
    }


    @Accessor(qualifier = "refDivisionConditions", type = Accessor.Type.GETTER)
    public String getRefDivisionConditions()
    {
        return (String)getPersistenceContext().getPropertyValue("refDivisionConditions");
    }


    @Accessor(qualifier = "refDivisionConditionsName", type = Accessor.Type.GETTER)
    public String getRefDivisionConditionsName()
    {
        return getRefDivisionConditionsName(null);
    }


    @Accessor(qualifier = "refDivisionConditionsName", type = Accessor.Type.GETTER)
    public String getRefDivisionConditionsName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("refDivisionConditionsName", loc);
    }


    @Accessor(qualifier = "refDivisionCustomer", type = Accessor.Type.GETTER)
    public String getRefDivisionCustomer()
    {
        return (String)getPersistenceContext().getPropertyValue("refDivisionCustomer");
    }


    @Accessor(qualifier = "refDivisionCustomerName", type = Accessor.Type.GETTER)
    public String getRefDivisionCustomerName()
    {
        return getRefDivisionCustomerName(null);
    }


    @Accessor(qualifier = "refDivisionCustomerName", type = Accessor.Type.GETTER)
    public String getRefDivisionCustomerName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("refDivisionCustomerName", loc);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.GETTER)
    public String getSalesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("salesOrganization");
    }


    @Accessor(qualifier = "division", type = Accessor.Type.SETTER)
    public void setDivision(String value)
    {
        getPersistenceContext().setPropertyValue("division", value);
    }


    @Accessor(qualifier = "divisionName", type = Accessor.Type.SETTER)
    public void setDivisionName(String value)
    {
        setDivisionName(value, null);
    }


    @Accessor(qualifier = "divisionName", type = Accessor.Type.SETTER)
    public void setDivisionName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("divisionName", loc, value);
    }


    @Accessor(qualifier = "refDivisionConditions", type = Accessor.Type.SETTER)
    public void setRefDivisionConditions(String value)
    {
        getPersistenceContext().setPropertyValue("refDivisionConditions", value);
    }


    @Accessor(qualifier = "refDivisionConditionsName", type = Accessor.Type.SETTER)
    public void setRefDivisionConditionsName(String value)
    {
        setRefDivisionConditionsName(value, null);
    }


    @Accessor(qualifier = "refDivisionConditionsName", type = Accessor.Type.SETTER)
    public void setRefDivisionConditionsName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("refDivisionConditionsName", loc, value);
    }


    @Accessor(qualifier = "refDivisionCustomer", type = Accessor.Type.SETTER)
    public void setRefDivisionCustomer(String value)
    {
        getPersistenceContext().setPropertyValue("refDivisionCustomer", value);
    }


    @Accessor(qualifier = "refDivisionCustomerName", type = Accessor.Type.SETTER)
    public void setRefDivisionCustomerName(String value)
    {
        setRefDivisionCustomerName(value, null);
    }


    @Accessor(qualifier = "refDivisionCustomerName", type = Accessor.Type.SETTER)
    public void setRefDivisionCustomerName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("refDivisionCustomerName", loc, value);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.SETTER)
    public void setSalesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("salesOrganization", value);
    }
}
