package de.hybris.platform.personalizationfacades.converters;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public interface ConfigurableConverter<SOURCE, TARGET, OPTION> extends Converter<SOURCE, TARGET>
{
    default TARGET convert(SOURCE source)
    {
        return convert(source, getTargetInstance(source));
    }


    default TARGET convert(SOURCE source, TARGET prototype)
    {
        return convert(source, prototype, getDefaultOptions());
    }


    default TARGET convert(SOURCE source, Collection<OPTION> options)
    {
        return convert(source, getTargetInstance(source), options);
    }


    TARGET convert(SOURCE paramSOURCE, TARGET paramTARGET, Collection<OPTION> paramCollection);


    List<TARGET> convertAll(Collection<? extends SOURCE> sources, OPTION... options)
    {
        if(options != null)
        {
            return convertAll(sources, Arrays.asList(options));
        }
        return Collections.emptyList();
    }


    default List<TARGET> convertAll(Collection<? extends SOURCE> sources, Collection<OPTION> options)
    {
        if(CollectionUtils.isEmpty(sources) || CollectionUtils.isEmpty(options))
        {
            return Collections.emptyList();
        }
        return (List<TARGET>)sources.stream().map(s -> convert((SOURCE)s, options)).filter(Objects::nonNull).collect(Collectors.toList());
    }


    TARGET getTargetInstance(SOURCE paramSOURCE);


    Collection<OPTION> getDefaultOptions();
}
