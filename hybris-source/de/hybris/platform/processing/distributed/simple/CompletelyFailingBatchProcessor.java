package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.core.PK;
import java.util.List;

public class CompletelyFailingBatchProcessor extends TestBatchProcessor
{
    protected List<PK> asList(Object ctx)
    {
        throw new IllegalStateException("Test exception");
    }
}
