package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class WherePartModel extends ItemModel
{
    public static final String _TYPECODE = "WherePart";
    public static final String _QUERY2WHEREPARTRELATION = "Query2WherePartRelation";
    public static final String AND = "and";
    public static final String REPLACEPATTERN = "replacePattern";
    public static final String SAVEDQUERY = "savedQuery";
    public static final String DYNAMICPARAMS = "dynamicParams";


    public WherePartModel()
    {
    }


    public WherePartModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WherePartModel(String _replacePattern, AdvancedSavedQueryModel _savedQuery)
    {
        setReplacePattern(_replacePattern);
        setSavedQuery(_savedQuery);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WherePartModel(ItemModel _owner, String _replacePattern, AdvancedSavedQueryModel _savedQuery)
    {
        setOwner(_owner);
        setReplacePattern(_replacePattern);
        setSavedQuery(_savedQuery);
    }


    @Accessor(qualifier = "and", type = Accessor.Type.GETTER)
    public Boolean getAnd()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("and");
    }


    @Accessor(qualifier = "dynamicParams", type = Accessor.Type.GETTER)
    public Collection<AbstractAdvancedSavedQuerySearchParameterModel> getDynamicParams()
    {
        return (Collection<AbstractAdvancedSavedQuerySearchParameterModel>)getPersistenceContext().getPropertyValue("dynamicParams");
    }


    @Accessor(qualifier = "replacePattern", type = Accessor.Type.GETTER)
    public String getReplacePattern()
    {
        return (String)getPersistenceContext().getPropertyValue("replacePattern");
    }


    @Accessor(qualifier = "savedQuery", type = Accessor.Type.GETTER)
    public AdvancedSavedQueryModel getSavedQuery()
    {
        return (AdvancedSavedQueryModel)getPersistenceContext().getPropertyValue("savedQuery");
    }


    @Accessor(qualifier = "and", type = Accessor.Type.SETTER)
    public void setAnd(Boolean value)
    {
        getPersistenceContext().setPropertyValue("and", value);
    }


    @Accessor(qualifier = "dynamicParams", type = Accessor.Type.SETTER)
    public void setDynamicParams(Collection<AbstractAdvancedSavedQuerySearchParameterModel> value)
    {
        getPersistenceContext().setPropertyValue("dynamicParams", value);
    }


    @Accessor(qualifier = "replacePattern", type = Accessor.Type.SETTER)
    public void setReplacePattern(String value)
    {
        getPersistenceContext().setPropertyValue("replacePattern", value);
    }


    @Accessor(qualifier = "savedQuery", type = Accessor.Type.SETTER)
    public void setSavedQuery(AdvancedSavedQueryModel value)
    {
        getPersistenceContext().setPropertyValue("savedQuery", value);
    }
}
