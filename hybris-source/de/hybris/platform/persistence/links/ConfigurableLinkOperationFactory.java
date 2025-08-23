package de.hybris.platform.persistence.links;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkOperation;
import de.hybris.platform.jalo.link.LinkOperationFactory;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class ConfigurableLinkOperationFactory implements LinkOperationFactory, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(ConfigurableLinkOperationFactory.class);
    private final Iterable<PluggableLinkOperationFactory> factoriesInOrder;
    private volatile LinkOperationFactory selectedFactory;


    public ConfigurableLinkOperationFactory(List<PluggableLinkOperationFactory> factoriesInOrder)
    {
        Preconditions.checkNotNull(factoriesInOrder, "factoriesInOrder can't be null");
        this.factoriesInOrder = (Iterable<PluggableLinkOperationFactory>)ImmutableList.copyOf(factoriesInOrder);
    }


    public LinkOperation createInsertOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List items, int position, boolean shift, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return getSelectedFactory().createInsertOperation(ctx, item, itemIsSource, qualifier, language, items, position, shift, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createRemoveOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return getSelectedFactory().createRemoveOperation(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Language language, List<Item> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        return getSelectedFactory().createSetOperation(ctx, item, itemIsSource, qualifier, language, items, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Item item, boolean itemIsSource, String qualifier, Map<Language, List<Item>> items, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean preserveHiddenLanguages, boolean markModified)
    {
        return getSelectedFactory().createSetOperation(ctx, item, itemIsSource, qualifier, items, sortSrc2Tgt, sortTgt2Src, preserveHiddenLanguages, markModified);
    }


    public LinkOperation createSetOperation(SessionContext ctx, Collection<Item> itemColl, boolean itemsAreSource, String qualifier, Language language, List<Item> items, boolean markModified)
    {
        return getSelectedFactory().createSetOperation(ctx, itemColl, itemsAreSource, qualifier, language, items, markModified);
    }


    private LinkOperationFactory getSelectedFactory()
    {
        if(this.selectedFactory == null)
        {
            return this.selectedFactory = detectFactory();
        }
        return this.selectedFactory;
    }


    public void afterPropertiesSet() throws Exception
    {
        Registry.getCurrentTenantNoFallback().getConfig().registerConfigChangeListener((ConfigIntf.ConfigChangeListener)new Object(this));
    }


    private LinkOperationFactory detectFactory()
    {
        for(PluggableLinkOperationFactory factory : this.factoriesInOrder)
        {
            if(factory != null)
            {
                if(factory.isEnabled())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Using factory " + factory.getClass().getName());
                    }
                    return (LinkOperationFactory)factory;
                }
            }
        }
        LOG.error("Can't find configured factory to use. Available factories: " + Iterables.toString(this.factoriesInOrder));
        throw new IllegalStateException("Can't find configured factory to use");
    }
}
