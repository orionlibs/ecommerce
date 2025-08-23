package de.hybris.bootstrap.typesystem;

import de.hybris.platform.core.HybrisEnumValue;
import java.io.InvalidClassException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;

public class HybrisDynamicEnumValueSerializedForm implements Serializable
{
    private static final Logger LOG = Logger.getLogger(HybrisDynamicEnumValueSerializedForm.class);
    public static final String VALUE_OF = "valueOf";
    private static final long serialVersionUID = 0L;
    private final Class<? extends HybrisEnumValue> enumClass;
    private final String value;


    public HybrisDynamicEnumValueSerializedForm(Class<? extends HybrisEnumValue> enumClass, String value)
    {
        this.enumClass = enumClass;
        this.value = value;
    }


    private Object readResolve() throws ObjectStreamException
    {
        Method[] methods = this.enumClass.getDeclaredMethods();
        for(Method method : methods)
        {
            if(isValueOfMethod(method))
            {
                try
                {
                    return method.invoke(this.enumClass, new Object[] {this.value});
                }
                catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
                {
                    LOG.error(String.format("Unable to deserialize class %s with value %s", new Object[] {this.enumClass.getCanonicalName(), this.value}), e);
                    throw new InvalidClassException(e.getMessage());
                }
            }
        }
        return null;
    }


    private static boolean isValueOfMethod(Method method)
    {
        return (method.getName().equals("valueOf") && method.getParameterCount() == 1 && method
                        .getParameterTypes()[0].equals(String.class));
    }
}
