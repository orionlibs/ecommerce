package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.ImpExExportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExExportJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.exp.Report;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpExManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("systemType", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.type.ComposedType", Collections.unmodifiableMap(tmp));
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


    public DistributedImportProcess createDistributedImportProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.DISTRIBUTEDIMPORTPROCESS);
            return (DistributedImportProcess)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating DistributedImportProcess : " + e.getMessage(), 0);
        }
    }


    public DistributedImportProcess createDistributedImportProcess(Map attributeValues)
    {
        return createDistributedImportProcess(getSession().getSessionContext(), attributeValues);
    }


    public DistributedImportSplitErrorDump createDistributedImportSplitErrorDump(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.DISTRIBUTEDIMPORTSPLITERRORDUMP);
            return (DistributedImportSplitErrorDump)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating DistributedImportSplitErrorDump : " + e.getMessage(), 0);
        }
    }


    public DistributedImportSplitErrorDump createDistributedImportSplitErrorDump(Map attributeValues)
    {
        return createDistributedImportSplitErrorDump(getSession().getSessionContext(), attributeValues);
    }


    public Export createExport(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.EXPORT);
            return (Export)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Export : " + e.getMessage(), 0);
        }
    }


    public Export createExport(Map attributeValues)
    {
        return createExport(getSession().getSessionContext(), attributeValues);
    }


    public ExternalImportKey createExternalImportKey(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.EXTERNALIMPORTKEY);
            return (ExternalImportKey)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ExternalImportKey : " + e.getMessage(), 0);
        }
    }


    public ExternalImportKey createExternalImportKey(Map attributeValues)
    {
        return createExternalImportKey(getSession().getSessionContext(), attributeValues);
    }


    public HeaderLibrary createHeaderLibrary(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.HEADERLIBRARY);
            return (HeaderLibrary)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating HeaderLibrary : " + e.getMessage(), 0);
        }
    }


    public HeaderLibrary createHeaderLibrary(Map attributeValues)
    {
        return createHeaderLibrary(getSession().getSessionContext(), attributeValues);
    }


    public ImpexDocumentId createImpexDocumentId(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXDOCUMENTID);
            return (ImpexDocumentId)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpexDocumentId : " + e.getMessage(), 0);
        }
    }


    public ImpexDocumentId createImpexDocumentId(Map attributeValues)
    {
        return createImpexDocumentId(getSession().getSessionContext(), attributeValues);
    }


    public ImpExExportCronJob createImpExExportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXEXPORTCRONJOB);
            return (ImpExExportCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExExportCronJob : " + e.getMessage(), 0);
        }
    }


    public ImpExExportCronJob createImpExExportCronJob(Map attributeValues)
    {
        return createImpExExportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public ImpExExportJob createImpExExportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXEXPORTJOB);
            return (ImpExExportJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExExportJob : " + e.getMessage(), 0);
        }
    }


    public ImpExExportJob createImpExExportJob(Map attributeValues)
    {
        return createImpExExportJob(getSession().getSessionContext(), attributeValues);
    }


    public ImpExExportMedia createImpExExportMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXEXPORTMEDIA);
            return (ImpExExportMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExExportMedia : " + e.getMessage(), 0);
        }
    }


    public ImpExExportMedia createImpExExportMedia(Map attributeValues)
    {
        return createImpExExportMedia(getSession().getSessionContext(), attributeValues);
    }


    public ImpExImportCronJob createImpExImportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXIMPORTCRONJOB);
            return (ImpExImportCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExImportCronJob : " + e.getMessage(), 0);
        }
    }


    public ImpExImportCronJob createImpExImportCronJob(Map attributeValues)
    {
        return createImpExImportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public ImpExImportJob createImpExImportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXIMPORTJOB);
            return (ImpExImportJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExImportJob : " + e.getMessage(), 0);
        }
    }


    public ImpExImportJob createImpExImportJob(Map attributeValues)
    {
        return createImpExImportJob(getSession().getSessionContext(), attributeValues);
    }


    public ImpExMedia createImpExMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPEXMEDIA);
            return (ImpExMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImpExMedia : " + e.getMessage(), 0);
        }
    }


    public ImpExMedia createImpExMedia(Map attributeValues)
    {
        return createImpExMedia(getSession().getSessionContext(), attributeValues);
    }


    public ImportBatch createImportBatch(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPORTBATCH);
            return (ImportBatch)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImportBatch : " + e.getMessage(), 0);
        }
    }


    public ImportBatch createImportBatch(Map attributeValues)
    {
        return createImportBatch(getSession().getSessionContext(), attributeValues);
    }


    public ImportBatchContent createImportBatchContent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.IMPORTBATCHCONTENT);
            return (ImportBatchContent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ImportBatchContent : " + e.getMessage(), 0);
        }
    }


    public ImportBatchContent createImportBatchContent(Map attributeValues)
    {
        return createImportBatchContent(getSession().getSessionContext(), attributeValues);
    }


    public Report createReport(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedImpExConstants.TC.REPORT);
            return (Report)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Report : " + e.getMessage(), 0);
        }
    }


    public Report createReport(Map attributeValues)
    {
        return createReport(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "impex";
    }


    public Boolean isSystemType(SessionContext ctx, ComposedType item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedImpExConstants.Attributes.ComposedType.SYSTEMTYPE);
    }


    public Boolean isSystemType(ComposedType item)
    {
        return isSystemType(getSession().getSessionContext(), item);
    }


    public boolean isSystemTypeAsPrimitive(SessionContext ctx, ComposedType item)
    {
        Boolean value = isSystemType(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSystemTypeAsPrimitive(ComposedType item)
    {
        return isSystemTypeAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setSystemType(SessionContext ctx, ComposedType item, Boolean value)
    {
        item.setProperty(ctx, GeneratedImpExConstants.Attributes.ComposedType.SYSTEMTYPE, value);
    }


    public void setSystemType(ComposedType item, Boolean value)
    {
        setSystemType(getSession().getSessionContext(), item, value);
    }


    public void setSystemType(SessionContext ctx, ComposedType item, boolean value)
    {
        setSystemType(ctx, item, Boolean.valueOf(value));
    }


    public void setSystemType(ComposedType item, boolean value)
    {
        setSystemType(getSession().getSessionContext(), item, value);
    }
}
