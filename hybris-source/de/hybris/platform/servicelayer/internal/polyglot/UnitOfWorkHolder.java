package de.hybris.platform.servicelayer.internal.polyglot;

import de.hybris.platform.persistence.polyglot.uow.UnitOfWork;
import java.util.Optional;

class UnitOfWorkHolder
{
    private static final ThreadLocal<Scope> CURRENT_SCOPE = new ThreadLocal<>();


    Scope requireNew()
    {
        if(getCurrentScope().isPresent())
        {
            throw new IllegalStateException("Another scope is already running.");
        }
        Scope newScope = new Scope(new UnitOfWork());
        CURRENT_SCOPE.set(newScope);
        return newScope;
    }


    boolean isUnitOfWorkActivated()
    {
        return getCurrentScope().isPresent();
    }


    Optional<UnitOfWork> getCurrentUnitOfWork()
    {
        return getCurrentScope().map(s -> s.uow);
    }


    private Optional<Scope> getCurrentScope()
    {
        return Optional.ofNullable(CURRENT_SCOPE.get());
    }
}
