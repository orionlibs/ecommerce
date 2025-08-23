package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

public class ViewTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "ViewType";
    public static final String COLUMNS = "columns";
    public static final String PARAMS = "params";
    public static final String QUERY = "query";


    public ViewTypeModel()
    {
    }


    public ViewTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ViewTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, String _query, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setQuery(_query);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ViewTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, String _query, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setQuery(_query);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "columns", type = Accessor.Type.GETTER)
    public List<ViewAttributeDescriptorModel> getColumns()
    {
        return (List<ViewAttributeDescriptorModel>)getPersistenceContext().getPropertyValue("columns");
    }


    @Accessor(qualifier = "params", type = Accessor.Type.GETTER)
    public Set<ViewAttributeDescriptorModel> getParams()
    {
        return (Set<ViewAttributeDescriptorModel>)getPersistenceContext().getPropertyValue("params");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "columns", type = Accessor.Type.SETTER)
    public void setColumns(List<ViewAttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("columns", value);
    }


    @Accessor(qualifier = "params", type = Accessor.Type.SETTER)
    public void setParams(Set<ViewAttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("params", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }
}
