package de.hybris.platform.personalizationcmsweb.filter;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.webservicescommons.filter.CatalogVersionFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsCatalogVersionFilter extends CatalogVersionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(CmsCatalogVersionFilter.class);


    protected void process(HttpServletRequest servletRequest)
    {
        super.process(servletRequest);
        Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();
        Collection<CatalogVersionModel> allCatalogVersions = getCatalogVersionService().getAllCatalogVersions();
        boolean allVersionsInSession = (allCatalogVersions != null && allCatalogVersions.equals(sessionCatalogVersions));
        if(allVersionsInSession)
        {
            LOG.debug("Skipping cmsCatalogVersionFilter, all catalogs are in the session");
            return;
        }
        Map<String, CatalogVersionModel> catalogToVersionMap = new HashMap<>();
        for(CatalogVersionModel cv : sessionCatalogVersions)
        {
            String cataloName = cv.getCatalog().getId();
            catalogToVersionMap.put(cataloName, cv);
        }
        for(CatalogVersionModel cv : sessionCatalogVersions)
        {
            CatalogModel c = cv.getCatalog();
            if(c instanceof ContentCatalogModel)
            {
                ContentCatalogModel catalog = (ContentCatalogModel)c;
                while(catalog != null)
                {
                    catalogToVersionMap.putIfAbsent(catalog.getId(), catalog.getActiveCatalogVersion());
                    catalog = catalog.getSuperCatalog();
                }
            }
        }
        getCatalogVersionService().setSessionCatalogVersions(catalogToVersionMap.values());
    }
}
