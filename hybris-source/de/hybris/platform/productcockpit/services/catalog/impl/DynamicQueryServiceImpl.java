package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.dynamicquery.impl.DynamicQueryImpl;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.query.DynamicQueryService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class DynamicQueryServiceImpl extends AbstractServiceImpl implements DynamicQueryService
{
    String LAST_CHANGES = "query.lastchanges";
    String DUPLICATE_PRODUCT = "query.duplicateproduct";
    String CATALOG_VERSIONS_ATTRIBUTE = "catalogVersions";
    String DUPLICATE_PRODUCT_QUERY =
                    " SELECT {p:pk} FROM {Product AS p}  WHERE {p:catalogVersion} IN (?" + this.CATALOG_VERSIONS_ATTRIBUTE + ") AND  EXISTS ({{ SELECT * FROM {Product}  WHERE {code} = {p:code} AND  {pk}<>{p:pk} AND  {catalogVersion} IN (?" + this.CATALOG_VERSIONS_ATTRIBUTE + ") }})";
    String LAST_CHANGES_FLEXIBLE_QUERY = " SELECT {pk} FROM {Product}  WHERE {catalogVersion} IN (?" + this.CATALOG_VERSIONS_ATTRIBUTE + ") AND  {modifiedtime} >= ?time ORDER BY {modifiedtime} DESC ";
    private CatalogService productCockpitCatalogService;


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public List<TypedObject> getDynamicQueryResults(DynamicQuery query)
    {
        query.addQueryParameter(this.CATALOG_VERSIONS_ATTRIBUTE, getExtractedCatalogVersions());
        List<Class<?>> resultClasses = new ArrayList<>();
        resultClasses.add(Product.class);
        SessionContext ctx = null;
        try
        {
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            SearchResult searchResult = JaloSession.getCurrentSession().getFlexibleSearch().search(query.getFexibleQuery(), query
                            .getParameters(), resultClasses, true, true, 0, -1);
            return getTypeService().wrapItems(TypeTools.itemToPkList(searchResult.getResult()));
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public List<DynamicQuery> getAllDynamicQuery()
    {
        Map<String, Object> parameters = new HashMap<>();
        Calendar calendar = Utilities.getDefaultCalendar();
        calendar.add(7, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        parameters.put("time", calendar.getTime());
        List<DynamicQuery> allDynamicQuery = new ArrayList<>();
        allDynamicQuery.add(new DynamicQueryImpl(Labels.getLabel(this.LAST_CHANGES), this.LAST_CHANGES_FLEXIBLE_QUERY, parameters));
        parameters = new HashMap<>();
        allDynamicQuery.add(new DynamicQueryImpl(Labels.getLabel(this.DUPLICATE_PRODUCT), this.DUPLICATE_PRODUCT_QUERY, parameters));
        return allDynamicQuery;
    }


    private List<PK> getExtractedCatalogVersions()
    {
        List<PK> results = new ArrayList<>();
        for(CatalogVersionModel catalogVersion : this.productCockpitCatalogService.getAvailableCatalogVersions())
        {
            results.add(catalogVersion.getPk());
        }
        return results;
    }
}
