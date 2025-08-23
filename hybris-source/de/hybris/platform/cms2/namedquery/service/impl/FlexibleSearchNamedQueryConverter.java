package de.hybris.platform.cms2.namedquery.service.impl;

import de.hybris.platform.cms2.namedquery.NamedQueryConversionDto;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

public class FlexibleSearchNamedQueryConverter implements Converter<NamedQueryConversionDto, FlexibleSearchQuery>
{
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final Integer DEFAULT_INITIAL_PAGE = Integer.valueOf(0);
    protected static final String COMMA = ", ";
    protected static final String SPACE = " ";
    protected static final String EMPTY_STRING = "";
    protected static final String OPEN_BRACKET = "{";
    protected static final String CLOSE_BRACKET = "}";
    private Integer defaultPageSize;


    public FlexibleSearchQuery convert(NamedQueryConversionDto namedQueryConversion) throws ConversionException
    {
        String query = namedQueryConversion.getQuery() + namedQueryConversion.getQuery();
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, namedQueryConversion.getNamedQuery().getParameters());
        setFlexibleSearchStartAndCount(flexibleSearchQuery, namedQueryConversion.getNamedQuery().getCurrentPage(), namedQueryConversion
                        .getNamedQuery().getPageSize());
        return flexibleSearchQuery;
    }


    protected String appendSort(List<Sort> sort)
    {
        if(!CollectionUtils.isEmpty(sort))
        {
            return " ORDER BY " + StringUtils.join((Collection)sort
                            .stream()
                            .map(e -> "{" + e.getParameter() + "} " + e.getDirection().name())
                            .collect(Collectors.toList()), ", ");
        }
        return "";
    }


    protected void setFlexibleSearchStartAndCount(FlexibleSearchQuery flexibleSearchQuery, Integer requestedCurrentPage, Integer requestedPageSize)
    {
        Integer page = DEFAULT_INITIAL_PAGE;
        Integer pageSize = this.defaultPageSize;
        if(requestedCurrentPage != null && requestedCurrentPage.intValue() > 0)
        {
            page = requestedCurrentPage;
        }
        if(requestedPageSize != null)
        {
            pageSize = requestedPageSize;
        }
        int start = page.intValue() * pageSize.intValue();
        flexibleSearchQuery.setStart(start);
        flexibleSearchQuery.setCount(pageSize.intValue());
        flexibleSearchQuery.setNeedTotal(true);
    }


    @Required
    public void setDefaultPageSize(Integer defaultPageSize)
    {
        this.defaultPageSize = defaultPageSize;
    }


    protected Integer getDefaultPageSize()
    {
        return this.defaultPageSize;
    }
}
