package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class VariantTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "VariantType";
    public static final String VARIANTATTRIBUTES = "variantAttributes";


    public VariantTypeModel()
    {
    }


    public VariantTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "variantAttributes", type = Accessor.Type.GETTER)
    public List<VariantAttributeDescriptorModel> getVariantAttributes()
    {
        return (List<VariantAttributeDescriptorModel>)getPersistenceContext().getPropertyValue("variantAttributes");
    }


    @Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
    public void setSuperType(ComposedTypeModel value)
    {
        super.setSuperType(value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "variantAttributes", type = Accessor.Type.SETTER)
    public void setVariantAttributes(List<VariantAttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("variantAttributes", value);
    }
}
