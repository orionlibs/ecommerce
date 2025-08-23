package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import org.zkoss.util.resource.Labels;

public class DefaultGenericTypeSelectorPageController extends DefaultPageController
{
    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = false;
        if(page instanceof GenericTypeSelectorPage)
        {
            ret = (((GenericTypeSelectorPage)page).getChosenType() != null);
            if(!ret)
            {
                Message msg = new Message(3, Labels.getLabel("wizard.common.missingType"), null);
                wizard.addMessage(msg);
            }
        }
        return ret;
    }
}
