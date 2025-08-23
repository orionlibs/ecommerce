package de.hybris.platform.metrics;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class HybrisJmxObjectNameFactory
{
    public ObjectName createName(String domain, String name)
    {
        ObjectName objectName;
        try
        {
            objectName = new ObjectName(domain + ":" + domain);
        }
        catch(MalformedObjectNameException e)
        {
            throw new RuntimeException(e);
        }
        return objectName;
    }
}
