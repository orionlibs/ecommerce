package de.hybris.platform.apiregistryservices.model.events;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

public class EventPropertyConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "EventPropertyConfiguration";
    public static final String _EVENTCONFIGURATION2EVENTPROPERTYCONFIGURATION = "EventConfiguration2EventPropertyConfiguration";
    public static final String PROPERTYNAME = "propertyName";
    public static final String PROPERTYMAPPING = "propertyMapping";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String REQUIRED = "required";
    public static final String DESCRIPTION = "description";
    public static final String EXAMPLES = "examples";
    public static final String EVENTCONFIGURATIONPOS = "eventConfigurationPOS";
    public static final String EVENTCONFIGURATION = "eventConfiguration";


    public EventPropertyConfigurationModel()
    {
    }


    public EventPropertyConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventPropertyConfigurationModel(EventConfigurationModel _eventConfiguration, String _propertyMapping, String _propertyName, String _title, String _type)
    {
        setEventConfiguration(_eventConfiguration);
        setPropertyMapping(_propertyMapping);
        setPropertyName(_propertyName);
        setTitle(_title);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventPropertyConfigurationModel(EventConfigurationModel _eventConfiguration, ItemModel _owner, String _propertyMapping, String _propertyName, String _title, String _type)
    {
        setEventConfiguration(_eventConfiguration);
        setOwner(_owner);
        setPropertyMapping(_propertyMapping);
        setPropertyName(_propertyName);
        setTitle(_title);
        setType(_type);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "eventConfiguration", type = Accessor.Type.GETTER)
    public EventConfigurationModel getEventConfiguration()
    {
        return (EventConfigurationModel)getPersistenceContext().getPropertyValue("eventConfiguration");
    }


    @Accessor(qualifier = "examples", type = Accessor.Type.GETTER)
    public Map<String, String> getExamples()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("examples");
    }


    @Accessor(qualifier = "propertyMapping", type = Accessor.Type.GETTER)
    public String getPropertyMapping()
    {
        return (String)getPersistenceContext().getPropertyValue("propertyMapping");
    }


    @Accessor(qualifier = "propertyName", type = Accessor.Type.GETTER)
    public String getPropertyName()
    {
        return (String)getPersistenceContext().getPropertyValue("propertyName");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return (String)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return (String)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "required", type = Accessor.Type.GETTER)
    public boolean isRequired()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("required"));
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "eventConfiguration", type = Accessor.Type.SETTER)
    public void setEventConfiguration(EventConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("eventConfiguration", value);
    }


    @Accessor(qualifier = "examples", type = Accessor.Type.SETTER)
    public void setExamples(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("examples", value);
    }


    @Accessor(qualifier = "propertyMapping", type = Accessor.Type.SETTER)
    public void setPropertyMapping(String value)
    {
        getPersistenceContext().setPropertyValue("propertyMapping", value);
    }


    @Accessor(qualifier = "propertyName", type = Accessor.Type.SETTER)
    public void setPropertyName(String value)
    {
        getPersistenceContext().setPropertyValue("propertyName", value);
    }


    @Accessor(qualifier = "required", type = Accessor.Type.SETTER)
    public void setRequired(boolean value)
    {
        getPersistenceContext().setPropertyValue("required", toObject(value));
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(String value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
