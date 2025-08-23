package de.hybris.platform.persistence.links.jdbc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;

public class JdbcSetLinkOperation extends JdbcLinkOperation
{
    private final boolean preserveOtherLanguages;


    public JdbcSetLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Item parent, Language language, Iterable<Item> children, boolean isParentSource, boolean preserveOtherLanguages, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        this(executor, relationCode, parent, (Map<Language, ? extends Iterable<Item>>)ImmutableMap.of(normalize(language), emptyIfNull(children)), isParentSource, preserveOtherLanguages, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
    }


    public JdbcSetLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Item parent, Map<Language, ? extends Iterable<Item>> children, boolean isParentSource, boolean preserveOtherLanguages, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        this(executor, relationCode, (Iterable<Item>)ImmutableList.of(parent), children, isParentSource, preserveOtherLanguages, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
    }


    public JdbcSetLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Iterable<Item> parents, Language language, Iterable<Item> children, boolean isParentSource, boolean preserveOtherLanguages, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        this(executor, relationCode, parents, (Map<Language, ? extends Iterable<Item>>)ImmutableMap.of(normalize(language), children), isParentSource, preserveOtherLanguages, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
    }


    protected JdbcSetLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Iterable<Item> parents, Map<Language, ? extends Iterable<Item>> children, boolean isParentSource, boolean preserveOtherLanguages, boolean isMarkAsModified, boolean targetToSourceOrdered,
                    boolean sourceToTargetOrdered)
    {
        super(executor, relationCode, parents, children, isParentSource, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
        this.preserveOtherLanguages = preserveOtherLanguages;
    }


    public boolean isPreserveOtherLanguages()
    {
        return this.preserveOtherLanguages;
    }


    public void perform()
    {
        getExecutor().execute(this);
    }
}
