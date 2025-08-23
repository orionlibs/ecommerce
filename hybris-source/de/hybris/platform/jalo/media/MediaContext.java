package de.hybris.platform.jalo.media;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class MediaContext extends GeneratedMediaContext
{
    private static final Logger LOG = Logger.getLogger(MediaContext.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("qualifier", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new MediaContext", 0);
        }
        allAttributes.setAttributeMode("qualifier", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public MediaFormat getTargetFormat(MediaFormat source)
    {
        if(source == null)
        {
            return null;
        }
        for(MediaFormatMapping existentMapping : getMappings())
        {
            if(existentMapping.getSource().equals(source))
            {
                return existentMapping.getTarget();
            }
        }
        return null;
    }


    @ForceJALO(reason = "consistency check")
    public void setMappings(SessionContext ctx, Collection<MediaFormatMapping> value) throws JaloInvalidParameterException
    {
        List<MediaFormatMapping> mappings = new ArrayList<>(value);
        for(int i = 0; i < mappings.size(); i++)
        {
            MediaFormatMapping curMapping = mappings.get(i);
            for(int j = i + 1; j < value.size(); j++)
            {
                MediaFormatMapping nextMapping = mappings.get(j);
                if(curMapping.getSource(ctx).equals(nextMapping.getSource(ctx)))
                {
                    throw new JaloInvalidParameterException("Two mappings have the same source format " + curMapping
                                    .getSource(ctx).getQualifier(ctx), 0);
                }
            }
        }
        super.setMappings(ctx, value);
    }


    public void addToMappings(SessionContext ctx, MediaFormatMapping value) throws JaloInvalidParameterException
    {
        for(MediaFormatMapping existentMapping : getMappings(ctx))
        {
            if(existentMapping.getSource(ctx).equals(value.getSource(ctx)))
            {
                throw new JaloInvalidParameterException("A mapping with source format " + value.getSource(ctx).getQualifier(ctx) + " already exists at media context " +
                                getQualifier(ctx), 0);
            }
        }
        super.addToMappings(ctx, value);
    }
}
