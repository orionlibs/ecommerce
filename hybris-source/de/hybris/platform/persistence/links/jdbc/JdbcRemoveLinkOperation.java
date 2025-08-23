package de.hybris.platform.persistence.links.jdbc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;

public class JdbcRemoveLinkOperation extends JdbcLinkOperation
{
    public JdbcRemoveLinkOperation(JdbcLinkOperationExecutor executor, String relationCode, Item parent, Language language, Iterable<Item> children, boolean isParentSource, boolean isMarkAsModified, boolean targetToSourceOrdered, boolean sourceToTargetOrdered)
    {
        super(executor, relationCode, (Iterable)ImmutableList.of(parent), (Map)ImmutableMap.of(normalize(language), emptyIfNull(children)), isParentSource, isMarkAsModified, targetToSourceOrdered, sourceToTargetOrdered);
    }


    public void perform()
    {
        getExecutor().execute(this);
    }
}
