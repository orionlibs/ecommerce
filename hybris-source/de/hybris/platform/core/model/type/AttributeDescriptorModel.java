package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Locale;
import java.util.Set;

public class AttributeDescriptorModel extends DescriptorModel
{
    public static final String _TYPECODE = "AttributeDescriptor";
    public static final String _ATTRIBUTECONSTRAINTATTRIBUTEDESCRELATION = "AttributeConstraintAttributeDescRelation";
    public static final String DATABASECOLUMN = "databaseColumn";
    public static final String DEFAULTVALUE = "defaultValue";
    public static final String DEFAULTVALUEDEFINITIONSTRING = "defaultValueDefinitionString";
    public static final String ENCLOSINGTYPE = "enclosingType";
    public static final String DECLARINGENCLOSINGTYPE = "declaringEnclosingType";
    public static final String DESCRIPTION = "description";
    public static final String PERSISTENCECLASS = "persistenceClass";
    public static final String PERSISTENCEQUALIFIER = "persistenceQualifier";
    public static final String PERSISTENCETYPE = "persistenceType";
    public static final String ATTRIBUTEHANDLER = "attributeHandler";
    public static final String SELECTIONOF = "selectionOf";
    public static final String PROPOSEDDATABASECOLUMN = "proposedDatabaseColumn";
    public static final String MODIFIERS = "modifiers";
    public static final String INITIAL = "initial";
    public static final String LOCALIZED = "localized";
    public static final String OPTIONAL = "optional";
    public static final String PARTOF = "partOf";
    public static final String UNIQUE = "unique";
    public static final String PRIVATE = "private";
    public static final String PROPERTY = "property";
    public static final String READABLE = "readable";
    public static final String REMOVABLE = "removable";
    public static final String SEARCH = "search";
    public static final String WRITABLE = "writable";
    public static final String ENCRYPTED = "encrypted";
    public static final String PRIMITIVE = "primitive";
    public static final String HIDDENFORUI = "hiddenForUI";
    public static final String READONLYFORUI = "readOnlyForUI";
    public static final String CONSTRAINTS = "constraints";
    public static final String DONTCOPY = "dontCopy";


    public AttributeDescriptorModel()
    {
    }


