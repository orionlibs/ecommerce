package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.jmx.mbeans.FlexibleQueryMBean;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Overview of the hybris platform flexible query cache.")
public class FlexibleQueryMBeanImpl extends AbstractJMXMBean implements FlexibleQueryMBean
{
    @ManagedAttribute(description = "Maximum entities size of the flexible query.")
    public Integer getMaxSize()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current entities size of the flexible query.")
    public Integer getCurrentSize()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current entities size of the flexible query in percent.")
    public Integer getCurrentSizeInPercent()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }
}
