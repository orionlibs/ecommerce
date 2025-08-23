package de.hybris.platform.personalizationservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.CxContext;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.action.CxActionService;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.CxProcessService;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.servicelayer.action.ActionService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxService implements CxService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxService.class);
    private UserService userService;
    private ActionService actionService;
    private CxActionService cxActionService;
    private CxVariationService cxVariationService;
    private CxActionResultService actionResultService;
    private CxProcessService cxProcessService;
    private CxCatalogService cxCatalogService;


    public List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel user)
    {
        return startPersonalizationCalculationProcesses(user, this.cxCatalogService.getConfiguredCatalogVersions());
    }


    public List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel user, CxCalculationContext context)
    {
        if(context != null)
        {
            Map<String, Object> processParameters = new HashMap<>();
            processParameters.put("calcContextProcessParameter", context);
            return startPersonalizationCalculationProcesses(user, this.cxCatalogService.getConfiguredCatalogVersions(), processParameters);
        }
        return startPersonalizationCalculationProcesses(user, this.cxCatalogService.getConfiguredCatalogVersions(), null);
    }


    public List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel user, Collection<CatalogVersionModel> catalogVersions)
    {
        return startPersonalizationCalculationProcesses(user, catalogVersions, null);
    }


    protected List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel user, Collection<CatalogVersionModel> catalogVersions, Map<String, Object> processParameters)
    {
        if(catalogVersions == null || catalogVersions.isEmpty())
        {
            LOG.debug("No catalog version provided. Personalization process not started.");
            return Collections.emptyList();
        }
        Objects.requireNonNull(this.cxCatalogService);
        return (List<CxPersonalizationProcessModel>)catalogVersions.stream().filter(this.cxCatalogService::isPersonalizationInCatalog)
                        .map(cv -> this.cxProcessService.startPersonalizationCalculationProcess(user, cv, processParameters))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    public void calculateAndStoreDefaultPersonalization(Collection<CatalogVersionModel> catalogVersions)
    {
        catalogVersions.forEach(cv -> calculateAndStoreDefaultPersonalization(cv));
    }


    protected void calculateAndStoreDefaultPersonalization(CatalogVersionModel catalogVersion)
    {
        CustomerModel customerModel = this.userService.getAnonymousUser();
        List<CxVariationModel> variations = this.cxVariationService.getActiveVariations((UserModel)customerModel, catalogVersion);
        List<CxAbstractActionResult> actionResults = calculateActionResults((UserModel)customerModel, variations);
        this.actionResultService.storeDefaultActionResults((UserModel)customerModel, catalogVersion, actionResults);
    }


    public void calculateAndStorePersonalization(UserModel user)
    {
        this.cxCatalogService.getConfiguredCatalogVersions().forEach(cv -> calculateAndStorePersonalization(user, cv));
    }


    public void calculateAndStorePersonalization(UserModel user, CatalogVersionModel catalogVersion)
    {
        List<CxVariationModel> variations = this.cxVariationService.getActiveVariations(user, catalogVersion);
        List<CxAbstractActionResult> actionResults = calculateActionResults(user, variations);
        this.actionResultService.storeActionResults(user, catalogVersion, actionResults);
    }


    public void loadPersonalizationInSession(UserModel user)
    {
        loadPersonalizationInSession(user, this.cxCatalogService.getConfiguredCatalogVersions());
    }


    public void loadPersonalizationInSession(UserModel user, Collection<CatalogVersionModel> catalogVersions)
    {
        this.actionResultService.loadActionResultsInSession(user, catalogVersions);
    }


    public void calculateAndLoadPersonalizationInSession(UserModel user)
    {
        this.cxCatalogService.getConfiguredCatalogVersions().forEach(cv -> calculateAndLoadPersonalizationInSession(user, cv));
    }


    public void calculateAndLoadPersonalizationInSession(UserModel user, CatalogVersionModel catalogVersion)
    {
        List<CxVariationModel> variations = this.cxVariationService.getActiveVariations(user, catalogVersion);
        List<CxAbstractActionResult> actionResults = calculateActionResults(user, variations);
        this.actionResultService.setActionResultsInSession(user, catalogVersion, actionResults);
    }


    public void calculateAndLoadPersonalizationInSession(UserModel cxContextUser, CatalogVersionModel catalogVersion, Collection<CxVariationModel> variations)
    {
        List<CxAbstractActionResult> actionResults = calculateActionResults(cxContextUser, variations);
        this.actionResultService.setActionResultsInSession(cxContextUser, catalogVersion, actionResults);
    }


    public List<CxAbstractActionResult> getActionResultsFromSession(UserModel user)
    {
        return (List<CxAbstractActionResult>)this.cxCatalogService.getConfiguredCatalogVersions().stream()
                        .flatMap(cv -> this.actionResultService.getActionResults(user, cv).stream())
                        .collect(Collectors.toList());
    }


    public List<CxAbstractActionResult> getActionResultsFromSession(UserModel user, CatalogVersionModel catalogVersion)
    {
        return this.actionResultService.getActionResults(user, catalogVersion);
    }


    protected List<CxAbstractActionResult> calculateActionResults(UserModel user, Collection<CxVariationModel> variations)
    {
        List<CxAbstractActionModel> actionList = this.cxActionService.getActionsForVariations(variations);
        return (List<CxAbstractActionResult>)actionList.stream()
                        .map(action -> executeAction(action, user))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    protected CxAbstractActionResult executeAction(CxAbstractActionModel action, UserModel user)
    {
        CxContext context = createActionContext(user);
        this.actionService.prepareAndTriggerAction((AbstractActionModel)action, context);
        return context.getActionResult();
    }


    protected CxContext createActionContext(UserModel user)
    {
        CxContext context = new CxContext();
        context.setUser(user);
        return context;
    }


    public void clearPersonalizationInSession(UserModel user, CatalogVersionModel catalogVersion)
    {
        this.actionResultService.clearActionResultsInSession(user, catalogVersion);
    }


    @Required
    public void setActionService(ActionService actionService)
    {
        this.actionService = actionService;
    }


    @Required
    public void setCxVariationService(CxVariationService cxVariationService)
    {
        this.cxVariationService = cxVariationService;
    }


    @Required
    public void setCxActionResultService(CxActionResultService actionResultService)
    {
        this.actionResultService = actionResultService;
    }


    @Required
    public void setCxActionService(CxActionService cxActionService)
    {
        this.cxActionService = cxActionService;
    }


    @Required
    public void setCxProcessService(CxProcessService cxProcessService)
    {
        this.cxProcessService = cxProcessService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    protected ActionService getActionService()
    {
        return this.actionService;
    }


    protected CxActionService getCxActionService()
    {
        return this.cxActionService;
    }


    protected CxVariationService getCxVariationService()
    {
        return this.cxVariationService;
    }


    protected CxActionResultService getActionResultService()
    {
        return this.actionResultService;
    }


    protected CxProcessService getCxProcessService()
    {
        return this.cxProcessService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
