package com.hybris.backoffice.solrsearch.jalo;

import com.hybris.backoffice.solrsearch.constants.GeneratedBackofficesolrsearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeSolrIndexerCronJob extends SolrIndexerCronJob
{
    public static final String MODIFIEDITEMS = "modifiedItems";
    protected static final OneToManyHandler<SolrModifiedItem> MODIFIEDITEMSHANDLER = new OneToManyHandler(GeneratedBackofficesolrsearchConstants.TC.SOLRMODIFIEDITEM, false, "parent", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SolrIndexerCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<SolrModifiedItem> getModifiedItems(SessionContext ctx)
    {
        return MODIFIEDITEMSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrModifiedItem> getModifiedItems()
    {
        return getModifiedItems(getSession().getSessionContext());
    }


    public void setModifiedItems(SessionContext ctx, Collection<SolrModifiedItem> value)
    {
        MODIFIEDITEMSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setModifiedItems(Collection<SolrModifiedItem> value)
    {
        setModifiedItems(getSession().getSessionContext(), value);
    }


    public void addToModifiedItems(SessionContext ctx, SolrModifiedItem value)
    {
        MODIFIEDITEMSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToModifiedItems(SolrModifiedItem value)
    {
        addToModifiedItems(getSession().getSessionContext(), value);
    }


    public void removeFromModifiedItems(SessionContext ctx, SolrModifiedItem value)
    {
        MODIFIEDITEMSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromModifiedItems(SolrModifiedItem value)
    {
        removeFromModifiedItems(getSession().getSessionContext(), value);
    }
}
