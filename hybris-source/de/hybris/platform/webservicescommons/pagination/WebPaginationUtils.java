package de.hybris.platform.webservicescommons.pagination;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SearchPageWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface WebPaginationUtils
{
    int getPageSize(Map<String, String> paramMap);


    int getPageSize(Map<String, String> paramMap, int paramInt);


    int getDefaultPageSize();


    int getCurrentPage(Map<String, String> paramMap);


    boolean getNeedsTotal(Map<String, String> paramMap);


    int getStartPosition(Map<String, String> paramMap);


    int getStartPosition(Map<String, String> paramMap, int paramInt);


    PaginationWsDTO buildPagination(SearchResult<?> paramSearchResult);


    List<SortData> buildSortData(HttpServletRequest paramHttpServletRequest);


    List<SortData> buildSortData(Map<String, String> paramMap);


    List<SortData> buildSortData(String paramString);


    PaginationData buildPaginationData(HttpServletRequest paramHttpServletRequest);


    PaginationData buildPaginationData(Map<String, String> paramMap);


    PaginationData buildPaginationData(int paramInt1, int paramInt2);


    PaginationData buildPaginationData(int paramInt1, int paramInt2, boolean paramBoolean);


    <RESULT> SearchPageData<RESULT> buildSearchPageData(HttpServletRequest paramHttpServletRequest);


    <RESULT> SearchPageData<RESULT> buildSearchPageData(Map<String, String> paramMap);


    <RESULT> SearchPageData<RESULT> buildSearchPageData(String paramString, int paramInt1, int paramInt2, boolean paramBoolean);


    PaginationWsDTO buildPaginationWsDto(PaginationData paramPaginationData);


    List<SortWsDTO> buildSortWsDto(List<SortData> paramList);


    <RESULT, INPUT> SearchPageWsDTO<RESULT> buildSearchPageWsDto(SearchPageData<INPUT> paramSearchPageData);


    <RESULT, INPUT> SearchPageWsDTO<RESULT> buildSearchPageWsDto(SearchPageData<INPUT> paramSearchPageData, Converter<INPUT, RESULT> paramConverter);
}
