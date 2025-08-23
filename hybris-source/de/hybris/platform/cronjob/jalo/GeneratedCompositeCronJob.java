package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCompositeCronJob extends CronJob
{
    public static final String COMPOSITEENTRIES = "compositeEntries";
    protected static final OneToManyHandler<CompositeEntry> COMPOSITEENTRIESHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.COMPOSITEENTRY, true, "compositeCronJob", "compositeCronJobPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CompositeEntry> getCompositeEntries(SessionContext ctx)
    {
        return (List<CompositeEntry>)COMPOSITEENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<CompositeEntry> getCompositeEntries()
    {
        return getCompositeEntries(getSession().getSessionContext());
    }


    public void setCompositeEntries(SessionContext ctx, List<CompositeEntry> value)
    {
        COMPOSITEENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCompositeEntries(List<CompositeEntry> value)
    {
        setCompositeEntries(getSession().getSessionContext(), value);
    }


    public void addToCompositeEntries(SessionContext ctx, CompositeEntry value)
    {
        COMPOSITEENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCompositeEntries(CompositeEntry value)
    {
        addToCompositeEntries(getSession().getSessionContext(), value);
    }


    public void removeFromCompositeEntries(SessionContext ctx, CompositeEntry value)
    {
        COMPOSITEENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCompositeEntries(CompositeEntry value)
    {
        removeFromCompositeEntries(getSession().getSessionContext(), value);
    }
}
