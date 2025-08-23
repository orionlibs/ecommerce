package de.hybris.platform.core.model.cors;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CorsConfigurationPropertyModel extends ItemModel
{
    public static final String _TYPECODE = "CorsConfigurationProperty";
    public static final String CONTEXT = "context";
    public static final String KEY = "key";
    public static final String VALUE = "value";


    public CorsConfigurationPropertyModel()
    {
    }


    public CorsConfigurationPropertyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CorsConfigurationPropertyModel(String _context, String _key, String _value)
    {
        setContext(_context);
        setKey(_key);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CorsConfigurationPropertyModel(String _context, String _key, ItemModel _owner, String _value)
    {
        setContext(_context);
        setKey(_key);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "context", type = Accessor.Type.GETTER)
    public String getContext()
    {
        return (String)getPersistenceContext().getPropertyValue("context");
    }


    @Accessor(qualifier = "key", type = Accessor.Type.GETTER)
    public String getKey()
    {
        return (String)getPersistenceContext().getPropertyValue("key");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "context", type = Accessor.Type.SETTER)
    public void setContext(String value)
    {
        getPersistenceContext().setPropertyValue("context", value);
    }


    @Accessor(qualifier = "key", type = Accessor.Type.SETTER)
    public void setKey(String value)
    {
        getPersistenceContext().setPropertyValue("key", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
