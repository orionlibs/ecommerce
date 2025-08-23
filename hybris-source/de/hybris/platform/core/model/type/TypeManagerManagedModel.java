package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class TypeManagerManagedModel extends ItemModel
{
    public static final String _TYPECODE = "TypeManagerManaged";
    public static final String NAME = "name";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String DEPRECATED = "deprecated";
    public static final String AUTOCREATE = "autocreate";
    public static final String GENERATE = "generate";


    public TypeManagerManagedModel()
    {
    }


    public TypeManagerManagedModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeManagerManagedModel(Boolean _generate)
    {
        setGenerate(_generate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeManagerManagedModel(Boolean _generate, ItemModel _owner)
    {
        setGenerate(_generate);
        setOwner(_owner);
    }


    @Accessor(qualifier = "autocreate", type = Accessor.Type.GETTER)
    public Boolean getAutocreate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("autocreate");
    }


    @Accessor(qualifier = "deprecated", type = Accessor.Type.GETTER)
    public Boolean getDeprecated()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("deprecated");
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
    public String getExtensionName()
    {
        return (String)getPersistenceContext().getPropertyValue("extensionName");
    }


    @Accessor(qualifier = "generate", type = Accessor.Type.GETTER)
    public Boolean getGenerate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("generate");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "autocreate", type = Accessor.Type.SETTER)
    public void setAutocreate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("autocreate", value);
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
    public void setExtensionName(String value)
    {
        getPersistenceContext().setPropertyValue("extensionName", value);
    }


    @Accessor(qualifier = "generate", type = Accessor.Type.SETTER)
    public void setGenerate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("generate", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }
}
