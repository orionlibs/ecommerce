package de.hybris.platform.persistence.links.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperationHandler;
import java.util.List;

class JaloInsertLinkOperation extends JaloLinkOperation
{
    private final SessionContext ctx;
    private final Item item;
    private final boolean itemIsSource;
    private final String qualifier;
    private final Language language;
    private final List items;
    private final int position;
    private final boolean shift;
    private final boolean sortSrc2Tgt;
    private final boolean sortTgt2Src;
    private final boolean markModified;


    public JaloInsertLinkOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        this.ctx = ctx;
        this.item = item;
        this.itemIsSource = itemIsSource;
        this.qualifier = qualifier;
        this.language = language;
        this.items = items;
        this.position = position;
        this.shift = shift;
        this.sortSrc2Tgt = sortSrc2Tgt;
        this.sortTgt2Src = sortTgt2Src;
        this.markModified = markModified;
    }


    public void perform()
    {
        LinkOperationHandler operationHandler = new LinkOperationHandler(this.item, this.items, this.itemIsSource, this.qualifier, this.language, this.markModified);
        operationHandler.setSortSrcEnd(this.sortTgt2Src);
        operationHandler.setSortTgtEnd(this.sortSrc2Tgt);
        operationHandler.setReplace(false);
        operationHandler.setPosition(this.position);
        operationHandler.setShift(this.shift);
        operationHandler.setSkipQueryExistingLinks(skipChangingExistingLinks(this.ctx));
        operationHandler.perform(this.ctx);
    }
}
