package de.hybris.y2ysync.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.y2ysync.constants.GeneratedY2ysyncConstants;
import de.hybris.y2ysync.jalo.media.ConsumeMarkerMedia;
import de.hybris.y2ysync.jalo.media.SyncImpExMedia;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2ysyncManager extends Extension
{
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


    public ConsumeMarkerMedia createConsumeMarkerMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.CONSUMEMARKERMEDIA);
            return (ConsumeMarkerMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ConsumeMarkerMedia : " + e.getMessage(), 0);
        }
    }


    public ConsumeMarkerMedia createConsumeMarkerMedia(Map attributeValues)
    {
        return createConsumeMarkerMedia(getSession().getSessionContext(), attributeValues);
    }


    public SyncImpExMedia createSyncImpExMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.SYNCIMPEXMEDIA);
            return (SyncImpExMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating SyncImpExMedia : " + e.getMessage(), 0);
        }
    }


    public SyncImpExMedia createSyncImpExMedia(Map attributeValues)
    {
        return createSyncImpExMedia(getSession().getSessionContext(), attributeValues);
    }


    public Y2YBatch createY2YBatch(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YBATCH);
            return (Y2YBatch)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YBatch : " + e.getMessage(), 0);
        }
    }


    public Y2YBatch createY2YBatch(Map attributeValues)
    {
        return createY2YBatch(getSession().getSessionContext(), attributeValues);
    }


    public Y2YColumnDefintion createY2YColumnDefinition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YCOLUMNDEFINITION);
            return (Y2YColumnDefintion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YColumnDefinition : " + e.getMessage(), 0);
        }
    }


    public Y2YColumnDefintion createY2YColumnDefinition(Map attributeValues)
    {
        return createY2YColumnDefinition(getSession().getSessionContext(), attributeValues);
    }


    public Y2YDistributedProcess createY2YDistributedProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YDISTRIBUTEDPROCESS);
            return (Y2YDistributedProcess)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YDistributedProcess : " + e.getMessage(), 0);
        }
    }


    public Y2YDistributedProcess createY2YDistributedProcess(Map attributeValues)
    {
        return createY2YDistributedProcess(getSession().getSessionContext(), attributeValues);
    }


    public Y2YStreamConfiguration createY2YStreamConfiguration(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YSTREAMCONFIGURATION);
            return (Y2YStreamConfiguration)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YStreamConfiguration : " + e.getMessage(), 0);
        }
    }


    public Y2YStreamConfiguration createY2YStreamConfiguration(Map attributeValues)
    {
        return createY2YStreamConfiguration(getSession().getSessionContext(), attributeValues);
    }


    public Y2YStreamConfigurationContainer createY2YStreamConfigurationContainer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YSTREAMCONFIGURATIONCONTAINER);
            return (Y2YStreamConfigurationContainer)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YStreamConfigurationContainer : " + e.getMessage(), 0);
        }
    }


    public Y2YStreamConfigurationContainer createY2YStreamConfigurationContainer(Map attributeValues)
    {
        return createY2YStreamConfigurationContainer(getSession().getSessionContext(), attributeValues);
    }


    public Y2YSyncCronJob createY2YSyncCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YSYNCCRONJOB);
            return (Y2YSyncCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YSyncCronJob : " + e.getMessage(), 0);
        }
    }


    public Y2YSyncCronJob createY2YSyncCronJob(Map attributeValues)
    {
        return createY2YSyncCronJob(getSession().getSessionContext(), attributeValues);
    }


    public Y2YSyncJob createY2YSyncJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedY2ysyncConstants.TC.Y2YSYNCJOB);
            return (Y2YSyncJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Y2YSyncJob : " + e.getMessage(), 0);
        }
    }


    public Y2YSyncJob createY2YSyncJob(Map attributeValues)
    {
        return createY2YSyncJob(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "y2ysync";
    }
}
