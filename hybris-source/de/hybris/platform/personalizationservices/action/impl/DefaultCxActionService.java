package de.hybris.platform.personalizationservices.action.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.action.CxActionService;
import de.hybris.platform.personalizationservices.action.dao.CxActionDao;
import de.hybris.platform.personalizationservices.action.dao.CxActionTypeDao;
import de.hybris.platform.personalizationservices.action.property.ActionPropertySetter;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxActionService implements CxActionService
{
    private ModelService modelService;
    private CxActionDao cxActionDao;
    private ActionPropertySetter<CxAbstractActionModel> actionPropertySetter;
    private Map<CxActionType, CxActionTypeDao> actionTypeDaoMap = Collections.emptyMap();


    public Optional<CxAbstractActionModel> getAction(String code, CxVariationModel variation)
    {
        return this.cxActionDao.findActionByCode(code, variation);
    }


    public List<CxAbstractActionModel> getActions(CxVariationModel variation)
    {
        return this.cxActionDao.findActions(variation);
    }


    public <T extends CxAbstractActionModel> SearchPageData<T> getActions(CxActionType type, CatalogVersionModel catalogVersion, Map<String, String> searchCriteria, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(type, "Type must not be null");
        ServicesUtil.validateParameterNotNull(searchCriteria, "SearchCriteria must not be null");
        ServicesUtil.validateParameterNotNull(pagination, "Pagination must not be null");
        CxActionTypeDao typeDao = this.actionTypeDaoMap.get(type);
        if(typeDao == null)
        {
            throw new IllegalArgumentException("Type " + type.name() + " is not supported");
        }
        return typeDao.getActions(catalogVersion, searchCriteria, pagination);
    }


    public List<CxAbstractActionModel> getActionsForVariations(Collection<CxVariationModel> variations)
    {
        ServicesUtil.validateParameterNotNull(variations, "Variations must not be null");
        if(CollectionUtils.isEmpty(variations))
        {
            return Collections.emptyList();
        }
        return (List<CxAbstractActionModel>)this.cxActionDao.findActionsForVariations(variations).stream()
                        .filter(distinctBy(CxAbstractActionModel::getAffectedObjectKey))
                        .collect(Collectors.toList());
    }


    protected <T> Predicate<T> distinctBy(Function<? super T, ? super Object> attributeProvider)
    {
        Map<Object, Object> seenMap = new HashMap<>();
        return val -> (seenMap.putIfAbsent(attributeProvider.apply(val), val) == null);
    }


    public CxAbstractActionModel createAction(CxAbstractActionModel action, CxVariationModel variation)
    {
        ServicesUtil.validateParameterNotNull(action, "Action must not be null");
        ServicesUtil.validateParameterNotNull(action.getCode(), "Action code must not be null");
        ServicesUtil.validateParameterNotNull(variation, "Variation must not be null");
        ServicesUtil.validateParameterNotNull(variation.getCatalogVersion(), "Variation must belong to some catalog version");
        setDefaultPropertiesBeforeCreate(action);
        action.setVariation(variation);
        action.setCatalogVersion(variation.getCatalogVersion());
        getModelService().save(action);
        getModelService().refresh(variation);
        return action;
    }


    protected void setDefaultPropertiesBeforeCreate(CxAbstractActionModel action)
    {
        action.setType(ActionType.PLAIN);
        this.actionPropertySetter.setValues(action);
    }


    @Required
    public void setCxActionDao(CxActionDao cxActionDao)
    {
        this.cxActionDao = cxActionDao;
    }


    protected CxActionDao getCxActionDao()
    {
        return this.cxActionDao;
    }


    protected ActionPropertySetter<CxAbstractActionModel> getActionPropertySetter()
    {
        return this.actionPropertySetter;
    }


    @Required
    public void setActionPropertySetter(ActionPropertySetter<CxAbstractActionModel> actionPropertySetter)
    {
        this.actionPropertySetter = actionPropertySetter;
    }


    public void deleteAction(CxAbstractActionModel action)
    {
        getModelService().remove(action);
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected Map<CxActionType, CxActionTypeDao> getActionTypeDaoMap()
    {
        return this.actionTypeDaoMap;
    }


    @Autowired(required = false)
    public void setActionTypeDaoMap(Map<String, CxActionTypeDao> actionTypeDaoMap)
    {
        this
                        .actionTypeDaoMap = (Map<CxActionType, CxActionTypeDao>)actionTypeDaoMap.values().stream().collect(Collectors.toMap(CxActionTypeDao::getSupportedType, e -> e));
    }
}
