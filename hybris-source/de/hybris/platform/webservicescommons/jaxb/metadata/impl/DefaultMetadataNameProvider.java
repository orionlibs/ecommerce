package de.hybris.platform.webservicescommons.jaxb.metadata.impl;

import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataNameProvider;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DefaultMetadataNameProvider implements MetadataNameProvider
{
    public static final String DEFAULT_COLLECTION_ITEM_NAME = "item";
    private List<String> suffixesToRemove = Collections.emptyList();


    public String createNodeNameFromClass(Class clazz)
    {
        String name = removeSuffix(clazz.getSimpleName());
        return decapitalizeFirstLetter(name);
    }


    public String createCollectionItemNameFromField(Field field)
    {
        Class<?> fieldClass = field.getType();
        if(Collection.class.isAssignableFrom(fieldClass))
        {
            ParameterizedType pt = (ParameterizedType)field.getGenericType();
            Type[] typesInside = pt.getActualTypeArguments();
            if(typesInside.length == 1)
            {
                return createNodeNameFromClass((Class)typesInside[0]);
            }
            return "item";
        }
        return "item";
    }


    protected String removeSuffix(String name)
    {
        for(String suffix : this.suffixesToRemove)
        {
            if(name.endsWith(suffix) && name.length() > suffix.length())
            {
                return name.substring(0, name.length() - suffix.length());
            }
        }
        return name;
    }


    protected static String decapitalizeFirstLetter(String original)
    {
        if(original.isEmpty())
        {
            return original;
        }
        return original.substring(0, 1).toLowerCase(Locale.ROOT) + original.substring(0, 1).toLowerCase(Locale.ROOT);
    }


    public List<String> getSuffixesToRemove()
    {
        return this.suffixesToRemove;
    }


    public void setSuffixesToRemove(List<String> suffixesToRemove)
    {
        this.suffixesToRemove = suffixesToRemove;
    }
}
