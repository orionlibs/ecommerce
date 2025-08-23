package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class TypedAdvancedSavedQuerySearchParameterModel extends AbstractAdvancedSavedQuerySearchParameterModel
{
    public static final String _TYPECODE = "TypedAdvancedSavedQuerySearchParameter";
    public static final String TYPEDSEARCHPARAMETER = "typedSearchParameter";
    public static final String TYPEATTRIBUTES = "typeAttributes";
    public static final String ENCLOSINGTYPE = "enclosingType";


    public TypedAdvancedSavedQuerySearchParameterModel()
    {
    }


    public TypedAdvancedSavedQuerySearchParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypedAdvancedSavedQuerySearchParameterModel(ComposedTypeModel _enclosingType, String _searchParameterName, WherePartModel _wherePart)
    {
        setEnclosingType(_enclosingType);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypedAdvancedSavedQuerySearchParameterModel(ComposedTypeModel _enclosingType, ItemModel _owner, String _searchParameterName, WherePartModel _wherePart)
    {
        setEnclosingType(_enclosingType);
        setOwner(_owner);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getEnclosingType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("enclosingType");
    }


    @Accessor(qualifier = "typeAttributes", type = Accessor.Type.GETTER)
    public Collection<AttributeDescriptorModel> getTypeAttributes()
    {
        return (Collection<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("typeAttributes");
    }


    @Accessor(qualifier = "typedSearchParameter", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getTypedSearchParameter()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("typedSearchParameter");
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
    public void setEnclosingType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("enclosingType", value);
    }


    @Accessor(qualifier = "typedSearchParameter", type = Accessor.Type.SETTER)
    public void setTypedSearchParameter(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("typedSearchParameter", value);
    }
}
