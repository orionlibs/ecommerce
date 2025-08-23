package de.hybris.platform.cockpit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemTools
{
    private static final Logger LOG = LoggerFactory.getLogger(SystemTools.class);


    public static int waitForProcess(Process process, long millis)
    {
        long currentTime = 0L;
        int exitValue = -1;
        while(currentTime < millis)
        {
            try
            {
                exitValue = process.exitValue();
                break;
            }
            catch(IllegalThreadStateException illegalThreadStateException)
            {
                try
                {
                    Thread.sleep(100L);
                    currentTime += 100L;
                }
                catch(InterruptedException e)
                {
                    LOG.error(e.getMessage(), e);
                    break;
                }
            }
        }
        if(exitValue < 0)
        {
            process.destroy();
        }
        return exitValue;
    }
}
