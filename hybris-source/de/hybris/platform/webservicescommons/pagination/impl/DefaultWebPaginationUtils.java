package de.hybris.platform.webservicescommons.pagination.impl;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SearchPageWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWebPaginationUtils implements WebPaginationUtils
{
    private static final String DEFAULT_PAGE_SIZE_KEY = "webservicescommons.pagination.pageSize";
    private static final int DEFAULT_PAGE_SIZE_DEFAULT_VALUE = 10;
    private static final String MAX_PAGE_SIZE_KEY = "webservicescommons.pagination.maxPageSize";
    private static final int MAX_PAGE_SIZE_DEFAULT_VALUE = 1000;
    private ConfigurationService configurationService;
    private Converter<SortData, SortWsDTO> sortDataConverter;
    private Converter<PaginationData, PaginationWsDTO> paginationDataConverter;


    public int getPageSize(Map<String, String> params)
    {
        return getPageSize(params, getDefaultPageSize());
    }


    public int getPageSize(Map<String, String> params, int defaultValue)
    {
        int pageSize = getIntFromMap(params, "pageSize", defaultValue);
        return getPageSize(pageSize);
    }


    private int getPageSize(int pageSize)
    {
        if(pageSize > getMaxPageSize())
        {
            return getMaxPageSize();
        }
        if(pageSize < 0)
        {
            return 0;
        }
        return pageSize;
    }


    public int getDefaultPageSize()
    {
        if(this.configurationService == null)
        {
            return 10;
        }
        return this.configurationService.getConfiguration().getInt("webservicescommons.pagination.pageSize", 10);
    }


    public int getMaxPageSize()
    {
        if(this.configurationService == null)
        {
            return 1000;
        }
        return this.configurationService.getConfiguration().getInt("webservicescommons.pagination.maxPageSize", 1000);
    }


    public int getCurrentPage(Map<String, String> params)
    {
        int currentPage = getIntFromMap(params, "currentPage", 0);
        return (currentPage >= 0) ? currentPage : 0;
    }


    public int getStartPosition(Map<String, String> params)
    {
        return getStartPosition(params, getDefaultPageSize());
    }


    public int getStartPosition(Map<String, String> params, int defaultPageSize)
    {
        int pageSize = getPageSize(params, defaultPageSize);
        int currentPage = getCurrentPage(params);
        return calculateStartPosition(pageSize, currentPage);
    }


    public boolean getNeedsTotal(Map<String, String> requestParams)
    {
        return getBooleanFromMap(requestParams, "needsTotal", true);
    }


    private static int calculateStartPosition(int pageSize, int currentPage)
    {
        return Math.max(pageSize * currentPage, 0);
    }


    public PaginationWsDTO buildPagination(SearchResult<?> search)
    {
        PaginationWsDTO result = new PaginationWsDTO();
        result.setTotalCount(Long.valueOf(search.getTotalCount()));
        result.setCount(Integer.valueOf(search.getCount()));
        int reqCount = search.getRequestedCount();
        if(reqCount > 0)
        {
            result.setPage(Integer.valueOf(search.getRequestedStart() / search.getRequestedCount()));
        }
        else
        {
            result.setPage(Integer.valueOf(0));
        }
        double totalPages = Math.ceil(1.0D * search.getTotalCount() / search.getRequestedCount());
        if(Double.isFinite(totalPages))
        {
            result.setTotalPages(Integer.valueOf((int)totalPages));
        }
        else
        {
            result.setTotalPages(Integer.valueOf(0));
        }
        return result;
    }


    public List<SortData> buildSortData(HttpServletRequest request)
    {
        if(request == null)
        {
            return buildDefaultSortData();
        }
        return buildSortData(request.getParameter("sort"));
    }


    public List<SortData> buildSortData(Map<String, String> requestParams)
    {
        if(requestParams == null)
        {
            return buildDefaultSortData();
        }
        return buildSortData(requestParams.get("sort"));
    }


    public List<SortData> buildSortData(String sort)
    {
        if(StringUtils.isBlank(sort))
        {
            return buildDefaultSortData();
        }
        String[] sorts = sort.split(",");
        return (List<SortData>)Stream.<String>of(sorts).map(DefaultWebPaginationUtils::buildSingleSortData).filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    private static List<SortData> buildDefaultSortData()
    {
        return Collections.emptyList();
    }


    private static SortData buildSingleSortData(String sort)
    {
        if(StringUtils.isBlank(sort))
        {
            return null;
        }
        SortData result = new SortData();
        String[] split = sort.split(":");
        if(split.length > 1)
        {
            result.setCode(split[0]);
            result.setAsc(!StringUtils.equalsIgnoreCase(split[1], "desc"));
        }
        else
        {
            result.setCode(split[0]);
            result.setAsc(true);
        }
        return result;
    }


    public PaginationData buildPaginationData(HttpServletRequest request)
    {
        if(request == null)
        {
            return buildDefaultPaginationData();
        }
        Map<String, String> requestParams = getMapFromRequest(request);
        return buildPaginationData(requestParams);
    }


    public PaginationData buildPaginationData(Map<String, String> requestParams)
    {
        if(requestParams == null)
        {
            return buildDefaultPaginationData();
        }
        int currentPage = getCurrentPage(requestParams);
        int pageSize = getPageSize(requestParams);
        boolean needsTotal = getNeedsTotal(requestParams);
        return buildPaginationData(currentPage, pageSize, needsTotal);
    }


    public PaginationData buildPaginationData(int currentPage, int pageSize)
    {
        return buildPaginationData(currentPage, pageSize, true);
    }


    public PaginationData buildPaginationData(int currentPage, int pageSize, boolean needsTotal)
    {
        PaginationData result = new PaginationData();
        result.setCurrentPage(currentPage);
        result.setNeedsTotal(needsTotal);
        result.setPageSize(getPageSize(pageSize));
        return result;
    }


    private PaginationData buildDefaultPaginationData()
    {
        PaginationData result = new PaginationData();
        result.setCurrentPage(0);
        result.setPageSize(getDefaultPageSize());
        result.setNeedsTotal(true);
        return result;
    }


    public <RESULT> SearchPageData<RESULT> buildSearchPageData(HttpServletRequest request)
    {
        SearchPageData<RESULT> result = new SearchPageData();
        result.setPagination(buildPaginationData(request));
        result.setSorts(buildSortData(request));
        return result;
    }


    public <RESULT> SearchPageData<RESULT> buildSearchPageData(Map<String, String> requestParams)
    {
        SearchPageData<RESULT> result = new SearchPageData();
        result.setPagination(buildPaginationData(requestParams));
        result.setSorts(buildSortData(requestParams));
        return result;
    }


    public <RESULT> SearchPageData<RESULT> buildSearchPageData(String sort, int currentPage, int pageSize, boolean needsTotal)
    {
        SearchPageData<RESULT> result = new SearchPageData();
        result.setPagination(buildPaginationData(currentPage, pageSize, needsTotal));
        result.setSorts(buildSortData(sort));
        return result;
    }


    public PaginationWsDTO buildPaginationWsDto(PaginationData paginationData)
    {
        return (PaginationWsDTO)this.paginationDataConverter.convert(paginationData);
    }


    public List<SortWsDTO> buildSortWsDto(List<SortData> sortData)
    {
        return this.sortDataConverter.convertAll(sortData);
    }


    public <RESULT, INPUT> SearchPageWsDTO<RESULT> buildSearchPageWsDto(SearchPageData<INPUT> searchPage)
    {
        return buildSearchPageWsDto(searchPage, null);
    }


    public <RESULT, INPUT> SearchPageWsDTO<RESULT> buildSearchPageWsDto(SearchPageData<INPUT> searchPage, Converter<INPUT, RESULT> dataConverter)
    {
        SearchPageWsDTO<RESULT> result = new SearchPageWsDTO();
        if(dataConverter != null)
        {
            List<RESULT> resultList = dataConverter.convertAll(searchPage.getResults());
            result.setResults(resultList);
        }
        result.setPagination(buildPaginationWsDto(searchPage.getPagination()));
        if(searchPage.getResults() != null)
        {
            result.getPagination().setCount(Integer.valueOf(searchPage.getResults().size()));
        }
        result.setSorts(buildSortWsDto(searchPage.getSorts()));
        return result;
    }


    private static int getIntFromMap(Map<String, String> params, String key, int defaultValue)
    {
        if(MapUtils.isEmpty(params))
        {
            return defaultValue;
        }
        String val = params.getOrDefault(key, String.valueOf(defaultValue));
        try
        {
            return Integer.parseInt(val);
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }


    private static boolean getBooleanFromMap(Map<String, String> params, String key, boolean defaultValue)
    {
        if(MapUtils.isEmpty(params))
        {
            return defaultValue;
        }
        String val = params.getOrDefault(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(val);
    }


    private static Map<String, String> getMapFromRequest(HttpServletRequest request)
    {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> result = new LinkedHashMap<>(parameterMap.size());
        parameterMap.entrySet().stream()
                        .filter(e -> (((String[])e.getValue()).length > 0))
                        .forEach(e -> result.put((String)e.getKey(), ((String[])e.getValue())[0]));
        return result;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public Converter<PaginationData, PaginationWsDTO> getPaginationDataConverter()
    {
        return this.paginationDataConverter;
    }


    public void setPaginationDataConverter(Converter<PaginationData, PaginationWsDTO> paginationDataConverter)
    {
        this.paginationDataConverter = paginationDataConverter;
    }


    public Converter<SortData, SortWsDTO> getSortDataConverter()
    {
        return this.sortDataConverter;
    }


    public void setSortDataConverter(Converter<SortData, SortWsDTO> sortDataConverter)
    {
        this.sortDataConverter = sortDataConverter;
    }
}
