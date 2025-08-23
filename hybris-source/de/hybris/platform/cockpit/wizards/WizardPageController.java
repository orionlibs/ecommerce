package de.hybris.platform.cockpit.wizards;

import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;

public interface WizardPageController
{
    WizardPage getFirstPage(Wizard paramWizard);


    boolean validate(Wizard paramWizard, WizardPage paramWizardPage);


    void initPage(Wizard paramWizard, WizardPage paramWizardPage);


    WizardPage next(Wizard paramWizard, WizardPage paramWizardPage);


    WizardPage previous(Wizard paramWizard, WizardPage paramWizardPage);


    void cancel(Wizard paramWizard, WizardPage paramWizardPage);


    void done(Wizard paramWizard, WizardPage paramWizardPage) throws WizardConfirmationException;


    void beforeNext(Wizard paramWizard, WizardPage paramWizardPage);


    void beforeBack(Wizard paramWizard, WizardPage paramWizardPage);
}
