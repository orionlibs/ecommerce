package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

public class FlexibleSearchRetentionRuleModel extends AbstractRetentionRuleModel
{
    public static final String _TYPECODE = "FlexibleSearchRetentionRule";
    public static final String SEARCHQUERY = "searchQuery";
    public static final String QUERYPARAMETERS = "queryParameters";
    public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";


    public FlexibleSearchRetentionRuleModel()
    {
    }


    public FlexibleSearchRetentionRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchRetentionRuleModel(String _actionReference, String _code, String _searchQuery)
    {
        setActionReference(_actionReference);
        setCode(_code);
        setSearchQuery(_searchQuery);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchRetentionRuleModel(String _actionReference, String _code, ItemModel _owner, String _searchQuery)
    {
        setActionReference(_actionReference);
        setCode(_code);
        setOwner(_owner);
        setSearchQuery(_searchQuery);
    }


    @Accessor(qualifier = "queryParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getQueryParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("queryParameters");
    }


    @Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.GETTER)
    public Long getRetentionTimeSeconds()
    {
        return (Long)getPersistenceContext().getPropertyValue("retentionTimeSeconds");
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
    public String getSearchQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("searchQuery");
    }


    @Accessor(qualifier = "queryParameters", type = Accessor.Type.SETTER)
    public void setQueryParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("queryParameters", value);
    }


    @Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.SETTER)
    public void setRetentionTimeSeconds(Long value)
    {
        getPersistenceContext().setPropertyValue("retentionTimeSeconds", value);
    }


    @Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
    public void setSearchQuery(String value)
    {
        getPersistenceContext().setPropertyValue("searchQuery", value);
    }
}
