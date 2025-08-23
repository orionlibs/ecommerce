package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.mediaconversion.constants.GeneratedMediaConversionConstants;
import de.hybris.platform.mediaconversion.jalo.job.ExtractMediaMetaDataCronJob;
import de.hybris.platform.mediaconversion.jalo.job.MediaConversionCronJob;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaConversionManager extends Extension
{
    protected static final OneToManyHandler<Media> CONVERTEDMEDIASRELATIONCONVERTEDMEDIASHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.MEDIA, false, "original", null, false, true, 0);
    protected static final OneToManyHandler<MediaMetaData> MEDIATOMEDIAMETADATARELMETADATAHANDLER = new OneToManyHandler(GeneratedMediaConversionConstants.TC.MEDIAMETADATA, true, "media", null, false, true, 0);
    protected static final OneToManyHandler<ConversionErrorLog> CONTAINERTOCONVERSIONERRORLOGRELCONVERSIONERRORLOGHANDLER = new OneToManyHandler(GeneratedMediaConversionConstants.TC.CONVERSIONERRORLOG, false, "container", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("metaDataDataPK", Item.AttributeMode.INITIAL);
        tmp.put("originalDataPK", Item.AttributeMode.INITIAL);
        tmp.put("original", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.media.Media", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("conversionGroup", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.media.MediaContainer", Collections.unmodifiableMap(tmp));
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


    public Collection<ConversionErrorLog> getConversionErrorLog(SessionContext ctx, MediaContainer item)
    {
        return CONTAINERTOCONVERSIONERRORLOGRELCONVERSIONERRORLOGHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<ConversionErrorLog> getConversionErrorLog(MediaContainer item)
    {
        return getConversionErrorLog(getSession().getSessionContext(), item);
    }


    public ConversionGroup getConversionGroup(SessionContext ctx, MediaContainer item)
    {
        return (ConversionGroup)item.getProperty(ctx, GeneratedMediaConversionConstants.Attributes.MediaContainer.CONVERSIONGROUP);
    }


    public ConversionGroup getConversionGroup(MediaContainer item)
    {
        return getConversionGroup(getSession().getSessionContext(), item);
    }


    public void setConversionGroup(SessionContext ctx, MediaContainer item, ConversionGroup value)
    {
        item.setProperty(ctx, GeneratedMediaConversionConstants.Attributes.MediaContainer.CONVERSIONGROUP, value);
    }


    public void setConversionGroup(MediaContainer item, ConversionGroup value)
    {
        setConversionGroup(getSession().getSessionContext(), item, value);
    }


    public Collection<Media> getConvertedMedias(SessionContext ctx, Media item)
    {
        return CONVERTEDMEDIASRELATIONCONVERTEDMEDIASHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<Media> getConvertedMedias(Media item)
    {
        return getConvertedMedias(getSession().getSessionContext(), item);
    }


    public void setConvertedMedias(SessionContext ctx, Media item, Collection<Media> value)
    {
        CONVERTEDMEDIASRELATIONCONVERTEDMEDIASHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setConvertedMedias(Media item, Collection<Media> value)
    {
        setConvertedMedias(getSession().getSessionContext(), item, value);
    }


    public void addToConvertedMedias(SessionContext ctx, Media item, Media value)
    {
        CONVERTEDMEDIASRELATIONCONVERTEDMEDIASHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToConvertedMedias(Media item, Media value)
    {
        addToConvertedMedias(getSession().getSessionContext(), item, value);
    }


    public void removeFromConvertedMedias(SessionContext ctx, Media item, Media value)
    {
        CONVERTEDMEDIASRELATIONCONVERTEDMEDIASHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromConvertedMedias(Media item, Media value)
    {
        removeFromConvertedMedias(getSession().getSessionContext(), item, value);
    }


    public ConversionErrorLog createConversionErrorLog(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.CONVERSIONERRORLOG);
            return (ConversionErrorLog)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ConversionErrorLog : " + e.getMessage(), 0);
        }
    }


    public ConversionErrorLog createConversionErrorLog(Map attributeValues)
    {
        return createConversionErrorLog(getSession().getSessionContext(), attributeValues);
    }


    public ConversionGroup createConversionGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.CONVERSIONGROUP);
            return (ConversionGroup)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ConversionGroup : " + e.getMessage(), 0);
        }
    }


    public ConversionGroup createConversionGroup(Map attributeValues)
    {
        return createConversionGroup(getSession().getSessionContext(), attributeValues);
    }


    public ConversionMediaFormat createConversionMediaFormat(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.CONVERSIONMEDIAFORMAT);
            return (ConversionMediaFormat)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ConversionMediaFormat : " + e.getMessage(), 0);
        }
    }


    public ConversionMediaFormat createConversionMediaFormat(Map attributeValues)
    {
        return createConversionMediaFormat(getSession().getSessionContext(), attributeValues);
    }


    public ExtractMediaMetaDataCronJob createExtractMediaMetaDataCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.EXTRACTMEDIAMETADATACRONJOB);
            return (ExtractMediaMetaDataCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ExtractMediaMetaDataCronJob : " + e.getMessage(), 0);
        }
    }


    public ExtractMediaMetaDataCronJob createExtractMediaMetaDataCronJob(Map attributeValues)
    {
        return createExtractMediaMetaDataCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MediaConversionCronJob createMediaConversionCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.MEDIACONVERSIONCRONJOB);
            return (MediaConversionCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating MediaConversionCronJob : " + e.getMessage(), 0);
        }
    }


    public MediaConversionCronJob createMediaConversionCronJob(Map attributeValues)
    {
        return createMediaConversionCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MediaMetaData createMediaMetaData(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedMediaConversionConstants.TC.MEDIAMETADATA);
            return (MediaMetaData)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating MediaMetaData : " + e.getMessage(), 0);
        }
    }


    public MediaMetaData createMediaMetaData(Map attributeValues)
    {
        return createMediaMetaData(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "mediaconversion";
    }


    public Collection<MediaMetaData> getMetaData(SessionContext ctx, Media item)
    {
        return MEDIATOMEDIAMETADATARELMETADATAHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<MediaMetaData> getMetaData(Media item)
    {
        return getMetaData(getSession().getSessionContext(), item);
    }


    public void setMetaData(SessionContext ctx, Media item, Collection<MediaMetaData> value)
    {
        MEDIATOMEDIAMETADATARELMETADATAHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setMetaData(Media item, Collection<MediaMetaData> value)
    {
        setMetaData(getSession().getSessionContext(), item, value);
    }


    public void addToMetaData(SessionContext ctx, Media item, MediaMetaData value)
    {
        MEDIATOMEDIAMETADATARELMETADATAHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToMetaData(Media item, MediaMetaData value)
    {
        addToMetaData(getSession().getSessionContext(), item, value);
    }


    public void removeFromMetaData(SessionContext ctx, Media item, MediaMetaData value)
    {
        MEDIATOMEDIAMETADATARELMETADATAHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromMetaData(Media item, MediaMetaData value)
    {
        removeFromMetaData(getSession().getSessionContext(), item, value);
    }


    public Long getMetaDataDataPK(SessionContext ctx, Media item)
    {
        return (Long)item.getProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.METADATADATAPK);
    }


    public Long getMetaDataDataPK(Media item)
    {
        return getMetaDataDataPK(getSession().getSessionContext(), item);
    }


    public long getMetaDataDataPKAsPrimitive(SessionContext ctx, Media item)
    {
        Long value = getMetaDataDataPK(ctx, item);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getMetaDataDataPKAsPrimitive(Media item)
    {
        return getMetaDataDataPKAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setMetaDataDataPK(SessionContext ctx, Media item, Long value)
    {
        item.setProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.METADATADATAPK, value);
    }


    public void setMetaDataDataPK(Media item, Long value)
    {
        setMetaDataDataPK(getSession().getSessionContext(), item, value);
    }


    public void setMetaDataDataPK(SessionContext ctx, Media item, long value)
    {
        setMetaDataDataPK(ctx, item, Long.valueOf(value));
    }


    public void setMetaDataDataPK(Media item, long value)
    {
        setMetaDataDataPK(getSession().getSessionContext(), item, value);
    }


    public Media getOriginal(SessionContext ctx, Media item)
    {
        return (Media)item.getProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.ORIGINAL);
    }


    public Media getOriginal(Media item)
    {
        return getOriginal(getSession().getSessionContext(), item);
    }


    public void setOriginal(SessionContext ctx, Media item, Media value)
    {
        item.setProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.ORIGINAL, value);
    }


    public void setOriginal(Media item, Media value)
    {
        setOriginal(getSession().getSessionContext(), item, value);
    }


    public Long getOriginalDataPK(SessionContext ctx, Media item)
    {
        return (Long)item.getProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.ORIGINALDATAPK);
    }


    public Long getOriginalDataPK(Media item)
    {
        return getOriginalDataPK(getSession().getSessionContext(), item);
    }


    public long getOriginalDataPKAsPrimitive(SessionContext ctx, Media item)
    {
        Long value = getOriginalDataPK(ctx, item);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getOriginalDataPKAsPrimitive(Media item)
    {
        return getOriginalDataPKAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setOriginalDataPK(SessionContext ctx, Media item, Long value)
    {
        item.setProperty(ctx, GeneratedMediaConversionConstants.Attributes.Media.ORIGINALDATAPK, value);
    }


    public void setOriginalDataPK(Media item, Long value)
    {
        setOriginalDataPK(getSession().getSessionContext(), item, value);
    }


    public void setOriginalDataPK(SessionContext ctx, Media item, long value)
    {
        setOriginalDataPK(ctx, item, Long.valueOf(value));
    }


    public void setOriginalDataPK(Media item, long value)
    {
        setOriginalDataPK(getSession().getSessionContext(), item, value);
    }
}
