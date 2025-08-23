package de.hybris.y2ysync.jalo;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.y2ysync.constants.GeneratedY2ysyncConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2YSyncCronJob extends CronJob
{
    public static final String IMPEXZIP = "impexZip";
    public static final String MEDIASZIP = "mediasZip";
    protected static final BidirectionalOneToManyHandler<GeneratedY2YSyncCronJob> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedY2ysyncConstants.TC.Y2YSYNCJOB, false, "job", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("impexZip", Item.AttributeMode.INITIAL);
        tmp.put("mediasZip", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CatalogUnawareMedia getImpexZip(SessionContext ctx)
    {
        return (CatalogUnawareMedia)getProperty(ctx, "impexZip");
    }


    public CatalogUnawareMedia getImpexZip()
    {
        return getImpexZip(getSession().getSessionContext());
    }


    public void setImpexZip(SessionContext ctx, CatalogUnawareMedia value)
    {
        setProperty(ctx, "impexZip", value);
    }


    public void setImpexZip(CatalogUnawareMedia value)
    {
        setImpexZip(getSession().getSessionContext(), value);
    }


    public CatalogUnawareMedia getMediasZip(SessionContext ctx)
    {
        return (CatalogUnawareMedia)getProperty(ctx, "mediasZip");
    }


    public CatalogUnawareMedia getMediasZip()
    {
        return getMediasZip(getSession().getSessionContext());
    }


    public void setMediasZip(SessionContext ctx, CatalogUnawareMedia value)
    {
        setProperty(ctx, "mediasZip", value);
    }


    public void setMediasZip(CatalogUnawareMedia value)
    {
        setMediasZip(getSession().getSessionContext(), value);
    }
}
