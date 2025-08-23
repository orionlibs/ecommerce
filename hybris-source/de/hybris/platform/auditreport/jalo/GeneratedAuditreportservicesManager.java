package de.hybris.platform.auditreport.jalo;

import de.hybris.platform.auditreport.constants.GeneratedAuditreportservicesConstants;
import de.hybris.platform.core.audit.AuditReportConfig;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAuditreportservicesManager extends Extension
{
    protected static final OneToManyHandler<AuditReportData> AUDITREPORTDATA2AUDITREPORTCONFIGRELATIONAUDITREPORTDATAHANDLER = new OneToManyHandler(GeneratedAuditreportservicesConstants.TC.AUDITREPORTDATA, false, "auditReportConfig", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Collection<AuditReportData> getAuditReportData(SessionContext ctx, AuditReportConfig item)
    {
        return AUDITREPORTDATA2AUDITREPORTCONFIGRELATIONAUDITREPORTDATAHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<AuditReportData> getAuditReportData(AuditReportConfig item)
    {
        return getAuditReportData(getSession().getSessionContext(), item);
    }


    public void setAuditReportData(SessionContext ctx, AuditReportConfig item, Collection<AuditReportData> value)
    {
        AUDITREPORTDATA2AUDITREPORTCONFIGRELATIONAUDITREPORTDATAHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setAuditReportData(AuditReportConfig item, Collection<AuditReportData> value)
    {
        setAuditReportData(getSession().getSessionContext(), item, value);
    }


    public void addToAuditReportData(SessionContext ctx, AuditReportConfig item, AuditReportData value)
    {
        AUDITREPORTDATA2AUDITREPORTCONFIGRELATIONAUDITREPORTDATAHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToAuditReportData(AuditReportConfig item, AuditReportData value)
    {
        addToAuditReportData(getSession().getSessionContext(), item, value);
    }


    public void removeFromAuditReportData(SessionContext ctx, AuditReportConfig item, AuditReportData value)
    {
        AUDITREPORTDATA2AUDITREPORTCONFIGRELATIONAUDITREPORTDATAHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromAuditReportData(AuditReportConfig item, AuditReportData value)
    {
        removeFromAuditReportData(getSession().getSessionContext(), item, value);
    }


    public AuditReportData createAuditReportData(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAuditreportservicesConstants.TC.AUDITREPORTDATA);
            return (AuditReportData)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AuditReportData : " + e.getMessage(), 0);
        }
    }


    public AuditReportData createAuditReportData(Map attributeValues)
    {
        return createAuditReportData(getSession().getSessionContext(), attributeValues);
    }


    public AuditReportTemplate createAuditReportTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAuditreportservicesConstants.TC.AUDITREPORTTEMPLATE);
            return (AuditReportTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AuditReportTemplate : " + e.getMessage(), 0);
        }
    }


    public AuditReportTemplate createAuditReportTemplate(Map attributeValues)
    {
        return createAuditReportTemplate(getSession().getSessionContext(), attributeValues);
    }


    public CreateAuditReportCronJob createCreateAuditReportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAuditreportservicesConstants.TC.CREATEAUDITREPORTCRONJOB);
            return (CreateAuditReportCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CreateAuditReportCronJob : " + e.getMessage(), 0);
        }
    }


    public CreateAuditReportCronJob createCreateAuditReportCronJob(Map attributeValues)
    {
        return createCreateAuditReportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CreateAuditReportJob createCreateAuditReportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAuditreportservicesConstants.TC.CREATEAUDITREPORTJOB);
            return (CreateAuditReportJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CreateAuditReportJob : " + e.getMessage(), 0);
        }
    }


    public CreateAuditReportJob createCreateAuditReportJob(Map attributeValues)
    {
        return createCreateAuditReportJob(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "auditreportservices";
    }
}
