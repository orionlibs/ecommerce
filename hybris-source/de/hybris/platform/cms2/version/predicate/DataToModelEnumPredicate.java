package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.function.Predicate;
import org.apache.log4j.Logger;

public class DataToModelEnumPredicate implements Predicate<String>
{
    private static Logger LOG = Logger.getLogger(DataToModelEnumPredicate.class);


    public boolean test(String type)
    {
        try
        {
            Class<?> typeClass = Class.forName(type);
            return HybrisEnumValue.class.isAssignableFrom(typeClass);
        }
        catch(ClassNotFoundException e)
        {
            LOG.debug("Unable to find a Class called: " + type, e);
            return false;
        }
    }
}
