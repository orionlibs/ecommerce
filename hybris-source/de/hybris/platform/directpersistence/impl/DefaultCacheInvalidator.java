package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.cache.InvalidationKey;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.tx.Transaction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultCacheInvalidator implements CacheInvalidator
{
    private static final Map<CrudEnum, Integer> operationToInvalidationTypeMapping;

    static
    {
        Map<CrudEnum, Integer> tmp = new HashMap<>();
        tmp.put(CrudEnum.CREATE, Integer.valueOf(4));
        tmp.put(CrudEnum.DELETE, Integer.valueOf(2));
        tmp.put(CrudEnum.UPDATE, Integer.valueOf(1));
        operationToInvalidationTypeMapping = Collections.unmodifiableMap(tmp);
    }

    public void invalidate(Collection<PersistResult> results)
    {
        Transaction tx = Transaction.current();
        for(PersistResult res : results)
        {
            PK thePK = res.getPk();
            CrudEnum operation = res.getOperation();
            int invType = ((Integer)operationToInvalidationTypeMapping.get(operation)).intValue();
            Object[] key = InvalidationKey.entityArrayKey(thePK, res.getAdditionalInvalidationData());
            if(tx.isRunning())
            {
                tx.invalidateFromDirectPersistence(key, thePK, invType);
                continue;
            }
            tx.invalidateAndNotifyCommit(key, 3, invType);
        }
    }
}
