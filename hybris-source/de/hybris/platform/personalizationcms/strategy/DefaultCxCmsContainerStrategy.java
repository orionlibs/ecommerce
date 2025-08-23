package de.hybris.platform.personalizationcms.strategy;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;
import de.hybris.platform.personalizationcms.data.CxCmsActionResult;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxCmsContainerStrategy implements CMSComponentContainerStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultCxCmsContainerStrategy.class);
    private CMSComponentService cmsComponentService;
    private CxService cxService;
    private UserService userService;
    private CatalogVersionService catalogVersionService;


    public List<AbstractCMSComponentModel> getDisplayComponentsForContainer(AbstractCMSComponentContainerModel container)
    {
        if(container instanceof CxCmsComponentContainerModel)
        {
            return getDisplayComponents((CxCmsComponentContainerModel)container);
        }
        return Collections.emptyList();
    }


    protected List<AbstractCMSComponentModel> getDisplayComponents(CxCmsComponentContainerModel container)
    {
        List<CxAbstractActionResult> cxActionResults = this.cxService.getActionResultsFromSession(this.userService.getCurrentUser());
        Optional<CxCmsActionResult> actionResult = cxActionResults.stream().filter(ar -> ar instanceof CxCmsActionResult).map(ar -> (CxCmsActionResult)ar).filter(ar -> containerMatches(ar, container)).filter(this::catalogVersionFilter).findFirst();
        SimpleCMSComponentModel component = getComponent(actionResult.orElse(null), container);
        component = proxyComponent(component, container, (CxAbstractActionResult)actionResult.orElse(null));
        return (component != null) ? (List)Collections.singletonList(component) : Collections.<AbstractCMSComponentModel>emptyList();
    }


    protected boolean containerMatches(CxCmsActionResult ar, CxCmsComponentContainerModel container)
    {
        return StringUtils.equals(container.getSourceId(), ar.getContainerId());
    }


    protected boolean catalogVersionFilter(CxCmsActionResult ar)
    {
        return !getCatalogVersionsForComponent(ar).isEmpty();
    }


    protected Collection<CatalogVersionModel> getCatalogVersionsForComponent(CxCmsActionResult actionResult)
    {
        if(StringUtils.isNotEmpty(actionResult.getComponentCatalog()))
        {
            return this.catalogVersionService.getSessionCatalogVersionsForCatalog(actionResult.getComponentCatalog());
        }
        return this.catalogVersionService.getSessionCatalogVersions();
    }


    protected SimpleCMSComponentModel proxyComponent(SimpleCMSComponentModel component, CxCmsComponentContainerModel container, CxAbstractActionResult actionResult)
    {
        String uid = container.getUid();
        String sourceId = container.getSourceId();
        String type = container.getItemtype();
        String actionCode = (actionResult != null) ? actionResult.getActionCode() : null;
        String customizationCode = (actionResult != null) ? actionResult.getCustomizationCode() : null;
        String variationCode = (actionResult != null) ? actionResult.getVariationCode() : null;
        return (SimpleCMSComponentModel)Enhancer.create(component.getClass(), new Class[] {CmsCxAware.class}, (Callback)new CmsComponentInterceptor(component, uid, type, actionCode, customizationCode, variationCode, sourceId));
    }


    protected Optional<CxCmsActionResult> getFirstActionResult(List<CxAbstractActionResult> cxActionResults, Collection<String> validActionCodes)
    {
        return cxActionResults.stream()
                        .filter(ar -> ar instanceof CxCmsActionResult)
                        .map(ar -> (CxCmsActionResult)ar)
                        .filter(ar -> validActionCodes.contains(ar.getActionCode()))
                        .findFirst();
    }


    protected SimpleCMSComponentModel getComponent(CxCmsActionResult actionResult, CxCmsComponentContainerModel container)
    {
        SimpleCMSComponentModel result = null;
        if(actionResult != null)
        {
            try
            {
                String componentId = actionResult.getComponentId();
                Collection<CatalogVersionModel> componentCatalogVersions = getCatalogVersionsForComponent(actionResult);
                AbstractCMSComponentModel abstractCMSComponent = this.cmsComponentService.getAbstractCMSComponent(componentId, componentCatalogVersions);
                if(abstractCMSComponent instanceof SimpleCMSComponentModel)
                {
                    result = (SimpleCMSComponentModel)abstractCMSComponent;
                }
            }
            catch(CMSItemNotFoundException e)
            {
                LOG.warn("Action result (" + actionResult.getActionCode() + ") points to nonexisting cms component (" + actionResult
                                .getComponentId() + ")", (Throwable)e);
            }
        }
        return (result != null) ? result : getDefault((AbstractCMSComponentContainerModel)container);
    }


    protected SimpleCMSComponentModel getDefault(AbstractCMSComponentContainerModel container)
    {
        if(container instanceof CxCmsComponentContainerModel)
        {
            return ((CxCmsComponentContainerModel)container).getDefaultCmsComponent();
        }
        return null;
    }


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setCmsComponentService(CMSComponentService cmsComponentService)
    {
        this.cmsComponentService = cmsComponentService;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
