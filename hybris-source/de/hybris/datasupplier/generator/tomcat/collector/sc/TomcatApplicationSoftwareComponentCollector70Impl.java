package de.hybris.datasupplier.generator.tomcat.collector.sc;

import de.hybris.datasupplier.generator.tomcat.data.TomcatWebModule;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class TomcatApplicationSoftwareComponentCollector70Impl extends TomcatApplicationSoftwareComponentCollectorImpl
{
    protected TomcatWebModule buildWebModule(ObjectName webModuleName) throws ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        return buildWebModule70(webModuleName);
    }
}
