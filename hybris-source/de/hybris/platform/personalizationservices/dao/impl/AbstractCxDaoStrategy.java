package de.hybris.platform.personalizationservices.dao.impl;

import de.hybris.platform.personalizationservices.dao.CxDaoQueryBuilder;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategy;

public abstract class AbstractCxDaoStrategy implements CxDaoStrategy
{
    private CxDaoQueryBuilder cxDaoQueryBuilder;


    public void setCxDaoQueryBuilder(CxDaoQueryBuilder cxDaoQueryBuilder)
    {
        this.cxDaoQueryBuilder = cxDaoQueryBuilder;
    }


    protected CxDaoQueryBuilder getCxDaoQueryBuilder()
    {
        return this.cxDaoQueryBuilder;
    }
}
