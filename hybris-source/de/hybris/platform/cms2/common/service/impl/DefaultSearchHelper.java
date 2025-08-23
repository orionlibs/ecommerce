package de.hybris.platform.cms2.common.service.impl;

import de.hybris.platform.cms2.common.service.SearchHelper;
import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSearchHelper implements SearchHelper
{
    private static Logger LOG = LoggerFactory.getLogger(DefaultSearchHelper.class);
    private static final String COLON = ":";
    private static final String COMMA = ",";


    public List<Sort> convertSort(String sort, SortDirection defaultSortDirection)
    {
        if(StringUtils.isEmpty(sort))
        {
            return Collections.emptyList();
        }
        String[] sortBlocks = sort.trim().split(",");
        Stream<String> stream = Arrays.stream(sortBlocks);
        try
        {
            List<Sort> list = (List)stream.map(sortBlock -> convertSortBlock(sortBlock, defaultSortDirection)).collect(Collectors.toList());
            if(stream != null)
            {
                stream.close();
            }
            return list;
        }
        catch(Throwable throwable)
        {
            if(stream != null)
            {
                try
                {
                    stream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    public Sort convertSortBlock(String sortBlock, SortDirection defaultSortDirection)
    {
        SortDirection sortDirection;
        String[] sortPair = sortBlock.trim().split(":");
        if(sortPair.length < 1)
        {
            throw new ConversionException("The sort parameter format [" + sortBlock + "] is not correct.");
        }
        if(sortPair.length == 1)
        {
            return (new Sort()).withParameter(sortPair[0].trim()).withDirection(defaultSortDirection);
        }
        try
        {
            sortDirection = SortDirection.valueOf(sortPair[1].trim().toUpperCase());
        }
        catch(IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Invalid sort direction provided: " + sortPair[1] + "; defaulting sort direction to: " + defaultSortDirection, e);
            }
            sortDirection = defaultSortDirection;
        }
        return (new Sort()).withParameter(sortPair[0].trim()).withDirection(sortDirection);
    }
}
