package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ProductReferenceModel extends ItemModel
{
    public static final String _TYPECODE = "ProductReference";
    public static final String _PRODUCTREFERENCERELATION = "ProductReferenceRelation";
    public static final String QUALIFIER = "qualifier";
    public static final String TARGET = "target";
    public static final String QUANTITY = "quantity";
    public static final String REFERENCETYPE = "referenceType";
    public static final String ICON = "icon";
    public static final String DESCRIPTION = "description";
    public static final String PRESELECTED = "preselected";
    public static final String ACTIVE = "active";
    public static final String SOURCEPOS = "sourcePOS";
    public static final String SOURCE = "source";


    public ProductReferenceModel()
    {
    }


    public ProductReferenceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductReferenceModel(Boolean _active, Boolean _preselected, ProductModel _source, ProductModel _target)
    {
        setActive(_active);
        setPreselected(_preselected);
        setSource(_source);
        setTarget(_target);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductReferenceModel(Boolean _active, ItemModel _owner, Boolean _preselected, ProductModel _source, ProductModel _target)
    {
        setActive(_active);
        setOwner(_owner);
        setPreselected(_preselected);
        setSource(_source);
        setTarget(_target);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
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


    @Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
    public MediaModel getIcon()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("icon");
    }


    @Accessor(qualifier = "preselected", type = Accessor.Type.GETTER)
    public Boolean getPreselected()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("preselected");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Integer getQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "referenceType", type = Accessor.Type.GETTER)
    public ProductReferenceTypeEnum getReferenceType()
    {
        return (ProductReferenceTypeEnum)getPersistenceContext().getPropertyValue("referenceType");
    }


    @Accessor(qualifier = "source", type = Accessor.Type.GETTER)
    public ProductModel getSource()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("source");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public ProductModel getTarget()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
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


    @Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
    public void setIcon(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("icon", value);
    }


    @Accessor(qualifier = "preselected", type = Accessor.Type.SETTER)
    public void setPreselected(Boolean value)
    {
        getPersistenceContext().setPropertyValue("preselected", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }


    @Accessor(qualifier = "referenceType", type = Accessor.Type.SETTER)
    public void setReferenceType(ProductReferenceTypeEnum value)
    {
        getPersistenceContext().setPropertyValue("referenceType", value);
    }


    @Accessor(qualifier = "source", type = Accessor.Type.SETTER)
    public void setSource(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("source", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }
}
