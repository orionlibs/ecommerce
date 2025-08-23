package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import org.apache.log4j.Logger;

public class DefaultPrefetchAttributeMode implements AttributePrefetchMode
{
    private static final Logger LOG = Logger.getLogger(DefaultPrefetchAttributeMode.class);


    public boolean isPrefetched(AttributeDescriptor desc)
    {
        Boolean prefetchFlag = (Boolean)desc.getProperty("modelPrefetchMode");
        if(prefetchFlag == null)
        {
            return isPrefetchedAsDefault(desc);
        }
        return prefetchFlag.booleanValue();
    }


    public String toString()
    {
        return "literal";
    }


    protected boolean isPrefetchedAsDefault(AttributeDescriptor desc)
    {
        boolean mustConvert;
        if(desc.isLocalized())
        {
            mustConvert = mustConvert(((MapType)desc.getRealAttributeType()).getReturnType());
        }
        else
        {
            mustConvert = mustConvert(desc.getRealAttributeType());
        }
        if(!mustConvert)
        {
            if(desc.getRealAttributeType() instanceof de.hybris.platform.jalo.type.AtomicType && desc.getPersistenceType() == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("atomic, but calculated: " + desc.getAttributeType().getCode() + ":" + desc.getQualifier());
                }
                mustConvert = true;
            }
        }
        return !mustConvert;
    }


    private boolean mustConvert(Type type)
    {
        boolean ret = false;
        if(type instanceof de.hybris.platform.jalo.type.ComposedType)
        {
            ret = true;
        }
        else if(type instanceof de.hybris.platform.jalo.type.AtomicType)
        {
            ret = false;
        }
        else if(type instanceof CollectionType)
        {
            ret = mustConvert(((CollectionType)type).getElementType());
        }
        else if(type instanceof MapType)
        {
            ret = (mustConvert(((MapType)type).getArgumentType()) || mustConvert(((MapType)type).getReturnType()));
        }
        return ret;
    }


    static final AttributePrefetchMode ATTRIBUTE_PREFETCH_MODE_NONE = (AttributePrefetchMode)new Object();
    static final AttributePrefetchMode ATTRIBUTE_PREFETCH_MODE_ALL = (AttributePrefetchMode)new Object();
    static final AttributePrefetchMode ATTRIBUTE_PREFETCH_MODE_LITERAL = new DefaultPrefetchAttributeMode();
}
