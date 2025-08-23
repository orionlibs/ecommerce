package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TypeCopyDescriptor
{
    private final String code;
    private final AbstractItemCopyContext abstractItemCopyContext;
    private final Set<AttributeCopyDescriptor> initialOnlyADs;
    private final Set<AttributeCopyDescriptor> initialADs;
    private final Set<AttributeCopyDescriptor> partOfADs;
    private final Set<AttributeCopyDescriptor> otherADs;
    private final Set<String> qualifiers;


    public TypeCopyDescriptor(AbstractItemCopyContext abstractItemCopyContext, ComposedType targetType)
    {
        this.abstractItemCopyContext = abstractItemCopyContext;
        this.code = targetType.getCode();
        Set[] arrayOfSet = abstractItemCopyContext.splitDescriptors(this, targetType);
        this.initialOnlyADs = arrayOfSet[0];
        this.initialADs = arrayOfSet[1];
        this.partOfADs = arrayOfSet[2];
        this.otherADs = arrayOfSet[3];
        this.qualifiers = new HashSet<>();
        if(this.initialOnlyADs != null)
        {
            for(Iterator<AttributeCopyDescriptor> it = this.initialOnlyADs.iterator(); it.hasNext(); )
            {
                this.qualifiers.add(((AttributeCopyDescriptor)it.next()).getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
        }
        if(this.initialADs != null)
        {
            for(Iterator<AttributeCopyDescriptor> it = this.initialADs.iterator(); it.hasNext(); )
            {
                this.qualifiers.add(((AttributeCopyDescriptor)it.next()).getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
        }
        if(this.partOfADs != null)
        {
            for(Iterator<AttributeCopyDescriptor> it = this.partOfADs.iterator(); it.hasNext(); )
            {
                this.qualifiers.add(((AttributeCopyDescriptor)it.next()).getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
        }
        if(this.otherADs != null)
        {
            for(Iterator<AttributeCopyDescriptor> it = this.otherADs.iterator(); it.hasNext(); )
            {
                this.qualifiers.add(((AttributeCopyDescriptor)it.next()).getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
        }
    }


    public final Set<AttributeCopyDescriptor> getExcludedAttributeCopyDescriptors(boolean forUpdate, Map blacklist, Map whitelist)
    {
        if(this.abstractItemCopyContext.isDebugEnabled())
        {
            this.abstractItemCopyContext.debug("check for excluded attributes whitelist = " + whitelist + ", blacklist=" + blacklist);
        }
        boolean gotBlacklist = (blacklist != null && !blacklist.isEmpty());
        boolean gotWhitelist = (whitelist != null && !whitelist.isEmpty());
        if(!gotBlacklist && !gotWhitelist)
        {
            return Collections.EMPTY_SET;
        }
        Set<AttributeCopyDescriptor> ret = new HashSet<>();
        if(!forUpdate && this.initialOnlyADs != null)
        {
            for(AttributeCopyDescriptor acd : this.initialOnlyADs)
            {
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("checking initialOnly " + acd);
                }
                if((gotBlacklist && Boolean.TRUE.equals(blacklist.get(acd.getQualifier()))) || (gotWhitelist &&
                                !Boolean.TRUE.equals(whitelist.get(acd.getQualifier()))))
                {
                    ret.add(acd);
                    if(this.abstractItemCopyContext.isDebugEnabled())
                    {
                        this.abstractItemCopyContext.debug("acd " + acd + " was excluded");
                    }
                }
            }
        }
        if(this.initialADs != null)
        {
            for(AttributeCopyDescriptor acd : this.initialADs)
            {
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("checking initial " + acd);
                }
                if((gotBlacklist && blacklist.containsKey(acd.getQualifier())) || (gotWhitelist &&
                                !whitelist.containsKey(acd.getQualifier())))
                {
                    ret.add(acd);
                    if(this.abstractItemCopyContext.isDebugEnabled())
                    {
                        this.abstractItemCopyContext.debug("acd " + acd + " was excluded");
                    }
                }
            }
        }
        if(this.partOfADs != null)
        {
            for(AttributeCopyDescriptor acd : this.partOfADs)
            {
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("checking partOf " + acd);
                }
                if((gotBlacklist && blacklist.containsKey(acd.getQualifier())) || (gotWhitelist &&
                                !whitelist.containsKey(acd.getQualifier())))
                {
                    ret.add(acd);
                    if(this.abstractItemCopyContext.isDebugEnabled())
                    {
                        this.abstractItemCopyContext.debug("acd " + acd + " was excluded");
                    }
                }
            }
        }
        if(this.otherADs != null)
        {
            for(AttributeCopyDescriptor acd : this.otherADs)
            {
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("checking other " + acd);
                }
                if((gotBlacklist && blacklist.containsKey(acd.getQualifier())) || (gotWhitelist &&
                                !whitelist.containsKey(acd.getQualifier())))
                {
                    ret.add(acd);
                    if(this.abstractItemCopyContext.isDebugEnabled())
                    {
                        this.abstractItemCopyContext.debug("acd " + acd + " was excluded");
                    }
                }
            }
        }
        return ret;
    }


    public final Set<AttributeCopyDescriptor> getInitial(boolean forUpdate, Map blacklist, Map whitelist)
    {
        if(forUpdate)
        {
            return (this.initialADs != null) ? filter(this.initialADs, blacklist, whitelist) : Collections.EMPTY_SET;
        }
        Set<AttributeCopyDescriptor> ret = new LinkedHashSet<>();
        if(this.initialOnlyADs != null)
        {
            ret.addAll(this.initialOnlyADs);
        }
        if(this.initialADs != null)
        {
            ret.addAll(this.initialADs);
        }
        return filter(ret, blacklist, whitelist);
    }


    public String getCode()
    {
        return this.code;
    }


    public final boolean hasAttribute(String qualifier)
    {
        return this.qualifiers.contains(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public final Set<AttributeCopyDescriptor> getPartOf(Map blacklist, Map whitelist)
    {
        return (this.partOfADs != null) ? filter(this.partOfADs, blacklist, whitelist) : Collections.EMPTY_SET;
    }


    public final Set<AttributeCopyDescriptor> getOther(Map blacklist, Map whitelist)
    {
        return (this.otherADs != null) ? filter(this.otherADs, blacklist, whitelist) : Collections.EMPTY_SET;
    }


    protected ComposedType getTargetType()
    {
        return TypeManager.getInstance().getComposedType(this.code);
    }


    public String toString()
    {
        return getCode();
    }


    private final Set<AttributeCopyDescriptor> filter(Set<AttributeCopyDescriptor> originalACDs, Map blacklist, Map whitelist)
    {
        if(originalACDs == null || originalACDs.isEmpty())
        {
            return originalACDs;
        }
        boolean gotBlacklist = (blacklist != null && !blacklist.isEmpty());
        boolean gotWhitelist = (whitelist != null && !whitelist.isEmpty());
        if(!gotBlacklist && !gotWhitelist)
        {
            return originalACDs;
        }
        Set<AttributeCopyDescriptor> ret = new LinkedHashSet<>(originalACDs);
        for(Iterator<AttributeCopyDescriptor> iter = ret.iterator(); iter.hasNext(); )
        {
            String qualifier = ((AttributeCopyDescriptor)iter.next()).getQualifier();
            if(gotBlacklist && Boolean.TRUE.equals(blacklist.get(qualifier)))
            {
                iter.remove();
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("skipped attribute '" + qualifier + "' since it is contained in blacklist");
                }
                continue;
            }
            if(gotWhitelist && !Boolean.TRUE.equals(whitelist.get(qualifier)))
            {
                iter.remove();
                if(this.abstractItemCopyContext.isDebugEnabled())
                {
                    this.abstractItemCopyContext.debug("skipped attribute '" + qualifier + "' since it is not contained in non-empty whitelist");
                }
            }
        }
        return ret;
    }
}
