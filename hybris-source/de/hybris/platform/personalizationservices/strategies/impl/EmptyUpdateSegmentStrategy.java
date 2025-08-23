package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.strategies.UpdateSegmentStrategy;
import org.apache.log4j.Logger;

public class EmptyUpdateSegmentStrategy implements UpdateSegmentStrategy
{
    private static final Logger LOG = Logger.getLogger(EmptyUpdateSegmentStrategy.class);


    public void updateSegments(CxUpdateSegmentContext context)
    {
        LOG.debug("Segments are not updated because there is no UpdateSegmentStrategy configured.");
    }
}
