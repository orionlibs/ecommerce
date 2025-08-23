package de.hybris.platform.processing.distributed;

public interface DistributedProcessService
{
    <T extends de.hybris.platform.processing.model.DistributedProcessModel> T create(ProcessCreationData paramProcessCreationData);


    <T extends de.hybris.platform.processing.model.DistributedProcessModel> T start(String paramString);


    <T extends de.hybris.platform.processing.model.DistributedProcessModel> T requestToStop(String paramString);


    <T extends de.hybris.platform.processing.model.DistributedProcessModel> T wait(String paramString, long paramLong) throws InterruptedException;


    default ProcessStatus getCurrentStatus(String processCode)
    {
        return ProcessStatus.builder().build();
    }
}
