package de.hybris.platform.cms2.usergroups.service.impl;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.cms2.usergroups.service.UserGroupSearchService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultUserGroupNamedQuerySearchService implements UserGroupSearchService
{
    public static final String NAMED_QUERY_USER_GROUP_SEARCH_BY_TEXT = "namedQueryUserGroupByText";
    private NamedQueryService namedQueryService;


    public SearchResult<UserGroupModel> findUserGroups(String text, PageableData pageableData)
    {
        NamedQuery namedQuery = getNamedQueryForUserGroupSearch(text, pageableData);
        return getNamedQueryService().getSearchResult(namedQuery);
    }


    protected NamedQuery getNamedQueryForUserGroupSearch(String text, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(pageableData, "PageableData object cannot be null.");
        String queryText = StringUtils.isEmpty(text) ? "%%" : ("%" + text + "%");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", queryText);
        parameters.put("uid", queryText);
        if(StringUtils.isEmpty(pageableData.getSort()))
        {
            pageableData.setSort("name");
        }
        List<Sort> sortList = Arrays.asList(new Sort[] {(new Sort()).withParameter(pageableData.getSort()).withDirection(SortDirection.ASC)});
        return (new NamedQuery()).withQueryName("namedQueryUserGroupByText").withCurrentPage(Integer.valueOf(pageableData.getCurrentPage()))
                        .withPageSize(Integer.valueOf(pageableData.getPageSize())).withParameters(parameters).withSort(sortList);
    }


    protected NamedQueryService getNamedQueryService()
    {
        return this.namedQueryService;
    }


    @Required
    public void setNamedQueryService(NamedQueryService namedQueryService)
    {
        this.namedQueryService = namedQueryService;
    }
}
