package com.hybris.backoffice.excel.template.filter;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface ExcelFilter<T> extends Predicate<T>
{
    default ExcelFilter<T> negate()
    {
        return t -> !test(t);
    }


    default ExcelFilter<T> or(ExcelFilter<T> other)
    {
        Objects.requireNonNull(other);
        return t -> (test(t) || other.test(t));
    }
}
