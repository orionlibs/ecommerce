package de.hybris.platform.processing.distributed.simple;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import java.util.List;

public class SometimesFailingBatchProcessor extends TestBatchProcessor
{
    protected List<PK> asList(Object ctx)
    {
        Preconditions.checkState(ctx instanceof List, "ctx must be instance of List");
        List<PK> result = (List<PK>)ctx;
        if(result.size() < 100)
        {
            throw new IllegalStateException("Test exception");
        }
        return result;
    }
}
