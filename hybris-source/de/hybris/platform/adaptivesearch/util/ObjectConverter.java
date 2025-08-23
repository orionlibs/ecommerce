package de.hybris.platform.adaptivesearch.util;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.AsRuntimeException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class ObjectConverter
{
    protected static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final Map<String, Method> CONVERTERS = new HashMap<>();

    static
    {
        Method[] methods = ObjectConverter.class.getDeclaredMethods();
        for(Method method : methods)
        {
            if((method.getParameterTypes()).length == 1)
            {
                Class<?> sourceClass = method.getParameterTypes()[0];
                Class<?> targetClass = method.getReturnType();
                String converterKey = generateConverterKey(sourceClass, targetClass);
                CONVERTERS.put(converterKey, method);
            }
        }
    }

    protected static String generateConverterKey(Class<?> sourceClass, Class<?> targetClass)
    {
        return sourceClass.getCanonicalName() + "_" + sourceClass.getCanonicalName();
    }


    public static <T> T convert(Object value, Class<T> targetClass) throws AsException
    {
        if(targetClass == null)
        {
            throw new IllegalArgumentException("targetClass cannot be null");
        }
        if(value == null)
        {
            return null;
        }
        Class<?> sourceClass = value.getClass();
        if(targetClass.isAssignableFrom(sourceClass))
        {
            return (T)value;
        }
        String converterKey = generateConverterKey(sourceClass, targetClass);
        Method converter = CONVERTERS.get(converterKey);
        if(converter == null)
        {
            throw new AsException("Cannot find converter from " + sourceClass
                            .getCanonicalName() + " to " + targetClass.getCanonicalName());
        }
        try
        {
            return (T)converter.invoke(targetClass, new Object[] {value});
        }
        catch(RuntimeException | IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            throw new AsException("Cannot convert value " + value + " from " + sourceClass.getCanonicalName() + " to " + targetClass
                            .getCanonicalName(), e);
        }
    }


    public static String booleanToString(Boolean value)
    {
        return value.toString();
    }


    public static Boolean stringToBoolean(String value)
    {
        return Boolean.valueOf(value);
    }


    public static String shortToString(Short value)
    {
        return value.toString();
    }


    public static Short stringToShort(String value)
    {
        return Short.valueOf(value);
    }


    public static String integerToString(Integer value)
    {
        return value.toString();
    }


    public static Integer stringToInteger(String value)
    {
        return Integer.valueOf(value);
    }


    public static String longToString(Long value)
    {
        return value.toString();
    }


    public static Long stringToLong(String value)
    {
        return Long.valueOf(value);
    }


    public static String floatToString(Float value)
    {
        return value.toString();
    }


    public static Float stringToFloat(String value)
    {
        return Float.valueOf(value);
    }


    public static String doubleToString(Double value)
    {
        return value.toString();
    }


    public static Double stringToDouble(String value)
    {
        return Double.valueOf(value);
    }


    public static String bigIntegerToString(BigInteger value)
    {
        return value.toString();
    }


    public static BigInteger stringToBigInteger(String value)
    {
        return new BigInteger(value);
    }


    public static String bigDecimalToString(BigDecimal value)
    {
        return value.toString();
    }


    public static BigDecimal stringToBigDecimal(String value)
    {
        return new BigDecimal(value);
    }


    public static String dateToString(Date value)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return dateFormat.format(value);
    }


    public static Date stringToDate(String value)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return dateFormat.parse(value);
        }
        catch(ParseException e)
        {
            throw new AsRuntimeException(e);
        }
    }
}
