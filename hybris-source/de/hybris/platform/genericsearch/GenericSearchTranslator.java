package de.hybris.platform.genericsearch;

import de.hybris.platform.core.GenericQuery;
import java.util.Map;

public class GenericSearchTranslator
{
    public static String translate(GenericQuery genericQuery, Map values)
    {
        if(genericQuery.isTranslatableToPolyglotDialect())
        {
            return genericQuery.toPolyglotSearch(values);
        }
        return genericQuery.toFlexibleSearch(values);
    }
}
