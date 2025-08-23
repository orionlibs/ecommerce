package de.hybris.platform.persistence.polyglot.uow;

import java.util.Optional;

public interface UnitOfWorkProvider
{
    Optional<UnitOfWork> getUnitOfWork();
}
