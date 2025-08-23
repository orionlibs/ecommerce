package de.hybris.platform.aop;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jmx.mbeans.ProfilingReportBean;
import de.hybris.platform.jmx.mbeans.ProfilingReportTemplate;
import de.hybris.platform.jmx.mbeans.impl.HybrisStandardMBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.management.DynamicMBean;
import javax.management.JMException;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;

public abstract class AbstractProfilingAspect implements Profiler
{
    private static final Logger LOG = Logger.getLogger(AbstractProfilingAspect.class.getName());
    protected Class<?> template;
    protected String domain;
    protected String aspectNodeName;
    protected Enum accuracy = (Enum)Accuracy.MS;
    protected long limit;
    private MBeanInfoAssembler assembler;


    protected MBeanInfoAssembler getAssembler()
    {
        return this.assembler;
    }


    @Required
    public void setAssembler(MBeanInfoAssembler assembler)
    {
        this.assembler = assembler;
    }


    private static final Map<String, ObjectInstance> jmxBeans = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Object> jmxResources = Collections.synchronizedMap(new HashMap<>());


    @Required
    public void setTemplate(Class<? extends ProfilingReportBean> template)
    {
        this.template = template;
    }


    public void setAspectNodeName(String aspectNodeName)
    {
        if(aspectNodeName == null || aspectNodeName.trim().isEmpty())
        {
            aspectNodeName = getClass().getName();
        }
        this.aspectNodeName = aspectNodeName;
    }


    public void setAccuracy(String accuracy)
    {
        if(accuracy != null && accuracy.equalsIgnoreCase(Accuracy.MS.name()))
        {
            this.accuracy = (Enum)Accuracy.MS;
        }
        else if(accuracy != null && accuracy.equalsIgnoreCase(Accuracy.NS.name()))
        {
            this.accuracy = (Enum)Accuracy.NS;
        }
    }


    public void setDomain(String domain)
    {
        if(domain == null || domain.trim().isEmpty())
        {
            domain = "hybris";
        }
        this.domain = domain;
    }


    public void setLimit(long limit)
    {
        this.limit = limit;
    }


    protected long getTime()
    {
        if(this.accuracy.equals(Accuracy.MS))
        {
            return System.currentTimeMillis();
        }
        if(this.accuracy.equals(Accuracy.NS))
        {
            return System.nanoTime();
        }
        throw new IllegalStateException("Unsupported 'time measure accuracy' setting: " + this.accuracy);
    }


    protected long calculateExecutionTime(long startTime)
    {
        return getTime() - startTime;
    }


    protected ObjectInstance getOrCreateMBean(Object resource, String method) throws JMException, InvalidTargetObjectTypeException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        synchronized(this)
        {
            String key = ("" + getOwnTenant() + "." + getOwnTenant()).intern();
            ObjectInstance objectInstance = jmxBeans.get(key);
            if(objectInstance == null)
            {
                DynamicMBean mbean = createStandardMBean(resource);
                ProfilingJMXBindableTenantAwareObject profilingJMXBindableTenantAwareObject = new ProfilingJMXBindableTenantAwareObject(this, getOwnTenant());
                profilingJMXBindableTenantAwareObject.setJmxPath(method);
                objectInstance = ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, new ObjectName(profilingJMXBindableTenantAwareObject
                                .getObjectNameString()));
                jmxBeans.put(key, objectInstance);
            }
            return objectInstance;
        }
    }


    protected abstract Tenant getOwnTenant();


    protected ObjectInstance getOrCreateResource(Class template, String profiledMethod) throws InstantiationException, IllegalAccessException, IllegalArgumentException, JMException, InvalidTargetObjectTypeException, InvocationTargetException
    {
        synchronized(this)
        {
            String key = ("" + getOwnTenant() + "." + getOwnTenant()).intern();
            Object result = jmxResources.get(key);
            if(result == null)
            {
                result = template.newInstance();
                jmxResources.put(key, result);
            }
            return getOrCreateMBean(result, profiledMethod);
        }
    }


    public void logExecutionTime(ObjectInstance bean, long executionTime, long calledAt) throws JMException, InvalidTargetObjectTypeException
    {
        ProfilingReportTemplate profilingReportTemplate = MBeanServerInvocationHandler.<ProfilingReportTemplate>newProxyInstance(
                        ManagementFactory.getPlatformMBeanServer(), bean.getObjectName(), ProfilingReportTemplate.class, false);
        profilingReportTemplate.logExecutionTime(executionTime, calledAt);
    }


    public void logException(ObjectInstance bean) throws JMException, InvalidTargetObjectTypeException
    {
        ProfilingReportTemplate profilingReportTemplate = MBeanServerInvocationHandler.<ProfilingReportTemplate>newProxyInstance(
                        ManagementFactory.getPlatformMBeanServer(), bean.getObjectName(), ProfilingReportTemplate.class, false);
        profilingReportTemplate.logException();
    }


    private DynamicMBean createStandardMBean(Object resource) throws JMException, InvalidTargetObjectTypeException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        ModelMBeanInfoSupport filteredInfo = (ModelMBeanInfoSupport)getAssembler().getMBeanInfo(resource, null);
        Constructor<HybrisStandardMBean> constructor = (Constructor)HybrisStandardMBean.class.getConstructors()[0];
        if(ArrayUtils.isEmpty((Object[])resource.getClass().getInterfaces()))
        {
            throw new IllegalArgumentException("Class " + resource.getClass() + " used as template for profiling, should extend interface in order to instantiate StandardMBean ");
        }
        HybrisStandardMBean beanInstance = constructor.newInstance(new Object[] {resource, resource.getClass().getInterfaces()[0]});
        beanInstance.setCustomMBeanInfo(filteredInfo);
        return (DynamicMBean)beanInstance;
    }
}
