package de.hybris.platform.servicelayer.dto.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public interface Converter<SOURCE, TARGET> extends Converter<SOURCE, TARGET>
{
    TARGET convert(SOURCE paramSOURCE) throws ConversionException;


    TARGET convert(SOURCE paramSOURCE, TARGET paramTARGET) throws ConversionException;


    default List<TARGET> convertAll(Collection<? extends SOURCE> sources) throws ConversionException
    {
        if(CollectionUtils.isEmpty(sources))
        {
            return Collections.emptyList();
        }
        List<TARGET> result = new ArrayList<>(sources.size());
        for(SOURCE source : sources)
        {
            result.add(convert(source));
        }
        return result;
    }


    default List<TARGET> convertAllIgnoreExceptions(Collection<? extends SOURCE> sources)
    {
        if(CollectionUtils.isEmpty(sources))
        {
            return Collections.emptyList();
        }
        List<TARGET> targets = new ArrayList<>(sources.size());
        for(SOURCE source : sources)
        {
            try
            {
                targets.add(convert(source));
            }
            catch(ConversionException ex)
            {
                getLogger().warn("Exception while converting object!", (Throwable)ex);
            }
        }
        return targets;
    }


    default Logger getLogger()
    {
        return Logger.getLogger(getClass());
    }
}
