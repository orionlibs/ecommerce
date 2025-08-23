package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public WizardConfig createWizardConfig()
    {
        return new WizardConfig();
    }
}
