package de.hybris.platform.testframework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.collections.set.UnmodifiableSet;

public class Assert
{
    public static void assertCollectionElements(Collection actual, Object... objects)
    {
        assertCollection(null, Arrays.asList(objects), actual);
    }


    public static void assertCollection(Collection expected, Collection actual)
    {
        assertCollection(null, expected, actual);
    }


    public static <T extends Comparable> void assertValueInRange(T lowerLimit, T upperLimit, T actual)
    {
        assertValueInRange("", lowerLimit, upperLimit, actual);
    }


    public static <T extends Comparable> void assertValueInRange(String message, T lowerLimit, T upperLimit, T actual)
    {
        if(lowerLimit.compareTo(actual) > 0 || upperLimit.compareTo(actual) < 0)
        {
            org.junit.Assert.fail(message + " expected in range (<" + message + ">,<" + lowerLimit + "> ) but was:<" + upperLimit + ">");
        }
    }


    public static void assertCollection(String message, Collection<?> expected, Collection<?> actual)
    {
        if(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected))
        {
            return;
        }
        String formatted = "";
        if(message != null)
        {
            formatted = message + " ";
        }
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if(expectedString.equals(actualString))
        {
            expectedString = expectedString + " (" + expectedString + ")";
            actualString = actualString + " (" + actualString + ")";
        }
        org.junit.Assert.fail(formatted + "expected:<" + formatted + "> but was:<" + expectedString + ">");
    }


    private static String getCollectionAndElementClassNames(Collection coll)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(coll.getClass().getName());
        buffer.append(" containing ");
        for(Iterator iter = coll.iterator(); iter.hasNext(); )
        {
            Object nextObject = iter.next();
            buffer.append((nextObject == null) ? "null" : nextObject.getClass().getName());
            if(iter.hasNext())
            {
                buffer.append(", ");
            }
        }
        return buffer.toString();
    }


    protected <T> T singleElement(Collection<T> collection)
    {
        org.junit.Assert.assertEquals("found " + collection, 1L, collection.size());
        return collection.iterator().next();
    }


    public static final Set set()
    {
        return Collections.EMPTY_SET;
    }


    public static final Set set(Object object)
    {
        return Collections.singleton(object);
    }


    public static final Set set(Object object1, Object object2)
    {
        return UnmodifiableSet.decorate(new HashSet(Arrays.asList(new Object[] {object1, object2})));
    }


    public static final Set set(Object object1, Object object2, Object object3)
    {
        return UnmodifiableSet.decorate(new HashSet(Arrays.asList(new Object[] {object1, object2, object3})));
    }


    public static final Set set(Object object1, Object object2, Object object3, Object object4)
    {
        return UnmodifiableSet.decorate(new LinkedHashSet(Arrays.asList(new Object[] {object1, object2, object3, object4})));
    }


    public static final <T> List<T> toList(Collection<T> collection)
    {
        return new ArrayList<>(collection);
    }


    public static final List list()
    {
        return Collections.EMPTY_LIST;
    }


    public static final List list(Object object)
    {
        return Collections.singletonList(object);
    }


    public static final List list(Object object1, Object object2)
    {
        return UnmodifiableList.decorate(Arrays.asList(new Object[] {object1, object2}));
    }


    public static final List list(Object object1, Object object2, Object object3)
    {
        return UnmodifiableList.decorate(Arrays.asList(new Object[] {object1, object2, object3}));
    }


    public static final List list(Object object1, Object object2, Object object3, Object object4)
    {
        return UnmodifiableList.decorate(Arrays.asList(new Object[] {object1, object2, object3, object4}));
    }


    public static void assertEquals(Object expected, Object actual)
    {
        if(expected == null && actual == null)
        {
            return;
        }
        org.junit.Assert.assertNotNull(expected);
        org.junit.Assert.assertNotNull(actual);
        org.junit.Assert.assertTrue(expected.equals(actual));
        org.junit.Assert.assertTrue(actual.equals(expected));
        org.junit.Assert.assertEquals(expected.hashCode(), actual.hashCode());
    }


    public static void assertNotEquals(Object expected, Object actual)
    {
        assertNotEquals(null, expected, actual);
    }


    public static void assertNotEquals(String message, Object expected, Object actual)
    {
        if((expected == null && actual == null) || (expected != null && expected.equals(actual)) || (actual != null && actual
                        .equals(expected)))
        {
            failNotEquals(message, expected);
        }
    }


    private static void failNotEquals(String message, Object expected)
    {
        String formatted = "";
        if(message != null)
        {
            formatted = message + " ";
        }
        org.junit.Assert.fail(formatted + "expected not equals to: <" + formatted + ">");
    }
}
