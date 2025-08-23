package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import java.util.Collection;
import java.util.Set;

public class ComposedTypeModel extends TypeModel
{
    public static final String _TYPECODE = "ComposedType";
    public static final String _FORMAT2COMTYPREL = "Format2ComTypRel";
    public static final String _CONSTRAINTCOMPOSITETYPERELATION = "ConstraintCompositeTypeRelation";
    public static final String _SYNCJOB2TYPEREL = "SyncJob2TypeRel";
    public static final String _STREAMCONFIGURATIONEXCLUDEDSUBTYPES = "StreamConfigurationExcludedSubtypes";
    public static final String ABSTRACT = "abstract";
    public static final String DECLAREDATTRIBUTEDESCRIPTORS = "declaredattributedescriptors";
    public static final String DUMPPROPERTYTABLE = "dumpPropertyTable";
    public static final String ATTRIBUTEDESCRIPTORS = "attributedescriptors";
    public static final String INHERITANCEPATHSTRING = "inheritancePathString";
    public static final String INHERITEDATTRIBUTEDESCRIPTORS = "inheritedattributedescriptors";
    public static final String JALOCLASS = "jaloclass";
    public static final String JNDINAME = "jndiName";
    public static final String SINGLETON = "singleton";
    public static final String JALOONLY = "jaloonly";
    public static final String DYNAMIC = "dynamic";
    public static final String SUBTYPES = "subtypes";
    public static final String SUPERTYPE = "superType";
    public static final String TABLE = "table";
    public static final String ALLSUPERTYPES = "allSuperTypes";
    public static final String ALLSUBTYPES = "allSubTypes";
    public static final String LEGACYPERSISTENCE = "legacyPersistence";
    public static final String FORMATS = "formats";
    public static final String SYSTEMTYPE = "systemType";
    public static final String CONSTRAINTS = "constraints";
    public static final String CATALOGITEMTYPE = "catalogItemType";
    public static final String CATALOGVERSIONATTRIBUTE = "catalogVersionAttribute";
    public static final String CATALOGVERSIONATTRIBUTEQUALIFIER = "catalogVersionAttributeQualifier";
    public static final String UNIQUEKEYATTRIBUTES = "uniqueKeyAttributes";
    public static final String UNIQUEKEYATTRIBUTEQUALIFIER = "uniqueKeyAttributeQualifier";
    public static final String SYNCJOBS = "syncJobs";
    public static final String STREAMCONFIGURATIONS = "streamConfigurations";


    public ComposedTypeModel()
    {
    }


