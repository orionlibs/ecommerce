package de.hybris.platform.jmx;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import de.hybris.platform.jmx.mbeans.impl.HybrisStandardMBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.jmx.export.MBeanExportException;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
import org.springframework.util.ObjectUtils;

public class MBeanRegisterUtilities
{
    private static final Logger LOG = Logger.getLogger(MBeanRegisterUtilities.class.getName());
    private volatile MBeanServer mbs = null;
    private final Map<String, AbstractJMXMBean> regbeans = new ConcurrentHashMap<>();
    private final Map<String, AbstractJMXMBean> nonregbeans = new ConcurrentHashMap<>();
    private MBeanInfoAssembler assembler;


    public synchronized void registerMBeans(Map<String, AbstractJMXMBean> beans)
    {
        internalMBeanRegistration(true, beans);
        refreshMBeans();
    }


    public synchronized void unregisterMBeans(Map<String, AbstractJMXMBean> beans)
    {
        internalMBeanRegistration(false, beans);
        refreshMBeans();
    }


    protected StandardMBean createAndConfigureMBean(Object bean, String beanKey, Class beanInterafce) throws MBeanExportException
    {
        try
        {
            ModelMBeanInfoSupport filteredInfo = (ModelMBeanInfoSupport)getMBeanInfo(bean, beanKey);
            HybrisStandardMBean mbean = new HybrisStandardMBean(bean, beanInterafce);
            mbean.setCustomMBeanInfo(filteredInfo);
            return (StandardMBean)mbean;
        }
        catch(Exception ex)
        {
            throw new MBeanExportException("Could not create ModelMBean for managed resource [" + bean + "] with key '" + beanKey + "'", ex);
        }
    }


    private ModelMBeanInfo getMBeanInfo(Object bean, String beanKey) throws JMException
    {
        ModelMBeanInfo info = getAssembler().getMBeanInfo(bean, beanKey);
        if(ObjectUtils.isEmpty((Object[])info.getAttributes()) && ObjectUtils.isEmpty((Object[])info.getOperations()))
        {
            LOG.warn("Bean with key '" + beanKey + "' has been registered as an MBean but has no exposed attributes or operations.");
        }
        return info;
    }


    public Map<String, AbstractJMXMBean> getRegisteredBeans()
    {
        refreshMBeans();
        return Collections.unmodifiableMap(new LinkedHashMap<>(this.regbeans));
    }


    public Map<String, AbstractJMXMBean> getUnRegisteredBeans()
    {
        return Collections.unmodifiableMap(new LinkedHashMap<>(this.nonregbeans));
    }


