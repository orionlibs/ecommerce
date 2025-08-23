package de.hybris.platform.admincockpit.wizards;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;

public class ConstraintsMandatoryPage extends GenericItemMandatoryPage
{
    public WizardConfiguration getWizardConfiguration()
    {
        if(getWizard().getCurrentType() != null)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(
                            getWizard().getCurrentType().getCode());
            return (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
        }
        return null;
    }
}
