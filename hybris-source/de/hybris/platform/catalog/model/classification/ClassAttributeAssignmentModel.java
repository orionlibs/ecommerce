package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ClassAttributeAssignmentModel extends ItemModel
{
    public static final String _TYPECODE = "ClassAttributeAssignment";
    public static final String CLASSIFICATIONCLASS = "classificationClass";
    public static final String CLASSIFICATIONATTRIBUTE = "classificationAttribute";
    public static final String DESCRIPTION = "description";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String POSITION = "position";
    public static final String EXTERNALID = "externalID";
    public static final String UNIT = "unit";
    public static final String MANDATORY = "mandatory";
    public static final String LOCALIZED = "localized";
    public static final String RANGE = "range";
    public static final String MULTIVALUED = "multiValued";
    public static final String SEARCHABLE = "searchable";
    public static final String ATTRIBUTETYPE = "attributeType";
    public static final String REFERENCETYPE = "referenceType";
    public static final String REFERENCEINCLUDESSUBTYPES = "referenceIncludesSubTypes";
    public static final String FORMATDEFINITION = "formatDefinition";
    public static final String LISTABLE = "listable";
    public static final String COMPARABLE = "comparable";
    public static final String VISIBILITY = "visibility";
    public static final String ATTRIBUTEVALUES = "attributeValues";
    public static final String ATTRIBUTEVALUEDISPLAYSTRING = "attributeValueDisplayString";
    public static final String ENABLEWYSIWYGEDITOR = "enableWysiwygEditor";
    public static final String SAPCPIASSIGNMENTUNITCODE = "sapCpiAssignmentUnitCode";


    public ClassAttributeAssignmentModel()
    {
    }


    public ClassAttributeAssignmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassAttributeAssignmentModel(ClassificationAttributeModel _classificationAttribute, ClassificationClassModel _classificationClass)
    {
        setClassificationAttribute(_classificationAttribute);
        setClassificationClass(_classificationClass);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassAttributeAssignmentModel(ClassificationAttributeModel _classificationAttribute, ClassificationClassModel _classificationClass, ItemModel _owner, ClassificationSystemVersionModel _systemVersion)
    {
        setClassificationAttribute(_classificationAttribute);
        setClassificationClass(_classificationClass);
        setOwner(_owner);
        setSystemVersion(_systemVersion);
    }


    @Accessor(qualifier = "attributeType", type = Accessor.Type.GETTER)
    public ClassificationAttributeTypeEnum getAttributeType()
    {
        return (ClassificationAttributeTypeEnum)getPersistenceContext().getPropertyValue("attributeType");
    }


    @Accessor(qualifier = "attributeValueDisplayString", type = Accessor.Type.GETTER)
    public String getAttributeValueDisplayString()
    {
        return getAttributeValueDisplayString(null);
    }


    @Accessor(qualifier = "attributeValueDisplayString", type = Accessor.Type.GETTER)
    public String getAttributeValueDisplayString(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("attributeValueDisplayString", loc);
    }


    @Accessor(qualifier = "attributeValues", type = Accessor.Type.GETTER)
    public List<ClassificationAttributeValueModel> getAttributeValues()
    {
        return (List<ClassificationAttributeValueModel>)getPersistenceContext().getPropertyValue("attributeValues");
    }


    @Accessor(qualifier = "classificationAttribute", type = Accessor.Type.GETTER)
    public ClassificationAttributeModel getClassificationAttribute()
    {
        return (ClassificationAttributeModel)getPersistenceContext().getPropertyValue("classificationAttribute");
    }


    @Accessor(qualifier = "classificationClass", type = Accessor.Type.GETTER)
    public ClassificationClassModel getClassificationClass()
    {
        return (ClassificationClassModel)getPersistenceContext().getPropertyValue("classificationClass");
    }


    @Accessor(qualifier = "comparable", type = Accessor.Type.GETTER)
    public Boolean getComparable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("comparable");
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


    @Accessor(qualifier = "enableWysiwygEditor", type = Accessor.Type.GETTER)
    public Boolean getEnableWysiwygEditor()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableWysiwygEditor");
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "formatDefinition", type = Accessor.Type.GETTER)
    public String getFormatDefinition()
    {
        return (String)getPersistenceContext().getPropertyValue("formatDefinition");
    }


    @Accessor(qualifier = "listable", type = Accessor.Type.GETTER)
    public Boolean getListable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("listable");
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
    public Boolean getLocalized()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("localized");
    }


    @Accessor(qualifier = "mandatory", type = Accessor.Type.GETTER)
    public Boolean getMandatory()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("mandatory");
    }


    @Accessor(qualifier = "multiValued", type = Accessor.Type.GETTER)
    public Boolean getMultiValued()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("multiValued");
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public Integer getPosition()
    {
        return (Integer)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "range", type = Accessor.Type.GETTER)
    public Boolean getRange()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("range");
    }


    @Accessor(qualifier = "referenceIncludesSubTypes", type = Accessor.Type.GETTER)
    public Boolean getReferenceIncludesSubTypes()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("referenceIncludesSubTypes");
    }


    @Accessor(qualifier = "referenceType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getReferenceType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("referenceType");
    }


    @Accessor(qualifier = "sapCpiAssignmentUnitCode", type = Accessor.Type.GETTER)
    public String getSapCpiAssignmentUnitCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCpiAssignmentUnitCode");
    }


    @Accessor(qualifier = "searchable", type = Accessor.Type.GETTER)
    public Boolean getSearchable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchable");
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getSystemVersion()
    {
        return (ClassificationSystemVersionModel)getPersistenceContext().getPropertyValue("systemVersion");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public ClassificationAttributeUnitModel getUnit()
    {
        return (ClassificationAttributeUnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "visibility", type = Accessor.Type.GETTER)
    public ClassificationAttributeVisibilityEnum getVisibility()
    {
        return (ClassificationAttributeVisibilityEnum)getPersistenceContext().getPropertyValue("visibility");
    }


    @Accessor(qualifier = "attributeType", type = Accessor.Type.SETTER)
    public void setAttributeType(ClassificationAttributeTypeEnum value)
    {
        getPersistenceContext().setPropertyValue("attributeType", value);
    }


    @Accessor(qualifier = "attributeValues", type = Accessor.Type.SETTER)
    public void setAttributeValues(List<ClassificationAttributeValueModel> value)
    {
        getPersistenceContext().setPropertyValue("attributeValues", value);
    }


    @Accessor(qualifier = "classificationAttribute", type = Accessor.Type.SETTER)
    public void setClassificationAttribute(ClassificationAttributeModel value)
    {
        getPersistenceContext().setPropertyValue("classificationAttribute", value);
    }


    @Accessor(qualifier = "classificationClass", type = Accessor.Type.SETTER)
    public void setClassificationClass(ClassificationClassModel value)
    {
        getPersistenceContext().setPropertyValue("classificationClass", value);
    }


    @Accessor(qualifier = "comparable", type = Accessor.Type.SETTER)
    public void setComparable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("comparable", value);
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


    @Accessor(qualifier = "enableWysiwygEditor", type = Accessor.Type.SETTER)
    public void setEnableWysiwygEditor(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableWysiwygEditor", value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }


    @Accessor(qualifier = "formatDefinition", type = Accessor.Type.SETTER)
    public void setFormatDefinition(String value)
    {
        getPersistenceContext().setPropertyValue("formatDefinition", value);
    }


    @Accessor(qualifier = "listable", type = Accessor.Type.SETTER)
    public void setListable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("listable", value);
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
    public void setLocalized(Boolean value)
    {
        getPersistenceContext().setPropertyValue("localized", value);
    }


    @Accessor(qualifier = "mandatory", type = Accessor.Type.SETTER)
    public void setMandatory(Boolean value)
    {
        getPersistenceContext().setPropertyValue("mandatory", value);
    }


    @Accessor(qualifier = "multiValued", type = Accessor.Type.SETTER)
    public void setMultiValued(Boolean value)
    {
        getPersistenceContext().setPropertyValue("multiValued", value);
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(Integer value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }


    @Accessor(qualifier = "range", type = Accessor.Type.SETTER)
    public void setRange(Boolean value)
    {
        getPersistenceContext().setPropertyValue("range", value);
    }


    @Accessor(qualifier = "referenceIncludesSubTypes", type = Accessor.Type.SETTER)
    public void setReferenceIncludesSubTypes(Boolean value)
    {
        getPersistenceContext().setPropertyValue("referenceIncludesSubTypes", value);
    }


    @Accessor(qualifier = "referenceType", type = Accessor.Type.SETTER)
    public void setReferenceType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("referenceType", value);
    }


    @Accessor(qualifier = "sapCpiAssignmentUnitCode", type = Accessor.Type.SETTER)
    public void setSapCpiAssignmentUnitCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapCpiAssignmentUnitCode", value);
    }


    @Accessor(qualifier = "searchable", type = Accessor.Type.SETTER)
    public void setSearchable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchable", value);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
    public void setSystemVersion(ClassificationSystemVersionModel value)
    {
        getPersistenceContext().setPropertyValue("systemVersion", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(ClassificationAttributeUnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }


    @Accessor(qualifier = "visibility", type = Accessor.Type.SETTER)
    public void setVisibility(ClassificationAttributeVisibilityEnum value)
    {
        getPersistenceContext().setPropertyValue("visibility", value);
    }
}
