package de.hybris.deltadetection.jobs;

import de.hybris.deltadetection.ItemChangeDTO;
import java.util.Arrays;
import java.util.Map;
import org.apache.log4j.Logger;

public class ConsumeAllChangesJobPerformable extends AbstractChangeProcessorJobPerformable
{
    private static final Logger LOG = Logger.getLogger(ConsumeAllChangesJobPerformable.class.getName());


    boolean processChange(ItemChangeDTO change, Map<String, Object> ctx)
    {
        LOG.info("Consuming changes for " + change + " ...");
        getChangeDetectionService().consumeChanges(Arrays.asList(new ItemChangeDTO[] {change}));
        return true;
    }
}
