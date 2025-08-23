package de.hybris.platform.servicelayer.internal.polyglot;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import java.util.Objects;
import java.util.function.Supplier;

class UnitOfWorkInterceptor implements ServiceLayerPersistenceInterceptor
{
    private final UnitOfWorkHolder unitOfWorkHolder;


    public UnitOfWorkInterceptor(UnitOfWorkHolder unitOfWorkHolder)
    {
        this.unitOfWorkHolder = Objects.<UnitOfWorkHolder>requireNonNull(unitOfWorkHolder);
    }


    public void updateFromServiceLayer(PK pk, Runnable updateAction)
    {
        execute(pk, toSupplier(updateAction));
    }


    public <T> T createFromServiceLayer(Supplier<T> createAction)
    {
        return execute(PK.NULL_PK, createAction);
    }


    public void removeFromServiceLayer(PK pk, Runnable removeAction)
    {
        execute(pk, toSupplier(removeAction));
    }


    private <T> T execute(PK pk, Supplier<T> action)
    {
        if(!isUnitOfWorkRequired())
        {
            return action.get();
        }
        UnitOfWorkHolder.Scope scope = this.unitOfWorkHolder.requireNew();
        try
        {
            Long requiredVersion = HJMPUtils.getVersionForPk(pk);
            if(requiredVersion != null)
            {
                scope.requireVersion(PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toPolyglotLayer(pk), requiredVersion.longValue());
            }
            T result = action.get();
            scope.markAsSucceeded();
            T t1 = result;
            if(scope != null)
            {
                scope.close();
            }
            return t1;
        }
        catch(Throwable throwable)
        {
            if(scope != null)
            {
                try
                {
                    scope.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    private boolean isUnitOfWorkRequired()
    {
        return !this.unitOfWorkHolder.isUnitOfWorkActivated();
    }


    private static Supplier<Void> toSupplier(Runnable runnable)
    {
        return () -> {
            runnable.run();
            return null;
        };
    }
}
