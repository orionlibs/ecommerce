package de.hybris.platform.directpersistence;

import java.util.Collection;

public interface CacheInvalidator
{
    void invalidate(Collection<PersistResult> paramCollection);
}
