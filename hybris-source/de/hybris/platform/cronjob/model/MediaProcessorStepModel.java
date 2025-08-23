package de.hybris.platform.cronjob.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MediaProcessorStepModel extends StepModel
{
    public static final String _TYPECODE = "MediaProcessorStep";


    public MediaProcessorStepModel()
    {
    }


    public MediaProcessorStepModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaProcessorStepModel(BatchJobModel _batchJob, String _code, Integer _sequenceNumber)
    {
        setBatchJob(_batchJob);
        setCode(_code);
        setSequenceNumber(_sequenceNumber);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaProcessorStepModel(BatchJobModel _batchJob, String _code, ItemModel _owner, Integer _sequenceNumber)
    {
        setBatchJob(_batchJob);
        setCode(_code);
        setOwner(_owner);
        setSequenceNumber(_sequenceNumber);
    }
}
