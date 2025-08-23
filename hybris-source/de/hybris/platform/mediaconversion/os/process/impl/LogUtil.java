package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import java.util.Arrays;
import org.apache.log4j.Logger;

final class LogUtil
{
    static void log(Logger logger, ProcessContext context)
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("Executing " +
                            Arrays.toString((Object[])context.getCommand()) + (
                            (context.getDirectory() != null) ? (" in " + context.getDirectory()) : "") + (
                            (context.getEnvironment() != null && (context.getEnvironment()).length > 0) ? (" with environment " +
                                            Arrays.toString((Object[])context.getEnvironment())) : ""));
        }
    }
}
