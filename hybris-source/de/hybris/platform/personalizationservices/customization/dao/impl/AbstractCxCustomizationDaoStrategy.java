package de.hybris.platform.personalizationservices.customization.dao.impl;

import de.hybris.platform.personalizationservices.dao.CxItemStatusParamSupport;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDaoStrategy;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import java.util.Collection;

public abstract class AbstractCxCustomizationDaoStrategy extends AbstractCxDaoStrategy
{
    public static final String STATUSES = "statuses";
    public static final String NAME = "name";


    public Collection<CxItemStatus> getStatusesForCodesStr(String statuses)
    {
        return CxItemStatusParamSupport.buildStatuses(statuses);
    }
}
