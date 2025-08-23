package de.hybris.platform.cockpit.model.editor.search;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import java.util.Map;

public interface ConditionUIEditor extends UIEditor
{
    ConditionValueContainer getValue();


    Map<String, Object> getParameters();
}
