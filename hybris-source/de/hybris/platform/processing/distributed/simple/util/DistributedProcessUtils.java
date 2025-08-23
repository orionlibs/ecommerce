package de.hybris.platform.processing.distributed.simple.util;

import com.google.common.base.Preconditions;

public class DistributedProcessUtils
{
    public static <T> T assureInstanceOf(Object object, Class<T> expctedClass)
    {
        if(object == null)
        {
            return null;
        }
        Preconditions.checkArgument(expctedClass.isInstance(object), "object %s is not instance of %s", object, expctedClass);
        return (T)object;
    }
}
