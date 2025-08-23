package de.hybris.platform.cms2.namedquery.service.impl;

import de.hybris.platform.cms2.exceptions.InvalidNamedQueryException;
import de.hybris.platform.cms2.exceptions.SearchExecutionNamedQueryException;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.NamedQueryConversionDto;
import de.hybris.platform.cms2.namedquery.service.NamedQueryFactory;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class FlexibleSearchNamedQueryService implements NamedQueryService
{
    public static final String OUTER_JOIN_LP = "?outerJoinLP";
    private FlexibleSearchService flexibleSearchService;
    private FlexibleSearchNamedQueryConverter flexibleSearchNamedQueryConverter;
    private NamedQueryFactory namedQueryFactory;
    private I18NService i18NService;


    public <T> List<T> search(NamedQuery namedQuery)
    {
        return getSearchResult(namedQuery).getResult();
    }


    public <T> SearchResult<T> getSearchResult(NamedQuery namedQuery) throws InvalidNamedQueryException, SearchExecutionNamedQueryException
    {
        Locale currentLocale = getI18NService().getCurrentLocale();
        String query = getNamedQueryFactory().getNamedQuery(namedQuery.getQueryName());
        if(null != currentLocale)
        {
            query = query.replace("?outerJoinLP", "[" + currentLocale.getLanguage() + "]:o");
        }
        NamedQueryConversionDto internalNamedQuery = getInternalNamedQuery(namedQuery, query);
        FlexibleSearchQuery flexibleSearchQuery = getFlexibleSearchNamedQueryConverter().convert(internalNamedQuery);
        if(currentLocale != null)
        {
            flexibleSearchQuery.setLocale(currentLocale);
        }
        try
        {
            return getFlexibleSearchService().search(flexibleSearchQuery);
        }
        catch(Exception e)
        {
            throw new SearchExecutionNamedQueryException(
                            String.format("Error while executing namedQuery [%s]", new Object[] {namedQuery.getQueryName()}), e);
        }
    }


    protected NamedQueryConversionDto getInternalNamedQuery(NamedQuery namedQuery, String query)
    {
        NamedQueryConversionDto internalNamedQuery = (new NamedQueryConversionDto()).withQuery(query).withNamedQuery(namedQuery);
        return internalNamedQuery;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected FlexibleSearchNamedQueryConverter getFlexibleSearchNamedQueryConverter()
    {
        return this.flexibleSearchNamedQueryConverter;
    }


    @Required
    public void setFlexibleSearchNamedQueryConverter(FlexibleSearchNamedQueryConverter flexibleSearchNamedQueryConverter)
    {
        this.flexibleSearchNamedQueryConverter = flexibleSearchNamedQueryConverter;
    }


    protected NamedQueryFactory getNamedQueryFactory()
    {
        return this.namedQueryFactory;
    }


    @Required
    public void setNamedQueryFactory(NamedQueryFactory namedQueryFactory)
    {
        this.namedQueryFactory = namedQueryFactory;
    }


    protected I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
