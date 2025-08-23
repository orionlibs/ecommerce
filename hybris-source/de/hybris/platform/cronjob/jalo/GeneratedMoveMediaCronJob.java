package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMoveMediaCronJob extends CronJob
{
    public static final String MEDIAS = "medias";
    public static final String TARGETFOLDER = "targetFolder";
    public static final String MOVEDMEDIASCOUNT = "movedMediasCount";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("medias", Item.AttributeMode.INITIAL);
        tmp.put("targetFolder", Item.AttributeMode.INITIAL);
        tmp.put("movedMediasCount", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Media> getMedias(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "medias");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getMedias()
    {
        return getMedias(getSession().getSessionContext());
    }


    public void setMedias(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "medias", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setMedias(Collection<Media> value)
    {
        setMedias(getSession().getSessionContext(), value);
    }


    public Integer getMovedMediasCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "movedMediasCount");
    }


    public Integer getMovedMediasCount()
    {
        return getMovedMediasCount(getSession().getSessionContext());
    }


    public int getMovedMediasCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getMovedMediasCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMovedMediasCountAsPrimitive()
    {
        return getMovedMediasCountAsPrimitive(getSession().getSessionContext());
    }


    public void setMovedMediasCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "movedMediasCount", value);
    }


    public void setMovedMediasCount(Integer value)
    {
        setMovedMediasCount(getSession().getSessionContext(), value);
    }


    public void setMovedMediasCount(SessionContext ctx, int value)
    {
        setMovedMediasCount(ctx, Integer.valueOf(value));
    }


    public void setMovedMediasCount(int value)
    {
        setMovedMediasCount(getSession().getSessionContext(), value);
    }


    public MediaFolder getTargetFolder(SessionContext ctx)
    {
        return (MediaFolder)getProperty(ctx, "targetFolder");
    }


    public MediaFolder getTargetFolder()
    {
        return getTargetFolder(getSession().getSessionContext());
    }


    public void setTargetFolder(SessionContext ctx, MediaFolder value)
    {
        setProperty(ctx, "targetFolder", value);
    }


    public void setTargetFolder(MediaFolder value)
    {
        setTargetFolder(getSession().getSessionContext(), value);
    }
}
