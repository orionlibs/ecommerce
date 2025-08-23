package de.hybris.platform.persistence;

import de.hybris.platform.core.Initialization;
import java.util.Map;

@Deprecated(since = "18.08", forRemoval = true)
public abstract class AbstractTypeInitializer
{
    @Deprecated(since = "18.08", forRemoval = true)
    public static boolean forceClean(Map params)
    {
        return Initialization.forceClean(params);
    }
}
