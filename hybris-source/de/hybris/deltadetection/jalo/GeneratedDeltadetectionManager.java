package de.hybris.deltadetection.jalo;

import de.hybris.deltadetection.constants.GeneratedDeltadetectionConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedDeltadetectionManager extends Extension
{
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_SRC_ORDERED = "relation.StreamConfigurationExcludedSubtypes.source.ordered";
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_TGT_ORDERED = "relation.StreamConfigurationExcludedSubtypes.target.ordered";
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED = "relation.StreamConfigurationExcludedSubtypes.markmodified";
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


    public ChangeDetectionJob createChangeDetectionJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.CHANGEDETECTIONJOB);
            return (ChangeDetectionJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ChangeDetectionJob : " + e.getMessage(), 0);
        }
    }


    public ChangeDetectionJob createChangeDetectionJob(Map attributeValues)
    {
        return createChangeDetectionJob(getSession().getSessionContext(), attributeValues);
    }


    public ConsumeAllChangesJob createConsumeAllChangesJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.CONSUMEALLCHANGESJOB);
            return (ConsumeAllChangesJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ConsumeAllChangesJob : " + e.getMessage(), 0);
        }
    }


    public ConsumeAllChangesJob createConsumeAllChangesJob(Map attributeValues)
    {
        return createConsumeAllChangesJob(getSession().getSessionContext(), attributeValues);
    }


    public ItemVersionMarker createItemVersionMarker(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.ITEMVERSIONMARKER);
            return (ItemVersionMarker)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ItemVersionMarker : " + e.getMessage(), 0);
        }
    }


    public ItemVersionMarker createItemVersionMarker(Map attributeValues)
    {
        return createItemVersionMarker(getSession().getSessionContext(), attributeValues);
    }


    public ScriptChangeConsumptionJob createScriptChangeConsumptionJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.SCRIPTCHANGECONSUMPTIONJOB);
            return (ScriptChangeConsumptionJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ScriptChangeConsumptionJob : " + e.getMessage(), 0);
        }
    }


    public ScriptChangeConsumptionJob createScriptChangeConsumptionJob(Map attributeValues)
    {
        return createScriptChangeConsumptionJob(getSession().getSessionContext(), attributeValues);
    }


    public StreamConfiguration createStreamConfiguration(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.STREAMCONFIGURATION);
            return (StreamConfiguration)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating StreamConfiguration : " + e.getMessage(), 0);
        }
    }


    public StreamConfiguration createStreamConfiguration(Map attributeValues)
    {
        return createStreamConfiguration(getSession().getSessionContext(), attributeValues);
    }


    public StreamConfigurationContainer createStreamConfigurationContainer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedDeltadetectionConstants.TC.STREAMCONFIGURATIONCONTAINER);
            return (StreamConfigurationContainer)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating StreamConfigurationContainer : " + e.getMessage(), 0);
        }
    }


    public StreamConfigurationContainer createStreamConfigurationContainer(Map attributeValues)
    {
        return createStreamConfigurationContainer(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "deltadetection";
    }


    public Collection<StreamConfiguration> getStreamConfigurations(SessionContext ctx, ComposedType item)
    {
        List<StreamConfiguration> items = item.getLinkedItems(ctx, false, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, "StreamConfiguration", null, false, false);
        return items;
    }


    public Collection<StreamConfiguration> getStreamConfigurations(ComposedType item)
    {
        return getStreamConfigurations(getSession().getSessionContext(), item);
    }


    public long getStreamConfigurationsCount(SessionContext ctx, ComposedType item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, "StreamConfiguration", null);
    }


    public long getStreamConfigurationsCount(ComposedType item)
    {
        return getStreamConfigurationsCount(getSession().getSessionContext(), item);
    }


    public void setStreamConfigurations(SessionContext ctx, ComposedType item, Collection<StreamConfiguration> value)
    {
        item.setLinkedItems(ctx, false, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void setStreamConfigurations(ComposedType item, Collection<StreamConfiguration> value)
    {
        setStreamConfigurations(getSession().getSessionContext(), item, value);
    }


    public void addToStreamConfigurations(SessionContext ctx, ComposedType item, StreamConfiguration value)
    {
        item.addLinkedItems(ctx, false, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void addToStreamConfigurations(ComposedType item, StreamConfiguration value)
    {
        addToStreamConfigurations(getSession().getSessionContext(), item, value);
    }


    public void removeFromStreamConfigurations(SessionContext ctx, ComposedType item, StreamConfiguration value)
    {
        item.removeLinkedItems(ctx, false, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void removeFromStreamConfigurations(ComposedType item, StreamConfiguration value)
    {
        removeFromStreamConfigurations(getSession().getSessionContext(), item, value);
    }
}
