package de.hybris.platform.cms2.common.functions;

@FunctionalInterface
public interface Converter<S, T>
{
    T convert(S paramS);
}
