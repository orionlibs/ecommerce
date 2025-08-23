package de.hybris.platform.personalizationservices.trigger.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.trigger.CxTriggerService;
import de.hybris.platform.personalizationservices.trigger.dao.CxTriggerDao;
import de.hybris.platform.personalizationservices.trigger.strategy.CxTriggerStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxTriggerService implements CxTriggerService
{
    private ModelService modelService;
    private Map<String, CxTriggerStrategy> triggerStrategies;
    private CxTriggerDao cxTriggerDao;


    public CxAbstractTriggerModel createTrigger(CxAbstractTriggerModel trigger, CxVariationModel variation)
    {
        ServicesUtil.validateParameterNotNull(trigger, "trigger must not be null");
        ServicesUtil.validateParameterNotNull(variation, "variation must not be null");
        ServicesUtil.validateParameterNotNull(variation.getCatalogVersion(), "variation must belong to some catalog version");
        trigger.setVariation(variation);
        trigger.setCatalogVersion(variation.getCatalogVersion());
        getModelService().save(trigger);
        getModelService().refresh(variation);
        return trigger;
    }


    public Optional<CxAbstractTriggerModel> getTrigger(String code, CxVariationModel variation)
    {
        return this.cxTriggerDao.findTriggerByCode(code, variation);
    }


    public Collection<CxAbstractTriggerModel> getTriggers(CxVariationModel variation)
    {
        return this.cxTriggerDao.findTriggers(variation);
    }


    public Collection<CxVariationModel> getVariationsForUser(UserModel user, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(user, "user parameter can't be empty");
        ServicesUtil.validateParameterNotNull(catalogVersion, "catalogVersion parameter can't be empty");
        if(MapUtils.isEmpty(this.triggerStrategies))
        {
            return Collections.emptyList();
        }
        return (Collection<CxVariationModel>)this.triggerStrategies.values().stream()
                        .map(s -> s.getVariations(user, catalogVersion))
                        .flatMap(v -> v.stream())
                        .collect(Collectors.toSet());
    }


    @Required
    public void setCxTriggerDao(CxTriggerDao cxTriggerDao)
    {
        this.cxTriggerDao = cxTriggerDao;
    }


    protected CxTriggerDao getCxTriggerDao()
    {
        return this.cxTriggerDao;
    }


    @Autowired(required = false)
    public void setTriggerStrategies(Map<String, CxTriggerStrategy> triggerStrategies)
    {
        this.triggerStrategies = triggerStrategies;
    }


    protected Map<String, CxTriggerStrategy> getTriggerStrategies()
    {
        return this.triggerStrategies;
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
