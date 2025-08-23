package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import java.util.Collection;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;

@FunctionalInterface
public interface ExcelMapper<INPUT, OUTPUT> extends Function<INPUT, Collection<OUTPUT>>
{
    default boolean filter(OUTPUT output, Collection<ExcelFilter<OUTPUT>> filters)
    {
        return CollectionUtils.emptyIfNull(filters).stream().allMatch(filter -> filter.test(output));
    }
}
