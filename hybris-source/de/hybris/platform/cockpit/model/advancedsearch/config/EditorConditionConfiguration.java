package de.hybris.platform.cockpit.model.advancedsearch.config;

import java.util.List;

public interface EditorConditionConfiguration
{
    List<EditorConditionEntry> getConditionEntries(String paramString);


    String getDefaultConditions(String paramString);
}
