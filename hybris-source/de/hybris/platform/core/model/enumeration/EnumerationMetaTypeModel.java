package de.hybris.platform.core.model.enumeration;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class EnumerationMetaTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "EnumerationMetaType";
    public static final String COMPARATIONATTRIBUTE = "comparationAttribute";
    public static final String VALUES = "values";
    public static final String VALUETYPE = "valueType";
    public static final String ISSORTED = "isSorted";
    public static final String ISRESORTABLE = "isResortable";


    public EnumerationMetaTypeModel()
    {
    }


    public EnumerationMetaTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EnumerationMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EnumerationMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType, ComposedTypeModel _valueType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
        setValueType(_valueType);
    }


    @Accessor(qualifier = "comparationAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getComparationAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("comparationAttribute");
    }


    @Accessor(qualifier = "isResortable", type = Accessor.Type.GETTER)
    public Boolean getIsResortable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("isResortable");
    }


    @Accessor(qualifier = "isSorted", type = Accessor.Type.GETTER)
    public Boolean getIsSorted()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("isSorted");
    }


    @Accessor(qualifier = "values", type = Accessor.Type.GETTER)
    public Collection<ItemModel> getValues()
    {
        return (Collection<ItemModel>)getPersistenceContext().getPropertyValue("values");
    }


    @Accessor(qualifier = "valueType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getValueType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("valueType");
    }


    @Accessor(qualifier = "comparationAttribute", type = Accessor.Type.SETTER)
    public void setComparationAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("comparationAttribute", value);
    }


    @Accessor(qualifier = "values", type = Accessor.Type.SETTER)
    public void setValues(Collection<ItemModel> value)
    {
        getPersistenceContext().setPropertyValue("values", value);
    }


    @Accessor(qualifier = "valueType", type = Accessor.Type.SETTER)
    public void setValueType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("valueType", value);
    }
}
