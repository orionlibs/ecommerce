package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.PersistResult;
import java.util.Objects;

public class PersistResultWithAdditionalInvalidationData implements PersistResult
{
    private final PersistResult target;
    private final AdditionalInvalidationData data;


    public PersistResultWithAdditionalInvalidationData(PersistResult target, AdditionalInvalidationData data)
    {
        this.target = Objects.<PersistResult>requireNonNull(target);
        this.data = Objects.<AdditionalInvalidationData>requireNonNull(data);
    }


    public CrudEnum getOperation()
    {
        return this.target.getOperation();
    }


    public PK getPk()
    {
        return this.target.getPk();
    }


    public Long getPersistenceVersion()
    {
        return this.target.getPersistenceVersion();
    }


    public String getTypeCode()
    {
        return this.target.getTypeCode();
    }


    public AdditionalInvalidationData getAdditionalInvalidationData()
    {
        return this.data;
    }
}
