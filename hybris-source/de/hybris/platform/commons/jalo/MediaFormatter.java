package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.media.Media;
import org.apache.log4j.Logger;

public abstract class MediaFormatter extends GeneratedMediaFormatter
{
    private static final Logger log = Logger.getLogger(MediaFormatter.class.getName());


    public abstract Media format(Media paramMedia);
}
