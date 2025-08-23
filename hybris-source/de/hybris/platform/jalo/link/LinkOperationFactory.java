package de.hybris.platform.jalo.link;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LinkOperationFactory
{
    LinkOperation createInsertOperation(SessionContext paramSessionContext, Item paramItem, boolean paramBoolean1, String paramString, Language paramLanguage, List paramList, int paramInt, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5);


    LinkOperation createRemoveOperation(SessionContext paramSessionContext, Item paramItem, boolean paramBoolean1, String paramString, Language paramLanguage, List<Item> paramList, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4);


    LinkOperation createSetOperation(SessionContext paramSessionContext, Item paramItem, boolean paramBoolean1, String paramString, Language paramLanguage, List<Item> paramList, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4);


    LinkOperation createSetOperation(SessionContext paramSessionContext, Item paramItem, boolean paramBoolean1, String paramString, Map<Language, List<Item>> paramMap, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5);


    LinkOperation createSetOperation(SessionContext paramSessionContext, Collection<Item> paramCollection, boolean paramBoolean1, String paramString, Language paramLanguage, List<Item> paramList, boolean paramBoolean2);
}
