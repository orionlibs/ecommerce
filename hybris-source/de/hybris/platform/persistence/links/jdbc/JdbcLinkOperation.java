package de.hybris.platform.persistence.links.jdbc;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperation;
import java.util.Map;

public abstract class JdbcLinkOperation implements LinkOperation
{
    private final String relationCode;
    private final Iterable<Long> parentPKs;
    private final Map<Long, Iterable<Long>> languageToChildPKs;
    private final boolean isParentSource;
    private final boolean isMarkAsModified;
    private final JdbcLinkOperationExecutor executor;
    private final boolean targetToSourceOrdered;
    private final boolean sourceToTargetOrdered;


    public JdbcLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Iterable<Item> parents, Map<Language, ? extends Iterable<Item>> children, boolean isParentSource, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        Preconditions.checkNotNull(executor, "executor can't be null");
        Preconditions.checkNotNull(relationCode, "relationCode can't be null");
        Preconditions.checkNotNull(parents, "parents can't be null");
        Preconditions.checkNotNull(children, "children can't be null");
        this.executor = executor;
        this.relationCode = relationCode;
        this.parentPKs = extractParentPKsFrom(parents);
        this.languageToChildPKs = extractLanguageToChildPKsFrom(children);
        this.isParentSource = isParentSource;
        this.isMarkAsModified = isMarkAsModified;
        this.targetToSourceOrdered = targetToSourceOrdered;
        this.sourceToTargetOrdered = sourceToTargetOrdered;
    }


    protected JdbcLinkOperationExecutor getExecutor()
    {
        return this.executor;
    }


    public String getRelationCode()
    {
        return this.relationCode;
    }


    public Iterable<Long> getParentPKs()
    {
        return this.parentPKs;
    }


    public boolean isParentSource()
    {
        return this.isParentSource;
    }


    public boolean isMarkAsModified()
    {
        return this.isMarkAsModified;
    }


    public int getNumberOfLanguages()
    {
        return this.languageToChildPKs.keySet().size();
    }


    public Iterable<Long> getLanguagePKs()
    {
        return this.languageToChildPKs.keySet();
    }


    public Iterable<Long> getChildPKs(Long languagePK)
    {
        Preconditions.checkNotNull(languagePK);
        return this.languageToChildPKs.get(languagePK);
    }


    public boolean isTargetToSourceOrdered()
    {
        return this.targetToSourceOrdered;
    }


    public boolean isSourceToTargetOrdered()
    {
        return this.sourceToTargetOrdered;
    }


    protected static Language normalize(Language language)
    {
        return (language == null) ? JdbcLinksSupport.NONE_LANGUAGE : language;
    }


    protected static <T> Iterable<T> emptyIfNull(Iterable<T> input)
    {
        return (input == null) ? (Iterable<T>)ImmutableList.of() : input;
    }


    private static Iterable<Long> extractParentPKsFrom(Iterable<Item> parents)
    {
        ImmutableSet.Builder<Long> parentPKsBuilder = ImmutableSet.builder();
        for(Item parent : parents)
        {
            Preconditions.checkNotNull(parent, "Any of parent can't be null");
            Preconditions.checkNotNull(parent.getPK(), "All parent items must have PK");
            parentPKsBuilder.add(parent.getPK().getLong());
        }
        ImmutableSet<Long> allParentPKs = parentPKsBuilder.build();
        Preconditions.checkArgument(!allParentPKs.isEmpty(), "At least one parent must be given");
        return (Iterable<Long>)allParentPKs;
    }


    private static Map<Long, Iterable<Long>> extractLanguageToChildPKsFrom(Map<Language, ? extends Iterable<Item>> children)
    {
        ImmutableMap.Builder<Long, Iterable<Long>> languagesToChildPKsBuilder = ImmutableMap.builder();
        for(Map.Entry<Language, ? extends Iterable<Item>> languageToChildrenEntry : children.entrySet())
        {
            PK languagePK = normalize(languageToChildrenEntry.getKey()).getPK();
            Preconditions.checkNotNull(languagePK, "language must have PK");
            Iterable<Item> localizedChildren = emptyIfNull(languageToChildrenEntry.getValue());
            ImmutableList.Builder<Long> childPKsBuilder = ImmutableList.builder();
            for(Item child : localizedChildren)
            {
                Preconditions.checkNotNull(child, "Child item can't be null");
                Preconditions.checkNotNull(child.getPK(), "child must have PK");
                childPKsBuilder.add(child.getPK().getLong());
            }
            languagesToChildPKsBuilder.put(languagePK.getLong(), childPKsBuilder.build());
        }
        return (Map<Long, Iterable<Long>>)languagesToChildPKsBuilder.build();
    }
}
