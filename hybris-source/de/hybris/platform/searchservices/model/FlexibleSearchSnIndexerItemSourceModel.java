package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FlexibleSearchSnIndexerItemSourceModel extends AbstractSnIndexerItemSourceModel
{
    public static final String _TYPECODE = "FlexibleSearchSnIndexerItemSource";
    public static final String QUERY = "query";


    public FlexibleSearchSnIndexerItemSourceModel()
    {
    }


    public FlexibleSearchSnIndexerItemSourceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchSnIndexerItemSourceModel(String _query)
    {
        setQuery(_query);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchSnIndexerItemSourceModel(ItemModel _owner, String _query)
    {
        setOwner(_owner);
        setQuery(_query);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }
}
