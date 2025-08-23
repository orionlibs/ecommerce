package de.hybris.platform.omsbackoffice.renderers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.WordUtils;

public class NestedAttributeUtils
{
    public static final String GETTER = "get";
    public static final String FAIL_TO_FIND_COLLECTION_ITEM = "Failed to find collection item for property ";
    private static final Pattern COLLECTION_PATTERN = Pattern.compile("^([^\\[]+)\\[(\\d+)\\]$");
    private static final String END_BRACKET = "]";
    private static final int VALID_GROUP_COUNT = 2;


    public String propertyNameToGetter(String propertyName)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("get");
        builder.append(WordUtils.capitalize(propertyName));
        return builder.toString();
    }


    public List<String> splitQualifier(String qualifier)
    {
        String[] tokenMap = qualifier.split("\\.");
        return Arrays.asList(tokenMap);
    }


    public Object getNestedObject(Object object, String propertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidNestedAttributeException
    {
        if(propertyName.endsWith("]"))
        {
            return getArrayItem(object, propertyName);
        }
        return invokePropertyAsMethod(object, propertyName);
    }


    public String getNameOfClassWithoutModel(Object object)
    {
        String objectClass = object.getClass().getSimpleName();
        String classWithoutModel = objectClass.substring(0, objectClass.length() - 5);
        return classWithoutModel;
    }


    public Object getArrayItem(Object object, String propertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InvalidNestedAttributeException
    {
        Matcher matcher = COLLECTION_PATTERN.matcher(propertyName);
        if(!matcher.matches() || matcher.groupCount() != 2)
        {
            throw new InvalidNestedAttributeException("Failed to find collection item for property " + propertyName);
        }
        String collectionProperty = matcher.group(1);
        int index = Integer.parseInt(matcher.group(2));
        Object collection = invokePropertyAsMethod(object, collectionProperty);
        if(!(collection instanceof Collection))
        {
            throw new InvalidNestedAttributeException("Failed to find collection item for property " + propertyName);
        }
        int counter = 0;
        Iterator iterator = ((Collection)collection).iterator();
        while(iterator.hasNext())
        {
            if(counter == index)
            {
                return iterator.next();
            }
            iterator.next();
            counter++;
        }
        throw new InvalidNestedAttributeException("Failed to find collection item for property " + propertyName);
    }


    public Object invokePropertyAsMethod(Object object, String propertyName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Class<?> objectClass = object.getClass();
        Method getter = objectClass.getMethod(propertyNameToGetter(propertyName), new Class[0]);
        return getter.invoke(object, null);
    }
}
