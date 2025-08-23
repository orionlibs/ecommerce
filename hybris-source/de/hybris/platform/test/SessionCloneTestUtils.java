package de.hybris.platform.test;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.Assert;
import org.springframework.util.SerializationUtils;

public final class SessionCloneTestUtils
{
    public static void assertClonedContextAttributesEqual(Map<String, Object> original, Map<String, Object> serialized)
    {
        for(Map.Entry<String, Object> originalEntry : original.entrySet())
        {
            String originalKey = originalEntry.getKey();
            Object originalValue = originalEntry.getValue();
            Assert.assertTrue(serialized.containsKey(originalKey));
            Object clonedValue = serialized.get(originalKey);
            if(!isSameOrEqual(originalValue, clonedValue))
            {
                assertSameClassNoEquals(originalValue, clonedValue);
            }
        }
    }


    public static void assertSameClassNoEquals(Object o1, Object o2)
    {
        Assert.assertNotNull(o1);
        Assert.assertNotNull(o2);
        Assert.assertEquals(o1.getClass(), o2.getClass());
        try
        {
            Method equalsMethod = o1.getClass().getMethod("equals", new Class[] {Object.class});
            Assert.assertEquals(Object.class, equalsMethod.getDeclaringClass());
        }
        catch(SecurityException e)
        {
            Assert.fail(e.getMessage());
        }
        catch(NoSuchMethodException e)
        {
            Assert.fail(e.getMessage());
        }
    }


    public static boolean isSameOrEqual(Object o1, Object o2)
    {
        return (o1 == o2 || (o1 != null && o1.equals(o2)));
    }


    public static <T> T cloneViaSerialization(T source)
    {
        return (T)deserialize(serialize(source));
    }


    public static byte[] serialize(Object source)
    {
        return SerializationUtils.serialize(source);
    }


    public static Object deserialize(byte[] bytes)
    {
        return SerializationUtils.deserialize(bytes);
    }
}
