package de.hybris.platform.cockpit.model.advancedsearch.config;

import java.util.List;
import java.util.Map;

public interface ShortcutConditionEntry
{
    List<Object> getConditionValues();


    String getLabel(String paramString);


    Map<String, String> getAllLabels();


    void setTargetConditionEntry(EditorConditionEntry paramEditorConditionEntry);


    EditorConditionEntry getTargetEntry();
}
