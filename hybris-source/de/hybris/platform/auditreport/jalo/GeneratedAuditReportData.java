package de.hybris.platform.auditreport.jalo;

import de.hybris.platform.auditreport.constants.GeneratedAuditreportservicesConstants;
import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.core.audit.AuditReportConfig;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAuditReportData extends CatalogUnawareMedia
{
    public static final String AUDITROOTITEM = "auditRootItem";
    public static final String AUDITREPORTCONFIG = "auditReportConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedAuditReportData> AUDITREPORTCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedAuditreportservicesConstants.TC.AUDITREPORTDATA, false, "auditReportConfig", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogUnawareMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("auditRootItem", Item.AttributeMode.INITIAL);
        tmp.put("auditReportConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AuditReportConfig getAuditReportConfig(SessionContext ctx)
    {
        return (AuditReportConfig)getProperty(ctx, "auditReportConfig");
    }


    public AuditReportConfig getAuditReportConfig()
    {
        return getAuditReportConfig(getSession().getSessionContext());
    }


    public void setAuditReportConfig(SessionContext ctx, AuditReportConfig value)
    {
        AUDITREPORTCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setAuditReportConfig(AuditReportConfig value)
    {
        setAuditReportConfig(getSession().getSessionContext(), value);
    }


    public Item getAuditRootItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "auditRootItem");
    }


    public Item getAuditRootItem()
    {
        return getAuditRootItem(getSession().getSessionContext());
    }


    public void setAuditRootItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "auditRootItem", value);
    }


    public void setAuditRootItem(Item value)
    {
        setAuditRootItem(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        AUDITREPORTCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }
}