    public void refreshMBeans()
    {
        try
        {
            Map<String, AbstractJMXMBean> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)
                            getApplicationContext(), AbstractJMXMBean.class, true, true);
            this.regbeans.clear();
            this.nonregbeans.clear();
            for(Map.Entry<String, AbstractJMXMBean> genericEntry : beans.entrySet())
            {
                ObjectName objectname = null;
                try
                {
                    AbstractJMXMBean value = genericEntry.getValue();
                    objectname = value.getObjectName();
                    if(getMBeanServer().isRegistered(objectname))
                    {
                        this.regbeans.put(genericEntry.getKey(), value);
                        continue;
                    }
                    this.nonregbeans.put(genericEntry.getKey(), value);
                }
                catch(MalformedObjectNameException e)
                {
                    LOG.error("ObjectName of bean '" + (String)genericEntry
                                    .getKey() + "' is malformed. Please change jmxPath (jmxDomain). " + e
                                    .getMessage(), e);
                }
                catch(NullPointerException e)
                {
                    LOG.error("Creating ObjectName for bean '" + (String)genericEntry
                                    .getKey() + "' throws a NullPointer. " + e.getMessage(), e);
                }
            }
        }
        catch(BeanCreationException e)
        {
            LOG.error("Could not create a JMX MBean. " + e.getMessage());
        }
    }


    private ApplicationContext getApplicationContext()
    {
        return Registry.getCoreApplicationContext();
    }


    private void internalMBeanRegistration(boolean register, Map<String, AbstractJMXMBean> beans)
    {
        if(getMBeanServer() != null)
        {
            for(Map.Entry<String, AbstractJMXMBean> mentry : beans.entrySet())
            {
                ObjectName objectname = null;
                try
                {
                    if(mentry.getValue() != null && ((AbstractJMXMBean)mentry.getValue()).getObjectName() != null)
                    {
                        objectname = ((AbstractJMXMBean)mentry.getValue()).getObjectName();
                        if(getMBeanServer().isRegistered(objectname))
                        {
                            getMBeanServer().unregisterMBean(objectname);
                        }
                        if(register)
                        {
                            StandardMBean mbean = createAndConfigureMBean(mentry.getValue(), ((AbstractJMXMBean)mentry.getValue())
                                            .getObjectNameString(), ((AbstractJMXMBean)mentry
                                            .getValue()).getBeanInterface());
                            getMBeanServer().registerMBean(mbean, objectname);
                            LOG.debug("Registered MBean " + ((AbstractJMXMBean)mentry.getValue()).getObjectNameString() + " for interface " + ((AbstractJMXMBean)mentry
                                            .getValue()).getBeanInterface());
                        }
                        continue;
                    }
                    LOG.warn(" Retrieved  AbstractJMXMBean bean instance during " + (
                                    register ? "registration" : "unregistration") + " was invalid " + (
                                    (mentry.getValue() != null) ? (
                                                    (((AbstractJMXMBean)mentry.getValue()).getObjectNameString() == null) ? (", object name for a bean " +
                                                                    mentry.getValue() + " was null.") :
                                                                    String.valueOf("")) :
                                                    ", mentry value was null."));
                }
                catch(MalformedObjectNameException e)
                {
                    LOG.error("ObjectName of bean '" + (String)mentry
                                    .getKey() + "' is malformed. Please change jmxPath (jmxDomain). " + e
                                    .getMessage(), e);
                }
                catch(NullPointerException e)
                {
                    LOG.error("Creating ObjectName for bean '" + (String)mentry
                                    .getKey() + "(" + mentry + ")' throws a NullPointer. " + e
                                    .getMessage(), e);
                }
                catch(MBeanRegistrationException e)
                {
                    LOG.warn("Could not un~/register MBean with id '" + (String)mentry
                                    .getKey() + "(" + mentry + ")'! Skipping this bean.");
                }
                catch(InstanceNotFoundException e)
                {
                    LOG.warn("Could not unregister MBean with id '" + (String)mentry.getKey() + "(" + mentry + ")'! Ignoring.");
                }
                catch(InstanceAlreadyExistsException e)
                {
                    LOG.error("Could not register MBean with id '" + (String)mentry.getKey() + "(" + mentry + ")'! This MBean should already be unregisterd " + e
                                    .getMessage(), e);
                }
                catch(NotCompliantMBeanException e)
                {
                    LOG.error("Given MBean with id '" + (String)mentry.getKey() + "(" + mentry + ")' is not a JMX compliant MBean. Refactor it. " + e
                                    .getMessage(), e);
                }
                catch(MBeanExportException e)
                {
                    LOG.error("Error exporting bean MBean with id '" + (String)mentry.getKey() + "(" + mentry + ")'" + e.getMessage(), (Throwable)e);
                }
            }
        }
        else
        {
            LOG.warn("Did not find active MBeanServer. Cannot register any MBeans.");
        }
    }


    private MBeanServer getMBeanServer()
    {
        if(this.mbs == null)
        {
            this.mbs = ManagementFactory.getPlatformMBeanServer();
        }
        return this.mbs;
    }


    @Required
    public void setAssembler(MBeanInfoAssembler assembler)
    {
        this.assembler = assembler;
    }


    public MBeanInfoAssembler getAssembler()
    {
        return this.assembler;
    }
}
