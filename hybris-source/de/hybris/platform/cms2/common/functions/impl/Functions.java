package de.hybris.platform.cms2.common.functions.impl;

import de.hybris.platform.cms2.common.functions.ChainFunction;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Functions
{
    public static <SOURCE, TARGET> ChainFunction<SOURCE, TARGET> ofSupplierConstrainedBy(Supplier<TARGET> supplier, Predicate<SOURCE> conditionalTo)
    {
        return source -> conditionalTo.test(source) ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }
}
