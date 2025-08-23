package de.hybris.platform.personalizationfacades.action.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.action.ActionFacade;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationfacades.data.ActionFullData;
import de.hybris.platform.personalizationservices.action.CxActionService;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultActionFacade extends AbstractBaseFacade implements ActionFacade
{
    private static final String ACTION = "Action";
    private static final String VARIATION = "Variation";
    private static final String CUSTOMIZATION = "Customization";
    private CxCustomizationService customizationService;
    private CxVariationService variationService;
    private CxActionService actionService;
    private Converter<CxAbstractActionModel, ActionData> actionConverter;
    private Converter<ActionData, CxAbstractActionModel> actionReverseConverter;
    private Converter<CxAbstractActionModel, ActionFullData> actionFullConverter;
    private KeyGenerator actionCodeGenerator;


    public List<ActionData> getActions(String customizationCode, String variationCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        Collection<CxAbstractActionModel> actions = filterActions(variationModel.getActions(), catalogVersion);
        return this.actionConverter.convertAll(actions);
    }


    protected Collection<CxAbstractActionModel> filterActions(Collection<CxAbstractActionModel> actions, CatalogVersionModel catalogVersion)
    {
        if(actions == null || actions.isEmpty())
        {
            return Collections.emptyList();
        }
        return (Collection<CxAbstractActionModel>)actions.stream()
                        .filter(a -> catalogVersion.equals(a.getCatalogVersion()))
                        .collect(Collectors.toList());
    }


    public ActionData getAction(String customizationCode, String variationCode, String actionCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        validateCode("Action", actionCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        return getAction(customizationCode, variationCode, actionCode, catalogVersion);
    }


    protected ActionData getAction(String customizationCode, String variationCode, String actionCode, CatalogVersionModel catalogVersion)
    {
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        Optional<CxAbstractActionModel> actionModel = this.actionService.getAction(actionCode, variationModel);
        CxAbstractActionModel action = actionModel.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Action", actionCode));
        return (ActionData)this.actionConverter.convert(action);
    }


    public SearchPageData<ActionFullData> getActions(CxActionType type, String catalogId, String catalogVersionId, Map<String, String> searchCriteria, SearchPageData<?> pagination)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        SearchPageData<CxAbstractActionModel> actions = this.actionService.getActions(type, catalogVersion, searchCriteria, pagination);
        Objects.requireNonNull(this.actionFullConverter);
        return convertSearchPage(actions, this.actionFullConverter::convertAll);
    }


    public ActionData createAction(String customizationCode, String variationCode, ActionData actionData, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        setDefaultPropertiesForData(actionData);
        Optional<CxAbstractActionModel> existingAction = this.actionService.getAction(actionData.getCode(), variationModel);
        existingAction.ifPresent(a -> throwAlreadyExists("Action", actionData.getCode()));
        CxAbstractActionModel actionModel = (CxAbstractActionModel)this.actionReverseConverter.convert(actionData);
        CxAbstractActionModel resultAction = this.actionService.createAction(actionModel, variationModel);
        return (ActionData)this.actionConverter.convert(resultAction);
    }


    public Collection<ActionData> createActions(String customizationCode, String variationCode, Collection<ActionData> actionsData, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        actionsData.forEach(this::setDefaultPropertiesForData);
        actionsData.stream()
                        .map(actionData -> this.actionService.getAction(actionData.getCode(), variationModel))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(AbstractActionModel::getCode)
                        .reduce((a, b) -> a + ", " + a)
                        .ifPresent(e -> throwAlreadyExists("Action", e));
        return (Collection<ActionData>)executeInTransaction(() -> {
            Objects.requireNonNull(this.actionReverseConverter);
            Objects.requireNonNull(this.actionConverter);
            return (List)actionsData.stream().map(this.actionReverseConverter::convert).map(()).map(this.actionConverter::convert).collect(Collectors.toList());
        } () -> getModelService().refresh(variationModel));
    }


    protected void setDefaultPropertiesForData(ActionData action)
    {
        if(StringUtils.isEmpty(action.getCode()))
        {
            action.setCode((String)this.actionCodeGenerator.generate());
        }
    }


    public ActionData updateAction(String customizationCode, String variationCode, String actionCode, ActionData action, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        validateCode("Action", actionCode);
        Assert.notNull(action, "Action data cannot be null");
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        action.setCode(actionCode);
        Optional<CxAbstractActionModel> existingAction = this.actionService.getAction(action.getCode(), variationModel);
        CxAbstractActionModel actionModel = existingAction.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Action", action.getCode()));
        actionModel = (CxAbstractActionModel)this.actionReverseConverter.convert(action, actionModel);
        getModelService().save(actionModel);
        return (ActionData)this.actionConverter.convert(actionModel);
    }


    public void deleteAction(String customizationCode, String variationCode, String actionCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        validateCode("Action", actionCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        Optional<CxAbstractActionModel> actionModel = this.actionService.getAction(actionCode, variationModel);
        CxAbstractActionModel action = actionModel.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Action", actionCode));
        this.actionService.deleteAction(action);
        getModelService().refresh(variationModel);
    }


    public void deleteActions(String customizationCode, String variationCode, Collection<String> actionCodes, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        if(actionCodes == null)
        {
            throw new IllegalArgumentException("actionCodes parameter can't be null");
        }
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variationModel = getVariation(customizationCode, variationCode, catalogVersion);
        executeInTransaction(() -> {
            Objects.requireNonNull(this.actionService);
            actionCodes.stream().map(()).filter(Optional::isPresent).map(Optional::get).forEach(this.actionService::deleteAction);
            return null;
        } () -> getModelService().refresh(variationModel));
        getModelService().refresh(variationModel);
    }


    protected CxVariationModel getVariation(String customizationCode, String variationCode, CatalogVersionModel catalogVersion)
    {
        Optional<CxCustomizationModel> customization = this.customizationService.getCustomization(customizationCode, catalogVersion);
        CxCustomizationModel customizationModel = customization.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
        Optional<CxVariationModel> variation = this.variationService.getVariation(variationCode, customizationModel);
        return variation.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Variation", variationCode));
    }


    @Required
    public void setActionConverter(Converter<CxAbstractActionModel, ActionData> actionConverter)
    {
        this.actionConverter = actionConverter;
    }


    protected Converter<CxAbstractActionModel, ActionData> getActionConverter()
    {
        return this.actionConverter;
    }


    @Required
    public void setActionReverseConverter(Converter<ActionData, CxAbstractActionModel> actionReverseConverter)
    {
        this.actionReverseConverter = actionReverseConverter;
    }


    protected Converter<ActionData, CxAbstractActionModel> getActionReverseConverter()
    {
        return this.actionReverseConverter;
    }


    @Required
    public void setActionService(CxActionService actionService)
    {
        this.actionService = actionService;
    }


    protected CxActionService getActionService()
    {
        return this.actionService;
    }


    @Required
    public void setVariationService(CxVariationService variationService)
    {
        this.variationService = variationService;
    }


    protected CxVariationService getVariationService()
    {
        return this.variationService;
    }


    @Required
    public void setActionCodeGenerator(KeyGenerator actionCodeGenerator)
    {
        this.actionCodeGenerator = actionCodeGenerator;
    }


    protected KeyGenerator getActionCodeGenerator()
    {
        return this.actionCodeGenerator;
    }


    @Required
    public void setCustomizationService(CxCustomizationService customizationService)
    {
        this.customizationService = customizationService;
    }


    protected CxCustomizationService getCustomizationService()
    {
        return this.customizationService;
    }


    @Required
    public void setActionFullConverter(Converter<CxAbstractActionModel, ActionFullData> actionFullConverter)
    {
        this.actionFullConverter = actionFullConverter;
    }


    protected Converter<CxAbstractActionModel, ActionFullData> getActionFullConverter()
    {
        return this.actionFullConverter;
    }
}
