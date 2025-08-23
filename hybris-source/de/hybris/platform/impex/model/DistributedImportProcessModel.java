package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DistributedImportProcessModel extends DistributedProcessModel
{
    public static final String _TYPECODE = "DistributedImportProcess";
    public static final String IMPEXIMPORTCRONJOB = "impExImportCronJob";
    public static final String METADATA = "metadata";


    public DistributedImportProcessModel()
    {
    }


    public DistributedImportProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedImportProcessModel(String _code, String _currentExecutionId, ImpExImportCronJobModel _impExImportCronJob, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setImpExImportCronJob(_impExImportCronJob);
        setState(_state);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedImportProcessModel(String _code, String _currentExecutionId, String _handlerBeanId, ImpExImportCronJobModel _impExImportCronJob, String _metadata, String _nodeGroup, ItemModel _owner, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setHandlerBeanId(_handlerBeanId);
        setImpExImportCronJob(_impExImportCronJob);
        setMetadata(_metadata);
        setNodeGroup(_nodeGroup);
        setOwner(_owner);
        setState(_state);
    }


    @Accessor(qualifier = "impExImportCronJob", type = Accessor.Type.GETTER)
    public ImpExImportCronJobModel getImpExImportCronJob()
    {
        return (ImpExImportCronJobModel)getPersistenceContext().getPropertyValue("impExImportCronJob");
    }


    @Accessor(qualifier = "metadata", type = Accessor.Type.GETTER)
    public String getMetadata()
    {
        return (String)getPersistenceContext().getPropertyValue("metadata");
    }


    @Accessor(qualifier = "impExImportCronJob", type = Accessor.Type.SETTER)
    public void setImpExImportCronJob(ImpExImportCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("impExImportCronJob", value);
    }


    @Accessor(qualifier = "metadata", type = Accessor.Type.SETTER)
    public void setMetadata(String value)
    {
        getPersistenceContext().setPropertyValue("metadata", value);
    }
}
