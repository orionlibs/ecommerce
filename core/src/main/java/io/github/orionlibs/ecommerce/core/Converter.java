package io.github.orionlibs.ecommerce.core;

/**
 * It converts a data type to another
 * @param <FROM>
 * @param <TO>
 */
public interface Converter<FROM, TO>
{
    TO convert(FROM fromType);
}