    public ComposedTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComposedTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComposedTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "abstract", type = Accessor.Type.GETTER)
    public Boolean getAbstract()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("abstract");
    }


    @Accessor(qualifier = "allSubTypes", type = Accessor.Type.GETTER)
    public Collection<ComposedTypeModel> getAllSubTypes()
    {
        return (Collection<ComposedTypeModel>)getPersistenceContext().getPropertyValue("allSubTypes");
    }


    @Accessor(qualifier = "allSuperTypes", type = Accessor.Type.GETTER)
    public Collection<ComposedTypeModel> getAllSuperTypes()
    {
        return (Collection<ComposedTypeModel>)getPersistenceContext().getPropertyValue("allSuperTypes");
    }


    @Accessor(qualifier = "catalogItemType", type = Accessor.Type.GETTER)
    public Boolean getCatalogItemType()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("catalogItemType");
    }


    @Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getCatalogVersionAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("catalogVersionAttribute");
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
    public Set<AbstractConstraintModel> getConstraints()
    {
        return (Set<AbstractConstraintModel>)getPersistenceContext().getPropertyValue("constraints");
    }


    @Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.GETTER)
    public Collection<AttributeDescriptorModel> getDeclaredattributedescriptors()
    {
        return (Collection<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("declaredattributedescriptors");
    }


    @Accessor(qualifier = "dumpPropertyTable", type = Accessor.Type.GETTER)
    public String getDumpPropertyTable()
    {
        return (String)getPersistenceContext().getPropertyValue("dumpPropertyTable");
    }


    @Accessor(qualifier = "dynamic", type = Accessor.Type.GETTER)
    public Boolean getDynamic()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("dynamic");
    }


    @Accessor(qualifier = "formats", type = Accessor.Type.GETTER)
    public Collection<FormatModel> getFormats()
    {
        return (Collection<FormatModel>)getPersistenceContext().getPropertyValue("formats");
    }


    @Accessor(qualifier = "inheritedattributedescriptors", type = Accessor.Type.GETTER)
    public Collection<AttributeDescriptorModel> getInheritedattributedescriptors()
    {
        return (Collection<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("inheritedattributedescriptors");
    }


    @Accessor(qualifier = "jaloclass", type = Accessor.Type.GETTER)
    public Class getJaloclass()
    {
        return (Class)getPersistenceContext().getPropertyValue("jaloclass");
    }


    @Accessor(qualifier = "jaloonly", type = Accessor.Type.GETTER)
    public Boolean getJaloonly()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("jaloonly");
    }


    @Accessor(qualifier = "jndiName", type = Accessor.Type.GETTER)
    public String getJndiName()
    {
        return (String)getPersistenceContext().getPropertyValue("jndiName");
    }


    @Accessor(qualifier = "legacyPersistence", type = Accessor.Type.GETTER)
    public Boolean getLegacyPersistence()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("legacyPersistence");
    }


    @Accessor(qualifier = "singleton", type = Accessor.Type.GETTER)
    public Boolean getSingleton()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("singleton");
    }


    @Accessor(qualifier = "streamConfigurations", type = Accessor.Type.GETTER)
    public Collection<StreamConfigurationModel> getStreamConfigurations()
    {
        return (Collection<StreamConfigurationModel>)getPersistenceContext().getPropertyValue("streamConfigurations");
    }


    @Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
    public Collection<ComposedTypeModel> getSubtypes()
    {
        return (Collection<ComposedTypeModel>)getPersistenceContext().getPropertyValue("subtypes");
    }


    @Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getSuperType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("superType");
    }


    @Accessor(qualifier = "systemType", type = Accessor.Type.GETTER)
    public Boolean getSystemType()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("systemType");
    }


    @Accessor(qualifier = "table", type = Accessor.Type.GETTER)
    public String getTable()
    {
        return (String)getPersistenceContext().getPropertyValue("table");
    }


    @Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.GETTER)
    public Collection<AttributeDescriptorModel> getUniqueKeyAttributes()
    {
        return (Collection<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("uniqueKeyAttributes");
    }


    @Accessor(qualifier = "catalogItemType", type = Accessor.Type.SETTER)
    public void setCatalogItemType(Boolean value)
    {
        getPersistenceContext().setPropertyValue("catalogItemType", value);
    }


    @Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.SETTER)
    public void setCatalogVersionAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersionAttribute", value);
    }


    @Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
    public void setConstraints(Set<AbstractConstraintModel> value)
    {
        getPersistenceContext().setPropertyValue("constraints", value);
    }


    @Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.SETTER)
    public void setDeclaredattributedescriptors(Collection<AttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("declaredattributedescriptors", value);
    }


    @Accessor(qualifier = "formats", type = Accessor.Type.SETTER)
    public void setFormats(Collection<FormatModel> value)
    {
        getPersistenceContext().setPropertyValue("formats", value);
    }


    @Accessor(qualifier = "jaloclass", type = Accessor.Type.SETTER)
    public void setJaloclass(Class value)
    {
        getPersistenceContext().setPropertyValue("jaloclass", value);
    }


    @Accessor(qualifier = "jaloonly", type = Accessor.Type.SETTER)
    public void setJaloonly(Boolean value)
    {
        getPersistenceContext().setPropertyValue("jaloonly", value);
    }


    @Accessor(qualifier = "legacyPersistence", type = Accessor.Type.SETTER)
    public void setLegacyPersistence(Boolean value)
    {
        getPersistenceContext().setPropertyValue("legacyPersistence", value);
    }


    @Accessor(qualifier = "singleton", type = Accessor.Type.SETTER)
    public void setSingleton(Boolean value)
    {
        getPersistenceContext().setPropertyValue("singleton", value);
    }


    @Accessor(qualifier = "streamConfigurations", type = Accessor.Type.SETTER)
    public void setStreamConfigurations(Collection<StreamConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("streamConfigurations", value);
    }


    @Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
    public void setSuperType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("superType", value);
    }


    @Accessor(qualifier = "systemType", type = Accessor.Type.SETTER)
    public void setSystemType(Boolean value)
    {
        getPersistenceContext().setPropertyValue("systemType", value);
    }


    @Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.SETTER)
    public void setUniqueKeyAttributes(Collection<AttributeDescriptorModel> value)
    {
        getPersistenceContext().setPropertyValue("uniqueKeyAttributes", value);
    }
}
