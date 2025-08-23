package de.hybris.platform.cms2.version.predicate;

import java.util.Date;
import java.util.function.Predicate;
import org.apache.log4j.Logger;

public class DataToModelDatePredicate implements Predicate<String>
{
    private static Logger LOG = Logger.getLogger(DataToModelDatePredicate.class);


    public boolean test(String type)
    {
        try
        {
            Class<?> typeClass = Class.forName(type);
            return Date.class.isAssignableFrom(typeClass);
        }
        catch(ClassNotFoundException e)
        {
            LOG.debug("Unable to find a Class called: " + type, e);
            return false;
        }
    }
}
