package de.hybris.platform.personalizationsearchweb.queries;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.ProductSearchStrategyFactory;
import de.hybris.platform.personalizationsearchweb.data.CxSearchIndexTypeIdListWsDTO;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

public class CxGetIndexTypesForSiteExecutor extends AbstractRestQueryExecutor
{
    public static final String BASE_SITE_ID = "baseSiteId";
    private static final String[] REQUIRED_FIELDS = new String[] {"baseSiteId", "catalog", "catalogVersion"};
    private BaseSiteService baseSiteService;
    private BaseStoreService baseStoreService;
    private ProductSearchStrategyFactory productSearchStrategyFactory;


    protected Object executeAfterValidation(Map<String, String> params)
    {
        String baseSiteParam = params.get("baseSiteId");
        String catalogParam = params.get("catalog");
        String catalogVersionParam = params.get("catalogVersion");
        Set<String> indexTypes = this.productSearchStrategyFactory.getSearchStrategy().getIndexTypes(baseSiteParam, catalogParam, catalogVersionParam);
        CxSearchIndexTypeIdListWsDTO cxSearchIndexTypeIdListWsDTO = new CxSearchIndexTypeIdListWsDTO();
        cxSearchIndexTypeIdListWsDTO.setIndexTypeIds(indexTypes);
        return cxSearchIndexTypeIdListWsDTO;
    }


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        validateMissingField(errors, REQUIRED_FIELDS);
        BaseSiteModel model = this.baseSiteService.getBaseSiteForUID(params.get("baseSiteId"));
        if(model == null)
        {
            errors.reject("Parameter 'baseSite' must have proper.");
        }
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        if(params.get("catalog") == null || params.get("catalogVersion") == null)
        {
            Collections.emptyList();
        }
        CatalogVersionWsDTO catalogVersion = new CatalogVersionWsDTO();
        catalogVersion.setCatalog(params.get("catalog"));
        catalogVersion.setVersion(params.get("catalogVersion"));
        return Arrays.asList(new CatalogVersionWsDTO[] {catalogVersion});
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        if(params.get("catalog") == null || params.get("catalogVersion") == null)
        {
            Collections.emptyList();
        }
        CatalogVersionWsDTO catalogVersion = new CatalogVersionWsDTO();
        catalogVersion.setCatalog(params.get("catalog"));
        catalogVersion.setVersion(params.get("catalogVersion"));
        return Arrays.asList(new CatalogVersionWsDTO[] {catalogVersion});
    }


    @Deprecated(since = "2011")
    protected SolrFacetSearchConfigModel getSolrConfig(Map<String, String> params)
    {
        BaseSiteModel baseSite = getBaseSite(params);
        SolrFacetSearchConfigModel result = getSolrConfigForBaseSite(baseSite);
        if(result == null)
        {
            result = getSolrConfigForBaseStore(baseSite);
        }
        return result;
    }


    @Deprecated(since = "2011")
    protected BaseSiteModel getBaseSite(Map<String, String> params)
    {
        return this.baseSiteService.getBaseSiteForUID(params.get("baseSiteId"));
    }


    @Deprecated(since = "2011")
    protected SolrFacetSearchConfigModel getSolrConfigForBaseSite(BaseSiteModel baseSite)
    {
        if(baseSite != null)
        {
            return baseSite.getSolrFacetSearchConfiguration();
        }
        return null;
    }


    @Deprecated(since = "2011")
    protected SolrFacetSearchConfigModel getSolrConfigForBaseStore(BaseSiteModel baseSite)
    {
        BaseStoreModel baseStoreModel = (BaseStoreModel)getLocalViewExecutor().executeInLocalView(() -> {
            this.baseSiteService.setCurrentBaseSite(baseSite, true);
            return this.baseStoreService.getCurrentBaseStore();
        });
        if(baseStoreModel != null)
        {
            return baseStoreModel.getSolrFacetSearchConfiguration();
        }
        return null;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    @Deprecated(since = "2011")
    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    public ProductSearchStrategyFactory getProductSearchStrategyFactory()
    {
        return this.productSearchStrategyFactory;
    }


    @Required
    public void setProductSearchStrategyFactory(ProductSearchStrategyFactory productSearchStrategyFactory)
    {
        this.productSearchStrategyFactory = productSearchStrategyFactory;
    }
}
