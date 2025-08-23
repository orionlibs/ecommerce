package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import org.zkoss.zk.ui.Component;

public interface CmsWizardPage extends WizardPage
{
    Component createRepresentationItself();


    CmsWizard getWizard();
}
