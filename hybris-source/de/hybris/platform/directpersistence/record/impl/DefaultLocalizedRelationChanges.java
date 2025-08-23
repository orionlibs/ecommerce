package de.hybris.platform.directpersistence.record.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.RelationChanges;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DefaultLocalizedRelationChanges implements LocalizedRelationChanges
{
    private Map<Locale, DefaultRelationChanges> relationChangeSet;
    private final RelationMetaInfo relationMetaInfo;


    public DefaultLocalizedRelationChanges(RelationMetaInfo relationMetaInfo)
    {
        this.relationMetaInfo = relationMetaInfo;
    }


    public Map<Locale, DefaultRelationChanges> getRelationChanges()
    {
        if(this.relationChangeSet == null)
        {
            return Collections.emptyMap();
        }
        return (Map<Locale, DefaultRelationChanges>)ImmutableMap.copyOf(this.relationChangeSet);
    }


    public DefaultRelationChanges getRelationChangeForLanguage(Locale locale)
    {
        ensureRelationChangeSetCreated();
        return this.relationChangeSet.get(locale);
    }


    public void put(Locale locale, DefaultRelationChanges changeSet)
    {
        ensureRelationChangeSetCreated();
        this.relationChangeSet.put(locale, changeSet);
    }


    private void ensureRelationChangeSetCreated()
    {
        if(this.relationChangeSet == null)
        {
            this.relationChangeSet = new TreeMap<>((Comparator<? super Locale>)new RelationChangesComparator(this));
        }
    }


    public <V> V accept(RelationMetaInfo metaInfo, RelationChanges.RelationChangesVisitor<V> visitor)
    {
        return (V)visitor.visit(metaInfo, this);
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if(this.relationChangeSet != null)
        {
            for(Map.Entry<Locale, DefaultRelationChanges> relations : this.relationChangeSet.entrySet())
            {
                builder.append("Processing relation for language: " + relations.getKey() + "\n");
                builder.append(((DefaultRelationChanges)relations.getValue()).toString());
                builder.append("Processing relation for language: " + relations.getKey() + " finished\n");
            }
        }
        return builder.toString();
    }


    public void groupOrderInformation()
    {
        if(this.relationChangeSet != null)
        {
            for(DefaultRelationChanges relationChanges : this.relationChangeSet.values())
            {
                relationChanges.groupOrderInformation();
            }
        }
    }


    public RelationMetaInfo getRelationMetaInfo()
    {
        return this.relationMetaInfo;
    }
}
