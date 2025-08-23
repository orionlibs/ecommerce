package de.hybris.platform.cockpit.wizards.generic;

import org.zkoss.zk.ui.Component;

public interface GenericItemWizardPage
{
    Component createRepresentationItself();


    GenericItemWizard getWizard();
}
