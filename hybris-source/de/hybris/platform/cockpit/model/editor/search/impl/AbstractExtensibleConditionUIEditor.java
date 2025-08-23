package de.hybris.platform.cockpit.model.editor.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionConfiguration;
import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.ShortcutConditionEntry;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ValuePair;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;

public abstract class AbstractExtensibleConditionUIEditor extends AbstractConditionUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractExtensibleConditionUIEditor.class);
    private EditorConditionConfiguration editorConditionConfig = null;
    private List<EditorConditionEntry> conditionEntries = null;


    protected List<EditorConditionEntry> getConditionEntries()
    {
        if(this.conditionEntries == null)
        {
            this.conditionEntries = new ArrayList<>();
            EditorConditionConfiguration config = getEditorConditionConfiguration();
            if(config != null)
            {
                this.conditionEntries.addAll(config.getConditionEntries(getValueType()));
                this.conditionEntries.add(new ClearConditionEntry());
            }
        }
        return this.conditionEntries;
    }


    protected EditorConditionConfiguration getEditorConditionConfiguration()
    {
        if(this.editorConditionConfig == null)
        {
            this.editorConditionConfig = (EditorConditionConfiguration)SpringUtil.getBean("editorConditionConfiguration");
        }
        return this.editorConditionConfig;
    }


    protected EditorConditionEntry getConditionEntry(Operator operator)
    {
        if(operator == null)
        {
            return null;
        }
        for(EditorConditionEntry entry : getConditionEntries())
        {
            if(StringUtils.equalsIgnoreCase(entry.getOperator(), operator.getQualifier()))
            {
                return entry;
            }
        }
        return null;
    }


    protected String getDisplayLabel(Object value)
    {
        if(value instanceof TypedObject)
        {
            return UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel((TypedObject)value);
        }
        if(value == null)
        {
            return "-";
        }
        if(value instanceof java.util.Date)
        {
            return
                            DateFormat.getDateTimeInstance(3, 3, UISessionUtils.getCurrentSession().getLocale()).format(value);
        }
        return value.toString();
    }


    protected void filterConditionEntries(Map<String, ? extends Object> parameters)
    {
        String defaultConditions = getEditorConditionConfiguration().getDefaultConditions(getValueType());
        List<EditorConditionEntry> filteredEntries = new ArrayList<>();
        List<String> defaultConditionList = parseConditionList(defaultConditions);
        List<EditorConditionEntry> allEntries = getConditionEntries();
        Map<String, EditorConditionEntry> entryMap = new HashMap<>();
        for(EditorConditionEntry editorConditionEntry : allEntries)
        {
            entryMap.put(StringUtils.lowerCase(editorConditionEntry.getOperator()), editorConditionEntry);
        }
        for(String condQuali : defaultConditionList)
        {
            EditorConditionEntry editorConditionEntry = entryMap.get(StringUtils.lowerCase(condQuali));
            if(editorConditionEntry != null)
            {
                filteredEntries.add(editorConditionEntry);
                continue;
            }
            LOG.warn("No editor condition entry defined for operator '" + StringUtils.lowerCase(condQuali) + "', ignoring.");
        }
        if(parameters != null)
        {
            List<EditorConditionEntry> configuredConditionEntries = (List<EditorConditionEntry>)parameters.get("conditionEntries");
            SearchFieldConfiguration.EntryListMode entryListMode = (SearchFieldConfiguration.EntryListMode)parameters.get("entryListMode");
            if(configuredConditionEntries != null && entryListMode != null)
            {
                if(SearchFieldConfiguration.EntryListMode.REPLACE.equals(entryListMode))
                {
                    filteredEntries.clear();
                    for(EditorConditionEntry configuredEntry : configuredConditionEntries)
                    {
                        EditorConditionEntry editorConditionEntry = entryMap.get(configuredEntry.getOperator());
                        if(editorConditionEntry == null)
                        {
                            LOG.warn("Condition entry for operator '" + configuredEntry.getOperator() + "' doesn't exist, ignoring.");
                            continue;
                        }
                        if(configuredEntry instanceof ShortcutConditionEntry)
                        {
                            ((ShortcutConditionEntry)configuredEntry).setTargetConditionEntry(editorConditionEntry);
                            filteredEntries.add(configuredEntry);
                            continue;
                        }
                        filteredEntries.add(editorConditionEntry);
                    }
                }
                else if(SearchFieldConfiguration.EntryListMode.APPEND.equals(entryListMode))
                {
                    List<ValuePair<EditorConditionEntry, Integer>> insertLaterList = new ArrayList<>();
                    for(EditorConditionEntry configuredEntry : configuredConditionEntries)
                    {
                        EditorConditionEntry editorConditionEntry = entryMap.get(configuredEntry.getOperator());
                        int index = configuredEntry.getIndex();
                        if(editorConditionEntry != null)
                        {
                            if(!filteredEntries.contains(editorConditionEntry))
                            {
                                EditorConditionEntry toAdd;
                                if(configuredEntry instanceof ShortcutConditionEntry)
                                {
                                    ((ShortcutConditionEntry)configuredEntry).setTargetConditionEntry(editorConditionEntry);
                                    toAdd = configuredEntry;
                                }
                                else
                                {
                                    toAdd = editorConditionEntry;
                                }
                                if(index >= 0)
                                {
                                    insertLaterList.add(new ValuePair(toAdd, Integer.valueOf(index)));
                                    continue;
                                }
                                filteredEntries.add(toAdd);
                            }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(insertLaterList))
                    {
                        Collections.sort(insertLaterList, (Comparator<? super ValuePair<EditorConditionEntry, Integer>>)new Object(this));
                        for(ValuePair<EditorConditionEntry, Integer> valuePair : insertLaterList)
                        {
                            int intValue = ((Integer)valuePair.getSecond()).intValue();
                            if(intValue < filteredEntries.size())
                            {
                                filteredEntries.add(intValue, (EditorConditionEntry)valuePair.getFirst());
                                continue;
                            }
                            filteredEntries.add((EditorConditionEntry)valuePair.getFirst());
                        }
                    }
                }
                else if(SearchFieldConfiguration.EntryListMode.EXCLUDE.equals(entryListMode))
                {
                    for(EditorConditionEntry configuredEntry : configuredConditionEntries)
                    {
                        EditorConditionEntry editorConditionEntry = entryMap.get(configuredEntry.getOperator());
                        if(editorConditionEntry != null)
                        {
                            filteredEntries.remove(editorConditionEntry);
                        }
                    }
                }
            }
        }
        if(filteredEntries.isEmpty())
        {
            LOG.warn("No valid entry in the list, ignoring filtering.");
        }
        else
        {
            this.conditionEntries = filteredEntries;
            this.conditionEntries.add(new ClearConditionEntry());
        }
    }


    protected void filterConditionEntries(String whiteList, String blackList)
    {
        String defaultConditions = getEditorConditionConfiguration().getDefaultConditions(getValueType());
        String addToWhiteList = null;
        String baseList = defaultConditions;
        if(whiteList != null)
        {
            if(whiteList.trim().startsWith("+"))
            {
                addToWhiteList = whiteList.substring(1);
            }
            else
            {
                baseList = whiteList;
            }
        }
        if(baseList == null && blackList == null)
        {
            return;
        }
        List<EditorConditionEntry> allEntries = getConditionEntries();
        Map<String, EditorConditionEntry> entryMap = new HashMap<>();
        for(EditorConditionEntry editorConditionEntry : allEntries)
        {
            entryMap.put(StringUtils.lowerCase(editorConditionEntry.getOperator()), editorConditionEntry);
        }
        List<EditorConditionEntry> definedEntries = new ArrayList<>();
        List<String> conditionWhiteList = parseConditionList(baseList);
        if(addToWhiteList != null)
        {
            List<String> addList = parseConditionList(addToWhiteList);
            for(String string : addList)
            {
                if(!conditionWhiteList.contains(string))
                {
                    conditionWhiteList.add(string);
                }
            }
        }
        for(String condQuali : conditionWhiteList)
        {
            EditorConditionEntry editorConditionEntry = entryMap.get(StringUtils.lowerCase(condQuali));
            if(editorConditionEntry != null)
            {
                definedEntries.add(editorConditionEntry);
                continue;
            }
            LOG.warn("No editor condition entry defined for operator '" + StringUtils.lowerCase(condQuali) + "', ignoring.");
        }
        List<EditorConditionEntry> filteredEntries = definedEntries;
        if(blackList != null)
        {
            filteredEntries = new ArrayList<>();
            Set<String> conditionBlackSet = new HashSet<>(parseConditionList(blackList));
            for(EditorConditionEntry entry : definedEntries)
            {
                if(!conditionBlackSet.contains(entry.getOperator()))
                {
                    filteredEntries.add(entry);
                }
            }
        }
        if(filteredEntries.isEmpty())
        {
            LOG.warn("No valid entry in the list, ignoring filtering.");
        }
        else
        {
            this.conditionEntries = filteredEntries;
            this.conditionEntries.add(new ClearConditionEntry());
        }
    }


    private List<String> parseConditionList(String listStr)
    {
        List<String> ret = new ArrayList<>();
        try
        {
            String[] split = listStr.split(",");
            for(String string : split)
            {
                ret.add(string.toLowerCase());
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.error("Could not parse condition list", e);
            }
        }
        return ret;
    }
}
