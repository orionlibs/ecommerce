package de.hybris.platform.cms2.common.functions;

import java.util.Optional;
import java.util.function.Function;

public interface ChainFunction<SOURCE_TYPE, TARGET_TYPE> extends Function<SOURCE_TYPE, Optional<TARGET_TYPE>>
{
    default ChainFunction<SOURCE_TYPE, TARGET_TYPE> orElse(ChainFunction<SOURCE_TYPE, TARGET_TYPE> orElse)
    {
        return value -> {
            Optional<TARGET_TYPE> apply = (Optional<TARGET_TYPE>)apply(value);
            return apply.isPresent() ? apply : (Optional)orElse.apply(value);
        };
    }
}
