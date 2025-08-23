package de.hybris.platform.impex.distributed.batch;

import de.hybris.platform.impex.distributed.batch.impl.BatchData;
import java.util.List;

public interface BatchingImpExCRUDHandler
{
    List<BatchData.ImportData> processInBulk(List<BatchData.ImportData> paramList);


    List<BatchData.ImportData> processByLine(List<BatchData.ImportData> paramList);
}
