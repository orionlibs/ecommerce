package de.hybris.platform.spring;

import org.springframework.util.ClassUtils;

public class CGLibUtils
{
    public static Class getOriginalClass(Class clazz)
    {
        if(ClassUtils.isCglibProxyClass(clazz))
        {
            return clazz.getSuperclass();
        }
        return clazz;
    }


    public static boolean isAssignableFrom(Class clazz1, Class<?> clazz2)
    {
        clazz1 = getOriginalClass(clazz1);
        clazz2 = getOriginalClass(clazz2);
        return clazz1.isAssignableFrom(clazz2);
    }
}
