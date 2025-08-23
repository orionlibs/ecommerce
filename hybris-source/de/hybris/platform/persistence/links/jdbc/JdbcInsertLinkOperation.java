package de.hybris.platform.persistence.links.jdbc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;

public class JdbcInsertLinkOperation extends JdbcLinkOperation
{
    public static final int INSERT_AT_THE_END = -1;
    private final int position;


    public JdbcInsertLinkOperation(JdbcLinkOperationExecutor executionStrategy, String relationCode, Item parent, Language language, Iterable<Item> children, boolean isParentSource, int position, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        super(executionStrategy, relationCode, (Iterable)ImmutableList.of(parent),
                        (Map)ImmutableMap.of(normalize(language), emptyIfNull(children)), isParentSource, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
        this.position = (position < 0) ? -1 : position;
    }


    public void perform()
    {
        getExecutor().execute(this);
    }


    public int getPosition()
    {
        return this.position;
    }
}
