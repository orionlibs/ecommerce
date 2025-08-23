package de.hybris.platform.personalizationservices.customization.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDao;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoParamStrategy;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoStrategy;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.exceptions.EmptyResultParameterCombinationException;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultCxCustomizationDao extends AbstractCxDao<CxCustomizationModel> implements CxCustomizationDao
{
    public static final String CATALOGS = "catalogVersions";
    private static final Logger LOGGER = Logger.getLogger(DefaultCxCustomizationDao.class);
    private static final String CATALOG_VERSION = "catalogVersion";
    private List<CxCustomizationDaoStrategy> cxCustomizationDaoStrategies = Collections.emptyList();
    private List<CxCustomizationDaoParamStrategy> cxCustomizationDaoParamStrategies = Collections.emptyList();
    private CxCustomizationDaoStrategy defaultStrategy = (CxCustomizationDaoStrategy)new DefaultCxCustomizationDaoStrategy();


    public DefaultCxCustomizationDao()
    {
        super("CxCustomization");
    }


    public Optional<CxCustomizationModel> findCustomizationByCode(String code, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(code, "Customization code must not be null");
        String query = "SELECT {pk} FROM {CxCustomization} WHERE {code} LIKE ?code AND {catalogVersion} =?catalogVersion";
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("catalogVersion", catalogVersion);
        return querySingle("SELECT {pk} FROM {CxCustomization} WHERE {code} LIKE ?code AND {catalogVersion} =?catalogVersion", params);
    }


    public List<CxCustomizationModel> findCustomizations(CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {CxCustomization} WHERE {catalogVersion} =?catalogVersion ORDER BY P_GROUPPOS ASC";
        return queryList("SELECT {pk} FROM {CxCustomization} WHERE {catalogVersion} =?catalogVersion ORDER BY P_GROUPPOS ASC", Collections.singletonMap("catalogVersion", catalogVersion));
    }


    public SearchPageData<CxCustomizationModel> findCustomizations(CatalogVersionModel catalogVersion, Map<String, String> params, SearchPageData<?> pagination)
    {
        Map<String, Object> queryParams = Collections.singletonMap("catalogVersion", catalogVersion);
        try
        {
            queryParams = expandParams(queryParams, params, this.cxCustomizationDaoParamStrategies);
        }
        catch(EmptyResultParameterCombinationException e)
        {
            LOGGER.debug("Problem with parameter occurred: ", (Throwable)e);
            return buildEmptySearchPageData(pagination.getPagination());
        }
        String query = this.defaultStrategy.getQuery(params, queryParams).getQuery();
        return queryList(query, queryParams, this.cxCustomizationDaoStrategies, params, pagination);
    }


    @Autowired(required = false)
    public void setCxCustomizationDaoStrategies(List<CxCustomizationDaoStrategy> cxCustomizationDaoStrategies)
    {
        this.cxCustomizationDaoStrategies = cxCustomizationDaoStrategies;
    }


    @Autowired(required = false)
    public void setCxCustomizationDaoParamStrategies(List<CxCustomizationDaoParamStrategy> cxCustomizationDaoParamStrategies)
    {
        this.cxCustomizationDaoParamStrategies = cxCustomizationDaoParamStrategies;
    }


    public void setDefaultStrategy(CxCustomizationDaoStrategy defaultStrategy)
    {
        this.defaultStrategy = defaultStrategy;
    }


    protected List<CxCustomizationDaoStrategy> getCxCustomizationDaoStrategies()
    {
        return this.cxCustomizationDaoStrategies;
    }


    protected List<CxCustomizationDaoParamStrategy> getCxCustomizationDaoParamStrategies()
    {
        return this.cxCustomizationDaoParamStrategies;
    }


    protected CxCustomizationDaoStrategy getDefaultStrategy()
    {
        return this.defaultStrategy;
    }
}
