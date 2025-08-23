package de.hybris.platform.servicelayer.internal.polyglot;

import de.hybris.platform.core.PK;
import java.util.function.Supplier;

public interface ServiceLayerPersistenceInterceptor
{
    void updateFromServiceLayer(PK paramPK, Runnable paramRunnable);


    <T> T createFromServiceLayer(Supplier<T> paramSupplier);


    void removeFromServiceLayer(PK paramPK, Runnable paramRunnable);
}
