package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;

public interface CmsPageSelectorPage extends CmsWizardPage
{
    void setDisplaySubtypes(boolean paramBoolean);


    void setController(WizardPageController paramWizardPageController);


    void setRootSelectorType(ObjectType paramObjectType);


    void setWizard(Wizard paramWizard);
}
