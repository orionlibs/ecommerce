package de.hybris.platform.core;

import com.google.common.base.Joiner;
import de.hybris.platform.jmx.MBeanRegisterUtilities;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class JMXBeanLoader implements TenantListener
{
    private static final String MBEAN_REGISTER_UTILITY = "mbeanRegisterUtility";
    private static final Logger LOG = Logger.getLogger(JMXBeanLoader.class.getName());


    public void afterSetActivateSession(Tenant tenant)
    {
    }


    public void afterTenantStartUp(Tenant tenant)
    {
        boolean switchedTenant = false;
        Tenant previousTenant = null;
        try
        {
            previousTenant = Registry.getCurrentTenantNoFallback();
            if(previousTenant != tenant)
            {
                Registry.setCurrentTenant(tenant);
                switchedTenant = true;
            }
            MBeanRegisterUtilities mbeanRegistry = getMBeanRegistry();
            mbeanRegistry.refreshMBeans();
            Map<String, AbstractJMXMBean> unRegisteredBeans = mbeanRegistry.getUnRegisteredBeans();
            if(MapUtils.isNotEmpty(unRegisteredBeans))
            {
                mbeanRegistry.registerMBeans(unRegisteredBeans);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registering, unregistered beans [" + Joiner.on(",")
                                .join(unRegisteredBeans.values()) + "]  for tenant " + tenant);
            }
            int size = mbeanRegistry.getRegisteredBeans().size();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registered " + size + " JMX bean(s)" + ((size > 1) ? "s" : "") + " for tenant " + tenant);
            }
        }
        catch(NoSuchBeanDefinitionException nsbde)
        {
            LOG.error("Can't register jmxbeans on start of the tenant " + tenant + " check spring configuration for jmx beans ", (Throwable)nsbde);
        }
        catch(BeansException be)
        {
            LOG.error("Can't register jmxbeans on start of the tenant " + tenant, (Throwable)be);
        }
        finally
        {
            if(switchedTenant)
            {
                Registry.unsetCurrentTenant();
                if(previousTenant != null)
                {
                    Registry.setCurrentTenant(previousTenant);
                }
            }
        }
    }


    public void beforeTenantShutDown(Tenant tenant)
    {
        boolean switchedTenant = false;
        try
        {
            if(Registry.getCurrentTenantNoFallback() != tenant)
            {
                Registry.setCurrentTenant(tenant);
                switchedTenant = true;
            }
            MBeanRegisterUtilities mbeanRegistry = getMBeanRegistry();
            Map<String, AbstractJMXMBean> unRegisteredBeans = mbeanRegistry.getRegisteredBeans();
            if(MapUtils.isNotEmpty(unRegisteredBeans))
            {
                mbeanRegistry.unregisterMBeans(unRegisteredBeans);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Unregistering beans [ " + Joiner.on(",")
                                .join(unRegisteredBeans.values()) + "]  for tenant " + tenant);
            }
        }
        catch(NoSuchBeanDefinitionException nsbde)
        {
            LOG.error("Can't unregister jmxbeans on shutdown of the tenant " + tenant + " check spring configuration for jmx beans ", (Throwable)nsbde);
        }
        catch(BeansException be)
        {
            LOG.error("Can't unregister jmxbeans on shutdown of the tenant " + tenant, (Throwable)be);
        }
        finally
        {
            if(switchedTenant)
            {
                Registry.unsetCurrentTenant();
            }
        }
    }


    private MBeanRegisterUtilities getMBeanRegistry()
    {
        return (MBeanRegisterUtilities)Registry.getSingletonGlobalApplicationContext().getBean("mbeanRegisterUtility", MBeanRegisterUtilities.class);
    }


    public void beforeUnsetActivateSession(Tenant tenant)
    {
    }
}
