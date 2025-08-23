package de.hybris.platform.cms2.common.functions.impl;

import de.hybris.platform.cms2.common.functions.ThrowableSupplier;
import java.util.function.Supplier;

public class FunctionExceptionHandler
{
    public static <R> Supplier<R> supplier(ThrowableSupplier<R> supplier)
    {
        return () -> {
            try
            {
                return supplier.get();
            }
            catch(Throwable ex)
            {
                throwAsUnchecked(ex);
                return null;
            }
        };
    }


    static <T extends Throwable> void throwAsUnchecked(Throwable throwable) throws T
    {
        throw (T)throwable;
    }
}
