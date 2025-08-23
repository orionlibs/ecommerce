package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConfigAttributeDescriptorModel extends AttributeDescriptorModel
{
    public static final String _TYPECODE = "ConfigAttributeDescriptor";
    public static final String EXTERNALQUALIFIER = "externalQualifier";
    public static final String STOREINDATABASE = "storeInDatabase";
    public static final String NEEDRESTART = "needRestart";


    public ConfigAttributeDescriptorModel()
    {
    }


    public ConfigAttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, String _externalQualifier, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setExternalQualifier(_externalQualifier);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, String _externalQualifier, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setExternalQualifier(_externalQualifier);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "externalQualifier", type = Accessor.Type.GETTER)
    public String getExternalQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("externalQualifier");
    }


    @Accessor(qualifier = "needRestart", type = Accessor.Type.GETTER)
    public Boolean getNeedRestart()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("needRestart");
    }


    @Accessor(qualifier = "storeInDatabase", type = Accessor.Type.GETTER)
    public Boolean getStoreInDatabase()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("storeInDatabase");
    }


    @Accessor(qualifier = "externalQualifier", type = Accessor.Type.SETTER)
    public void setExternalQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("externalQualifier", value);
    }


    @Accessor(qualifier = "needRestart", type = Accessor.Type.SETTER)
    public void setNeedRestart(Boolean value)
    {
        getPersistenceContext().setPropertyValue("needRestart", value);
    }


    @Accessor(qualifier = "storeInDatabase", type = Accessor.Type.SETTER)
    public void setStoreInDatabase(Boolean value)
    {
        getPersistenceContext().setPropertyValue("storeInDatabase", value);
    }
}
