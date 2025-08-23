package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;

public class CmsWizardUtils
{
    private static final Logger LOG = Logger.getLogger(CmsWizardUtils.class);
    protected static final String WIZARD_CONFIG = "wizardConfig";


    public static void openRestrictionWizard(String title, Component parent, TypedObject item, PropertyDescriptor propDescr, UISession session)
    {
        String valueTypeCode = session.getTypeService().getValueTypeCode(propDescr);
        ObjectTemplate template = session.getTypeService().getObjectTemplate(valueTypeCode);
        try
        {
            WizardConfiguration wizardConfiguration = (WizardConfiguration)session.getUiConfigurationService().getComponentConfiguration(template, "wizardConfig", WizardConfiguration.class);
            if(session.getCurrentPerspective() instanceof de.hybris.platform.cmscockpit.session.impl.CmsCockpitPerspective)
            {
                CmsContentEdiotrRelatedTypeWizard wizard = new CmsContentEdiotrRelatedTypeWizard(parent.getPage().getFirstRoot(), template, item, propDescr);
                wizard.setWizardConfiguration(wizardConfiguration);
                wizard.setBrowserModel(
                                UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser());
                wizard.start(title);
            }
            else
            {
                LOG.warn("Can not create item. Reason: Current perspective not a CmsCockpitPerspective.");
            }
        }
        catch(UIConfigurationException e)
        {
            LOG.warn("A configuration is missing for template" + template.getCode() + "!", (Throwable)e);
        }
    }
}
