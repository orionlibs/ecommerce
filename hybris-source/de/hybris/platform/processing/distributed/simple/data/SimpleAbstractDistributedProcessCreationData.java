package de.hybris.platform.processing.distributed.simple.data;

import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.util.Config;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public abstract class SimpleAbstractDistributedProcessCreationData implements ProcessCreationData
{
    protected static final int DEFAULT_BATCH_SIZE = 100;
    protected static final int DEFAULT_NUM_RETRIES = 3;
    protected final String processId;
    protected final String handlerId;
    protected final String scriptCode;
    protected final String nodeGroup;
    protected final int batchSize;
    protected final int numOfRetries;
    protected final Class<? extends SimpleDistributedProcessModel> processModelClass;


    protected SimpleAbstractDistributedProcessCreationData(String handlerId, String processId, String scriptCode, String nodeGroup, Integer batchSize, Integer numOfRetries, Class<? extends SimpleDistributedProcessModel> processModelClass)
    {
        this.handlerId = Objects.<String>requireNonNull(handlerId, "handlerId is required");
        this.processId = createProcessId(processId);
        this.scriptCode = scriptCode;
        this.nodeGroup = nodeGroup;
        this.batchSize = createBatchSize(batchSize);
        this.numOfRetries = createNumOfRetries(numOfRetries);
        this.processModelClass = processModelClass;
    }


    private String createProcessId(String processId)
    {
        return StringUtils.isEmpty(processId) ? (this.handlerId + "_" + this.handlerId) : processId;
    }


    private int createBatchSize(Integer batchSize)
    {
        if(batchSize == null)
        {
            return Config.getInt("distributed.process.simple.template.batch.size", 100);
        }
        return batchSize.intValue();
    }


    private int createNumOfRetries(Integer numOfRetries)
    {
        if(numOfRetries == null)
        {
            return Config.getInt("distributed.process.simple.template.max.batch.retries", 3);
        }
        return numOfRetries.intValue();
    }


    public String getProcessId()
    {
        return this.processId;
    }


    public int getNumOfRetries()
    {
        return this.numOfRetries;
    }


    public int getBatchSize()
    {
        return this.batchSize;
    }


    public String getScriptCode()
    {
        return this.scriptCode;
    }


    public Class<? extends SimpleDistributedProcessModel> getProcessModelClass()
    {
        return this.processModelClass;
    }


    public abstract Stream<? extends SimpleBatchCreationData> initialBatches();


    public String getHandlerBeanId()
    {
        return this.handlerId;
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }
}