    public AttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "attributeHandler", type = Accessor.Type.GETTER)
    public String getAttributeHandler()
    {
        return (String)getPersistenceContext().getPropertyValue("attributeHandler");
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
    public Set<AttributeConstraintModel> getConstraints()
    {
        return (Set<AttributeConstraintModel>)getPersistenceContext().getPropertyValue("constraints");
    }


    @Accessor(qualifier = "databaseColumn", type = Accessor.Type.GETTER)
    public String getDatabaseColumn()
    {
        return (String)getPersistenceContext().getPropertyValue("databaseColumn");
    }


    @Accessor(qualifier = "declaringEnclosingType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getDeclaringEnclosingType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("declaringEnclosingType");
    }


    @Accessor(qualifier = "defaultValue", type = Accessor.Type.GETTER)
    public Object getDefaultValue()
    {
        return getPersistenceContext().getPropertyValue("defaultValue");
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


    @Accessor(qualifier = "dontCopy", type = Accessor.Type.GETTER)
    public Boolean getDontCopy()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("dontCopy");
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getEnclosingType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("enclosingType");
    }


    @Accessor(qualifier = "encrypted", type = Accessor.Type.GETTER)
    public Boolean getEncrypted()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("encrypted");
    }


    @Accessor(qualifier = "hiddenForUI", type = Accessor.Type.GETTER)
    public Boolean getHiddenForUI()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("hiddenForUI");
    }


    @Accessor(qualifier = "initial", type = Accessor.Type.GETTER)
    public Boolean getInitial()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("initial");
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
    public Boolean getLocalized()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("localized");
    }


    @Accessor(qualifier = "modifiers", type = Accessor.Type.GETTER)
    public Integer getModifiers()
    {
        return (Integer)getPersistenceContext().getPropertyValue("modifiers");
    }


    @Accessor(qualifier = "optional", type = Accessor.Type.GETTER)
    public Boolean getOptional()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("optional");
    }


    @Accessor(qualifier = "partOf", type = Accessor.Type.GETTER)
    public Boolean getPartOf()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("partOf");
    }


    @Accessor(qualifier = "persistenceClass", type = Accessor.Type.GETTER)
    public Class getPersistenceClass()
    {
        return (Class)getPersistenceContext().getPropertyValue("persistenceClass");
    }


    @Accessor(qualifier = "primitive", type = Accessor.Type.GETTER)
    public Boolean getPrimitive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("primitive");
    }


    @Accessor(qualifier = "private", type = Accessor.Type.GETTER)
    public Boolean getPrivate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("private");
    }


    @Accessor(qualifier = "property", type = Accessor.Type.GETTER)
    public Boolean getProperty()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("property");
    }


    @Accessor(qualifier = "proposedDatabaseColumn", type = Accessor.Type.GETTER)
    public String getProposedDatabaseColumn()
    {
        return (String)getPersistenceContext().getPropertyValue("proposedDatabaseColumn");
    }


    @Accessor(qualifier = "readable", type = Accessor.Type.GETTER)
    public Boolean getReadable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("readable");
    }


    @Accessor(qualifier = "readOnlyForUI", type = Accessor.Type.GETTER)
    public Boolean getReadOnlyForUI()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("readOnlyForUI");
    }


    @Accessor(qualifier = "removable", type = Accessor.Type.GETTER)
    public Boolean getRemovable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("removable");
    }


    @Accessor(qualifier = "search", type = Accessor.Type.GETTER)
    public Boolean getSearch()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("search");
    }


    @Accessor(qualifier = "selectionOf", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getSelectionOf()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("selectionOf");
    }


    @Accessor(qualifier = "unique", type = Accessor.Type.GETTER)
    public Boolean getUnique()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("unique");
    }


    @Accessor(qualifier = "writable", type = Accessor.Type.GETTER)
    public Boolean getWritable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("writable");
    }


    @Accessor(qualifier = "attributeHandler", type = Accessor.Type.SETTER)
    public void setAttributeHandler(String value)
    {
        getPersistenceContext().setPropertyValue("attributeHandler", value);
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
    public void setConstraints(Set<AttributeConstraintModel> value)
    {
        getPersistenceContext().setPropertyValue("constraints", value);
    }


    @Accessor(qualifier = "databaseColumn", type = Accessor.Type.SETTER)
    public void setDatabaseColumn(String value)
    {
        getPersistenceContext().setPropertyValue("databaseColumn", value);
    }


    @Accessor(qualifier = "defaultValue", type = Accessor.Type.SETTER)
    public void setDefaultValue(Object value)
    {
        getPersistenceContext().setPropertyValue("defaultValue", value);
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


    @Accessor(qualifier = "dontCopy", type = Accessor.Type.SETTER)
    public void setDontCopy(Boolean value)
    {
        getPersistenceContext().setPropertyValue("dontCopy", value);
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
    public void setEnclosingType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("enclosingType", value);
    }


    @Accessor(qualifier = "encrypted", type = Accessor.Type.SETTER)
    public void setEncrypted(Boolean value)
    {
        getPersistenceContext().setPropertyValue("encrypted", value);
    }


    @Accessor(qualifier = "hiddenForUI", type = Accessor.Type.SETTER)
    public void setHiddenForUI(Boolean value)
    {
        getPersistenceContext().setPropertyValue("hiddenForUI", value);
    }


    @Accessor(qualifier = "initial", type = Accessor.Type.SETTER)
    public void setInitial(Boolean value)
    {
        getPersistenceContext().setPropertyValue("initial", value);
    }


    @Accessor(qualifier = "modifiers", type = Accessor.Type.SETTER)
    public void setModifiers(Integer value)
    {
        getPersistenceContext().setPropertyValue("modifiers", value);
    }


    @Accessor(qualifier = "optional", type = Accessor.Type.SETTER)
    public void setOptional(Boolean value)
    {
        getPersistenceContext().setPropertyValue("optional", value);
    }


    @Accessor(qualifier = "partOf", type = Accessor.Type.SETTER)
    public void setPartOf(Boolean value)
    {
        getPersistenceContext().setPropertyValue("partOf", value);
    }


    @Accessor(qualifier = "primitive", type = Accessor.Type.SETTER)
    public void setPrimitive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("primitive", value);
    }


    @Accessor(qualifier = "private", type = Accessor.Type.SETTER)
    public void setPrivate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("private", value);
    }


    @Accessor(qualifier = "property", type = Accessor.Type.SETTER)
    public void setProperty(Boolean value)
    {
        getPersistenceContext().setPropertyValue("property", value);
    }


    @Accessor(qualifier = "readable", type = Accessor.Type.SETTER)
    public void setReadable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("readable", value);
    }


    @Accessor(qualifier = "readOnlyForUI", type = Accessor.Type.SETTER)
    public void setReadOnlyForUI(Boolean value)
    {
        getPersistenceContext().setPropertyValue("readOnlyForUI", value);
    }


    @Accessor(qualifier = "removable", type = Accessor.Type.SETTER)
    public void setRemovable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("removable", value);
    }


    @Accessor(qualifier = "search", type = Accessor.Type.SETTER)
    public void setSearch(Boolean value)
    {
        getPersistenceContext().setPropertyValue("search", value);
    }


    @Accessor(qualifier = "unique", type = Accessor.Type.SETTER)
    public void setUnique(Boolean value)
    {
        getPersistenceContext().setPropertyValue("unique", value);
    }


    @Accessor(qualifier = "writable", type = Accessor.Type.SETTER)
    public void setWritable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("writable", value);
    }
}
