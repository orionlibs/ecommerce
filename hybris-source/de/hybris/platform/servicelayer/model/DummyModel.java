package de.hybris.platform.servicelayer.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Collection;
import java.util.Locale;

public class DummyModel extends AbstractItemModel
{
    public static final String _TYPECODE = "dummyModel";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DISCOUNTSINCLUDEDELIVERYCOST = "discountsIncludeDeliveryCost";
    public static final String ALLSUBCATEGORIES = "allSubcategories";
    public static final String ASSIGNMENT = "assignment";


    public DummyModel()
    {
    }


    public DummyModel(ItemModelInternalContext ctx)
    {
        super((ItemModelContext)ctx);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public DummyModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    public String getName()
    {
        return getName(null);
    }


    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    public void setName(String value)
    {
        setName(value, null);
    }


    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    public boolean isDiscountsIncludeDeliveryCost()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("discountsIncludeDeliveryCost"));
    }


    public void setDiscountsIncludeDeliveryCost(boolean value)
    {
        getPersistenceContext().setPropertyValue("discountsIncludeDeliveryCost", toObject(value));
    }


    public Collection<String> getAllSubcategories()
    {
        return (Collection<String>)getPersistenceContext().getDynamicValue(this, "allSubcategories");
    }


    public void setAllSubcategories(Collection<String> subs)
    {
        getPersistenceContext().setDynamicValue(this, "allSubcategories", subs);
    }


    public int getDynFoo()
    {
        return toPrimitive((Integer)getPersistenceContext().getDynamicValue(this, "allSubcategories"));
    }


    public void setDynFoo(int value)
    {
        getPersistenceContext().setDynamicValue(this, "allSubcategories", toObject(value));
    }


    public String getAssignment()
    {
        return getAssignment(null);
    }


    public String getAssignment(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedDynamicValue(this, "assignment", loc);
    }


    public void setAssignment(String value)
    {
        setAssignment(value, null);
    }


    public void setAssignment(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedDynamicValue(this, "assignment", loc, value);
    }
}
