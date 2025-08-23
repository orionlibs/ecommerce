package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class DestinationTargetModel extends ItemModel
{
    public static final String _TYPECODE = "DestinationTarget";
    public static final String ID = "id";
    public static final String DESTINATIONCHANNEL = "destinationChannel";
    public static final String TEMPLATE = "template";
    public static final String REGISTRATIONSTATUS = "registrationStatus";
    public static final String REGISTRATIONSTATUSINFO = "registrationStatusInfo";
    public static final String EVENTCONFIGURATIONS = "eventConfigurations";
    public static final String DESTINATIONS = "destinations";


    public DestinationTargetModel()
    {
    }


    public DestinationTargetModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DestinationTargetModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DestinationTargetModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "destinationChannel", type = Accessor.Type.GETTER)
    public DestinationChannel getDestinationChannel()
    {
        return (DestinationChannel)getPersistenceContext().getPropertyValue("destinationChannel");
    }


    @Accessor(qualifier = "destinations", type = Accessor.Type.GETTER)
    public Collection<AbstractDestinationModel> getDestinations()
    {
        return (Collection<AbstractDestinationModel>)getPersistenceContext().getPropertyValue("destinations");
    }


    @Accessor(qualifier = "eventConfigurations", type = Accessor.Type.GETTER)
    public Collection<EventConfigurationModel> getEventConfigurations()
    {
        return (Collection<EventConfigurationModel>)getPersistenceContext().getPropertyValue("eventConfigurations");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "registrationStatus", type = Accessor.Type.GETTER)
    public RegistrationStatus getRegistrationStatus()
    {
        return (RegistrationStatus)getPersistenceContext().getPropertyValue("registrationStatus");
    }


    @Accessor(qualifier = "registrationStatusInfo", type = Accessor.Type.GETTER)
    public String getRegistrationStatusInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("registrationStatusInfo");
    }


    @Accessor(qualifier = "template", type = Accessor.Type.GETTER)
    public Boolean getTemplate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("template");
    }


    @Accessor(qualifier = "destinationChannel", type = Accessor.Type.SETTER)
    public void setDestinationChannel(DestinationChannel value)
    {
        getPersistenceContext().setPropertyValue("destinationChannel", value);
    }


    @Accessor(qualifier = "destinations", type = Accessor.Type.SETTER)
    public void setDestinations(Collection<AbstractDestinationModel> value)
    {
        getPersistenceContext().setPropertyValue("destinations", value);
    }


    @Accessor(qualifier = "eventConfigurations", type = Accessor.Type.SETTER)
    public void setEventConfigurations(Collection<EventConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("eventConfigurations", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "registrationStatus", type = Accessor.Type.SETTER)
    public void setRegistrationStatus(RegistrationStatus value)
    {
        getPersistenceContext().setPropertyValue("registrationStatus", value);
    }


    @Accessor(qualifier = "registrationStatusInfo", type = Accessor.Type.SETTER)
    public void setRegistrationStatusInfo(String value)
    {
        getPersistenceContext().setPropertyValue("registrationStatusInfo", value);
    }


    @Accessor(qualifier = "template", type = Accessor.Type.SETTER)
    public void setTemplate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("template", value);
    }
}
