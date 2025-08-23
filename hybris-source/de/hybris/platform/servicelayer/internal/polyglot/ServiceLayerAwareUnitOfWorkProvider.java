package de.hybris.platform.servicelayer.internal.polyglot;

import de.hybris.platform.persistence.polyglot.uow.UnitOfWork;
import de.hybris.platform.persistence.polyglot.uow.UnitOfWorkProvider;
import java.util.Optional;

class ServiceLayerAwareUnitOfWorkProvider implements UnitOfWorkProvider
{
    public Optional<UnitOfWork> getUnitOfWork()
    {
        return getUnitOfWorkHolder().getCurrentUnitOfWork();
    }


    private UnitOfWorkHolder getUnitOfWorkHolder()
    {
        return PolyglotPersistenceServiceLayerSupport.getUnitOfWorkHolder();
    }
}
