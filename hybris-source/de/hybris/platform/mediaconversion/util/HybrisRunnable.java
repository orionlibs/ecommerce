package de.hybris.platform.mediaconversion.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class HybrisRunnable implements Runnable
{
    private static final Logger LOG = Logger.getLogger(HybrisRunnable.class);
    private final Runnable runnable;
    private final String tenantId;
    private final Map<String, Object> ctxAttributes;


    public HybrisRunnable(Runnable runnable)
    {
        this(runnable, JaloSession.getCurrentSession().getSessionContext());
    }


    public HybrisRunnable(Runnable runnable, SessionContext ctx)
    {
        this.runnable = runnable;
        if(this.runnable == null)
        {
            throw new IllegalArgumentException("Runnable must not be null.");
        }
        this.ctxAttributes = ctx.getAttributes();
        if(this.ctxAttributes == null)
        {
            throw new IllegalArgumentException("SessionContext must not be null.");
        }
        this.tenantId = Registry.getCurrentTenant().getTenantID();
        if(this.tenantId == null)
        {
            throw new IllegalArgumentException("Tenant Id must not be null.");
        }
    }


    public void run()
    {
        try
        {
            Registry.setCurrentTenantByID(this.tenantId);
            JaloSession.getCurrentSession().setAttributes(this.ctxAttributes);
            this.runnable.run();
        }
        finally
        {
            try
            {
                JaloSession.deactivate();
            }
            catch(Exception e)
            {
                LOG.error("Failed to deactivate session.", e);
            }
            try
            {
                Registry.unsetCurrentTenant();
            }
            catch(Exception e)
            {
                LOG.error("Failed to unset current tenant.", e);
            }
        }
    }
}
