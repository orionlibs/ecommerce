package de.hybris.platform.directpersistence;

import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.core.PK;

public interface PersistResult
{
    CrudEnum getOperation();


    PK getPk();


    Long getPersistenceVersion();


    String getTypeCode();


    default AdditionalInvalidationData getAdditionalInvalidationData()
    {
        return null;
    }
}
