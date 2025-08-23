package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaFolderDao;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class DefaultCMSMediaFolderDao extends AbstractCMSItemDao implements CMSMediaFolderDao
{
    public SearchResult<MediaFolderModel> findMediaFolders(String qualifier, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "qualifier cannot be null");
        String query = "SELECT {pk} FROM {MediaFolder} WHERE LOWER({qualifier}) LIKE LOWER(?qualifier) ORDER BY {qualifier} ASC";
        Map<String, Object> params = new HashMap<>();
        params.put("qualifier", "%" + (StringUtils.isNotBlank(qualifier) ? qualifier.toLowerCase() : "") + "%");
        return getFlexibleSearchService()
                        .search(buildQuery(query, params, pageableData.getCurrentPage(), pageableData.getPageSize()));
    }
}
