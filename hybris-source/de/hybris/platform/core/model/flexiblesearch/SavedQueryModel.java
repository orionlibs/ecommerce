package de.hybris.platform.core.model.flexiblesearch;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Map;

public class SavedQueryModel extends ItemModel
{
    public static final String _TYPECODE = "SavedQuery";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PARAMTYPES = "paramtypes";
    public static final String PARAMS = "params";
    public static final String QUERY = "query";
    public static final String RESULTTYPE = "resultType";


    public SavedQueryModel()
    {
    }


    public SavedQueryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedQueryModel(String _code, String _query, ComposedTypeModel _resultType)
    {
        setCode(_code);
        setQuery(_query);
        setResultType(_resultType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedQueryModel(String _code, ItemModel _owner, String _query, ComposedTypeModel _resultType)
    {
        setCode(_code);
        setOwner(_owner);
        setQuery(_query);
        setResultType(_resultType);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "params", type = Accessor.Type.GETTER)
    public Map<String, TypeModel> getParams()
    {
        return (Map<String, TypeModel>)getPersistenceContext().getPropertyValue("params");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "resultType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getResultType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("resultType");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "params", type = Accessor.Type.SETTER)
    public void setParams(Map<String, TypeModel> value)
    {
        getPersistenceContext().setPropertyValue("params", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "resultType", type = Accessor.Type.SETTER)
    public void setResultType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("resultType", value);
    }
}
