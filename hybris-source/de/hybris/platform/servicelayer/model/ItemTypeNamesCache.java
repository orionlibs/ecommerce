package de.hybris.platform.servicelayer.model;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.ReflectionUtils;

class ItemTypeNamesCache
{
    private static final ConcurrentHashMap<String, String> classNameToItemTypeName = new ConcurrentHashMap<>();


    public String getItemTypeName(Class<?> modelClass)
    {
        Objects.requireNonNull(modelClass, "modelClass mustn't be null");
        return classNameToItemTypeName.computeIfAbsent(modelClass.getName(), s -> getItemTypeNameInternal(modelClass));
    }


    private String getItemTypeNameInternal(Class<?> modelClass)
    {
        Field typecodeField = ReflectionUtils.findField(modelClass, "_TYPECODE");
        if(typecodeField == null)
        {
            String modelClassName = modelClass.getSimpleName();
            if(modelClassName.length() <= "Model".length() || !modelClassName.endsWith("Model"))
            {
                return modelClassName;
            }
            return modelClassName.substring(0, modelClassName.length() - "Model".length());
        }
        try
        {
            return (String)typecodeField.get(null);
        }
        catch(IllegalArgumentException | IllegalAccessException e)
        {
            throw new IllegalStateException("Unexpected error when obtaining _TYPECODE value from class " + modelClass, e);
        }
    }
}
