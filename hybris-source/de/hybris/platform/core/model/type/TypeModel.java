package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class TypeModel extends TypeManagerManagedModel
{
    public static final String _TYPECODE = "Type";
    public static final String XMLDEFINITION = "xmldefinition";
    public static final String CODE = "code";
    public static final String DEFAULTVALUE = "defaultValue";
    public static final String DESCRIPTION = "description";


    public TypeModel()
    {
    }


    public TypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeModel(String _code, Boolean _generate)
    {
        setCode(_code);
        setGenerate(_generate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeModel(String _code, Boolean _generate, ItemModel _owner)
    {
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
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


    @Accessor(qualifier = "xmldefinition", type = Accessor.Type.GETTER)
    public String getXmldefinition()
    {
        return (String)getPersistenceContext().getPropertyValue("xmldefinition");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
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
}
