package de.hybris.platform.jalo.media;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class MediaFormatMapping extends GeneratedMediaFormatMapping
{
    private static final Logger LOG = Logger.getLogger(MediaFormatMapping.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missingSet = new HashSet();
        if(((!checkMandatoryAttribute("source", allAttributes, missingSet) ? 1 : 0) | (
                        !checkMandatoryAttribute("target", allAttributes, missingSet) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("Missing " + missingSet + " for creating a new " + getClass().getName(), 0);
        }
        allAttributes.setAttributeMode("source", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("target", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("mediaContext", Item.AttributeMode.INITIAL);
        MediaFormat source = (MediaFormat)allAttributes.get("source");
        MediaContext context = (MediaContext)allAttributes.get("mediaContext");
        if(context != null)
        {
            for(MediaFormatMapping existentMapping : context.getMappings(ctx))
            {
                if(existentMapping.getSource(ctx).equals(source))
                {
                    throw new JaloInvalidParameterException("A mapping with source format " + source.getQualifier(ctx) + " already exists at media context " + context
                                    .getQualifier(ctx), 0);
                }
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "consistency check")
    public void setMediaContext(SessionContext ctx, MediaContext value) throws JaloInvalidParameterException
    {
        if(value != null && getSource(ctx) != null)
        {
            for(MediaFormatMapping existentMapping : value.getMappings(ctx))
            {
                if(existentMapping.getSource(ctx).equals(getSource(ctx)))
                {
                    throw new JaloInvalidParameterException("A mapping with source format " + getSource(ctx).getQualifier(ctx) + " already exists at media context " + value
                                    .getQualifier(ctx), 0);
                }
            }
        }
        super.setMediaContext(ctx, value);
    }


    @ForceJALO(reason = "consistency check")
    protected void setSource(SessionContext ctx, MediaFormat value) throws JaloInvalidParameterException
    {
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'source' is not changeable", 0);
        }
        super.setSource(ctx, value);
    }


    @ForceJALO(reason = "consistency check")
    protected void setTarget(SessionContext ctx, MediaFormat value) throws JaloInvalidParameterException
    {
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'target' is not changeable", 0);
        }
        super.setTarget(ctx, value);
    }
}
