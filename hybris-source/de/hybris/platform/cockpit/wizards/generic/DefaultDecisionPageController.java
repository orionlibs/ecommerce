package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import org.zkoss.util.resource.Labels;

public class DefaultDecisionPageController extends DefaultPageController
{
    public WizardPage next(Wizard wizard, WizardPage page)
    {
        WizardPage ret = null;
        if(page instanceof AbstractGenericItemPage)
        {
            AbstractGenericItemPage decisionPage = (AbstractGenericItemPage)page;
            String nextWizardPageId = decisionPage.getNextPageWizardId();
            ret = wizard.getPage(nextWizardPageId);
            if(ret == null)
            {
                return ret = super.next(wizard, (WizardPage)decisionPage);
            }
        }
        return ret;
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = true;
        if(page instanceof DecisionPage)
        {
            DecisionPage decisionPage = (DecisionPage)page;
            if(decisionPage.getCurrentDecsison() == null)
            {
                Message msg = new Message(3, Labels.getLabel("wizard.decionpage.emptydecision"), null);
                wizard.addMessage(msg);
                ret = false;
            }
        }
        return ret;
    }
}
