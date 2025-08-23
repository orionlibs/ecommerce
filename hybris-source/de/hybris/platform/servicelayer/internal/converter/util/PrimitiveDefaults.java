package de.hybris.platform.servicelayer.internal.converter.util;

import java.util.Arrays;
import java.util.List;

public class PrimitiveDefaults
{
    private static final List<Class> PRIMITIVE_TYPES = Arrays.asList(new Class[] {byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class});
    public static boolean DEFAULT_BOOLEAN;
    public static byte DEFAULT_BYTE;
    public static short DEFAULT_SHORT;
    public static int DEFAULT_INT;
    public static char DEFAULT_CHAR;
    public static long DEFAULT_LONG;
    public static float DEFAULT_FLOAT;
    public static double DEFAULT_DOUBLE;


    public static Object getDefaultValue(Class clazz)
    {
        if(clazz.equals(char.class))
        {
            return Character.valueOf(DEFAULT_CHAR);
        }
        if(clazz.equals(boolean.class))
        {
            return Boolean.valueOf(DEFAULT_BOOLEAN);
        }
        if(clazz.equals(byte.class))
        {
            return Byte.valueOf(DEFAULT_BYTE);
        }
        if(clazz.equals(short.class))
        {
            return Short.valueOf(DEFAULT_SHORT);
        }
        if(clazz.equals(int.class))
        {
            return Integer.valueOf(DEFAULT_INT);
        }
        if(clazz.equals(long.class))
        {
            return Long.valueOf(DEFAULT_LONG);
        }
        if(clazz.equals(float.class))
        {
            return Float.valueOf(DEFAULT_FLOAT);
        }
        if(clazz.equals(double.class))
        {
            return Double.valueOf(DEFAULT_DOUBLE);
        }
        throw new IllegalArgumentException("Class type " + clazz + " not supported");
    }


    public static boolean isPrimitive(Class<?> clazz)
    {
        return PRIMITIVE_TYPES.contains(clazz);
    }
}
