package de.hybris.platform.servicelayer.internal.converter.util;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.util.logging.Logs;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

public final class ModelUtils
{
    private static final Logger LOG = Logger.getLogger(ModelUtils.class);


    public static Object getFieldValue(Object model, String field)
    {
        if(model instanceof AbstractItemModel)
        {
            return ((ItemModelContextImpl)((AbstractItemModel)model).getItemModelContext()).getRawPropertyValue(field);
        }
        Field fieldReflect = ReflectionUtils.findField(model.getClass(), field);
        if(fieldReflect == null)
        {
            LOG.error("Can not find field " + field + " at model " + model.getClass().getName());
            return null;
        }
        fieldReflect.setAccessible(true);
        return ReflectionUtils.getField(fieldReflect, model);
    }


    public static boolean setFieldValue(Object model, String field, Object value)
    {
        if(model instanceof AbstractItemModel && findConstant(model.getClass(), field) != null)
        {
            ((ItemModelContextImpl)((AbstractItemModel)model).getItemModelContext()).setRawPropertyValue(field, value);
            return true;
        }
        Field fieldReflect = ReflectionUtils.findField(model.getClass(), field);
        if(fieldReflect == null)
        {
            LOG.error("Can not find field " + field + " at model " + model.getClass().getName());
            return false;
        }
        fieldReflect.setAccessible(true);
        ReflectionUtils.setField(fieldReflect, model, value);
        return true;
    }


    public static boolean isPrimitiveField(Class model, String field)
    {
        Class fieldType = determineFieldType(model, field);
        return (fieldType != null && fieldType.isPrimitive());
    }


    public static Class getFieldType(Class model, String field)
    {
        return determineFieldType(model, field);
    }


    public static boolean existsMethod(Class model, String method)
    {
        return (ReflectionUtils.findMethod(model, method, (Class[])null) != null);
    }


    public static boolean existsField(Class model, String field)
    {
        boolean fieldExists = (ReflectionUtils.findField(model, field) != null);
        if(fieldExists)
        {
            return true;
        }
        fieldExists = (findConstant(model, field) != null);
        return fieldExists;
    }


    public static boolean enumEquals(HybrisEnumValue enum1, Object obj)
    {
        try
        {
            HybrisEnumValue enum2 = (HybrisEnumValue)obj;
            return (enum1 == enum2 || (enum1 != null && enum2 != null && !enum1.getClass().isEnum() && !enum2.getClass().isEnum() && enum1
                            .getType().equalsIgnoreCase(enum2.getType()) && enum1.getCode().equalsIgnoreCase(enum2.getCode())));
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }


    private static Field findConstant(Class model, String field)
    {
        return ReflectionUtils.findField(model, field.toUpperCase(LocaleHelper.getPersistenceLocale()));
    }


    private static Class determineFieldType(Class model, String field)
    {
        Field fieldReflect = ReflectionUtils.findField(model, field);
        if(fieldReflect == null)
        {
            if(findConstant(model, field) != null)
            {
                Method getter = null;
                PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(model, field);
                if(propertyDescriptor == null || (getter = propertyDescriptor.getReadMethod()) == null)
                {
                    Logs.debug(LOG, () -> "no getter for field: " + field);
                }
                return (getter == null) ? null : getter.getReturnType();
            }
            LOG.error("Can not find field " + field + " at model " + model.getName());
            return null;
        }
        return fieldReflect.getType();
    }
}
