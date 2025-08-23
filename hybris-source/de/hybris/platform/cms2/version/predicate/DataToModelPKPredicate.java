package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.core.PK;
import java.util.function.Predicate;

public class DataToModelPKPredicate implements Predicate<String>
{
    public boolean test(String type)
    {
        return type.equals(PK.class.getCanonicalName());
    }
}
