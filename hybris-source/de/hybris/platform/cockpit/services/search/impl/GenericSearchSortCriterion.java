package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchOrderBy;

public interface GenericSearchSortCriterion
{
    GenericSearchOrderBy createOrderBy(GenericQuery paramGenericQuery, boolean paramBoolean);
}
