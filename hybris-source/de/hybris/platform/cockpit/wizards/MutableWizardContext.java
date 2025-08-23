package de.hybris.platform.cockpit.wizards;

import java.util.Map;

public interface MutableWizardContext extends WizardContext
{
    void setAttribute(String paramString, Object paramObject);


    Map<String, Object> getAttributes();
}
