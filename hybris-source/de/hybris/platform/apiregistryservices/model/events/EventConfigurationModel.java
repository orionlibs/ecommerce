package de.hybris.platform.apiregistryservices.model.events;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.enums.EventMappingType;
import de.hybris.platform.apiregistryservices.enums.EventPriority;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class EventConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "EventConfiguration";
    public static final String _DESTINATIONTARGET2EVENTCONFIGURATION = "DestinationTarget2EventConfiguration";
    public static final String EVENTCLASS = "eventClass";
    public static final String VERSION = "version";
    public static final String EXPORTFLAG = "exportFlag";
    public static final String PRIORITY = "priority";
    public static final String EXPORTNAME = "exportName";
    public static final String MAPPINGTYPE = "mappingType";
    public static final String CONVERTERBEAN = "converterBean";
    public static final String DESCRIPTION = "description";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String FILTERLOCATION = "filterLocation";
    public static final String EVENTPROPERTYCONFIGURATIONS = "eventPropertyConfigurations";
    public static final String DESTINATIONTARGET = "destinationTarget";


    public EventConfigurationModel()
    {
    }


    public EventConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventConfigurationModel(DestinationTargetModel _destinationTarget, String _eventClass, String _exportName)
    {
        setDestinationTarget(_destinationTarget);
        setEventClass(_eventClass);
        setExportName(_exportName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventConfigurationModel(DestinationTargetModel _destinationTarget, String _eventClass, String _exportName, ItemModel _owner)
    {
        setDestinationTarget(_destinationTarget);
        setEventClass(_eventClass);
        setExportName(_exportName);
        setOwner(_owner);
    }


    @Accessor(qualifier = "converterBean", type = Accessor.Type.GETTER)
    public String getConverterBean()
    {
        return (String)getPersistenceContext().getPropertyValue("converterBean");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.GETTER)
    public DestinationTargetModel getDestinationTarget()
    {
        return (DestinationTargetModel)getPersistenceContext().getPropertyValue("destinationTarget");
    }


    @Accessor(qualifier = "eventClass", type = Accessor.Type.GETTER)
    public String getEventClass()
    {
        return (String)getPersistenceContext().getPropertyValue("eventClass");
    }


    @Accessor(qualifier = "eventPropertyConfigurations", type = Accessor.Type.GETTER)
    public List<EventPropertyConfigurationModel> getEventPropertyConfigurations()
    {
        return (List<EventPropertyConfigurationModel>)getPersistenceContext().getPropertyValue("eventPropertyConfigurations");
    }


    @Accessor(qualifier = "exportName", type = Accessor.Type.GETTER)
    public String getExportName()
    {
        return (String)getPersistenceContext().getPropertyValue("exportName");
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
    public String getExtensionName()
    {
        return (String)getPersistenceContext().getPropertyValue("extensionName");
    }


    @Accessor(qualifier = "filterLocation", type = Accessor.Type.GETTER)
    public String getFilterLocation()
    {
        return (String)getPersistenceContext().getPropertyValue("filterLocation");
    }


    @Accessor(qualifier = "mappingType", type = Accessor.Type.GETTER)
    public EventMappingType getMappingType()
    {
        return (EventMappingType)getPersistenceContext().getPropertyValue("mappingType");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public EventPriority getPriority()
    {
        return (EventPriority)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public int getVersion()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("version"));
    }


    @Accessor(qualifier = "exportFlag", type = Accessor.Type.GETTER)
    public boolean isExportFlag()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("exportFlag"));
    }


    @Accessor(qualifier = "converterBean", type = Accessor.Type.SETTER)
    public void setConverterBean(String value)
    {
        getPersistenceContext().setPropertyValue("converterBean", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.SETTER)
    public void setDestinationTarget(DestinationTargetModel value)
    {
        getPersistenceContext().setPropertyValue("destinationTarget", value);
    }


    @Accessor(qualifier = "eventClass", type = Accessor.Type.SETTER)
    public void setEventClass(String value)
    {
        getPersistenceContext().setPropertyValue("eventClass", value);
    }


    @Accessor(qualifier = "eventPropertyConfigurations", type = Accessor.Type.SETTER)
    public void setEventPropertyConfigurations(List<EventPropertyConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("eventPropertyConfigurations", value);
    }


    @Accessor(qualifier = "exportFlag", type = Accessor.Type.SETTER)
    public void setExportFlag(boolean value)
    {
        getPersistenceContext().setPropertyValue("exportFlag", toObject(value));
    }


    @Accessor(qualifier = "exportName", type = Accessor.Type.SETTER)
    public void setExportName(String value)
    {
        getPersistenceContext().setPropertyValue("exportName", value);
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
    public void setExtensionName(String value)
    {
        getPersistenceContext().setPropertyValue("extensionName", value);
    }


    @Accessor(qualifier = "filterLocation", type = Accessor.Type.SETTER)
    public void setFilterLocation(String value)
    {
        getPersistenceContext().setPropertyValue("filterLocation", value);
    }


    @Accessor(qualifier = "mappingType", type = Accessor.Type.SETTER)
    public void setMappingType(EventMappingType value)
    {
        getPersistenceContext().setPropertyValue("mappingType", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(EventPriority value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(int value)
    {
        getPersistenceContext().setPropertyValue("version", toObject(value));
    }
}
