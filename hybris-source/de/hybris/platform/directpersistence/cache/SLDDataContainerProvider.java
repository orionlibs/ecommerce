package de.hybris.platform.directpersistence.cache;

import de.hybris.platform.core.PK;
import java.util.List;

public interface SLDDataContainerProvider
{
    SLDDataContainer get(PK paramPK);


    List<SLDDataContainer> getAll(List<PK> paramList);
}
