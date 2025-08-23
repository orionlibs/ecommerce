package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RelationMetaTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "RelationMetaType";
    public static final String LOCALIZED = "localized";
    public static final String SOURCEATTRIBUTE = "sourceAttribute";
    public static final String TARGETATTRIBUTE = "targetAttribute";
    public static final String SOURCETYPE = "sourceType";
    public static final String TARGETTYPE = "targetType";
    public static final String SOURCETYPEROLE = "sourceTypeRole";
    public static final String TARGETTYPEROLE = "targetTypeRole";
    public static final String SOURCENAVIGABLE = "sourceNavigable";
    public static final String TARGETNAVIGABLE = "targetNavigable";
    public static final String SOURCETYPECARDINALITY = "sourceTypeCardinality";
    public static final String TARGETTYPECARDINALITY = "targetTypeCardinality";
    public static final String ORDERINGATTRIBUTE = "orderingAttribute";
    public static final String LOCALIZATIONATTRIBUTE = "localizationAttribute";


    public RelationMetaTypeModel()
    {
    }


    public RelationMetaTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RelationMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _localized, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setLocalized(_localized);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RelationMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, AttributeDescriptorModel _localizationAttribute, Boolean _localized, AttributeDescriptorModel _orderingAttribute, ItemModel _owner, Boolean _singleton, RelationDescriptorModel _sourceAttribute,
                    Boolean _sourceNavigable, ComposedTypeModel _sourceType, RelationEndCardinalityEnum _sourceTypeCardinality, String _sourceTypeRole, ComposedTypeModel _superType, RelationDescriptorModel _targetAttribute, Boolean _targetNavigable, ComposedTypeModel _targetType,
                    RelationEndCardinalityEnum _targetTypeCardinality, String _targetTypeRole)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setLocalizationAttribute(_localizationAttribute);
        setLocalized(_localized);
        setOrderingAttribute(_orderingAttribute);
        setOwner(_owner);
        setSingleton(_singleton);
        setSourceAttribute(_sourceAttribute);
        setSourceNavigable(_sourceNavigable);
        setSourceType(_sourceType);
        setSourceTypeCardinality(_sourceTypeCardinality);
        setSourceTypeRole(_sourceTypeRole);
        setSuperType(_superType);
        setTargetAttribute(_targetAttribute);
        setTargetNavigable(_targetNavigable);
        setTargetType(_targetType);
        setTargetTypeCardinality(_targetTypeCardinality);
        setTargetTypeRole(_targetTypeRole);
    }


    @Accessor(qualifier = "localizationAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getLocalizationAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("localizationAttribute");
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
    public Boolean getLocalized()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("localized");
    }


    @Accessor(qualifier = "orderingAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getOrderingAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("orderingAttribute");
    }


    @Accessor(qualifier = "sourceAttribute", type = Accessor.Type.GETTER)
    public RelationDescriptorModel getSourceAttribute()
    {
        return (RelationDescriptorModel)getPersistenceContext().getPropertyValue("sourceAttribute");
    }


    @Accessor(qualifier = "sourceNavigable", type = Accessor.Type.GETTER)
    public Boolean getSourceNavigable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sourceNavigable");
    }


    @Accessor(qualifier = "sourceType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getSourceType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("sourceType");
    }


    @Accessor(qualifier = "sourceTypeCardinality", type = Accessor.Type.GETTER)
    public RelationEndCardinalityEnum getSourceTypeCardinality()
    {
        return (RelationEndCardinalityEnum)getPersistenceContext().getPropertyValue("sourceTypeCardinality");
    }


    @Accessor(qualifier = "sourceTypeRole", type = Accessor.Type.GETTER)
    public String getSourceTypeRole()
    {
        return (String)getPersistenceContext().getPropertyValue("sourceTypeRole");
    }


    @Accessor(qualifier = "targetAttribute", type = Accessor.Type.GETTER)
    public RelationDescriptorModel getTargetAttribute()
    {
        return (RelationDescriptorModel)getPersistenceContext().getPropertyValue("targetAttribute");
    }


    @Accessor(qualifier = "targetNavigable", type = Accessor.Type.GETTER)
    public Boolean getTargetNavigable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("targetNavigable");
    }


    @Accessor(qualifier = "targetType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getTargetType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("targetType");
    }


    @Accessor(qualifier = "targetTypeCardinality", type = Accessor.Type.GETTER)
    public RelationEndCardinalityEnum getTargetTypeCardinality()
    {
        return (RelationEndCardinalityEnum)getPersistenceContext().getPropertyValue("targetTypeCardinality");
    }


    @Accessor(qualifier = "targetTypeRole", type = Accessor.Type.GETTER)
    public String getTargetTypeRole()
    {
        return (String)getPersistenceContext().getPropertyValue("targetTypeRole");
    }


    @Accessor(qualifier = "localizationAttribute", type = Accessor.Type.SETTER)
    public void setLocalizationAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("localizationAttribute", value);
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
    public void setLocalized(Boolean value)
    {
        getPersistenceContext().setPropertyValue("localized", value);
    }


    @Accessor(qualifier = "orderingAttribute", type = Accessor.Type.SETTER)
    public void setOrderingAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("orderingAttribute", value);
    }


    @Accessor(qualifier = "sourceAttribute", type = Accessor.Type.SETTER)
    public void setSourceAttribute(RelationDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("sourceAttribute", value);
    }


    @Accessor(qualifier = "sourceNavigable", type = Accessor.Type.SETTER)
    public void setSourceNavigable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sourceNavigable", value);
    }


    @Accessor(qualifier = "sourceType", type = Accessor.Type.SETTER)
    public void setSourceType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("sourceType", value);
    }


    @Accessor(qualifier = "sourceTypeCardinality", type = Accessor.Type.SETTER)
    public void setSourceTypeCardinality(RelationEndCardinalityEnum value)
    {
        getPersistenceContext().setPropertyValue("sourceTypeCardinality", value);
    }


    @Accessor(qualifier = "sourceTypeRole", type = Accessor.Type.SETTER)
    public void setSourceTypeRole(String value)
    {
        getPersistenceContext().setPropertyValue("sourceTypeRole", value);
    }


    @Accessor(qualifier = "targetAttribute", type = Accessor.Type.SETTER)
    public void setTargetAttribute(RelationDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("targetAttribute", value);
    }


    @Accessor(qualifier = "targetNavigable", type = Accessor.Type.SETTER)
    public void setTargetNavigable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("targetNavigable", value);
    }


    @Accessor(qualifier = "targetType", type = Accessor.Type.SETTER)
    public void setTargetType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("targetType", value);
    }


    @Accessor(qualifier = "targetTypeCardinality", type = Accessor.Type.SETTER)
    public void setTargetTypeCardinality(RelationEndCardinalityEnum value)
    {
        getPersistenceContext().setPropertyValue("targetTypeCardinality", value);
    }


    @Accessor(qualifier = "targetTypeRole", type = Accessor.Type.SETTER)
    public void setTargetTypeRole(String value)
    {
        getPersistenceContext().setPropertyValue("targetTypeRole", value);
    }
}
