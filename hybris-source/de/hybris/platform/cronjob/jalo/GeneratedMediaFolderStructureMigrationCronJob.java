package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.MediaFolder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaFolderStructureMigrationCronJob extends CronJob
{
    public static final String MEDIAFOLDER = "mediaFolder";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("mediaFolder", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public MediaFolder getMediaFolder(SessionContext ctx)
    {
        return (MediaFolder)getProperty(ctx, "mediaFolder");
    }


    public MediaFolder getMediaFolder()
    {
        return getMediaFolder(getSession().getSessionContext());
    }


    public void setMediaFolder(SessionContext ctx, MediaFolder value)
    {
        setProperty(ctx, "mediaFolder", value);
    }


    public void setMediaFolder(MediaFolder value)
    {
        setMediaFolder(getSession().getSessionContext(), value);
    }
}
