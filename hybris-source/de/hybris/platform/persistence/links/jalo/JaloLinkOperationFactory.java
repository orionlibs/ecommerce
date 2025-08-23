package de.hybris.platform.persistence.links.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperation;
import de.hybris.platform.persistence.links.PluggableLinkOperationFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JaloLinkOperationFactory implements PluggableLinkOperationFactory
{
    public LinkOperation createInsertOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JaloInsertLinkOperation(ctx, item, itemIsSource, qualifier, language, items, position, shift, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createRemoveOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JaloRemoveLinkOperation(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return (LinkOperation)new JaloSetLinkOperationForSingleLanguage(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean preserveHiddenLanguages, boolean markModified)
    {
        return (LinkOperation)new JaloSetLinkOperationForMultipleLanguages(ctx, item, itemIsSource, qualifier, items, sortSrc2Tgt, sortTgt2Src, preserveHiddenLanguages, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean markModified)
    {
        return (LinkOperation)new JaloSetLinkOperationForMultipleItems(ctx, itemColl, itemsAreSource, qualifier, language, items, markModified);
    }


    public boolean isEnabled()
    {
        return true;
    }
}
