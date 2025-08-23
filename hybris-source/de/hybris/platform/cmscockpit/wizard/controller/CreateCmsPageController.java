package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.session.impl.CmsCockpitPerspective;
import de.hybris.platform.cmscockpit.wizard.page.CreatePageMandatoryPage;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;

public class CreateCmsPageController extends CmsPageController
{
    private boolean openInEditor = false;


    public void done(Wizard wizard, WizardPage page)
    {
        if(page instanceof CreatePageMandatoryPage)
        {
            this.openInEditor = ((CreatePageMandatoryPage)page).isOpenInEditor();
        }
        super.done(wizard, page);
    }


    protected void activateItemInEditor(UICockpitPerspective currentPerspective, TypedObject newItem)
    {
        if(this.openInEditor)
        {
            ((CmsCockpitPerspective)currentPerspective).activateItemInEditor(newItem);
        }
    }
}
