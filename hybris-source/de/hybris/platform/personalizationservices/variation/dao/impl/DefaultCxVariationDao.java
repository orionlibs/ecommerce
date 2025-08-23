package de.hybris.platform.personalizationservices.variation.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.variation.dao.CxVariationDao;
import de.hybris.platform.personalizationservices.variation.dao.CxVariationDaoStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultCxVariationDao extends AbstractCxDao<CxVariationModel> implements CxVariationDao
{
    private static final String CATALOG_VERSION = "catalogVersion";
    private static final String SEPERATOR = "\\_/";
    @Autowired(required = false)
    private List<CxVariationDaoStrategy> cxVariationDaoStrategy = Collections.emptyList();


    public DefaultCxVariationDao()
    {
        super("CxVariation");
    }


    public Optional<CxVariationModel> findVariationByCode(String code, CxCustomizationModel customization)
    {
        ServicesUtil.validateParameterNotNull(code, "Variation code must not be null");
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        ServicesUtil.validateParameterNotNull(customization.getCatalogVersion(), "customization must have a not null catalogVersion");
        String query = "SELECT {pk} FROM {CxVariation} WHERE {code} = ?code AND {customization} = ?customization AND {catalogVersion} = ?catalogVersion ";
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("customization", customization);
        params.put("catalogVersion", customization.getCatalogVersion());
        return querySingle("SELECT {pk} FROM {CxVariation} WHERE {code} = ?code AND {customization} = ?customization AND {catalogVersion} = ?catalogVersion ", params);
    }


    public Collection<CxVariationModel> findVariations(Collection<CxVariationKey> codes, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(codes, "Variation code must not be null");
        ServicesUtil.validateParameterNotNull(catalogVersion, "catalogVersion must not be null");
        String query = "SELECT {V.pk} FROM {CxVariation AS V JOIN CxCustomization AS C ON {C.pk} = {V.customization} } WHERE CONCAT( CONCAT({V.code}, '\\_/') ,{C.code}) IN (?codes) AND {catalogVersion} = ?catalogVersion ORDER BY {C.groupPOS},{V.customizationPOS}";
        List<String> mappedCodes = (List<String>)codes.stream().map(k -> k.getVariationCode() + "\\_/" + k.getVariationCode()).collect(Collectors.toList());
        Map<String, Object> params = new HashMap<>();
        params.put("codes", mappedCodes);
        params.put("catalogVersion", catalogVersion);
        return queryList("SELECT {V.pk} FROM {CxVariation AS V JOIN CxCustomization AS C ON {C.pk} = {V.customization} } WHERE CONCAT( CONCAT({V.code}, '\\_/') ,{C.code}) IN (?codes) AND {catalogVersion} = ?catalogVersion ORDER BY {C.groupPOS},{V.customizationPOS}", params);
    }


    public SearchPageData<CxVariationModel> findVariations(CxCustomizationModel customization, Map<String, String> params, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        ServicesUtil.validateParameterNotNull(customization.getCatalogVersion(), "customization must have a not null catalogVersion");
        String query = "SELECT {pk} FROM {CxVariation} WHERE {customization} = ?customization AND {catalogVersion} = ?catalogVersion ORDER BY rank";
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("customization", customization);
        queryParam.put("catalogVersion", customization.getCatalogVersion());
        return queryList("SELECT {pk} FROM {CxVariation} WHERE {customization} = ?customization AND {catalogVersion} = ?catalogVersion ORDER BY rank", queryParam, this.cxVariationDaoStrategy, params, pagination);
    }


    @Autowired(required = false)
    public void setCxVariationDaoStrategy(List<CxVariationDaoStrategy> cxVariationDaoStrategy)
    {
        this.cxVariationDaoStrategy = cxVariationDaoStrategy;
    }


    protected List<CxVariationDaoStrategy> getCxVariationDaoStrategy()
    {
        return this.cxVariationDaoStrategy;
    }
}
