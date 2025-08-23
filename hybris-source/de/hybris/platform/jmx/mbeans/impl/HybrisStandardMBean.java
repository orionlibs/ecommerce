package de.hybris.platform.jmx.mbeans.impl;

import javax.management.MBeanInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class HybrisStandardMBean<T> extends StandardMBean
{
    private MBeanInfo mbeanInfo = null;


    public HybrisStandardMBean(T implementation, Class<T> mbeanInterface) throws NotCompliantMBeanException
    {
        super(implementation, mbeanInterface);
    }


    public void setCustomMBeanInfo(MBeanInfo info)
    {
        this.mbeanInfo = info;
    }


    public MBeanInfo getMBeanInfo()
    {
        return this.mbeanInfo;
    }


    protected MBeanInfo getCachedMBeanInfo()
    {
        return this.mbeanInfo;
    }
}
