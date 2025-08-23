package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.Utilities;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractJMXMBean extends JMXBindableTenantAwareObject implements InitializingBean
{
    private Class beanInterface;
    private static final Logger LOG = Logger.getLogger(AbstractJMXMBean.class.getName());


    public void afterPropertiesSet() throws Exception
    {
        setTenant(Registry.getCurrentTenantNoFallback());
    }


    protected void activateTenant()
    {
        try
        {
            Registry.setCurrentTenantByID(getTenantId());
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Got Exception in activateTenant()! " + e.getMessage(), e);
        }
    }


    protected void deactivateTenant()
    {
        try
        {
            if(JaloSession.hasCurrentSession())
            {
                JaloSession.getCurrentSession().close();
            }
        }
        catch(Throwable t)
        {
            LOG.error("Unexpected error closing session in " + this + ": " + t.getMessage());
            LOG.error(Utilities.getStackTraceAsString(t));
        }
        try
        {
            Registry.unsetCurrentTenant();
        }
        catch(Exception e)
        {
            LOG.error("Got Exception in deactivateTenant() " + e.getMessage(), e);
        }
    }


    public ObjectName getObjectName() throws MalformedObjectNameException, NullPointerException
    {
        return new ObjectName(getObjectNameString());
    }


    protected boolean isJNDIContextAware()
    {
        return false;
    }


    @Required
    public void setJmxPath(String jmxPath)
    {
        super.setJmxPath(jmxPath);
    }


    public void setBeanInterface(Class beanInterface)
    {
        this.beanInterface = beanInterface;
    }


    public Class getBeanInterface()
    {
        return this.beanInterface;
    }
}
