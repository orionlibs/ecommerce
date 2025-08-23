package de.hybris.platform.util.migration;

import de.hybris.platform.core.PK;

public interface PKMapper
{
    PK mapPK(OldPK paramOldPK);
}
