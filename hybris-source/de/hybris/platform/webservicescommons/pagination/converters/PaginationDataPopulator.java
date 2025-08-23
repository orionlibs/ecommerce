package de.hybris.platform.webservicescommons.pagination.converters;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import org.springframework.util.Assert;

public class PaginationDataPopulator implements Populator<PaginationData, PaginationWsDTO>
{
    public void populate(PaginationData source, PaginationWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCount(Integer.valueOf(source.getPageSize()));
        target.setPage(Integer.valueOf(source.getCurrentPage()));
        if(source.isNeedsTotal())
        {
            target.setTotalCount(Long.valueOf(source.getTotalNumberOfResults()));
            target.setTotalPages(Integer.valueOf(source.getNumberOfPages()));
        }
        target.setHasNext(source.getHasNext());
        target.setHasPrevious(source.getHasPrevious());
    }
}
