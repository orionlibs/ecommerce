package de.hybris.platform.webservicescommons.util;

import java.util.function.Supplier;

public interface LocalViewExecutor
{
    <T> T executeInLocalView(Supplier<T> paramSupplier);


    <T> T executeWithAllCatalogs(Supplier<T> paramSupplier);
}
