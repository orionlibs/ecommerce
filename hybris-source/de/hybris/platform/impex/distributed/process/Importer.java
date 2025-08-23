package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import org.apache.commons.lang.StringUtils;

class Importer implements ImportBatchHandler
{
    private final String content;
    private final ImportMetadata processMetadata;
    private final ImportMetadata batchMetadata;
    private String importResult;
    private long remainingWorkLoad;


    public Importer(String content, ImportMetadata batchMetadata, ImportMetadata processMetadata)
    {
        this.content = content;
        this.batchMetadata = batchMetadata;
        this.processMetadata = processMetadata;
    }


    public String getImportResult()
    {
        return this.importResult;
    }


    public long getRemainingWorkLoad()
    {
        return this.remainingWorkLoad;
    }


    public String dumpMetadata()
    {
        return this.batchMetadata.getStringRepresentation();
    }


    public String getInputData()
    {
        return this.content;
    }


    public void setOutputData(String data)
    {
        this.importResult = data;
    }


    public void setRemainingWorkLoad(long remainingWorkLoad)
    {
        this.remainingWorkLoad = remainingWorkLoad;
    }


    public String getProperty(String propertyName)
    {
        String value = this.batchMetadata.get(propertyName);
        if(StringUtils.isNotEmpty(value))
        {
            return value;
        }
        return this.processMetadata.get(propertyName);
    }


    public void setProperty(String propertyName, String propertyValue)
    {
        this.batchMetadata.set(propertyName, propertyValue);
    }
}
