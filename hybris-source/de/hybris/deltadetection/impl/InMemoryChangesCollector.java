package de.hybris.deltadetection.impl;

import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class InMemoryChangesCollector implements ChangesCollector
{
    private static final Logger LOG = Logger.getLogger(InMemoryChangesCollector.class.getName());
    private final List<ItemChangeDTO> changes = new ArrayList<>();


    public boolean collect(ItemChangeDTO change)
    {
        this.changes.add(change);
        return true;
    }


    public List<ItemChangeDTO> getChanges()
    {
        return this.changes;
    }


    public void clearChanges()
    {
        this.changes.clear();
    }


    public void finish()
    {
        LOG.info("Collecting changes has been finished - " + this.changes.size() + " changes collected");
        if(LOG.isDebugEnabled())
        {
            for(ItemChangeDTO change : this.changes)
            {
                LOG.debug(change);
            }
        }
    }
}
