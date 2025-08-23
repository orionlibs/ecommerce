package de.hybris.platform.advancedsavedquery.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ComposedTypeAdvancedSavedQuerySearchParameterModel extends TypedAdvancedSavedQuerySearchParameterModel
{
    public static final String _TYPECODE = "ComposedTypeAdvancedSavedQuerySearchParameter";


    public ComposedTypeAdvancedSavedQuerySearchParameterModel()
    {
    }


    public ComposedTypeAdvancedSavedQuerySearchParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComposedTypeAdvancedSavedQuerySearchParameterModel(ComposedTypeModel _enclosingType, String _searchParameterName, WherePartModel _wherePart)
    {
        setEnclosingType(_enclosingType);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComposedTypeAdvancedSavedQuerySearchParameterModel(ComposedTypeModel _enclosingType, ItemModel _owner, String _searchParameterName, WherePartModel _wherePart)
    {
        setEnclosingType(_enclosingType);
        setOwner(_owner);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }
}
