package de.hybris.platform.persistence.links.jdbc;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperation;
import de.hybris.platform.persistence.links.PluggableLinkOperationFactory;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JdbcLinkOperationFactory implements PluggableLinkOperationFactory
{
    private static final String LINK_ENABLE_JDBC_MODE = "link.jdbc.mode.enabled";
    private final JdbcLinkOperationExecutor executor;


    public JdbcLinkOperationFactory(JdbcLinkOperationExecutor executor)
    {
        Preconditions.checkNotNull(executor, "executor can't be null");
        this.executor = executor;
    }


    public LinkOperation createInsertOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JdbcInsertLinkOperation(this.executor, qualifier, item, language, items, itemIsSource, position, markModified, sortTgt2Src, sortSrc2Tgt);
    }


    public LinkOperation createRemoveOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JdbcRemoveLinkOperation(this.executor, qualifier, item, language, items, itemIsSource, markModified, sortTgt2Src, sortSrc2Tgt);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JdbcSetLinkOperation(this.executor, qualifier, item, language, items, itemIsSource, true, markModified, sortTgt2Src, sortSrc2Tgt);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean preserveHiddenLanguages, boolean markModified)
    {
        return (LinkOperation)new JdbcSetLinkOperation(this.executor, qualifier, item, items, itemIsSource, preserveHiddenLanguages, markModified, sortTgt2Src, sortSrc2Tgt);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean markModified)
    {
        return (LinkOperation)new JdbcSetLinkOperation(this.executor, qualifier, itemColl, language, items, itemsAreSource, true, markModified, true, true);
    }


    public boolean isEnabled()
    {
        return Config.getBoolean("link.jdbc.mode.enabled", true);
    }
}
