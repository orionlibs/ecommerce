package de.hybris.platform.adaptivesearch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public interface ContextAwareConverter<S, T, C>
{
    T convert(S paramS, C paramC);


    default List<T> convertAll(Collection<? extends S> sources, C context)
    {
        if(CollectionUtils.isEmpty(sources))
        {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(sources.size());
        for(S source : sources)
        {
            result.add(convert(source, context));
        }
        return result;
    }
}
