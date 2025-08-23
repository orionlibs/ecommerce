package de.hybris.platform.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Constants
{
    static
    {
        checkUnique();
    }

    public static void checkUnique()
    {
        checkUnique(TC.class);
    }


    private static void checkUnique(Class clazz)
    {
        Field[] fields = clazz.getFields();
        Map<Object, Object> valueToField = new HashMap<>();
        for(int i = 0; i < fields.length; i++)
        {
            try
            {
                Field oldField = (Field)valueToField.put(fields[i].get(null), fields[i]);
                if(oldField != null)
                {
                    throw new RuntimeException("duplicate value " + fields[i].get(null) + " found in " + clazz.getName() + "." + fields[i]
                                    .getName() + " and " + clazz.getName() + "." + oldField.getName());
                }
            }
            catch(IllegalArgumentException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static final String DISABLE_CYCLIC_CHECKS = "disableCyclicChecks".intern();
    public static final String DB_DETECT_CONNECTION_LOST = "db.detect.connection.errors";
    public static final boolean DB_DETECT_CONNECTION_LOST_DEFAULT = true;
}
