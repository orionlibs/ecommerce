package de.hybris.platform.webservicescommons.jaxb.util;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ReflectionUtils
{
    public static Collection<Class> extractTypes(Field field)
    {
        Class<?> clazz = field.getType();
        clazz = getArrayType(clazz);
        if(Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz))
        {
            return extractActualType(new Type[] {field.getGenericType()});
        }
        return (Collection)Collections.singleton(clazz);
    }


    public static Collection<Class> extractActualType(Type... types)
    {
        Set<Class<?>> result = new HashSet<>();
        for(Type t : types)
        {
            if(t instanceof ParameterizedType)
            {
                Type[] actualTypeArguments = ((ParameterizedType)t).getActualTypeArguments();
                result.addAll((Collection)extractActualType(actualTypeArguments));
            }
            else if(t instanceof GenericArrayType)
            {
                Type genericComponentType = ((GenericArrayType)t).getGenericComponentType();
                result.addAll((Collection)extractActualType(new Type[] {genericComponentType}));
            }
            else if(t instanceof Class)
            {
                Class<?> c = getArrayType((Class)t);
                result.add(c);
            }
        }
        return (Collection)result;
    }


    public static Class getArrayType(Class clazz)
    {
        if(clazz.isArray())
        {
            return getArrayType(clazz.getComponentType());
        }
        return clazz;
    }


    public static Collection<Field> getAllFields(Class<?> clazz)
    {
        Set<Field> result = new HashSet<>();
        Class<?> c = clazz;
        while(c != null && c != Object.class)
        {
            result.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return result;
    }
}
