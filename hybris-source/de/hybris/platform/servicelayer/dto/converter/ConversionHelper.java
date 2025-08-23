package de.hybris.platform.servicelayer.dto.converter;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;

public final class ConversionHelper
{
    static final Map<String, PropertyDescriptor[]> propertyDescriptorCache = (Map)new ConcurrentHashMap<>();


    public static void copyProperties(Object source, Object target) throws ConversionException
    {
        ServicesUtil.validateParameterNotNull(source, "source must not be null");
        ServicesUtil.validateParameterNotNull(target, "target must not be null");
        Class<?> targetClass = target.getClass();
        PropertyDescriptor[] targetPds = propertyDescriptorCache.get(targetClass.getName());
        if(targetPds == null)
        {
            targetPds = BeanUtils.getPropertyDescriptors(targetClass);
            propertyDescriptorCache.put(targetClass.getName(), targetPds);
        }
        for(int i = 0; i < targetPds.length; i++)
        {
            PropertyDescriptor targetPd = targetPds[i];
            if(targetPd.getWriteMethod() != null)
            {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if(sourcePd != null && sourcePd.getReadMethod() != null)
                {
                    try
                    {
                        Method readMethod = sourcePd.getReadMethod();
                        if(!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()))
                        {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);
                        Method writeMethod = targetPd.getWriteMethod();
                        if(!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()))
                        {
                            writeMethod.setAccessible(true);
                        }
                        Object[] args = {value};
                        try
                        {
                            writeMethod.invoke(target, args);
                        }
                        catch(IllegalArgumentException illegalArgumentException)
                        {
                        }
                    }
                    catch(IllegalArgumentException e)
                    {
                        throw new ConversionException(e.getMessage(), e);
                    }
                    catch(IllegalAccessException e)
                    {
                        throw new ConversionException(e.getMessage(), e);
                    }
                    catch(InvocationTargetException e)
                    {
                        throw new ConversionException(e.getMessage(), e);
                    }
                }
            }
        }
    }


    public static <SRC, DEST, COLL extends Collection<DEST>> COLL convertAll(Collection<SRC> src, Converter<SRC, DEST> converter, COLL target)
    {
        for(SRC srcElem : src)
        {
            DEST destElem = (DEST)converter.convert(srcElem);
            target.add(destElem);
        }
        return target;
    }
}
