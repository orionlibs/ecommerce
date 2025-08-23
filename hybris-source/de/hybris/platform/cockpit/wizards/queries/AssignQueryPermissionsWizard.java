package de.hybris.platform.cockpit.wizards.queries;

import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;

public class AssignQueryPermissionsWizard extends GenericItemWizard
{
    public void doDone()
    {
        super.doDone();
        getCurrentController().done((Wizard)this, (WizardPage)getCurrentPage());
    }
}
