package de.hybris.platform.cockpit.wizards.resetconfigs;

import de.hybris.platform.cockpit.CockpitConfigurationService;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.cockpit.services.config.impl.UIComponentCache;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserModel;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

public class ResetConfigWizardController extends DefaultPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(ResetConfigWizardController.class);
    private CockpitConfigurationService cockpitConfigurationService;
    private UIComponentCache uiComponentCache;


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
        if(UISessionUtils.getCurrentSession().getUiAccessRightService()
                        .canWrite(UISessionUtils.getCurrentSession().getUser(), "cockpit.personalizedconfiguration"))
        {
            List<CockpitUIComponentConfigurationModel> configurations = new ArrayList<>();
            Object attribute = wizard.getWizardContext().getAttribute("selectedItems");
            if(attribute instanceof java.util.Collection)
            {
                try
                {
                    for(Object element : attribute)
                    {
                        configurations.add((CockpitUIComponentConfigurationModel)((Listitem)element).getValue());
                    }
                    this.cockpitConfigurationService.removeComponentConfigurations(configurations);
                    this.uiComponentCache.clear();
                    resetCachedColumnModel();
                    UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
                    UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().update();
                    UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
                }
                catch(ClassCastException e)
                {
                    LOG.error("Selected items were not of expected class.", e);
                    throw new WizardConfirmationException(e);
                }
            }
        }
        else
        {
            try
            {
                Messagebox.show(Labels.getLabel("cockpit.permissionerror.general"));
            }
            catch(InterruptedException e)
            {
                LOG.error("Error occured: ", e);
            }
        }
    }


    private void resetCachedColumnModel()
    {
        List<BrowserModel> browsers = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getBrowsers();
        if(browsers != null)
        {
            for(BrowserModel bm : browsers)
            {
                if(bm instanceof AbstractBrowserModel)
                {
                    AbstractBrowserModel abm = (AbstractBrowserModel)bm;
                    abm.setCacheView(null);
                }
            }
        }
    }


    public List<CockpitUIComponentConfigurationModel> getAvailablePersonalizedConfigs()
    {
        return this.cockpitConfigurationService.getDedicatedComponentConfigurationsForPrincipal((PrincipalModel)UISessionUtils.getCurrentSession()
                        .getUser());
    }


    @Required
    public void setCockpitConfigurationService(CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setUiComponentCache(UIComponentCache uiComponentCache)
    {
        this.uiComponentCache = uiComponentCache;
    }
}
