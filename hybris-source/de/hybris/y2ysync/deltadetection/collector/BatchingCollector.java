package de.hybris.y2ysync.deltadetection.collector;

import de.hybris.deltadetection.ChangesCollector;
import de.hybris.platform.core.PK;
import java.util.List;

public interface BatchingCollector extends ChangesCollector
{
    List<PK> getPksOfBatches();


    void setId(String paramString);
}
