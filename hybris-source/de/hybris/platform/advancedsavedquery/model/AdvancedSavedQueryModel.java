package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.flexiblesearch.SavedQueryModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AdvancedSavedQueryModel extends SavedQueryModel
{
    public static final String _TYPECODE = "AdvancedSavedQuery";
    public static final String GENERATEDFLEXIBLESEARCH = "generatedFlexibleSearch";
    public static final String WHEREPARTS = "whereparts";


    public AdvancedSavedQueryModel()
    {
    }


    public AdvancedSavedQueryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AdvancedSavedQueryModel(String _code, String _query, ComposedTypeModel _resultType)
    {
        setCode(_code);
        setQuery(_query);
        setResultType(_resultType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AdvancedSavedQueryModel(String _code, ItemModel _owner, String _query, ComposedTypeModel _resultType)
    {
        setCode(_code);
        setOwner(_owner);
        setQuery(_query);
        setResultType(_resultType);
    }


    @Accessor(qualifier = "generatedFlexibleSearch", type = Accessor.Type.GETTER)
    public String getGeneratedFlexibleSearch()
    {
        return (String)getPersistenceContext().getPropertyValue("generatedFlexibleSearch");
    }


    @Accessor(qualifier = "whereparts", type = Accessor.Type.GETTER)
    public Collection<WherePartModel> getWhereparts()
    {
        return (Collection<WherePartModel>)getPersistenceContext().getPropertyValue("whereparts");
    }


    @Accessor(qualifier = "whereparts", type = Accessor.Type.SETTER)
    public void setWhereparts(Collection<WherePartModel> value)
    {
        getPersistenceContext().setPropertyValue("whereparts", value);
    }
}
