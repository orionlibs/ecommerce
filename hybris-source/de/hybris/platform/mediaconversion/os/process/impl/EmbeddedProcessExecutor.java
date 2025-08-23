package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.process.rmi.BasicProcessExecutor;
import java.io.IOException;
import org.apache.log4j.Logger;

public class EmbeddedProcessExecutor extends BasicProcessExecutor
{
    private static final Logger LOG = Logger.getLogger(EmbeddedProcessExecutor.class);


    public void quit() throws IOException
    {
        super.quit();
        LOG.debug("Quitting.");
    }


    public int execute(ProcessContext context) throws IOException
    {
        LogUtil.log(LOG, context);
        return super.execute(context);
    }
}
