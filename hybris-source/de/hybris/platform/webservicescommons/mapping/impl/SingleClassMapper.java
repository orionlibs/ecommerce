package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

@WsDTOMapping
public class SingleClassMapper extends DefaultClassMapper
{
    public SingleClassMapper(Class<?> sourceAndTarget)
    {
        super(sourceAndTarget, sourceAndTarget);
    }
}
