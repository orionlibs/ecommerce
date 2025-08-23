package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.wizard.page.ReferencePage;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import org.zkoss.util.resource.Labels;

public class CmsReferencePageController extends CmsPageController
{
    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = false;
        if(page instanceof ReferencePage)
        {
            ret = (((ReferencePage)page).getChosenReference() != null);
            if(!ret)
            {
                Message msg = new Message(3, Labels.getLabel("wizard.common.missingReference"), null);
                wizard.addMessage(msg);
            }
        }
        return ret;
    }
}
