package de.hybris.platform.auditreport.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCreateAuditReportCronJob extends CronJob
{
    public static final String ROOTITEM = "rootItem";
    public static final String CONFIGNAME = "configName";
    public static final String REPORTID = "reportId";
    public static final String AUDIT = "audit";
    public static final String INCLUDEDLANGUAGES = "includedLanguages";
    public static final String AUDITREPORTTEMPLATE = "auditReportTemplate";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("rootItem", Item.AttributeMode.INITIAL);
        tmp.put("configName", Item.AttributeMode.INITIAL);
        tmp.put("reportId", Item.AttributeMode.INITIAL);
        tmp.put("audit", Item.AttributeMode.INITIAL);
        tmp.put("includedLanguages", Item.AttributeMode.INITIAL);
        tmp.put("auditReportTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAudit(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "audit");
    }


    public Boolean isAudit()
    {
        return isAudit(getSession().getSessionContext());
    }


    public boolean isAuditAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAudit(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAuditAsPrimitive()
    {
        return isAuditAsPrimitive(getSession().getSessionContext());
    }


    public void setAudit(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "audit", value);
    }


    public void setAudit(Boolean value)
    {
        setAudit(getSession().getSessionContext(), value);
    }


    public void setAudit(SessionContext ctx, boolean value)
    {
        setAudit(ctx, Boolean.valueOf(value));
    }


    public void setAudit(boolean value)
    {
        setAudit(getSession().getSessionContext(), value);
    }


    public AuditReportTemplate getAuditReportTemplate(SessionContext ctx)
    {
        return (AuditReportTemplate)getProperty(ctx, "auditReportTemplate");
    }


    public AuditReportTemplate getAuditReportTemplate()
    {
        return getAuditReportTemplate(getSession().getSessionContext());
    }


    public void setAuditReportTemplate(SessionContext ctx, AuditReportTemplate value)
    {
        setProperty(ctx, "auditReportTemplate", value);
    }


    public void setAuditReportTemplate(AuditReportTemplate value)
    {
        setAuditReportTemplate(getSession().getSessionContext(), value);
    }


    public String getConfigName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "configName");
    }


    public String getConfigName()
    {
        return getConfigName(getSession().getSessionContext());
    }


    public void setConfigName(SessionContext ctx, String value)
    {
        setProperty(ctx, "configName", value);
    }


    public void setConfigName(String value)
    {
        setConfigName(getSession().getSessionContext(), value);
    }


    public Collection<Language> getIncludedLanguages(SessionContext ctx)
    {
        Collection<Language> coll = (Collection<Language>)getProperty(ctx, "includedLanguages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Language> getIncludedLanguages()
    {
        return getIncludedLanguages(getSession().getSessionContext());
    }


    public void setIncludedLanguages(SessionContext ctx, Collection<Language> value)
    {
        setProperty(ctx, "includedLanguages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setIncludedLanguages(Collection<Language> value)
    {
        setIncludedLanguages(getSession().getSessionContext(), value);
    }


    public String getReportId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "reportId");
    }


    public String getReportId()
    {
        return getReportId(getSession().getSessionContext());
    }


    public void setReportId(SessionContext ctx, String value)
    {
        setProperty(ctx, "reportId", value);
    }


    public void setReportId(String value)
    {
        setReportId(getSession().getSessionContext(), value);
    }


    public Item getRootItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "rootItem");
    }


    public Item getRootItem()
    {
        return getRootItem(getSession().getSessionContext());
    }


    public void setRootItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "rootItem", value);
    }


    public void setRootItem(Item value)
    {
        setRootItem(getSession().getSessionContext(), value);
    }
}
