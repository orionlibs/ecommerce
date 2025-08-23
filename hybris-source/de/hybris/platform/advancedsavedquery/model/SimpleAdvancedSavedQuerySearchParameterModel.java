package de.hybris.platform.advancedsavedquery.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleAdvancedSavedQuerySearchParameterModel extends AbstractAdvancedSavedQuerySearchParameterModel
{
    public static final String _TYPECODE = "SimpleAdvancedSavedQuerySearchParameter";


    public SimpleAdvancedSavedQuerySearchParameterModel()
    {
    }


    public SimpleAdvancedSavedQuerySearchParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleAdvancedSavedQuerySearchParameterModel(String _searchParameterName, WherePartModel _wherePart)
    {
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleAdvancedSavedQuerySearchParameterModel(ItemModel _owner, String _searchParameterName, WherePartModel _wherePart)
    {
        setOwner(_owner);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }
}
