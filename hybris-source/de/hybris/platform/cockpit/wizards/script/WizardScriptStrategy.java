package de.hybris.platform.cockpit.wizards.script;

import java.util.Map;

public interface WizardScriptStrategy
{
    void evaluateScript(String paramString, Map<String, Object> paramMap);
}
