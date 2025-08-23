package de.hybris.platform.directpersistence.statement;

import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.persistence.property.TypeInfoMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractStoreStatementsBuilder
{
    protected TypeInfoMap infoMap;
    protected Set<PersistResult> persistResults = new HashSet<>();


    public Set<PersistResult> getPersistResults()
    {
        return this.persistResults;
    }
}
