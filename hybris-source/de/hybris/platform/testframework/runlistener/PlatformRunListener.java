package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemNotInitializedException;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class PlatformRunListener extends RunListener
{
    private static final Logger log = Logger.getLogger(PlatformRunListener.class.getName());
    protected JaloSession jaloSession;


    public void testRunStarted(Description description) throws Exception
    {
        Registry.activateStandaloneMode();
        Utilities.setJUnitTenant();
        if(log.isDebugEnabled())
        {
            log.debug("Setting Cluster and Tenant");
        }
        JaloConnection con = JaloConnection.getInstance();
        boolean sysIni = con.isSystemInitialized();
        if(!sysIni)
        {
            JaloSystemNotInitializedException jaloSystemNotInitializedException = new JaloSystemNotInitializedException(null, "Test system is not initialized", -1);
            StackTraceElement[] trimmedStack = new StackTraceElement[Math.min((jaloSystemNotInitializedException.getStackTrace()).length, 3)];
            System.arraycopy(jaloSystemNotInitializedException.getStackTrace(), 0, trimmedStack, 0, trimmedStack.length);
            jaloSystemNotInitializedException.setStackTrace(trimmedStack);
            jaloSystemNotInitializedException.printStackTrace();
            throw jaloSystemNotInitializedException;
        }
    }


    public void testStarted(Description description) throws Exception
    {
        this.jaloSession = JaloConnection.getInstance().createAnonymousCustomerSession();
    }


    public void testFinished(Description description) throws Exception
    {
        if(this.jaloSession != null && JaloSession.assureSessionNotStale(this.jaloSession))
        {
            this.jaloSession.close();
        }
    }
}
