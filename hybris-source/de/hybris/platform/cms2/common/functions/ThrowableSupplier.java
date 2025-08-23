package de.hybris.platform.cms2.common.functions;

@FunctionalInterface
public interface ThrowableSupplier<T>
{
    T get() throws Throwable;
}
