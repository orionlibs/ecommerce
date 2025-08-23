package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProcessEventConfigurationModel extends EventConfigurationModel
{
    public static final String _TYPECODE = "ProcessEventConfiguration";
    public static final String PROCESS = "process";


    public ProcessEventConfigurationModel()
    {
    }


    public ProcessEventConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessEventConfigurationModel(DestinationTargetModel _destinationTarget, String _eventClass, String _exportName, String _process)
    {
        setDestinationTarget(_destinationTarget);
        setEventClass(_eventClass);
        setExportName(_exportName);
        setProcess(_process);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessEventConfigurationModel(DestinationTargetModel _destinationTarget, String _eventClass, String _exportName, ItemModel _owner, String _process)
    {
        setDestinationTarget(_destinationTarget);
        setEventClass(_eventClass);
        setExportName(_exportName);
        setOwner(_owner);
        setProcess(_process);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public String getProcess()
    {
        return (String)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(String value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }
}
