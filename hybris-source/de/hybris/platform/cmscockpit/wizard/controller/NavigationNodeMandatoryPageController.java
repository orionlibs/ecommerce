package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.wizard.DefaultNavigationNodeWizard;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.DefaultGenericItemMandatoryPageController;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;

public class NavigationNodeMandatoryPageController extends DefaultGenericItemMandatoryPageController
{
    private GenericItemWizard genericItemWizard;
    private DefaultNavigationNodeWizard parentWizard;


    public NavigationNodeMandatoryPageController()
    {
        this.genericItemWizard = null;
        this.parentWizard = null;
    }


    public NavigationNodeMandatoryPageController(GenericItemWizard genericItemWizard, DefaultNavigationNodeWizard parentWizard)
    {
        this.genericItemWizard = genericItemWizard;
        this.parentWizard = parentWizard;
    }


    public void done(Wizard wizard, WizardPage page)
    {
        try
        {
            createItem((Wizard)this.genericItemWizard, page);
            this.parentWizard.setResources(Collections.singletonList(this.genericItemWizard.getItem()));
            this.parentWizard.getDefaultController().done(wizard, page);
        }
        catch(WizardConfirmationException e)
        {
            this.parentWizard.addMessage(this.genericItemWizard.getMessages().iterator().next());
            this.parentWizard.updateView();
            throw e;
        }
    }


    public void initPage(Wizard wizard, WizardPage page)
    {
        this.parentWizard.setShowDone(!this.parentWizard.isMediaModeSelected());
        this.parentWizard.setShowNext(this.parentWizard.isMediaModeSelected());
    }


    public void beforeNext(Wizard wizard, WizardPage page)
    {
        try
        {
            if(this.parentWizard.isMediaModeSelected() && page instanceof GenericItemMandatoryPage && ((GenericItemMandatoryPage)page)
                            .getValue() == null)
            {
                TypedObject createdItem = createItem((Wizard)this.genericItemWizard, page);
                this.parentWizard.setResources(Collections.singletonList(createdItem));
            }
        }
        catch(WizardConfirmationException e)
        {
            this.parentWizard.addMessage(this.genericItemWizard.getMessages().iterator().next());
            throw e;
        }
    }


    public void beforeBack(Wizard wizard, WizardPage page)
    {
        if(CollectionUtils.isNotEmpty(this.genericItemWizard.getMessages()))
        {
            this.genericItemWizard.getMessages().clear();
        }
    }


    public GenericItemWizard getGenericItemWizard()
    {
        return this.genericItemWizard;
    }


    public void setGenericItemWizard(GenericItemWizard genericItemWizard)
    {
        this.genericItemWizard = genericItemWizard;
    }


    public DefaultNavigationNodeWizard getParentWizard()
    {
        return this.parentWizard;
    }


    public void setParentWizard(DefaultNavigationNodeWizard parentWizard)
    {
        this.parentWizard = parentWizard;
    }
}
