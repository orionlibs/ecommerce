package com.hybris.backoffice.search.events;

import com.hybris.backoffice.events.AbstractBackofficeEventListener;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfterInitializationEndBackofficeSearchListener extends AbstractBackofficeEventListener<AfterInitializationEndEvent>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AfterInitializationEndBackofficeSearchListener.class);


    public AfterInitializationEndBackofficeSearchListener()
    {
        LOGGER.debug(AfterInitializationEndBackofficeSearchListener.class.getSimpleName());
    }
}
