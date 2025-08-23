package de.hybris.platform.util.logging;

import de.hybris.platform.core.Log4JUtils;
import org.apache.commons.logging.impl.Log4JLogger;

public class CommonsHybrisLog4jWrapper extends Log4JLogger
{
    static
    {
        Log4JUtils.startup();
    }

    public CommonsHybrisLog4jWrapper(String name)
    {
        super(name);
    }
}
