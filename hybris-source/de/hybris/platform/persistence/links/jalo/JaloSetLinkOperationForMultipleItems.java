package de.hybris.platform.persistence.links.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class JaloSetLinkOperationForMultipleItems extends JaloLinkOperation
{
    private final SessionContext ctx;
    private final Collection<Item> itemColl;
    private final boolean itemsAreSource;
    private final String qualifier;
    private final Language language;
    private final List<Item> items;
    private final boolean markModified;


    public JaloSetLinkOperationForMultipleItems(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean markModified)
    {
        this.ctx = ctx;
        this.itemColl = itemColl;
        this.itemsAreSource = itemsAreSource;
        this.qualifier = qualifier;
        this.language = language;
        this.items = items;
        this.markModified = markModified;
    }


    public void perform()
    {
        LinkOperationHandler operationHandler = new LinkOperationHandler((this.itemColl instanceof List) ? (List)this.itemColl : new ArrayList<>(this.itemColl), this.items, this.itemsAreSource, this.qualifier, this.language, this.markModified);
        operationHandler.setReplace(true);
        operationHandler.setPreserveHiddenLanguages(true);
        operationHandler.setSkipQueryExistingLinks(skipChangingExistingLinks(this.ctx));
        operationHandler.perform(this.ctx);
    }
}
