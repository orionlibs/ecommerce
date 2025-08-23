package de.hybris.platform.personalizationservices.variation.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.trigger.CxTriggerService;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.personalizationservices.variation.dao.CxVariationDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.ObjectUtils;

public class DefaultCxVariationService implements CxVariationService
{
    private ModelService modelService;
    private CxVariationDao cxVariationDao;
    private CxTriggerService cxTriggerService;


    public CxVariationModel createVariation(CxVariationModel variation, CxCustomizationModel customization, Integer rank)
    {
        ServicesUtil.validateParameterNotNull(variation, "Variation must not be null");
        ServicesUtil.validateParameterNotNull(variation.getCode(), "Variation code must not be null");
        ServicesUtil.validateParameterNotNull(variation.getName(), "Variation name must not be null");
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        ServicesUtil.validateParameterNotNull(customization.getCatalogVersion(), "Customization must belong to some catalog version");
        variation.setCustomization(customization);
        variation.setCatalogVersion(customization.getCatalogVersion());
        getModelService().save(variation);
        variation.setRank(rank);
        getModelService().save(variation.getCustomization());
        return variation;
    }


    public Optional<CxVariationModel> getVariation(String code, CxCustomizationModel customization)
    {
        ServicesUtil.validateParameterNotNull(code, "Variation code must not be null");
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        return this.cxVariationDao.findVariationByCode(code, customization);
    }


    public Collection<CxVariationModel> getVariations(Collection<CxVariationKey> codes, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(codes, "VariationKey list must not be null");
        ServicesUtil.validateParameterNotNull(catalogVersion, "Catalog Version must not be null");
        return this.cxVariationDao.findVariations(codes, catalogVersion);
    }


    public SearchPageData<CxVariationModel> getVariations(CxCustomizationModel customization, Map<String, String> params, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        ServicesUtil.validateParameterNotNull(params, "params must not be null");
        return this.cxVariationDao.findVariations(customization, params, pagination);
    }


    public List<CxVariationModel> getActiveVariations(UserModel user, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        ServicesUtil.validateParameterNotNull(catalogVersion, "Catalog Version must not be null");
        return (List<CxVariationModel>)((Map)this.cxTriggerService
                        .getVariationsForUser(user, catalogVersion).stream()
                        .filter(CxVariationModel::isActive)
                        .collect(Collectors.groupingBy(CxVariationModel::getCustomization, Collectors.toSet())))
                        .entrySet().stream()
                        .sorted(this::compareCustomizationsPriority)
                        .map(this::getPriorityVariation)
                        .collect(Collectors.toList());
    }


    protected int compareCustomizationsPriority(Map.Entry<CxCustomizationModel, Set<CxVariationModel>> a, Map.Entry<CxCustomizationModel, Set<CxVariationModel>> b)
    {
        return ObjectUtils.compare(((CxCustomizationModel)a.getKey()).getRank(), ((CxCustomizationModel)b.getKey()).getRank());
    }


    protected CxVariationModel getPriorityVariation(Map.Entry<CxCustomizationModel, Set<CxVariationModel>> e)
    {
        return ((Set<CxVariationModel>)e.getValue()).stream().min(Comparator.comparing(CxVariationModel::getRank)).orElse(null);
    }


    public void setCxVariationDao(CxVariationDao cxVariationDao)
    {
        this.cxVariationDao = cxVariationDao;
    }


    protected CxVariationDao getCxVariationDao()
    {
        return this.cxVariationDao;
    }


    public void setCxTriggerService(CxTriggerService cxTriggerService)
    {
        this.cxTriggerService = cxTriggerService;
    }


    protected CxTriggerService getCxTriggerService()
    {
        return this.cxTriggerService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
