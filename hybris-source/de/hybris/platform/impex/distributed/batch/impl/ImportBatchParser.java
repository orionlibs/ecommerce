package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ImportBatchParser
{
    private ImportDataDumpStrategy dumpStrategy;


    public BatchData parse(ImportBatchHandler batchHandler)
    {
        BatchingImpexReader reader = getImpExReader(batchHandler);
        HeaderDescriptor headerDescriptor = null;
        List<ValueLine> valueLines = new ArrayList<>();
        while(true)
        {
            Object line = readLine((ImpExReader)reader);
            if(line == null)
            {
                break;
            }
            if(line instanceof HeaderDescriptor)
            {
                headerDescriptor = (HeaderDescriptor)line;
                continue;
            }
            if(line instanceof ValueLine)
            {
                valueLines.add((ValueLine)line);
            }
        }
        return new BatchData(this.dumpStrategy, reader, headerDescriptor, valueLines);
    }


    private BatchingImpexReader getImpExReader(ImportBatchHandler batchHandler)
    {
        BatchingImpexReader impExReader = new BatchingImpexReader(batchHandler);
        String codeExecution = batchHandler.getProperty("code.execution");
        if(Boolean.parseBoolean(codeExecution))
        {
            impExReader.enableCodeExecution(true);
        }
        return impExReader;
    }


    private Object readLine(ImpExReader impExReader)
    {
        try
        {
            return impExReader.readLine();
        }
        catch(ImpExException e)
        {
            throw new IllegalStateException(e);
        }
    }


    @Required
    public void setDumpStrategy(ImportDataDumpStrategy dumpStrategy)
    {
        this.dumpStrategy = dumpStrategy;
    }
}
