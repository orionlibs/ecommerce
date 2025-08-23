package de.hybris.platform.scripting.engine.content;

import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;

public interface AutoDisablingScriptContent extends ScriptContent
{
    AutoDisablingScriptStrategy getAutoDisablingScriptStrategy();
}
