package de.hybris.platform.spring.ctx;

import de.hybris.platform.util.RedeployUtilities;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.GenericApplicationContext;

public class CloseAwareApplicationContext extends GenericApplicationContext
{
    private static final Logger LOG = Logger.getLogger(CloseAwareApplicationContext.class.getName());
    private static final int TIMEOUT_LOOP_COUNT = 10;
    private static final int SLEEP_TIME = 500;
    private final Object mutualLock = new Object();
    private boolean isClosing = false;
    private final ThreadLocal<Boolean> isClosingThread = (ThreadLocal<Boolean>)new Object(this);
    private final AtomicLong pendingCallsCounter = new AtomicLong(0L);


    public CloseAwareApplicationContext(String name)
    {
        setDisplayName(name);
    }


    public CloseAwareApplicationContext(String uid, String name)
    {
        setDisplayName(name);
        setId(uid);
    }


    public void publishEvent(ApplicationEvent event)
    {
        boolean dispatchAllowed = false;
        synchronized(this.mutualLock)
        {
            if(this.isClosing)
            {
                dispatchAllowed = ((Boolean)this.isClosingThread.get()).booleanValue();
                if(dispatchAllowed)
                {
                    LOG.debug("Enable event dispatch from Spring context closing thread: " + event);
                }
            }
            else
            {
                dispatchAllowed = true;
            }
            if(dispatchAllowed)
            {
                this.pendingCallsCounter.incrementAndGet();
            }
        }
        try
        {
            if(dispatchAllowed)
            {
                super.publishEvent(event);
            }
            else
            {
                LOG.debug("Event dispatching is not allowed since the Spring context is closing right now: " + event);
            }
        }
        catch(IllegalStateException ile)
        {
            if(RedeployUtilities.isShutdownInProgress())
            {
                LOG.info("Unable to publish event " + event + " due to  " + ile.getMessage());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(ile);
                }
            }
            else
            {
                throw ile;
            }
        }
        finally
        {
            if(dispatchAllowed)
            {
                this.pendingCallsCounter.decrementAndGet();
            }
        }
    }


    protected void doClose()
    {
        try
        {
            boolean closingAllowed;
            synchronized(this.mutualLock)
            {
                this.isClosing = true;
                this.isClosingThread.set(Boolean.TRUE);
                closingAllowed = (this.pendingCallsCounter.get() == 0L);
            }
            if(!closingAllowed)
            {
                LOG.warn("closing of Spring context is not allowed since there are pending calls to publishEvent(). Waiting for the calls to finish.");
                closingAllowed = waitForPublishEventFinish();
                if(!closingAllowed)
                {
                    LOG.error("Pending call to publishEvent() did not finish within 5000 [ms]. Closing anyway...");
                }
            }
            super.doClose();
        }
        finally
        {
            this.isClosingThread.set(Boolean.FALSE);
        }
    }


    private boolean waitForPublishEventFinish()
    {
        boolean endOfLoop = false;
        for(int i = 0; i < 10 && !endOfLoop; i++)
        {
            endOfLoop = (this.pendingCallsCounter.get() == 0L);
            if(!endOfLoop)
            {
                LOG.debug("Awaiting for the pending calls to publishEvent() to finish...");
                try
                {
                    Thread.sleep(500L);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                    LOG.warn(ex.getMessage(), ex);
                    break;
                }
            }
        }
        return endOfLoop;
    }


    public String toString()
    {
        return getDisplayName();
    }
}
