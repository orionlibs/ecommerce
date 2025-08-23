package de.hybris.platform.directpersistence;

import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import java.util.Collection;

public interface WritePersistenceGateway
{
    Collection<PersistResult> persist(ChangeSet paramChangeSet) throws ModelPersistenceException;
}
