package de.hybris.platform.persistence.links.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperationHandler;
import java.util.List;
import java.util.Map;

class JaloSetLinkOperationForMultipleLanguages extends JaloLinkOperation
{
    private final SessionContext ctx;
    private final Item item;
    private final boolean itemIsSource;
    private final String qualifier;
    private final Map<Language, List<Item>> items;
    private final boolean sortSrc2Tgt;
    private final boolean sortTgt2Src;
    private final boolean preserveHiddenLanguages;
    private final boolean markModified;


    public JaloSetLinkOperationForMultipleLanguages(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean preserveHiddenLanguages, boolean markModified)
    {
        this.ctx = ctx;
        this.item = item;
        this.itemIsSource = itemIsSource;
        this.qualifier = qualifier;
        this.items = items;
        this.sortSrc2Tgt = sortSrc2Tgt;
        this.sortTgt2Src = sortTgt2Src;
        this.preserveHiddenLanguages = preserveHiddenLanguages;
        this.markModified = markModified;
    }


    public void perform()
    {
        LinkOperationHandler operationHandler = new LinkOperationHandler(this.item, this.items, this.itemIsSource, this.qualifier);
        operationHandler.setSortSrcEnd(this.sortTgt2Src);
        operationHandler.setSortTgtEnd(this.sortSrc2Tgt);
        operationHandler.setReplace(true);
        operationHandler.setPreserveHiddenLanguages(this.preserveHiddenLanguages);
        operationHandler.setSkipQueryExistingLinks(skipChangingExistingLinks(this.ctx));
        operationHandler.setMarkItemsModified(this.markModified);
        operationHandler.perform(this.ctx);
    }
}
