package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.impex.distributed.batch.BatchingImpExCRUDHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class BatchingInsertUpdateHandler implements BatchingImpExCRUDHandler
{
    private BatchingInsertHandler insertHandler;
    private BatchingUpdateHandler updateHandler;


    public List<BatchData.ImportData> processInBulk(List<BatchData.ImportData> importDatas)
    {
        Objects.requireNonNull(this.updateHandler);
        Objects.requireNonNull(this.insertHandler);
        return process(importDatas, this.updateHandler::processInBulk, this.insertHandler::processInBulk);
    }


    public List<BatchData.ImportData> processByLine(List<BatchData.ImportData> importDatas)
    {
        Objects.requireNonNull(this.updateHandler);
        Objects.requireNonNull(this.insertHandler);
        return process(importDatas, this.updateHandler::processByLine, this.insertHandler::processByLine);
    }


    private List<BatchData.ImportData> process(List<BatchData.ImportData> importDatas, Function<List<BatchData.ImportData>, List<BatchData.ImportData>> updateFunc, Function<List<BatchData.ImportData>, List<BatchData.ImportData>> insertFunc)
    {
        List<BatchData.ImportData> toUpdate = new ArrayList<>();
        List<BatchData.ImportData> toInsert = new ArrayList<>();
        for(BatchData.ImportData data : importDatas)
        {
            if(data.hasExistingItems())
            {
                toUpdate.add(data);
                continue;
            }
            toInsert.add(data);
        }
        List<BatchData.ImportData> unresolvedUpdates = updateFunc.apply(toUpdate);
        List<BatchData.ImportData> unresolvedInserts = insertFunc.apply(toInsert);
        if(CollectionUtils.isEmpty(unresolvedUpdates) && CollectionUtils.isEmpty(unresolvedInserts))
        {
            return Collections.emptyList();
        }
        List<BatchData.ImportData> unresolved = new ArrayList<>();
        unresolved.addAll(unresolvedUpdates);
        unresolved.addAll(unresolvedInserts);
        return unresolved;
    }


    @Required
    public void setInsertHandler(BatchingInsertHandler insertHandler)
    {
        this.insertHandler = insertHandler;
    }


    @Required
    public void setUpdateHandler(BatchingUpdateHandler updateHandler)
    {
        this.updateHandler = updateHandler;
    }
}
