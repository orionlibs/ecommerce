package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class B2BReportingSetModel extends ItemModel
{
    public static final String _TYPECODE = "B2BReportingSet";
    public static final String _B2BREPORTINGENTRY = "B2BReportingEntry";
    public static final String CODE = "code";
    public static final String REPORTINGENTRIES = "ReportingEntries";


    public B2BReportingSetModel()
    {
    }


    public B2BReportingSetModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BReportingSetModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BReportingSetModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "ReportingEntries", type = Accessor.Type.GETTER)
    public Set<ItemModel> getReportingEntries()
    {
        return (Set<ItemModel>)getPersistenceContext().getPropertyValue("ReportingEntries");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "ReportingEntries", type = Accessor.Type.SETTER)
    public void setReportingEntries(Set<ItemModel> value)
    {
        getPersistenceContext().setPropertyValue("ReportingEntries", value);
    }
}
