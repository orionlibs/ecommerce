package de.hybris.platform.cockpit.wizards.comments;

import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.impl.DefaultPage;
import org.zkoss.zk.ui.Component;

public class AddCommentItemPage extends DefaultPage
{
    private WizardPageController controller;


    public void renderView(Component parent)
    {
        WizardContext context = getWizard().getWizardContext();
        if(context != null)
        {
            super.renderView(parent);
        }
    }


    public WizardPageController getController()
    {
        if(this.controller == null)
        {
            this.controller = (WizardPageController)new Object(this);
        }
        return this.controller;
    }
}
