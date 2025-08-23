package de.hybris.platform.jalo.media;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class MediaContainer extends GeneratedMediaContainer
{
    private static final Logger LOG = Logger.getLogger(MediaContainer.class.getName());


    @SLDSafe(portingClass = "UniqueAttributesInterceptor,MandatoryAttributesValidator")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("qualifier") == null)
        {
            throw new JaloInvalidParameterException("missing qualifier for creating a new MediaContainer", 0);
        }
        allAttributes.setAttributeMode("qualifier", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("name", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public Media getMedia(MediaFormat format)
    {
        for(Media existentMedia : getMedias())
        {
            if(format != null && format.equals(existentMedia.getMediaFormat()))
            {
                return existentMedia;
            }
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setMedia(Media media) throws JaloInvalidParameterException
    {
        if(media == null)
        {
            throw new JaloInvalidParameterException("Given media is null and can not be added to media container " +
                            getQualifier(), 0);
        }
        MediaFormat format = media.getMediaFormat();
        if(format != null)
        {
            for(Media existentMedia : getMedias())
            {
                if(format.equals(existentMedia.getMediaFormat()))
                {
                    removeFromMedias(existentMedia);
                    break;
                }
            }
        }
        addToMedias(media);
    }
}
