package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultSearchField;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;
import java.util.Map;

public interface SearchFieldConfiguration
{
    public static final String CONDITION_ENTRIES = "conditionEntries";
    public static final String ENTRY_LIST_MODE = "entryListMode";


    void setName(String paramString);


    String getName();


    void setVisible(boolean paramBoolean);


    boolean isVisible();


    void setSortDisabled(boolean paramBoolean);


    boolean isSortDisabled();


    DefaultSearchField getSearchField();


    PropertyDescriptor getPropertyDescriptor();


    String getEditor();


    void setEditor(String paramString);


    Map<String, String> getParameters();


    List<EditorConditionEntry> getConditionEntries();


    EntryListMode getEntryListMode();
}
