package de.hybris.platform.webservicescommons.pagination.converters;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;
import org.springframework.util.Assert;

public class SortDataPopulator implements Populator<SortData, SortWsDTO>
{
    public void populate(SortData source, SortWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setAsc(source.isAsc());
        target.setCode(source.getCode());
    }
}
