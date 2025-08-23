package de.hybris.platform.personalizationsearchweb.queries;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

public class CxUpdateSearchProfileActionRankExecutor extends AbstractRestQueryExecutor
{
    public static final String CUSTOMIZATION = "customization";
    public static final String VARIATION = "variation";
    public static final String ACTIONS = "actions";
    public static final String RANK_BEFORE_ACTION = "rankBeforeAction";
    public static final String RANK_AFTER_ACTION = "rankAfterAction";
    public static final String COMMA_SEPARATOR = ",";
    private static final String[] REQUIRED_FIELDS = new String[] {"customization", "variation", "actions", "catalog", "catalogVersion"};
    private ModelService modelService;
    private CatalogVersionService catalogVersionService;
    private CxCustomizationService cxCustomizationService;
    private CxVariationService cxVariationService;


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        validateMissingField(errors, REQUIRED_FIELDS);
        boolean isRankBeforeActionValid = StringUtils.isNotBlank(params.get("rankBeforeAction"));
        boolean isRankAfterActionValid = StringUtils.isNotBlank(params.get("rankAfterAction"));
        if(isRankBeforeActionValid && isRankAfterActionValid)
        {
            errors.reject("Only one of the following parameters should be specified: rankBeforeAction,rankAfterAction");
        }
        if(!isRankBeforeActionValid && !isRankAfterActionValid)
        {
            errors.reject("One of the following parameters must be specified: rankBeforeAction,rankAfterAction");
        }
    }


    protected Object executeAfterValidation(Map<String, String> params)
    {
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion(params.get("catalog"), params
                        .get("catalogVersion"));
        Optional<CxCustomizationModel> customization = this.cxCustomizationService.getCustomization(params.get("customization"), catalogVersion);
        if(!customization.isPresent())
        {
            return Boolean.FALSE;
        }
        Optional<CxVariationModel> variation = this.cxVariationService.getVariation(params.get("variation"), customization.get());
        if(!variation.isPresent())
        {
            return Boolean.FALSE;
        }
        List<CxAbstractActionModel> actions = findActions(variation.get(), params);
        if(CollectionUtils.isEmpty(actions))
        {
            return Boolean.FALSE;
        }
        return moveActions(variation.get(), actions, params);
    }


    protected List<CxAbstractActionModel> findActions(CxVariationModel variation, Map<String, String> params)
    {
        return (List<CxAbstractActionModel>)Stream.<String>of(((String)params.get("actions")).split(",")).map(actionCode -> findAction(variation, actionCode))
                        .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }


    protected Optional<CxAbstractActionModel> findAction(CxVariationModel variation, String actionCode)
    {
        if(CollectionUtils.isNotEmpty(variation.getActions()))
        {
            return variation.getActions().stream().filter(action -> actionMatches(action, actionCode)).findFirst();
        }
        return Optional.empty();
    }


    protected int findActionIndex(List<CxAbstractActionModel> actions, String actionCode)
    {
        if(CollectionUtils.isNotEmpty(actions))
        {
            int index = 0;
            for(CxAbstractActionModel action : actions)
            {
                if(actionMatches(action, actionCode))
                {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }


    protected boolean actionMatches(CxAbstractActionModel action, String actionCode)
    {
        return (StringUtils.isNotBlank(actionCode) && Objects.equals(action.getCode(), actionCode));
    }


    protected int calculateNewActionIndex(List<CxAbstractActionModel> actions, Map<String, String> params)
    {
        String rankBeforeAction = params.get("rankBeforeAction");
        if(StringUtils.isNotBlank(rankBeforeAction))
        {
            int index = findActionIndex(actions, rankBeforeAction);
            if(index == 0)
            {
                return 0;
            }
            if(index > 0)
            {
                return index;
            }
            return -1;
        }
        String rankAfterAction = params.get("rankAfterAction");
        if(StringUtils.isNotBlank(rankAfterAction))
        {
            int index = findActionIndex(actions, rankAfterAction);
            if(index >= 0)
            {
                return index + 1;
            }
            return -1;
        }
        return -1;
    }


    protected Boolean moveActions(CxVariationModel variation, List<CxAbstractActionModel> actions, Map<String, String> params)
    {
        List<CxAbstractActionModel> newActions = new ArrayList<>(variation.getActions());
        newActions.removeAll(actions);
        int index = calculateNewActionIndex(newActions, params);
        if(index < 0 || index > newActions.size())
        {
            return Boolean.FALSE;
        }
        newActions.addAll(index, actions);
        variation.setActions(newActions);
        this.modelService.save(variation);
        return Boolean.TRUE;
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CxCustomizationService getCxCustomizationService()
    {
        return this.cxCustomizationService;
    }


    @Required
    public void setCxCustomizationService(CxCustomizationService cxCustomizationService)
    {
        this.cxCustomizationService = cxCustomizationService;
    }


    public CxVariationService getCxVariationService()
    {
        return this.cxVariationService;
    }


    @Required
    public void setCxVariationService(CxVariationService cxVariationService)
    {
        this.cxVariationService = cxVariationService;
    }
}
