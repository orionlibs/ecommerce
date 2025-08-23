package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import java.util.List;

public interface SLDItemDAO
{
    SLDDataContainer load(PK paramPK);


    List<SLDDataContainer> load(List<PK> paramList);
}
